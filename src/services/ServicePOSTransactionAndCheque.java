package services;

import model.Call;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hamid on 6/9/2017.
 */
public class ServicePOSTransactionAndCheque {

    private Call call;
    private Set posAndChequeMenu = new HashSet ();
    private int posAndChequeMenuCount =0;
    private String firstChoice;

    public ServicePOSTransactionAndCheque (Call call) {
        this.call=call;
    }

    public void execute(){
        call.setServicePOSTransactionAndCheque (this);
    }

    private void createMainMenu(){
        /*
            first say balance
            1:pos 30 transactions
            2:pos date to date transactions
            3:cheque transactions
            0:exit
            9:return pre menu
       */
        posAndChequeMenu.add("1");
        posAndChequeMenu.add("2");
        if (permittedAccountUseChequeService ())  posAndChequeMenu.add("3");
        posAndChequeMenu.add("0");
        posAndChequeMenu.add("9");

    }

    public  void sayMainMenu() throws Exception {

        String Choice="";
        while ((posAndChequeMenuCount <3)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(posAndChequeMenu,"pos_");
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
            posAndChequeMenuCount++;
        }
        exit ();
    }

    private boolean permittedAccountUseChequeService(){
        return call.getAccount ().getAccountType ().equals ("01");
    }

    private void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case  "1":
                break;
            case  "2":
                break;
            case  "3":
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
}
