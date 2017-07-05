package services;

import model.Call;
import model.Pan;
import org.asteriskjava.fastagi.AgiException;
import util.Const;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 7/5/2017.
 */
public class ServiceTopUp {

    private Call call;
    private Set topUpMenu = new HashSet ();
    private int topUpMenuCount =0;
    private String firstChoice;
    private String mobileNumber="";
    private String mobileOperatorFlag="0";
    private String chargeValue="0";
    private String panNumber="";
    private String pinNumber="";



    public ServiceTopUp(Call call){

      this.call=call;
    }

    public  void execute() throws Exception {
        call.setServiceTopUp (this);

        createMainMenu ();
        sayMainMenu ();
    }

    private void createMainMenu(){
        /*

            1:MCI
            2:MTN
            0:exit
            9:return pre menu
       */
        topUpMenu.add("1");
        topUpMenu.add("2");
        topUpMenu.add("9");
        topUpMenu.add("0");

    }

    public  void sayMainMenu() throws Exception {

        String Choice="";
        while ((topUpMenuCount < Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(topUpMenu,Const.MENU_PREFIX_TOP_UP);
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
            topUpMenuCount++;
        }
        exit ();
    }

    private void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case  "1":mciCharge ();
                break;
            case  "2":mtnCharge ();
                break;
            case  "9":goToBackMenu ();
                break;
            case  "0":exit ();
                break;
            case "-1":inputError ();
                break;
            default:inputError ();
                break;

        }
    }

    private void chargeValueCreateMainMenu(){
        /*

            1:10000
            2:20000
            3:50000
            4:100000
            5:200000
            0:exit
            9:return pre menu
       */
        topUpMenu.add("1");
        topUpMenu.add("2");
        topUpMenu.add("3");
        topUpMenu.add("4");
        topUpMenu.add("5");
        topUpMenu.add("0");
        topUpMenu.add("9");

    }

    public  void chargeValueSayMainMenu() throws Exception {

        String Choice="";
        while ((topUpMenuCount < Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(topUpMenu,Const.MENU_PREFIX_TOP_UP_CHARGE_VALUE);
            else {
                Choice=firstChoice;
                firstChoice="";
            }
            if (!Choice.equals(Const.INVALID_ENTRY_MENU)){
              chargeValueSelectSubMenu (Choice);
            }
            else{
                inputError ();
            }
            topUpMenuCount++;
        }
        exit ();
    }

    private void chargeValueSelectSubMenu(String Choice) throws Exception {

        chargeValue="0";
        switch (Choice){
            case  "1":chargeValue="10000";
                break;
            case  "2":chargeValue="20000";
                break;
            case  "3":chargeValue="50000";
                break;
            case  "4":chargeValue="100000";
                break;
            case  "5":chargeValue="200000";
                break;
            case  "9":sayMainMenu ();
                break;
            case  "0":exit ();
                break;
            case "-1":exit ();
                break;
              default:inputError ();
                break;

        }
        if (!chargeValue.equals ("0")){
            continueOperations ();
        }

    }

    private void continueOperations() throws Exception {

        if (confirmationIsOK ()){
          if (getPanNumberIsOK ()){
              if (getPinOfPanIsOK ()){
                 doTopUp();
                 processResponseOfTopUp ();
              }
          }
        }
    }

    private void doTopUp () {
        call.setPan (new Pan ());
        call.getPan ().setPanNumber (panNumber);
        call.getPan ().setPin (pinNumber);
        call.getPan ().setMobileNumber (mobileNumber);
        call.getPan ().setMobileOperator (mobileOperatorFlag);
        call.getPan ().setMobileChargeValue (chargeValue);
        call.getPan ().setCallerID (call.getCallerID ());
        call.getPanFacade ().topUp (call.getPan ());

    }

    private void processResponseOfTopUp() throws Exception {

        String actionCode = call.getPan ().getActionCode ();
         if (actionCode.equals (Const.SUCCESS)) {
             call.getPlayVoiceTool ().mobileChargeKhahadShod ();
         }else if (actionCode.equals (Const.NETWORK_ERROR)){
             call.getPlayVoiceTool ().khataDarErtebatBaServer ();
         }else{
             call.getPlayVoiceTool ().playActionCode (actionCode);
         }


    }

    private boolean confirmationIsOK () throws Exception {
        call.getPlayVoiceTool ().mobileVaredShodeBarabarAstBa ();
        call.getPlayVoiceTool ().sayMobileNo (mobileNumber);
        call.getPlayVoiceTool ().mablaghe ();
        call.getPlayVoiceTool ().sayAmount (chargeValue);

        return call.getPlayVoiceTool ().agarSahihAstAdade5 ().equals (Const.CONFIRMATION_DIGIT);

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

    private void inputError () throws Exception {
        call.getPlayVoiceTool ().notClear();
    }

    private void exit () throws Exception {
        call.getPlayVoiceTool ().byAndHangup ();
    }

    private void goToBackMenu () throws Exception {
      call.getServiceBillPayment ().sayMainMenu ();
    }

    private void mciCharge() throws Exception {
       mobileOperatorFlag=Const.MOBILE_OPERATOR_MCI_SIGN;
       startTopUpOperations ();
    }

    private void mtnCharge() throws Exception {
        mobileOperatorFlag=Const.MOBILE_OPERATOR_MTN_SIGN;
        startTopUpOperations ();
    }

    private void startTopUpOperations() throws Exception {

        if(getMobileIsOK ()){
            getChargeValue ();
        }

    }

    private void getChargeValue() throws Exception {
        chargeValueCreateMainMenu ();
        chargeValueSayMainMenu ();
    }

    private boolean getMobileIsOK() throws Exception {
        int getMobileCount=0;
        while (getMobileCount<Const.MAX_GET_DTMF_MENU_COUNT){
            mobileNumber=call.getPlayVoiceTool ().shomareMobileRaVaredKonid ();
            if (mobileIsCorrect (mobileNumber)){
                return true;
            }else{
                mobileNotValid ();
                getMobileCount++;
            }
        }
        return false;

    }

    private boolean isNumber(String entrance){
        try{
            Long.parseLong(entrance);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean  mobileIsCorrect(String mobileNumber){
        if (isNumber (mobileNumber)){
            if (mobileNumber.length ()==Const.MOBILE_NUMBER_LEN){
                return true;
            }
        }
        return false;
    }

    private void mobileNotValid () throws Exception {
        call.getPlayVoiceTool ().mobileDorostNist ();
    }

    private void errorOnOperations () throws Exception {
        call.getPlayVoiceTool ().error ();
    }

    private boolean isTrueNumber(String s) {

        try{
            Long.valueOf(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }

    }



}
