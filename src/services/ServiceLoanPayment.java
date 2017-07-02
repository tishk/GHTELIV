package services;

import model.Call;
import util.Const;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServiceLoanPayment {

    private Call call;
    private Set installmentMenu = new HashSet ();
    private int    MainMenuCount=0;
    private String firstChoice="";
    private String destinationLoanAccount="";
    private String amountOfLoan="";

    public ServiceLoanPayment (Call call) {
        this.call=call;
    }

    public   void execute() throws Exception {
        call.setServiceLoanPayment (this);
        if (getDestinationLoanAccountIsOK ()){
            createMainMenu ();
            sayMainMenu ();
        }
    }

    private  void createMainMenu(){
        /*
            first say balance
            1:facility inquiry
            2:loan Payment
            0:exit
            9:return pre menu
       */
        installmentMenu.add("1");
        installmentMenu.add("2");
        installmentMenu.add("9");
        installmentMenu.add("0");

    }

    public   void sayMainMenu() throws Exception {

        String Choice="";
        while ((MainMenuCount<3)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTools().sayMenu(installmentMenu,"PB_");
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
            MainMenuCount++;
        }
       exit ();
    }

    private  void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case "1":getLoanStatus ();
                break;
            case "2":paymentLoan ();
                break;
            case "9":goToBackMenu ();
                break;
            case "0":exit ();
                break;
            case "-1":inputError ();
                break;
            default:inputError ();
                break;

        }
    }

    private void goToBackMenu () throws Exception {
        call.getServiceAccount ().sayMainMenu ();
    }

    private  boolean getAmountOfLoanIsOK () throws Exception {
        int countOfGetAmount=0;
        boolean amountEntred=false;
        while (!amountEntred && countOfGetAmount<2){
            amountOfLoan=call.getPlayVoiceTools ().mablagheGhestRaVaredNamaeid ();
            if (amountOfLoan.trim().length()==0){
                call.getPlayVoiceTools ().notClear ();
                countOfGetAmount++;
            }else{
                if (isNumber (amountOfLoan)){
                    amountEntred=true;
                }else{
                    call.getPlayVoiceTools ().notClear ();
                    countOfGetAmount++;
                }
            }
        }
        return amountEntred;
    }

    private  boolean confirmLoanPaymentIsOK () throws Exception {
        String confirmation="";
        call.getPlayVoiceTools().mablaghe();
        call.getPlayVoiceTools().sayPersianDigit (amountOfLoan);
        call.getPlayVoiceTools().rial();
        call.getPlayVoiceTools().bardashVaBeHesabe();
        call.getPlayVoiceTools().sayPersianDigitsSeparate (destinationLoanAccount);
        call.getPlayVoiceTools().varizKhahadShod();
        confirmation=call.getPlayVoiceTools().agarSahihAstAdade5 ();

        if (confirmation.trim().equals("5")) return true;
        else return false;
    }

    private  boolean getDestinationLoanAccountIsOK() throws Exception {
        int countOfGetAcc=0;
        boolean accEntered=false;
        while (!accEntered && countOfGetAcc<2){
            destinationLoanAccount=call.getPlayVoiceTools ().shomareHesabePardakhteGhestRaVaredNamaeid ();
            if (destinationLoanAccount.length()==0){
                call.getPlayVoiceTools ().notClear ();
                countOfGetAcc++;
            }else{
                if (isNumber (destinationLoanAccount)){
                    accEntered=true;
                }else{
                    call.getPlayVoiceTools ().accountEntryInvalid ();
                    countOfGetAcc++;
                }
            }
        }
        return accEntered;
    }

    private  void getLoanStatus() throws Exception {
        call.getAccountFacade ().getLoanStatus (call.getAccount ());
        int acCode=Integer.valueOf (call.getAccount ().getActionCode ());
        switch (acCode){
            case    0:playLoanStatus ();
                break;
            case 9001:destinationAccountNotAvailable ();
                break;
            case 9008:destinationAccountNotAvailable ();
                break;
            case 9010:maxPaymentIsFull ();
                break;
            case 9300:accountNotRegistered ();
                break;
            case 939 :serverNotAvailable ();
                break;
            default://TODO
        }
    }

    private  void serverNotAvailable () throws Exception {
        call.getPlayVoiceTools ().baArzePoozesh ();
    }

    private  void playLoanStatus () throws Exception {
        call.getPlayVoiceTools ().mablagheGhesteInDore ();
        call.getPlayVoiceTools ().sayPersianDigit (call.getAccount ().getLoanAmount ());
        call.getPlayVoiceTools ().rial ();
        call.getPlayVoiceTools ().mandeBedehiShoma ();
        call.getPlayVoiceTools ().sayPersianDigit (correctNumberForPlay (call.getAccount ().getBalanceOfLoanDebt ()));
        call.getPlayVoiceTools ().rialMibashad ();
    }

    private  void paymentLoan() throws Exception {

       if (getAmountOfLoanIsOK ()){
          if (confirmLoanPaymentIsOK ()){
             doLoanPayment ();
          }
       }

    }

    private  void doLoanPayment () throws Exception {
        sendRequestToServer ();
        sayResponseToCustomer ();
    }

    private  void sendRequestToServer () {
        call.getAccount ().setDestinationAccount (destinationLoanAccount);
        call.getAccount ().setAmountOfTransfer (amountOfLoan);
        call.getAccount ().setTransferType (Const.LOAN_PAYMENT);
        call.getAccountFacade ().fundTransfer (call.getAccount ());
    }

    private  void sayResponseToCustomer () throws Exception {
        int acCode=Integer.valueOf (call.getAccount ().getActionCode ());
        switch (acCode){

            case    0:successOperations ();
                break;
            case 9001:destinationAccountNotAvailable ();
                break;
            case 9008:destinationAccountNotAvailable ();
                break;
            case 9010:maxPaymentIsFull ();
                break;
            case 9300:accountNotRegistered ();
                break;
            case 939 :serverNotAvailable ();
                break;
            default://TODO

        }

    }

    private  void accountNotRegistered () throws Exception {
        call.getPlayVoiceTools ().hesabSabtNashodeAst ();
    }

    private  void maxPaymentIsFull () throws Exception {
        call.getPlayVoiceTools ().saghfePardakhtPorAst ();
    }

    private  void destinationAccountNotAvailable () throws Exception {
        call.getPlayVoiceTools ().dastresiBeMaghsadMaghdorNist ();
    }

    private  void successOperations () throws Exception {
        call.getPlayVoiceTools ().mablaghe ();
        call.getPlayVoiceTools ().sayPersianDigit (call.getAccount ().getAmountOfTransfer ());
        call.getPlayVoiceTools ().rial ();
        call.getPlayVoiceTools ().montaghelKhahadshod ();
        call.getPlayVoiceTools ().zemnanShomarePeygirieshoma ();
        call.getPlayVoiceTools ().sayPersianDigitsSeparate (call.getAccount ().getReferenceCode ());
        call.getPlayVoiceTools ().mibashad ();
    }

    private  boolean isNumber(String entrance){
        try{
            Long.parseLong(entrance);
            return true;
        }catch (Exception e){
            return false;
        }
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

    private void inputError () throws Exception {
        call.getPlayVoiceTools().notClear();
    }

    private void exit () throws Exception {
        call.getPlayVoiceTools ().byAndHangup ();
    }


}
