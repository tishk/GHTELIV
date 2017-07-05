package services;

import dao.BranchDaoImpl;
import model.Account;
import model.Branch;
import model.Call;
import util.Const;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceIBANAndBranches {

    private Call call;
    private String accountNumber="";
    private String branchCode="";
    private Set menu = new HashSet ();
    private int menuCount =0;
    Branch branch;
    private String firstChoice;


    public ServiceIBANAndBranches (Call call) {
        this.call=call;
    }

    public void execute() throws Exception {

      call.setServiceIBANAndBranches (this);
      createMainMenu ();
      sayMainMenu ();

    }


    private void createMainMenu(){
        /*

            1:get IBAN
            2:say branches data
            0:exit
            9:return pre menu
       */
        menu.add("1");
        menu.add("2");
        menu.add("9");
        menu.add("0");

    }

    public  void sayMainMenu() throws Exception {

        String Choice="";
        while ((menuCount <Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(menu,Const.MENU_PREFIX_IBAN_AND_BRANCH);
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
            menuCount++;
        }
        exit ();
    }

    private void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case  "1":doIBANOperation ();
                break;
            case  "2":sayBranchDate();
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

    private void sayBranchDate () throws Exception {

        if (getBranchCodeIsOK ()){
              doBranchDataPlay();
        }
    }

    private void doBranchDataPlay () {

        initAndSendBranchDataToServer ();

        playBranchDataFromDB();



    }

    private void playBranchDataFromDB () {

    }

    private void initAndSendBranchDataToServer () {
        branch=new Branch ();
        branch.setBranchCode (branchCode);
        BranchDaoImpl.getInstance ().getBranchData (branch);
    }

    private boolean getBranchCodeIsOK () throws Exception {
        int counterOfGetBranchCode=1;
        while (counterOfGetBranchCode<Const.MAX_TEL_BANK_MENU_COUNT){
            branchCode=call.getPlayVoiceTool ().codeShobeRaVaredNamaeid ();

            if (branchCode.length ()>= Const.MIN_BRANCH_CODE_LEN ||
                    branchCode.length ()<= Const.MAX_BRANCH_CODE_LEN){
                if (isTrueNumber (branchCode)){
                    branchCode=call.getStrUtils ().fixLengthWithZero (branchCode,5);
                    return true;
                }else{
                    call.getPlayVoiceTool ().notClear ();
                }
            }else{
                call.getPlayVoiceTool ().notClear ();
            }
            counterOfGetBranchCode++;
        }

        return false;
    }

    private void inputError () throws Exception {
        call.getPlayVoiceTool ().notClear();
    }

    private void exit () throws Exception {
        call.getPlayVoiceTool ().byAndHangup ();
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
