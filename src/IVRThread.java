import model.Call;
import services.ServiceBillPayment;
import services.ServiceIBANAndBranches;
import services.ServiceTelBank;
import util.Voices;

/**
 * Created by Hamid on 6/30/2017.
 */
public    class   IVRThread extends Thread {
    private Call call=new Call();;
    private  String  inputByUser=null;
    public ServiceTelBank serviceTelBank=null;
    public ServiceBillPayment serviceBillPayment;
    public Voices Say=new  Voices();
    public ServiceIBANAndBranches serviceIBANAndBranches =null;

    int counterOfGetMenu=0;
    IVRThread(Call C) throws Exception {

        greeting();

    }
    private  void    greeting() throws Exception {


        while (counterOfGetMenu<3){
            getSelectedMenu();
            counterOfGetMenu++;
        }
        byAndHangUp();

    }

    private void getSelectedMenu() throws Exception {
        inputByUser= Say.playMainMenu();
        switch (inputByUser){
            case "1":startTelBankOperations();
            case "2":startBillPaymentOperations();
            case "3":startIBANOperations();
            default :entryIsInvalid();
        }
    }

    private  void    startTelBankOperations() throws Exception {
        serviceTelBank=new ServiceTelBank(call);
        serviceTelBank.execute();
    }
    private  void    startBillPaymentOperations() throws Exception {
        serviceBillPayment=new ServiceBillPayment(call);
        serviceBillPayment.execute();
    }
    private  void    startIBANOperations(){
        serviceIBANAndBranches =new ServiceIBANAndBranches (call);
        serviceIBANAndBranches.execute();
    }
    private void     entryIsInvalid() throws Exception {
        Say.shomareHesabSahihNist ();
    }

    private  void    byAndHangUp() throws Exception {
        System.gc();
        Say.byAndHangup();
    }
}