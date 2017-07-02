package factory;

import dao.TransactionDaoImpl;
import model.Account;
import model.Transaction;
import util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Hamid on 6/2/2017.
 */
public class AccountFacadeImpl implements AccountFacade {

    private StringUtils stringUtils=new StringUtils();

    private RequestToSwitch requestToSwitch=new RequestToSwitch();

    private static AccountFacadeImpl accountFacade;

    public static AccountFacadeImpl getInstance(){
        if (accountFacade==null){
            accountFacade=new AccountFacadeImpl();
        }
        return accountFacade;
    }

    private String randomNumber(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return String.valueOf(randomNum);
    }

    private String getMessageSequence(){
     return "TlBank"+ correctLen(Const.RANDOM_RANGE_CORRECTION_ZERO+
             randomNumber(Const.RANDOM_RANGE_MIN_VALUE,Const.RANDOM_RANGE_MAX_VALUE), 9)+Util.macAddress ;
    }
    private String getMessageSequenceType2(){
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

    @Override
    public void getBalance(Account account) {
      sendBalanceRequest(account);
      if (account.getResponseFromSwitch()!=null){
          processAccountBalanceResponse(account);
      }else{
          account.setActionCode(Const.NETWORK_ERROR);
      }
    }

    @Override
    public void getTransactions(Account account) {

        sendTransactionRequest(account);
        if (account.getResponseFromSwitch()!=null){
            processTransactionResponseFromServer(account);
        }else{
            account.setActionCode(Const.NETWORK_ERROR);
        }


    }

    @Override
    public void fundTransfer(Account account) {
        sendFundTransferRequest(account);
        if (account.getActionCode()!=null){
            processFundTransferResponse(account);
        }else{
            account.setActionCode(Const.NETWORK_ERROR);
        }
    }

    @Override
    public void changePin(Account account) {

        sendChangePinRequest(account);
        if (account.getResponseFromSwitch()!=null){
            processChangePinResponse(account);
        }else{
            account.setActionCode(Const.NETWORK_ERROR);
        }
    }

    @Override
    public void getLoanStatus (Account account) {

        sendLoanGetStatusRequest (account);
        if (account.getResponseFromSwitch()!=null){
            processLoanStatusResponse (account);
        }else {
            account.setActionCode(Const.NETWORK_ERROR);
        }

    }

    @Override
    public void smsAlarmSelect(Account account) {
      sendSMSCommand(account,Const.SMS_ALERTING_SELECT_SIGN);
      if (account.getResponseFromSwitch()!=null){
          account.setMobileNumber(account.getResponseFromSwitch());

      }
    }

    @Override
    public void smsAlarmRegister(Account account) {
        sendSMSCommand(account,Const.SMS_ALERTING_REGISTER_SIGN);
        if (account.getResponseFromSwitch()!=null){
            account.setActionCode(account.getResponseFromSwitch());
        }
    }

    @Override
    public void smsAlarmDelete(Account account) {
        sendSMSCommand(account,Const.SMS_ALERTING_DELETE_SIGN);
        if (account.getResponseFromSwitch()!=null){
            account.setActionCode(account.getResponseFromSwitch());
        }
    }

    @Override
    public void getTransactionOfPOS(Account account) {

        // should set kind of transaction
        TransactionDaoImpl.getInstance().getTransactionOfPos(account);
    }

    @Override
    public void getTransactionOfCheque(Account account) {

        sendChequeStatusRequest(account);
        if (account.getResponseFromSwitch()!=null){
            processChequeStatusResponse(account);
        }else{
            account.setActionCode(Const.NETWORK_ERROR);
        }

    }

    @Override
    public void getIBAN(Account account) {

        sendGetIBANRequest(account);
        if (account.getResponseFromSwitch()!=null){
            processGetIBANResponse(account);
        }else{
            account.setActionCode(Const.NETWORK_ERROR);
        }
    }

    private void processGetIBANResponse(Account account) {
        String response=stringUtils.rightString(account.getResponseFromSwitch(),account.getResponseFromSwitch().length()-15);
        String msgSequense=stringUtils.leftString(account.getResponseFromSwitch(),15);
        if (msgSequense.equals(account.getMessageSequence())){
            if (response.length()>24){
                account.setActionCode(Const.SUCCESS);
                account.setIBAN(response);
            }else{
                account.setActionCode("9001");
            }
        }
    }

    private void sendGetIBANRequest(Account account) {
        String messageSequence=getMessageSequenceType2();
        String message=messageSequence+
                Const.GET_IBAN_SIGN+
                "1"+
                correctLen(Const.ACCOUNT_CORRECTION_ZERO+account.getAccountNumber(),Const.ACCOUNT_CORRECTION_ZERO.length())+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+account.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());

        account.setMessageSequence(messageSequence);
        account.setRequestToSwitch(message);
        account.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void processChequeStatusResponse(Account account) {
        Tokenize tokenize=new Tokenize(account.getResponseFromSwitch());
        if (tokenize.tokenizeResponse()==account.getMessageSequence()){
            account.setActionCode(stringUtils.leftString(tokenize.getOriginalString(),4));
            if (account.getActionCode().equals("0000")){
                account.setResponseFromSwitch(tokenize.getOriginalString());
            }
        }
    }

    private void sendChequeStatusRequest(Account account) {
        String messageSequence=getMessageSequenceType2();
        String message=messageSequence+
                Const.ACCOUNT_SIGN+
                Const.CHEQUE_STATUS_SIGN +
                correctLen(Const.ACCOUNT_CORRECTION_ZERO+account.getAccountNumber(),Const.ACCOUNT_CORRECTION_ZERO.length())+
                Const.PARAM1_NUMBER+
                correctLen(Const.CHEQUE_NUMBER_CORRECTION_ZERO+account.getChequeNumber(),Const.CHEQUE_NUMBER_CORRECTION_ZERO.length())+
                Const.CHEQUE_STATUS_KEYWORD+
                account.getTransferType()+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+account.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        account.setMessageSequence(messageSequence);
        account.setRequestToSwitch(message);
        account.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void sendSMSCommand(Account account, String command) {

        sendSMSRequest(account, command);
        if (account.getResponseFromSwitch()!=null){
            processSMSAlertingResponse(account);
        }else{
          account.setActionCode(Const.NETWORK_ERROR);
        }
    }

    private void processSMSAlertingResponse(Account account) {
        Tokenize tokenize=new Tokenize(account.getResponseFromSwitch());
        if (tokenize.tokenizeResponse()==account.getMessageSequence()){
           account.setResponseFromSwitch(tokenize.getOriginalString());
           account.setActionCode("0000");
       }
    }

    private void sendSMSRequest(Account account, String command) {
        String messageSequence=getMessageSequence();
        String message=messageSequence+
                Const.SMS_ALERTING_SIGN+
                command+
                Const.TWO_ZERO+
                account.getMobileNumber()+
                Const.STAR+
                correctLen(Const.ACCOUNT_CORRECTION_ZERO+account.getAccountNumber(),Const.ACCOUNT_CORRECTION_ZERO.length())+
                Const.STAR+
                account.getPin()+
                Const.STAR+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+account.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        account.setMessageSequence(messageSequence);
        account.setRequestToSwitch(message);
        account.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void processLoanStatusResponse (Account account) {
        String response=stringUtils.rightString(account.getResponseFromSwitch(),account.getResponseFromSwitch().length()-15);
        Tokenize tokenize=new Tokenize(response);
        account.setActionCode (tokenize.tokenizeResponse ());
        if (account.getActionCode ().equals (Const.SUCCESS)){

            account.setLoanAmount (tokenize.tokenizeResponse ());
            account.setBalanceOfLoanDebt (tokenize.tokenizeResponse ());
            account.setLoanDate   (tokenize.tokenizeResponse ());
        }

    }

    private void sendLoanGetStatusRequest (Account account) {
        String messageSequence=getMessageSequence();
        String message=messageSequence+
                Const.ACCOUNT_SIGN+
                Const.INSTALLMENT_STATUS+
                correctLen(Const.ACCOUNT_CORRECTION_ZERO+account.getDestinationAccount(),Const.ACCOUNT_CORRECTION_ZERO.length())+
                Const.PARAM1_NUMBER+
                Const.INSTALLMENT_STATUS_KEYWORD+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+account.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        account.setMessageSequence(messageSequence);
        account.setRequestToSwitch(message);
        account.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void processChangePinResponse(Account account) {
        String response=stringUtils.rightString(account.getResponseFromSwitch(),account.getResponseFromSwitch().length()-15);
        account.setActionCode(stringUtils.leftString(response,4));
    }

    private void sendChangePinRequest(Account account) {
        String messageSequence=getMessageSequenceType2();
        String message=messageSequence+
                Const.CHANGE_PIN_SIGN+
                correctLen(Const.ACCOUNT_CORRECTION_ZERO+account.getAccountNumber(),Const.ACCOUNT_CORRECTION_ZERO.length())+
                Const.STAR+
                account.getPin()+
                Const.STAR+
                account.getNewPin()+
                Const.STAR+
                Const.CHANGE_PASS_KEYWORD;
        account.setMessageSequence(messageSequence);
        account.setRequestToSwitch(message);
        account.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void processFundTransferResponse(Account account) {

        String response=stringUtils.rightString(account.getResponseFromSwitch(),account.getResponseFromSwitch().length()-15);
        account.setActionCode(stringUtils.leftString(response,4));
        account.setReferenceCode(stringUtils.rightString(response,8));

    }

    private void sendFundTransferRequest(Account account) {
        String messageSequence=getMessageSequence();
        String message=messageSequence+

                Const.ACCOUNT_SIGN+
                account.getTransferType()+
                correctLen(Const.ACCOUNT_CORRECTION_ZERO+account.getAccountNumber(),Const.ACCOUNT_CORRECTION_ZERO.length())+
                correctLen(Const.ACCOUNT_CORRECTION_ZERO+account.getDestinationAccount(),Const.ACCOUNT_CORRECTION_ZERO.length())+
                correctLen(Const.ACCOUNT_AMOUNT_CORRECTION_ZERO+account.getAmountOfTransfer(),Const.ACCOUNT_AMOUNT_CORRECTION_ZERO.length())+
                Const.PARAM1_NUMBER+
                Const.FUND_TRANSFER_KEYWORD+
                account.getTransferType()+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+account.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        account.setMessageSequence(messageSequence);
        account.setRequestToSwitch(message);
        account.setResponseFromSwitch(requestToSwitch.send(message));
    }

    private void processTransactionResponseFromServer(Account account) {
        Tokenize tokenize=new Tokenize(account.getResponseFromSwitch());
        if (tokenize.tokenizeResponse()==account.getMessageSequence()){
            account.setActionCode(stringUtils.leftString(tokenize.getOriginalString(),4));
            tokenize.setOriginalString(stringUtils.rightString(tokenize.getOriginalString(),tokenize.getOriginalString().length()-4));
            if (account.getActionCode().equals(Const.SUCCESS)){
                doSuccessOperationOfTransactions(account, tokenize);
            }

        }
    }
    private void doSuccessOperationOfTransactions(Account account, Tokenize tokenize) {
        account.setNameAndFamily (tokenize.tokenizeResponse());
        account.setShetabNumber(tokenize.tokenizeResponse());
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
        account.setTransactions(transactions);

    }
    private void processAccountBalanceResponse(Account account) {
        String response=stringUtils.rightString(account.getResponseFromSwitch(),account.getResponseFromSwitch().length()-15);
        account.setActionCode(stringUtils.leftString(response,4));
        if (account.getActionCode()== Const.SUCCESS){

            account.setAccountType(stringUtils.midString(response,5,2));
            account.setBalance(stringUtils.midString(response,13,18));
        }
    }

    private void sendBalanceRequest(Account account) {
        account.setMessageSequence(getMessageSequence());
        String message=account.getMessageSequence()+
                Const.ACCOUNT_SIGN+
                Const.ACCOUNT_BALANCE_SIGN+
                Const.ACCOUNT_REQUEST_ID+
                correctLen(Const.ACCOUNT_CORRECTION_ZERO+account.getAccountNumber(),Const.ACCOUNT_CORRECTION_ZERO.length())+
                correctLen("00"+String.valueOf(account.getPin().length()),2)+
                correctLen(Const.PIN_CORRECTION_ZERO+account.getPin(),Const.PIN_CORRECTION_ZERO.length())+
                Const.PARAM1_NUMBER+
                Const.BALANCE_KEYWORD+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO+account.getCallerID(),Const.CALLER_ID_CORRECTION_ZERO.length());
        account.setRequestToSwitch(message);
        account.setResponseFromSwitch(requestToSwitch.send(message));

    }

    private void sendTransactionRequest(Account account) {
        String transMode=getTransMode(account.getKindOfFax());
        String messageSequence=getMessageSequence();
        String message=messageSequence+
                Util.macAddress+
                Const.ACCOUNT_SIGN+
                Const.TRANSACTION_SIGN+
                transMode+
                getCorrectedAccountLen(account.getAccountNumber()) +
                Const.PARAM1_NUMBER+
                Const.TRANSACTION_KEYWORD+
                String.valueOf(account.getKindOfFax())+
                correctLen(Const.CALLER_ID_CORRECTION_ZERO + account.getCallerID(), 15) +
                correctLen(Const.FAX_COUNT_CORRECTION_ZERO+String.valueOf(account.getFaxCount()), 3) +
                correctLen(Const.FAX_START_DATE_CORRECTION_ZERO+account.getStartDateOfFax(), 8) +
                correctLen(Const.FAX_END_DATE_CORRECTION_ZERO+account.getEndDateOfFax(), 8) +
                Const.PAN_CORRECTION_ZERO;
        account.setMessageSequence(messageSequence);
        account.setRequestToSwitch(message);
        account.setResponseFromSwitch(requestToSwitch.send(message));

    }

    private String correctLen(String str, int index) {
        return stringUtils.rightString(str, index);
    }

    private String getCorrectedAccountLen(String accountNumber) {
        return correctLen(Const.ACCOUNT_CORRECTION_ZERO+accountNumber, Const.ACCOUNT_CORRECTION_ZERO.length());
    }





}
