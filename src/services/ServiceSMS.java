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

    public ServiceSMS(Call call) {
        this.call=call;
    }

    public void execute() throws Exception {
       call.setServiceSMS(this);
       if (getAccountStatusIsSuccessful ()){
           createMainMenu ();
           sayMainMenu ();
       }
    }

    private  void createMainMenu(){
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

    public   void sayMainMenu() throws Exception {

        String Choice="";
        while ((smsMenuCount <3)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTools().sayMenu(smsMenu,"sms_");
            else {
                Choice=firstChoice;
                firstChoice="";
            }
            if (!Choice.equals("-1")){
                selectSubMenu(Choice);
            }
            else{
                inputError ();
            }
            smsMenuCount++;
        }
        exit ();
    }

    private  void selectSubMenu(String Choice) throws Exception {

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
        call.getPlayVoiceTools().notClear();
    }

    private void exit () throws Exception {
        call.getPlayVoiceTools ().byAndHangup ();
    }

    private void goToBackMenu () throws Exception {
        call.getServiceAccount ().sayMainMenu ();
    }

    private void registerMobileNumber() throws Exception {

        if (deleteMobileNumber ()){

        }else{
            errorOnOperations ();
        }

    }

    private boolean errorOnOperations () throws Exception {
        return call.getPlayVoiceTools ().error ();
    }

    private boolean deleteMobileNumber(){
        call.getAccountFacade ().smsAlarmDelete (call.getAccount ());
        if (call.getAccount ().getActionCode ().equals (Const.SUCCESS)){
            return true;
        }else{
            return false;
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
    private  boolean isNumber(String entrance){
        try{
            Long.parseLong(entrance);
            return true;
        }catch (Exception e){
            return false;
        }
    }





}
