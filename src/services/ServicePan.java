package services;


import model.Call;
import model.Transaction;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import util.Const;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Hamid on 6/5/2017.
 */
public class ServicePan {

    private Call call;
    private Set mainMenuOfPan = new HashSet();
    private int    MainMenuCount=0;
    private String firstChoice="";

    public ServicePan(Call call) {
        this.call=call;
    }

    public void execute() throws Exception {
      call.setServicePan(this);
      sayLastBalance();
      createMainMenu();
      sayMainMenu();
    }

    private  void createMainMenu(){
        /*
        first say balance
        1:last balance
        2:5 last transaction
        3:fax report
        4:Hot Card
        7:SMS alarm settings
        9:exit
        0:exit
         */
        mainMenuOfPan.add("1");
        mainMenuOfPan.add("2");
        mainMenuOfPan.add("3");
        mainMenuOfPan.add("4");
        mainMenuOfPan.add("7");
        mainMenuOfPan.add("9");
        mainMenuOfPan.add("0");

    }

    public   void sayMainMenu() throws Exception {

        String Choice=null;

        while ((MainMenuCount< Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(mainMenuOfPan,Const.MENU_PREFIX_PAN);
            else {
                Choice=firstChoice;
                firstChoice="";
            }
            if (!Choice.equals(Const.INVALID_ENTRY_MENU)){
                selectSubMenu(Choice);
            }
            else{
                entryNotClear();
            }
            MainMenuCount++;
        }
        exit ();
    }

    private  void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case "1":sayLastBalance();
                break;
            case "2":sayLast5Transactions();
                break;
            case "3":faxReport();
                break;
            case "4":hotCard();
                break;
            case "7":smsOperations();
                break;
            case "9":exit ();
                break;
            case "0":exit ();
                break;
            case "-1":entryNotClear();
                break;
            default  :entryNotClear();
                break;

        }
    }

    private void sayLastBalance() throws Exception {

        String sign=call.getPan().getBalance().substring(0,1);
        if (sign.equals(Const.NEGATIVE_SIGN)) {
            sayBestankarBalance();
        }else{
            sayBedehkarBalance();
        }

    }

    private void sayBedehkarBalance() throws Exception {
        call.getPlayVoiceTool ().mojodieHesabeShoma();
        String balance="";
        balance=call.getPan().getBalance();
        call.getPlayVoiceTool ().sayPersianDigit(balance);
        call.getPlayVoiceTool ().mibashad();
    }

    private void sayBestankarBalance() throws Exception {
        call.getPlayVoiceTool ().mojodieHesabeShoma();
        String balance="";
        balance=call.getStrUtils().rightString(call.getPan().getBalance(),call.getPan().getBalance().length()-1);
        call.getPlayVoiceTool ().sayPersianDigit(balance);
        call.getPlayVoiceTool ().sayCurrency(Const.CURRENCY_RIAL_SIGN);
        call.getPlayVoiceTool ().bedehkarMibashad();
    }

    private void sayLast5Transactions() throws Exception {

        call.getPan().setKindOfFax(Const.ZERO);
        call.getPanFacade().getTransactions(call.getPan());
        if (call.getPan().getActionCode().equals(Const.SUCCESS)){
            doSuccessTransactionOperations();
        }else{
            call.getPlayVoiceTool ().khataDarErtebatBaServer();
        }

    }

    private void doSuccessTransactionOperations() throws Exception {
        List<Transaction> transactions=call.getPan().getTransactions();
        int i=Const.SAY_TRANSACTION_COUNT;
        if (transactions.size()>Const.ZERO){
            while ( i>=Const.ZERO){
                if (transactions.get(i).getAmount()!="" ){
                    sayTransaction(transactions, i);
                }
                i--;
            }
        }else{
            call.getPlayVoiceTool ().gardeshiMojodNist();
        }
    }

    private void sayTransaction(List<Transaction> transactions, int i) throws Exception {
        call.getPlayVoiceTool ().gardesheHesabeShoma();
        if (convertToNumber(transactions.get(i).getAmount())>=Const.ZERO){
            call.getPlayVoiceTool ().varize();
        }else{
            call.getPlayVoiceTool ().bardashte();
        }
        call.getPlayVoiceTool ().mablaghe();
        call.getPlayVoiceTool ().sayPersianDigit(correctNumberForPlay(transactions.get(i).getAmount()));
        call.getPlayVoiceTool ().sayCurrency(Const.CURRENCY_RIAL_SIGN);
        call.getPlayVoiceTool ().beTarikhe();
        call.getPlayVoiceTool ().sayDate(transactions.get(i).getfDescription());
        call.getPlayVoiceTool ().mibashad();
    }

    private void faxReport() throws Exception {
        new ServiceFaxReport(call).execute();
    }

    private void hotCard() throws Exception {

        String choice=call.getPlayVoiceTool().cardGheyreFaalMishavadAddade5();
        if (choice.equals(Const.CONFIRMATION_DIGIT)){
           call.getPanFacade().hotCard(call.getPan());
           String actionCode=call.getPan().getActionCode();
            switch (actionCode){
                case Const.SUCCESS:hotCardSuccess();
                    break;
                case Const.NETWORK_ERROR:outOfService();
                    break;
                case Const.SERVER_CONNECTION_ERROR:outOfService();
                    break;
                default:hotCardFailed();
            }

        }else{
            hotCardFailed();
        }
    }

    private void outOfService() throws Exception {
        call.getPlayVoiceTool().baArzePoozesh();
    }

    private void hotCardSuccess() throws Exception {
        call.getPlayVoiceTool().cardMasdodShod();
    }

    private void hotCardFailed() throws Exception {
        call.getPlayVoiceTool().cardMasdodNashod();
    }

    private void smsOperations () throws Exception {
        new ServiceSMS (call).execute ();
    }

    private void entryNotClear() throws Exception {
        call.getPlayVoiceTool ().notClear();
    }

    private void exit () throws Exception {
        call.getPlayVoiceTool ().byAndHangup ();
    }

    private  String correctNumberForPlay(String number) {
        return String.valueOf(convertToNumber(number));
    }

    private  Long   convertToNumber(String number){
        try{
            Long longNumber=Long.valueOf(number);
            return longNumber;
        }catch (Exception e){
            return -1L;
        }
    }


}
