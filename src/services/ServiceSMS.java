package services;

import model.Call;
import util.Const;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hamid on 6/5/2017.
 */
public class ServiceSMS {

    private Call call;
    private Set smsMenu = new HashSet ();
    private int smsMenuCount =0;
    private String firstChoice;
    private String mobileNumber="";

    public ServiceSMS(Call call) {
        this.call=call;
    }

    public  void execute() throws Exception {
       call.setServiceSMS(this);
       if (getAccountStatusIsSuccessful ()){
           createMainMenu ();
           sayMainMenu ();
       }
    }

    private void createMainMenu(){
        /*
            first say balance
            1:register mobile number
            2:delete mobile number
            0:exit
            9:return pre menu
       */
        smsMenu.add("1");
        if (accountRegistered ()) smsMenu.add("2");
        smsMenu.add("9");
        smsMenu.add("0");

    }

    public  void sayMainMenu() throws Exception {

        String Choice="";
        while ((smsMenuCount <Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(smsMenu,"sms_");
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
            smsMenuCount++;
        }
        exit ();
    }

    private void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case  "1":registerMobileNumber ();
                break;
            case  "2":deleteMobileNumber ();
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

    private void inputError () throws Exception {
        call.getPlayVoiceTool ().notClear();
    }

    private void exit () throws Exception {
        call.getPlayVoiceTool ().byAndHangup ();
    }

    private void goToBackMenu () throws Exception {
        call.getServiceAccount ().sayMainMenu ();
    }

    private void registerMobileNumber() throws Exception {
        if (accountRegistered ()){
            if (deleteMobileHappened ()){
                doRegister ();
            }else{
                errorOnOperations ();
            }
        }else{
            doRegister ();
        }

    }

    private void doRegister () throws Exception {
        int getMobileCount=0;
        boolean mobileEnteredIsCorrect=false;
        while (!mobileEnteredIsCorrect && getMobileCount<3){
            mobileNumber=call.getPlayVoiceTool ().shomareMobileRaVaredKonid ();
            if (mobileIsCorrect (mobileNumber)){
                mobileEnteredIsCorrect=true;
            }else{
                mobileNotValid ();
                getMobileCount++;
            }
        }
        if (mobileEnteredIsCorrect){
            call.getAccount ().setMobileNumber (mobileNumber);
            call.getAccountFacade ().smsAlarmRegister (call.getAccount ());
            if (call.getAccount ().getActionCode ().equals (Const.SUCCESS)){
                call.getPlayVoiceTool ().baMovafaghiatSabtShod ();
            }else{
                errorOnOperations ();
            }
        }
    }

    private void mobileNotValid () throws Exception {
        call.getPlayVoiceTool ().mobileDorostNist ();
    }

    private void errorOnOperations () throws Exception {
         call.getPlayVoiceTool ().error ();
    }

    private void deleteMobileNumber() throws Exception {
        if (deleteMobileHappened ()){
            call.getPlayVoiceTool ().baMovafaghiatHazfShod ();
        }else{
            errorOnOperations ();
        }
    }

    private boolean getAccountStatusIsSuccessful (){

        call.getAccountFacade ().smsAlarmSelect (call.getAccount ());
        if (call.getAccount ().getActionCode ().equals (Const.SUCCESS)){
            return true;
        }else{
            return false;
        }
    }

    private boolean accountRegistered(){

        return isNumber (call.getAccount ().getMobileNumber ());
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

    private boolean deleteMobileHappened(){
        call.getAccountFacade ().smsAlarmDelete (call.getAccount ());
        if (call.getAccount ().getActionCode ().equals (Const.SUCCESS)){
            return true;
        }else{
            return false;
        }
    }



}
