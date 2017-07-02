package services;

import model.Call;

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
    public ServiceLoanPayment (Call call) {
        this.call=call;
    }
    public void execute(){
        call.setServiceLoanPayment (this);
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
                call.getPlayVoiceTools().notClear();
            }
            MainMenuCount++;
        }
        call.getPlayVoiceTools().byAndHangup();
    }

    private  void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case "1":
                break;
            case "2":
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


}
