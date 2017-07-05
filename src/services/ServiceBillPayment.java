package services;

import model.Call;
import model.Pan;
import org.asteriskjava.fastagi.AgiException;
import util.Const;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceBillPayment {


    private Call call;
    private Set billPaymentMenu = new HashSet ();
    private Set billPaymentSubMenu = new HashSet ();
    private int billPaymentMenuCount =0;
    private int billPaymentSubMenuCount =0;
    private String firstChoice="";
    private String firstChoice2="";
    private String payID="";
    private String billID="";
    private String pinNumber ="";
    private String panNumber ="";

    private String amount ="";

    public ServiceBillPayment(Call call) {
        this.call=call;
    }

    public void execute() throws Exception {
        call.setServiceBillPayment(this);
        createMainMenu ();
        sayMainMenu ();
    }

    private void createMainMenu(){
        /*
        first say balance
        1:bill payment
        2:follow up
        3:top up
        9:exit
        0:exit
         */
        billPaymentMenu.add("1");
        billPaymentMenu.add("2");
        billPaymentMenu.add("3");
        billPaymentMenu.add("9");
        billPaymentMenu.add("0");

    }

    public  void sayMainMenu() throws Exception {

        String Choice="";
        while ((billPaymentMenuCount < Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(billPaymentMenu,Const.MENU_PREFIX_BILL_PAYMENT);
            else {
                Choice=firstChoice;
                firstChoice="";
            }
            if (!Choice.equals(Const.INVALID_ENTRY_MENU)){
                selectSubMenu(Choice);
            }
            else{
                inputError ();
            }
            billPaymentMenuCount++;
        }
        exit ();
    }

    private void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case  "1":billPayment ();
                break;
            case  "2":followUp ();
                break;
            case  "3":topUp ();
                break;
            case  "9":exit ();
                break;
            case  "0":exit ();
                break;
            case "-1":inputError ();
                break;
            default:inputError ();
                break;

        }
    }

    private void subMenuOperations() throws Exception {
        subMenuCreateSubMenu ();
        subMenuSayMenu ();
    }

    private void subMenuCreateSubMenu (){
        /*
        first say balance
        1:replay follow up code
        2:retry bil payment
        9:exit
        0:exit
         */
        billPaymentSubMenu.add("1");
        billPaymentSubMenu.add("2");
        billPaymentSubMenu.add("9");
        billPaymentSubMenu.add("0");


    }

    private void subMenuSayMenu () throws Exception {

        String Choice="";
        while ((billPaymentSubMenuCount < Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice2.equals("")){
                Choice = call.getPlayVoiceTool ().sayMenu(billPaymentSubMenu,Const.MENU_PREFIX_BILL_PAYMENT_SUB_MENU);
            }
            else {
                Choice=firstChoice2;
                firstChoice2="";
            }
            if (!Choice.equals(Const.INVALID_ENTRY_MENU)){
                subMenuSelectSubMenu (Choice);
            }
            else{
                inputError ();
            }
            billPaymentSubMenuCount++;
        }
        exit ();
    }

    private void subMenuSelectSubMenu (String Choice) throws Exception {

        switch (Choice){
            case  "1":sayFollowupCode();
                break;
            case  "2":billPayment ();
                break;
            case  "9":exit ();
                break;
            case  "0":exit ();
                break;
            case "-1":inputError ();
                break;
            default:inputError ();
                break;

        }
    }

    private void sayFollowupCode () throws Exception {
        call.getPlayVoiceTool ().pardakhtAnjamShode ();
        call.getPlayVoiceTool ().zemnanShomarePeygirieshoma ();
        String refCode=correctNumberForPlay (call.getPan ().getReferenceCode ());
        call.getPlayVoiceTool ().sayPersianDigitsSeparate (refCode);
        call.getPlayVoiceTool ().mibashad ();
    }

    private void billPayment() throws Exception {
        if (getBillIDIsOK ()){
            if (getPaymentIDIsOK ()){
                if (confirmBillDataIsOK ()){
                    doBillPayment ();
                }
            }
        }
    }

    private void doBillPayment() throws Exception {
        if (getPanNumberIsOK ()){
            if(getPinOfPanIsOK ()){
                initAndSendBillPaymentRequest ();
                processBillPaymentResponse ();
            }
        }
    }

    private void processBillPaymentResponse () throws Exception {

        sayResultToCustomer ();
        subMenuOperations ();

    }

    private void sayResultToCustomer () throws Exception {
        int acCode=-1;
        if (isTrueNumber (call.getPan ().getActionCode ())){
            acCode= Integer.valueOf (call.getPan ().getActionCode ())  ;
        }
        switch (acCode){
            case 0:  sayFollowupCode ();
                break;
            case 5055:playActionCode ();
                break;
            case 9014:
                serverConnectionFailed ();
                break;
            case 9999:
                serverConnectionFailed ();
                break;
            default:playActionCode ();
        }
    }

    private void playActionCode () throws Exception {
        call.getPlayVoiceTool ().playActionCode (call.getPan ().getActionCode ());
    }

    private void initAndSendBillPaymentRequest () {
        call.setPan (new Pan ());
        call.getPan ().setCallerID (call.getCallerID ());
        call.getPan ().setPanNumber (panNumber);
        call.getPan ().setPin (pinNumber);
        call.getPan ().setBillID (billID);
        call.getPan ().setPaymentID (payID);
        call.getPan ().setAmountOfBill (amount);
        call.getPanFacade ().billPayment (call.getPan ());
    }

    private void followUp() throws Exception {

        if(getBillIDIsOK ()){
            if (getPaymentIDIsOK ()){
                doFollowup();
            }
        }
    }

    private void doFollowup () throws Exception {

        initAndSendFollowupRequest ();

        sayFollowUpResult ();

    }

    private void sayFollowUpResult () throws Exception {

       switch (call.getPan ().getActionCode ()){
           case Const.SUCCESS: playSuccessResponseOfFollowup ();
                break;
           case Const.SERVER_CONNECTION_ERROR:serverConnectionFailed ();
               break;
           case Const.SERVER_CONNECTION_ERROR2:serverConnectionFailed ();
               break;
           default:dataNotFound ();

       }
    }

    private void dataNotFound () throws IOException, AgiException {
        call.getPlayVoiceTool ().etelaatYaftNashod ();
    }

    private void serverConnectionFailed () throws Exception {
        call.getPlayVoiceTool ().khataDarErtebatBaServer ();
    }

    private void playSuccessResponseOfFollowup () throws Exception {
        call.getPlayVoiceTool ().darTarikhe ();
        call.getPlayVoiceTool ().sayDate (call.getPan ().getPaymentDate ());
        call.getPlayVoiceTool ().vaBaShomarePeygirie();
        String refCode = correctNumberForPlay (call.getPan ().getReferenceCode ());
        call.getPlayVoiceTool ().sayPersianDigitsSeparate (refCode);
        call.getPlayVoiceTool ().padakhtShodeAst();
    }

    private void initAndSendFollowupRequest () {
        call.getPan ().setCallerID (call.getCallerID ());
        call.getPan ().setBillID (billID);
        call.getPan ().setPaymentID (payID);
        call.getPanFacade ().followUp (call.getPan ());
    }


    private void topUp() throws Exception {
       new ServiceTopUp ().execute ();
    }

    private void inputError () throws Exception {
        call.getPlayVoiceTool ().notClear();
    }

    private void exit () throws Exception {
        call.getPlayVoiceTool ().byAndHangup ();
    }

    private boolean isTrueNumber(String s) {

        try{
            Long.valueOf(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }

    }

    private boolean checkDigit(int Len, String D, int Type) {
        int Sum = 0;
        int j = 2;

        try {
            int First = Integer.parseInt(D.substring(Len - Type - 1, Len - Type));

            for(int i = Len - 1 - Type; i >= 1; --i) {
                String N = D.substring(i - 1, i);
                int S = Integer.parseInt(N) * j;
                Sum += S;
                ++j;
                if(j == 8) {
                    j = 2;
                }
            }

            int R = Sum % 11;
            if(R != 0 && R != 1) {
                int var15 = 11 - R;
                if(var15 == First) {
                    return true;
                } else {
                    return false;
                }
            } else {
                byte Digit = 0;
                if(Digit == First) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (StringIndexOutOfBoundsException var14) {
            return false;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private boolean getBillIDIsOK() throws Exception {
        int getBillIDCount=0;
        boolean getBillIDIsOK=false;
        while ((!getBillIDIsOK) && (getBillIDCount<Const.MAX_GET_DTMF_MENU_COUNT)){
            billID=call.getPlayVoiceTool ().shenaseGhabzRaVaredNamaeid ();
            if (isTrueNumber (billID)&&(billID.length()!=Const.ZERO)&&(billID.length()==Const.MAX_BILL_LEN)){
                if (checkDigit(billID.length(),billID,Const.ZERO)){
                    getBillIDIsOK=true;
                }else{
                    call.getPlayVoiceTool ().notClear ();
                    getBillIDCount++;
                }
            }else{
                call.getPlayVoiceTool ().notClear ();
                getBillIDCount++;
            }
        }
        return getBillIDIsOK;
    }

    private boolean getPaymentIDIsOK() throws Exception {
        int getPaymentIDCount=0;
        boolean getPaymentIDIsOK=false;
        while ((!getPaymentIDIsOK) && (getPaymentIDCount<Const.MAX_GET_DTMF_MENU_COUNT)){
            payID=call.getPlayVoiceTool ().shenasePardakhtRaVaredNamaeid ().trim();
            if (isTrueNumber (payID)&&(payID.length()!=0)){
                if (checkDigit(payID.length(),payID,1)){
                    try{ amount=String.valueOf(Long.valueOf(payID.substring(0, payID.length() - 5) + "000"));}catch (Exception e){amount="0";}
                    getPaymentIDIsOK=true;
                }else{
                    call.getPlayVoiceTool ().notClear ();
                    getPaymentIDCount++;
                }
            }else{
                call.getPlayVoiceTool ().notClear ();
                getPaymentIDCount++;
            }
        }
        return getPaymentIDIsOK;
    }

    private boolean confirmBillDataIsOK() throws Exception {
        String confirmation="";

        call.getPlayVoiceTool ().mablaghe();
        call.getPlayVoiceTool ().sayPersianDigit (amount);
        call.getPlayVoiceTool ().rialBabateGhabze();
        call.getPlayVoiceTool ().sayBillKind (getBillType());
        call.getPlayVoiceTool ().vaShenaseGhabze();
        call.getPlayVoiceTool ().sayPersianDigitsSeparate (billID);
        call.getPlayVoiceTool ().bardashtKhahadShod ();
        confirmation=call.getPlayVoiceTool ().agarSahihAstAdade5 ();
        if (confirmation.trim().equals("5")) return true;
        else return false;
    }

    private boolean getPanNumberIsOK () throws Exception {

        int counterOfGetPan=1;
        while (counterOfGetPan<Const.MAX_TEL_BANK_MENU_COUNT){
            panNumber =call.getPlayVoiceTool ().shomareCardRaVaredNamaeid ();
            if (panNumber.length ()>= Const.MIN_PAN_LEN){
                if (isTrueNumber (panNumber)){
                    return true;
                }else{
                    call.getPlayVoiceTool ().shomareCardSahihNist ();
                }
            }else{
                call.getPlayVoiceTool ().shomareCardSahihNist ();
            }
            counterOfGetPan++;
        }
        return false;
    }

    private boolean getPinOfPanIsOK() throws Exception {
        int counterOfGetPass=1;
        while (counterOfGetPass<Const.MAX_TEL_BANK_MENU_COUNT) {
            pinNumber = call.getPlayVoiceTool ().ramzeCardRaVaredNamaeid ();
            if (pinNumber.length () < Const.MIN_PAN_PASS_LEN && pinNumber.length () > Const.MAX_PAN_PASS_LEN) {
                call.getPlayVoiceTool ().passNotValid ();
            } else {
                if (isTrueNumber (pinNumber)) {
                    return true;
                } else {
                    call.getPlayVoiceTool ().passNotValid ();
                }
            }
            counterOfGetPass++;
        }
        return false;
    }

    private  String  correctNumberForPlay(String number) {
        return String.valueOf(convertToNumber(number));
    }

    private  Long    convertToNumber(String number){
        try{
            Long longNumber=Long.valueOf(number);
            return longNumber;
        }catch (Exception e){
            return -1L;
        }
    }

    private  int     getBillType(){
        int intKind=-1;
        String kind= call.getStrUtils ().midString(billID,billID.length()-1,1);
        try{
            intKind=Integer.valueOf(kind);
            if (intKind==9){
                String  temp= call.getStrUtils ().midString(billID,billID.length()-4,3);
                if (!(temp.equals ("001") || temp.equals ("002"))){
                    return -1;
                }else{
                    return intKind;
                }
            }else {
                return intKind;
            }

        }catch (Exception e){
            return -1;
        }
    }

}
