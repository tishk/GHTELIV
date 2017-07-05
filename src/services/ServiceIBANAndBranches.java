package services;

import model.Account;
import model.Call;
import util.Const;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceIBANAndBranches {

    private Call call;
    private String accountNumber="";

    public ServiceIBANAndBranches (Call call) {
        this.call=call;
    }

    public void execute() throws Exception {

      call.setServiceIBANAndBranches (this);
      doIBANOperation ();

    }

    private void doIBANOperation() throws Exception {

       if (getAccountNumberIsOK ()){
           initAndSendRequest ();
           processResponseFromServer ();
       }

    }

    private void processResponseFromServer () throws Exception {
        String actionCode = call.getAccount ().getActionCode ();
        if (actionCode.equals (Const.SUCCESS)){
            sayIBAN ();
        }else if (actionCode.equals ("9001")){
            accountNumberNotValid ();
        }else if (actionCode.equals ("5999") || actionCode.equals ("9999")){
            sorrySystemNotAvailable ();
        }else{
            sorrySystemNotAvailable ();
        }
    }

    private void initAndSendRequest () {
        call.setAccount (new Account ());
        call.getAccount ().setAccountNumber (accountNumber);
        call.getAccount ().setCallerID (call.getCallerID ());
        call.getAccountFacade ().getIBAN (call.getAccount ());
    }

    private void sorrySystemNotAvailable () throws Exception {
        call.getPlayVoiceTool ().baArzePoozesh ();
    }

    private void accountNumberNotValid () throws Exception {
        call.getPlayVoiceTool ().shomareHesabSahihNist ();
    }

    private void sayIBAN () throws Exception {
        call.getPlayVoiceTool ().shabaDescription ();
        call.getPlayVoiceTool ().shomareShabaieshoma ();
        call.getPlayVoiceTool ().ir ();
        call.getPlayVoiceTool ().saySeparateDigits (call.getAccount ().getIBAN ());
        //repeat
        call.getPlayVoiceTool ().shomareShabaieshoma ();
        call.getPlayVoiceTool ().ir ();
        call.getPlayVoiceTool ().saySeparateDigits (call.getAccount ().getIBAN ());
        call.getPlayVoiceTool ().mibashad ();
    }

    private boolean isTrueNumber(String s) {

        try{
            Long.valueOf(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }

    }

    private boolean getAccountNumberIsOK () throws Exception {

        int counterOfGetAccount=1;
        while (counterOfGetAccount<Const.MAX_TEL_BANK_MENU_COUNT){
            accountNumber =call.getPlayVoiceTool ().shomareHesabRaVaredNamaeid ();
            if (accountNumber.length ()< Const.MIN_PAN_LEN){
                if (isTrueNumber (accountNumber)){
                    return true;
                }else{
                    call.getPlayVoiceTool ().shomareHesabSahihNist ();
                }
            }else{
                call.getPlayVoiceTool ().shomareHesabSahihNist ();
            }
            counterOfGetAccount++;
        }
        return false;
    }




}
