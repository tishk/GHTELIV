package factory;

import model.Pan;
import model.Transaction;
import util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Hamid on 6/2/2017.
 */
public class PanFacadeImpl implements PanFacade {

    private RequestToSwitch requestToSwitch=new RequestToSwitch();

    private StringUtils stringUtils=new StringUtils();

    private static PanFacadeImpl cardFacadeImpl;

    public  static PanFacadeImpl getInstance(){
        if (cardFacadeImpl==null){
            cardFacadeImpl=new PanFacadeImpl();
        }
        return cardFacadeImpl;
    }

    private String randomNumber(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return String.valueOf(randomNum);
    }

    private String getMessageSequense(){
        return "TlBank"+stringUtils.rightString(Const.RANDOM_RANGE_CORRECTION_ZERO+
                randomNumber(Const.RANDOM_RANGE_MIN_VALUE,Const.RANDOM_RANGE_MAX_VALUE),9);
    }

    private String getMessageSequenseType2(){
        return Util.clientNo+
                correctLen(Const.CHANNEL_NUMBER_CORRECTION_ZERO+Util.channelNo,3)+
                correctLen(Const.RANDOM_RANGE_CORRECTION_ZERO+
                        randomNumber(Const.RANDOM_RANGE_MIN_VALUE,Const.RANDOM_RANGE_MAX_VALUE), 9)+Util.macAddress ;
    }


    private String getTransMode(int kind) {
        String transMode;
        switch (kind){
            case 0:transMode="M";
            case 1:transMode="F";
            case 2:transMode="D";
            case 3:transMode="D";
            default:transMode="M";
        }
        return transMode;
    }

    private String correctLen(String str, int index) {
        return stringUtils.rightString(str, index);
    }

    @Override
    public void getBalance(Pan pan) {
        sendBalanceRequest(pan);
        if (pan.getResponseFromSwitch()!=null){
            processPanBalanceResponse(pan);
        }else{
            pan.setActionCode("5999");
        }
    }

    @Override
    public  void getTransactions(Pan pan) {

        sendTransactionRequest(pan);
        if (pan.getResponseFromSwitch()!=null){
            processTransactionResponse(pan);
        }else{
            pan.setActionCode(Const.NETWORK_ERROR);
        }



    }

    @Override
    public void hotCard(Pan pan) {

        sendHotCardRequest(pan);
        if (pan.getResponseFromSwitch()!=null){
            processHotCardResponse(pan);
        }else{
            pan.setActionCode(Const.NETWORK_ERROR);
        }

    }

    @Override
    public void billPayment(Pan pan) {

        sendBillPaymentRequest(pan);
        if (pan.getResponseFromSwitch()!=null){
            processBillPaymentResponse(pan);
        }else{
            pan.setActionCode(Const.NETWORK_ERROR);
        }
    }

    @Override
    public void followUp(Pan pan) {

        sendFollowUpRequest(pan);
        if (pan.getResponseFromSwitch()!=null){
            processFollowUpResponse(pan);
        }else{
            pan.setActionCode(Const.NETWORK_ERROR);
        }
    }

    @Override
    public void topUp(Pan pan) {

        sendTopUpRequest(pan);
        if (pan.getResponseFromSwitch()!=null){
            processTopUpResponse(pan);
        }else{
            pan.setActionCode(Const.NETWORK_ERROR);
        }
    }



    private void processTopUpResponse(Pan pan) {
        Tokenize tokenize=new Tokenize(pan.getResponseFromSwitch());
        if (tokenize.tokenizeResponse()==pan.getMessageSequence()){
            String responseCode=tokenize.tokenizeResponse();
            pan.setActionCode(tokenize.tokenizeResponse());
            int responseCodeInt=Integer.valueOf(responseCode);
            if (responseCodeInt>=1 && responseCodeInt<=99){
                pan.setActionCode("50"+responseCode);
            }

        }
    }

