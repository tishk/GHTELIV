package services;

import model.Account;
import model.Call;
import model.Pan;
import util.Const;

import java.io.IOException;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceTelBank {

    private Call   call;
    private boolean maybeIsPan =false;
    private boolean maybeIsAccount =false;
    private boolean isNotEnterAny=false;
    private boolean beanNotOK=false;
    private int counterOfGetMenu=0;
    private String entrance=null;
    private String panPass=null;
    private String accountPass=null;
    public ServiceAccount serviceAccount=null;
    public ServicePan servicePan=null;
    public Account account=null;
    public Pan pan=null;

    public ServiceTelBank(Call call) {

        this.call=call;
        this.call.setServiceTelBank(this);

    }

    public  void execute() throws Exception {

        doOperation();

    }

    private void doOperation() throws Exception {

        while (counterOfGetMenu<Const.MAX_TEL_BANK_MENU_COUNT){
            enterAccountOrPan();
            setKindOfEntrance();
            if (doSwitchPanOrAccountSuccess()){
                break;
            }else{
                counterOfGetMenu++;
            }
        }
        System.gc();
    }

    private void enterAccountOrPan() throws Exception {
        entrance=call.getPlayVoiceTools().enterAccountOrPanNumber();
        call.setUserEntrance(entrance);
    }

    private boolean doSwitchPanOrAccountSuccess() throws Exception {

        if (isAccount()){
            return doAccountOperationsResult();
        }else if (isPan()){
            return doPanOperationsResult();
        }else if (isNotEntranceHappened()){
            doNotClearOperations();
            return false;
        }else if (isIncorrectEntrance()){
            doIncorrectEntranceOperations();
            return false;
        }else{
            return false;
        }
    }
    private boolean doPanOperationsResult() throws Exception {
        if (authenticateOfPanISOK()){
            call.getPlayVoiceTools().advertisement();
            servicePan=new ServicePan(call);
            servicePan.execute();
            return true;
        }else{
            return false;
        }
    }
    private boolean doAccountOperationsResult() throws Exception {
        if (authenticateOfAccountISOK()){
            call.getPlayVoiceTools().advertisement();
            serviceAccount=new ServiceAccount(call);
            serviceAccount.execute();
            return true;
        }else{
            return false;
        }
    }

    private void doIncorrectEntranceOperations() throws Exception {
        if (beanNotOK){
            call.getPlayVoiceTools().panEntryInvalid();
        }else {
            call.getPlayVoiceTools().accountEntryInvalid();
        }
    }
    private void doNotClearOperations() throws Exception {
        call.getPlayVoiceTools().notClear();
    }
    private void setKindOfEntrance() throws IOException {

        if (call.getUserEntrance().length()==Const.ZERO){
            maybeIsPan =false;
            maybeIsAccount =false;
            isNotEnterAny=true;
        } else if (call.getUserEntrance().length()>=Const.MIN_PAN_LEN){
            maybeIsPan =true;
            maybeIsAccount =false;
            isNotEnterAny=false;
        } else if (call.getUserEntrance().length()>=Const.MIN_ACCOUNT_LEN && call.getUserEntrance().length()<=Const.MAX_ACCOUNT_LEN){
            maybeIsPan =false;
            maybeIsAccount =true;
            isNotEnterAny=false;
        } else{
            maybeIsPan =false;
            maybeIsAccount =false;
            isNotEnterAny=false;
        }

    }

    private boolean isNotEntranceHappened(){

        return (!maybeIsPan && !maybeIsAccount && isNotEnterAny);

    }
    private boolean isIncorrectEntrance(){
        return (!maybeIsPan && !maybeIsAccount && !isNotEnterAny);
    }
    private boolean isAccount(){

        if (maybeIsAccount){
           return isNumber(entrance);
        }else{
            return false;
        }
    }
    private boolean isPan(){

        if (maybeIsPan){
            if (beanIsOK()){
                return isNumber(entrance);
            }else{
                beanNotOK=true;
                return false;
            }
        }else{
            return false;
        }
    }
    private boolean beanIsOK(){
        return entrance.substring(0,6).equals(Const.BEAN_OF_PAN);
    }
    private boolean isNumber(String entrance){
        try{
            Long.parseLong(entrance);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    private boolean authenticateOfPanISOK() throws Exception {
        panPass=call.getPlayVoiceTools().enterAccountPassword();
        if (panPass.length()<Const.MIN_PAN_PASS_LEN && panPass.length()>Const.MAX_PAN_PASS_LEN){
            call.getPlayVoiceTools().passNotValid();
            return false;
        }else{
            if (isNumber(panPass)){
                return getResultOfPanAuthenticateRequestToServer();
            }else{
                call.getPlayVoiceTools().passNotValid();
                return false;
            }
        }
    }
    private boolean getResultOfPanAuthenticateRequestToServer() throws Exception {
        pan=new Pan();
        pan.setPanNumber(entrance);
        pan.setPin(panPass);
        pan.setCallerID(call.getCallerID());
        call.getPanFacade().getBalance(pan);
        if (pan.getActionCode().equals(Const.SUCCESS)){
            call.setPan(pan);
            return true;
        }else{
            if (Integer.valueOf(pan.getActionCode())<100){
                int actCode=Const.PREFIX_ACTION_CODE+Integer.valueOf(pan.getActionCode());
                pan.setActionCode(String.valueOf(actCode));
            }
            call.getPlayVoiceTools().playActionCode(pan.getActionCode());
            return false;
        }

    }
    private boolean authenticateOfAccountISOK() throws Exception {

        accountPass=call.getPlayVoiceTools().enterPanPassword();
        if (accountPass.length()<Const.MIN_ACCOUNT_PASS_LEN && accountPass.length()>Const.MAX_ACCOUNT_PASS_LEN){
            call.getPlayVoiceTools().passNotValid();
            return false;
        }else{
            if (isNumber(accountPass)){
                return getResultOfAccountAuthenticateRequestToServer();
            }else{
                call.getPlayVoiceTools().passNotValid();
                return false;
            }
        }

    }
    private boolean getResultOfAccountAuthenticateRequestToServer() throws Exception {
        account=new Account();
        account.setAccountNumber(entrance);
        account.setPin(accountPass);
        account.setCallerID(call.getCallerID());
        call.getAccountFacade().getBalance(account);
        if (account.getActionCode().equals(Const.SUCCESS)){
            call.setAccount(account);
            return true;
        }else{
            if (Integer.valueOf(account.getActionCode())<100){
                int actCode=Const.PREFIX_ACTION_CODE+Integer.valueOf(account.getActionCode());
                account.setActionCode(String.valueOf(actCode));
            }
            call.getPlayVoiceTools().playActionCode(account.getActionCode());
            return false;
        }

    }


}
