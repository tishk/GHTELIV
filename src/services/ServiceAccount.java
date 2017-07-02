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
    private String destinationAccount="";
    private String transferAmount="";
    private String newPin ="";

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
            case "4":fundTransfer ();
                break;
            case "5":changePin ();
                break;
            case "6":loanPayment ();
                break;
            case "7":smsOperations ();
                break;
            case "9":exit ();
                break;
            case "0":exit ();
                break;
            case "-1":call.getPlayVoiceTools().notClear();
                break;
            default:call.getPlayVoiceTools().notClear();
                break;

        }
    }

    private void exit () throws Exception {
        call.getPlayVoiceTools ().byAndHangup ();
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
        call.getPlayVoiceTools().sayPersianDigit(correctNumberForPlay(transactions.get(i).getAmount()));
        call.getPlayVoiceTools().sayCurrency("0");
        call.getPlayVoiceTools().beTarikhe();
        call.getPlayVoiceTools().sayDate(transactions.get(i).getfDescription());
        call.getPlayVoiceTools().mibashad();
    }

    private void faxReport() throws Exception {
        new ServiceFaxReport(call).execute();
    }

    private void fundTransfer() throws Exception {
        if (getDestinationAccountIsOK ()){
            if (getAmountIsOK ()){
                if (confirmFundTransferIsOK ()){
                    doFundTransfer ();
                }
            }
        }
    }

    private void doFundTransfer () throws Exception {
        sendRequestToServer ();
        sayResponseToCustomer ();
    }

    private void sendRequestToServer () {
        call.getAccount ().setDestinationAccount (destinationAccount);
        call.getAccount ().setAmountOfTransfer (transferAmount);
        call.getAccount ().setTransferType (Const.FUND_TRANSFER);
        call.getAccountFacade ().fundTransfer (call.getAccount ());
    }

    private void sayResponseToCustomer () throws Exception {
        int acCode=Integer.valueOf (call.getAccount ().getActionCode ());
        switch (acCode){

            case    0: successOperations ();
                break;
            case 9001:destinationAccountNotAvailable ();
                break;
            case 9008:destinationAccountNotAvailable ();
                break;
            case 9010:maxPaymentIsFull ();
                break;
            case 9300:accountNotRegistered ();
                break;
            case 939://TODO
                break;
            default://TODO

        }
    }

    private void accountNotRegistered () throws Exception {
        call.getPlayVoiceTools ().hesabSabtNashodeAst ();
    }

    private void maxPaymentIsFull () throws Exception {
        call.getPlayVoiceTools ().saghfePardakhtPorAst ();
    }

    private void destinationAccountNotAvailable () throws Exception {
        call.getPlayVoiceTools ().dastresiBeMaghsadMaghdorNist ();
    }

    private void successOperations () throws Exception {
        call.getPlayVoiceTools ().mablaghe ();
        call.getPlayVoiceTools ().sayPersianDigit (call.getAccount ().getAmountOfTransfer ());
        call.getPlayVoiceTools ().rial ();
        call.getPlayVoiceTools ().montaghelKhahadshod ();
        call.getPlayVoiceTools ().zemnanShomarePeygirieshoma ();
        call.getPlayVoiceTools ().sayPersianDigitsSeparate (call.getAccount ().getReferenceCode ());
        call.getPlayVoiceTools ().mibashad ();
    }

    private void changePin() throws Exception {
       if (getNewPinIsOk ()){
           sendChangePinRequestToServer ();
           processResponseOfChangePin ();
       }
    }

    private void processResponseOfChangePin () throws Exception {

        int acCode=Integer.valueOf (call.getAccount ().getActionCode ());
        switch (acCode){
            case    0:call.getPlayVoiceTools ().ramzTaghirKard ();
                break;
            case 9001:call.getPlayVoiceTools ().accountEntryInvalid ();
                break;
            case 9010:call.getPlayVoiceTools ().ramzTaghirNakard ();
                break;
            case 9999:call.getPlayVoiceTools ().baArzePoozesh ();
                break;
            case 5999:call.getPlayVoiceTools ().baArzePoozesh ();
                break;
            default://TODO

        }
    }

    private void sendChangePinRequestToServer () {
        call.getAccount ().setNewPin (newPin);
        call.getAccountFacade ().changePin (call.getAccount ());
    }

    private void loanPayment() throws Exception {
        new ServiceLoanPayment (call).execute ();
    }

    private void smsOperations () throws Exception {
        new ServiceSMS (call).execute ();
    }

    private  boolean isNumber(String entrance){
        try{
            Long.parseLong(entrance);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private  boolean getNewPinIsOk () throws Exception {
        int getPinCount=0;
        boolean getPinIsOk=false;
        String newPinRet="";
        while (!getPinIsOk && getPinCount<2){
            newPinRet=call.getPlayVoiceTools ().ramzeJadidRaVaredNamaeid ();
            if (newPinRet.length()!=4){
                if (newPinRet.length ()==0){
                    call.getPlayVoiceTools ().notClear ();
                }else{
                    call.getPlayVoiceTools ().shomareOboreMotabarNist ();
                }
                getPinCount++;
            }else{
                newPin= call.getPlayVoiceTools ().ramzeJadidRaMojadadanVaredNamaeid ();
                if (newPin.length()!=4){
                    if (newPin.length ()==0){
                        call.getPlayVoiceTools ().notClear ();
                    }else{
                        call.getPlayVoiceTools ().shomareOboreMotabarNist ();
                    }
                    getPinCount++;
                }else{
                    if (newPin.equals(newPinRet)){
                        getPinIsOk=true;
                    }else{
                        call.getPlayVoiceTools ().inDoShomareMotabeghatNadarad ();
                        getPinCount++;
                    }
                }
            }
        }
        return getPinIsOk;

    }

    private  boolean getDestinationAccountIsOK() throws Exception {
        int countOfGetAcc=0;
        boolean accEntered=false;
        while (!accEntered && countOfGetAcc<2){
            destinationAccount=call.getPlayVoiceTools ().shomareHesabeMaghsadRaVaredNamaeid ();
            if (destinationAccount.length()==0){
                call.getPlayVoiceTools ().notClear ();
                countOfGetAcc++;
            }else{
                if (isNumber (destinationAccount)){
                    accEntered=true;
                }else{
                    call.getPlayVoiceTools ().accountEntryInvalid ();
                    countOfGetAcc++;
                }
            }
        }
        return accEntered;
    }

    private  boolean getAmountIsOK() throws Exception {
        int countOfGetAmount=0;
        boolean amountEntred=false;
        while (!amountEntred && countOfGetAmount<2){
            transferAmount=call.getPlayVoiceTools ().lotfanMablaghRaVaredNamaeid ();
            if (transferAmount.trim().length()==0){
                call.getPlayVoiceTools ().notClear ();
                countOfGetAmount++;
            }else{
                if (isNumber (transferAmount)){
                    amountEntred=true;
                }else{
                    call.getPlayVoiceTools ().notClear ();
                    countOfGetAmount++;
                }
            }
        }
        return amountEntred;
    }

    private  boolean confirmFundTransferIsOK() throws Exception {
        String confirmation="";
        call.getPlayVoiceTools().mablaghe();
        call.getPlayVoiceTools().sayPersianDigit (transferAmount);
        call.getPlayVoiceTools().rial();
        call.getPlayVoiceTools().bardashVaBeHesabe();
        call.getPlayVoiceTools().sayPersianDigitsSeparate (destinationAccount);
        call.getPlayVoiceTools().varizKhahadShod();
        confirmation=call.getPlayVoiceTools().agarSahihAstAdade5 ();

        if (confirmation.trim().equals("5")) return true;
        else return false;
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