    private void sendTopUpRequest(Pan pan) {
        String messageSequence=getMessageSequense();
        String message=messageSequence+
                Const.TOP_UP_SIGN +
                Const.PAN_REQUEST_ID+
                correctLen(Const.PAN_CORRECTION_ZERO+pan.getPanNumber(),Const.PAN_CORRECTION_ZERO.length())+
                correctLen(Const.PIN_CORRECTION_ZERO+pan.getPin(),Const.PIN_CORRECTION_ZERO.length())+
                Const.STAR+
                pan.getMobileOperator()+
                Const.STAR+
                correctLen(Const.MOBILE_NUMBER_CORRECTION_ZERO+pan.getMobileNo(),Const.MOBILE_NUMBER_CORRECTION_ZERO.length())+
                Const.STAR+
                formatChargeValue(pan.getMobileChargeValue())+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+pan.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        pan.setMessageSequence(messageSequence);
        pan.setRequestToSwitch(message);
        pan.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private String formatChargeValue(String chargeValue) {
       return String.valueOf(Integer.valueOf(chargeValue)%Integer.valueOf(Const.THOUSAND));
    }

    private void processFollowUpResponse(Pan pan) {
        String response=stringUtils.rightString(pan.getResponseFromSwitch(),pan.getResponseFromSwitch().length()-15);
        pan.setActionCode(stringUtils.leftString(response,4));
        if (pan.getActionCode().equals(Const.SUCCESS)){
           response=stringUtils.rightString(response,response.length()-4);
           pan.setReferenceCode(response);
        }
    }

    private void sendFollowUpRequest(Pan pan) {
        String messageSequence=getMessageSequenseType2();
        String message=messageSequence+
                Const.PAN_BILL_PAYMENT_SIGN+
                correctLen(Const.BILLID_CORRECTION_ZERO+pan.getBillID(),Const.BILLID_CORRECTION_ZERO.length())+
                correctLen(Const.PAYMENT_ID_CORRECTION_ZERO+pan.getPaymentID(),Const.PAYMENT_ID_CORRECTION_ZERO.length())+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+pan.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        pan.setMessageSequence(messageSequence);
        pan.setRequestToSwitch(message);
        pan.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void processBillPaymentResponse(Pan pan) {

        String response=stringUtils.rightString(pan.getResponseFromSwitch(),pan.getResponseFromSwitch().length()-15);
        pan.setActionCode(stringUtils.leftString(response,4));
        try {
            Integer.parseInt(pan.getActionCode());
        }catch (Exception e){
            pan.setActionCode("-1");
        }
        if (pan.getActionCode().equals("9014")){
            pan.setReferenceCode(stringUtils.midString(response,5,12));
        }else if (pan.getActionCode().equals(Const.SUCCESS)){
            pan.setReferenceCode(stringUtils.midString(response,5,12));
            pan.setReferenceCode(stringUtils.midString(response,11,18));
        }else{
            int actionCode=Integer.valueOf(pan.getActionCode());
            if (actionCode!=-1){
                if (actionCode>=1 && actionCode<=99){
                    pan.setActionCode("50"+pan.getActionCode());
                }
            }
        }

    }

    private void sendBillPaymentRequest(Pan pan) {
        String messageSequence=getMessageSequenseType2();
        String message=messageSequence+
                Const.PAN_BILL_PAYMENT_SIGN+
                Const.PAN_REQUEST_ID+
                correctLen(Const.PAN_CORRECTION_ZERO+pan.getPanNumber(),Const.PAN_CORRECTION_ZERO.length())+
                correctLen(Const.TWO_ZERO+String.valueOf(pan.getPin().length()),Const.TWO_ZERO.length())+
                correctLen(Const.PIN_CORRECTION_ZERO+pan.getPin(),Const.PIN_CORRECTION_ZERO.length())+
                correctLen(Const.BILLID_CORRECTION_ZERO+pan.getBillID(),Const.BILLID_CORRECTION_ZERO.length())+
                correctLen(Const.PAYMENT_ID_CORRECTION_ZERO+pan.getPaymentID(),Const.PAYMENT_ID_CORRECTION_ZERO.length())+
                correctLen(Const.AMOUNT_CORRECTION_ZERO+pan.getAmountOfBill(),Const.AMOUNT_CORRECTION_ZERO.length())+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+pan.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        pan.setMessageSequence(messageSequence);
        pan.setRequestToSwitch(message);
        pan.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void processHotCardResponse(Pan pan) {
        String response=stringUtils.rightString(pan.getResponseFromSwitch(),pan.getResponseFromSwitch().length()-15);
        pan.setActionCode(stringUtils.leftString(response,4));
    }

    private void sendHotCardRequest(Pan pan) {
        String messageSequense= getMessageSequense();
        String message=messageSequense+
                Const.PAN_HOT_CARD_SIGN+
                Const.PAN_REQUEST_ID+
                pan.getPanNumber()+
                correctLen(Const.TWO_ZERO+String.valueOf(pan.getPin().length()),Const.TWO_ZERO.length())+
                correctLen(Const.PIN_CORRECTION_ZERO+pan.getPin(),Const.PIN_CORRECTION_ZERO.length())+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+pan.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        pan.setMessageSequence(messageSequense);
        pan.setRequestToSwitch(message);
        pan.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void sendBalanceRequest(Pan pan) {
        String messageSequence= getMessageSequense();
        String message=messageSequence+
                Const.PAN_BALANCE_SIGN+
                Const.PAN_REQUEST_ID+
                pan.getPanNumber()+
                correctLen("00"+String.valueOf(pan.getPin().length()),2)+
                correctLen(Const.PIN_CORRECTION_ZERO+pan.getPin(),Const.PIN_CORRECTION_ZERO.length())+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+pan.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        pan.setMessageSequence(messageSequence);
        pan.setRequestToSwitch(message);
        pan.setResponseFromSwitch(requestToSwitch.send(message));

    }

    private void processPanBalanceResponse(Pan pan) {

        String response=stringUtils.rightString(pan.getResponseFromSwitch(),pan.getResponseFromSwitch().length()-15);
        pan.setActionCode(stringUtils.leftString(response,4));
        if (pan.getActionCode()== Const.SUCCESS){
            pan.setBalance(stringUtils.rightString(response,18));
        }
    }

    private void processTransactionResponse(Pan pan) {
        Tokenize tokenize=new Tokenize(pan.getResponseFromSwitch());
        if (tokenize.tokenizeResponse()==pan.getMessageSequence()){
            pan.setActionCode(stringUtils.leftString(tokenize.getOriginalString(),4));
            tokenize.setOriginalString(stringUtils.rightString(tokenize.getOriginalString(),tokenize.getOriginalString().length()-4));
            if (pan.getActionCode().equals(Const.SUCCESS)){
                doSuccessOperationOfTransactions(pan, tokenize);
            }

        }
    }

    private void doSuccessOperationOfTransactions(Pan pan, Tokenize tokenize) {
        pan.setNameAndFamily(tokenize.tokenizeResponse());
        pan.setShebaNumber(tokenize.tokenizeResponse());
        List<Transaction> transactions=new ArrayList<Transaction>();
        Transaction transaction;
        int row=1;
        while (true){
            transaction=new Transaction();
            tokenize.tokenizeResponse();
            transaction.setRow(String.valueOf(row));
            transaction.setDate(tokenize.tokenizeResponse());
            transaction.setTime(tokenize.tokenizeResponse());
            transaction.setAmount(tokenize.tokenizeResponse());
            transaction.setDescription(tokenize.tokenizeResponse());
            transaction.setBranchCode(tokenize.tokenizeResponse());
            transaction.setDocumentType(tokenize.tokenizeResponse());
            transaction.setBalance(tokenize.tokenizeResponse());
            transaction.setfDescription(tokenize.tokenizeResponse());
            transactions.add(transaction);
            if (tokenize.getOriginalString().equals("")) break;
            row++;
        }
        pan.setTransactions(transactions);

    }

    private void sendTransactionRequest(Pan pan) {
        String transMode=getTransMode(pan.getKindOfFax());
        String messageSequence= getMessageSequense();
        String message=messageSequence+
                Util.macAddress+
                Const.ACCOUNT_SIGN+
                Const.TRANSACTION_SIGN+
                transMode+
                Const.ACCOUNT_CORRECTION_ZERO+
                Const.PARAM1_NUMBER+
                Const.TRANSACTION_KEYWORD+
                String.valueOf(pan.getKindOfFax())+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+pan.getCallerID(),15)+
                correctLen(Const.FAX_COUNT_CORRECTION_ZERO+pan.getFaxCount(),3)+
                correctLen(Const.FAX_START_DATE_CORRECTION_ZERO+pan.getStartDateOfFax(),8)+
                correctLen(Const.FAX_END_DATE_CORRECTION_ZERO+pan.getEndDateOfFax(),8)+
                pan.getPanNumber();
        pan.setMessageSequence(messageSequence);
        pan.setRequestToSwitch(message);
        pan.setResponseFromSwitch(requestToSwitch.send(message));
        String response=requestToSwitch.send(message);
    }

    private Transaction[] initializeTransactions(String responseFromeServer){
        return null;
    }



}
