package services;

import model.Call;
import model.Transaction;
import util.Const;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Hamid on 6/5/2017.
 */
public class ServiceAccount {

    private Call   call;
    private Set mainMenu = new HashSet();
    private int    MainMenuCount=0;
    private String firstChoice="";

    public ServiceAccount(Call call) {
        this.call=call;
    }

    public void execute() throws Exception {
        call.setServiceAccount(this);
        correctAccountLenWithZero();
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
        4:fund Transfer
        5:change pin
        6:installment payment
        7:SMS alarm settings
        8:cheque and pos
        9:exit
        0:exit
         */
        mainMenu.add("1");
        mainMenu.add("2");
        mainMenu.add("3");
        if (call.getAccount().getAccountType().equals("01")) mainMenu.add("4");
        mainMenu.add("5");
        if (call.getAccount().getAccountType().equals("01")) mainMenu.add("6");
        mainMenu.add("7");
        mainMenu.add("8");
        mainMenu.add("9");
        mainMenu.add("0");

    }

    public   void sayMainMenu() throws Exception {

        String Choice=null;



        while ((MainMenuCount<3)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTools().sayMenu(mainMenu,"AccMenu_");
            else {
                Choice=firstChoice;
                firstChoice="";
            }
            if (!Choice.equals("-1")){
                selectSubMenu(Choice);
            }
            else{
                call.getPlayVoiceTools().notClear();
            }
            MainMenuCount++;
        }
        call.getPlayVoiceTools().byAndHangup();
    }

    private  void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case "1":sayLastBalance();
                break;
            case "2":sayLast5Transactions();
                break;
            case "3":faxReport();
                break;
            case "4":
                break;
            case "5":
                break;
            case "6":
                break;
            case "7":
                break;
            case "9":
                break;
            case "0":
                break;
            case "-1":call.getPlayVoiceTools().notClear();
                break;
            default:call.getPlayVoiceTools().notClear();
                break;

        }
    }


    private void correctAccountLenWithZero() {
        call.getAccount().setAccountNumber(call.getStrUtils().fixLengthWithZero(call.getAccount().getAccountNumber(), Const.MAX_ACCOUNT_LEN));
    }

    private void sayLastBalance() throws Exception {

        String sign=call.getAccount().getBalance().substring(0,1);
        if (sign.equals(Const.NEGATIVE_SIGN)) {
            sayBestankarBalance();
        }else{
            sayBedehkarBalance();
        }

    }

    private void sayBedehkarBalance() throws Exception {
        call.getPlayVoiceTools().mojodieHesabeShoma();
        String balance="";
        balance=call.getAccount().getBalance();
        call.getPlayVoiceTools().sayPersianDigit(balance);
        call.getPlayVoiceTools().mibashad();
    }

    private void sayBestankarBalance() throws Exception {
        call.getPlayVoiceTools().mojodieHesabeShoma();
        String balance="";
        balance=call.getStrUtils().rightString(call.getAccount().getBalance(),call.getAccount().getBalance().length()-1);
        call.getPlayVoiceTools().sayPersianDigit(balance);
        call.getPlayVoiceTools().sayCurrency("0");
        call.getPlayVoiceTools().bedehkarMibashad();
    }

    private void sayLast5Transactions() throws Exception {

        call.getAccount().setKindOfFax(Const.ZERO);
        call.getAccountFacade().getTransactions(call.getAccount());
        if (call.getAccount().getActionCode().equals(Const.SUCCESS)){
            doSuccessTransactionOperations();
        }else{
            call.getPlayVoiceTools().khataDarErtebatBaServer();
        }

    }

    private void doSuccessTransactionOperations() throws Exception {
        List<Transaction> transactions=call.getAccount().getTransactions();
        int i=4;
        if (transactions.size()>0){
            while ( i>=0){
                if (transactions.get(i).getAmount()!="" ){
                    sayTransaction(transactions, i);
                }
                i--;
            }
       }else{
            call.getPlayVoiceTools().gardeshiMojodNist();
       }
    }

    private void sayTransaction(List<Transaction> transactions, int i) throws Exception {
        call.getPlayVoiceTools().gardesheHesabeShoma();
        if (convertToNumber(transactions.get(i).getAmount())>=0){
            call.getPlayVoiceTools().varize();
        }else{
            call.getPlayVoiceTools().bardashte();
        }
        call.getPlayVoiceTools().mablaghe();
        call.getPlayVoiceTools().sayPersianDigit(correctNumberForPlay(transactions.get(i).getAmount(), i));
        call.getPlayVoiceTools().sayCurrency("0");
        call.getPlayVoiceTools().beTarikhe();
        call.getPlayVoiceTools().sayDate(transactions.get(i).getfDescription());
        call.getPlayVoiceTools().mibashad();
    }

    private void faxReport() throws Exception {
        new ServiceFaxReport(call).execute();
    }


    private String correctNumberForPlay(String number, int i) {
        return String.valueOf(convertToNumber(number));
    }

    private Long   convertToNumber(String number){
        try{
         Long longNumber=Long.valueOf(number);
         return longNumber;
        }catch (Exception e){
            return -1L;
        }
    }


}
