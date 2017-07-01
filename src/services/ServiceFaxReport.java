package services;

import model.Call;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hamid on 6/5/2017.
 */
public class ServiceFaxReport {

    private Call call;
    private Set faxMainMenu = new HashSet();
    private int    MainMenuCount=0;
    private String firstChoice="";

    public ServiceFaxReport(Call call) {
        this.call=call;
    }
    public void execute() throws Exception {
        call.setServiceFaxReport(this);
        createMainMenu();
        sayMainMenu();
    }
    private  void createMainMenu(){
        /*
        first say balance
        1:30 transaction
        2:one Month
        3:from date to date
        9:exit
        0:exit
         */
        faxMainMenu.add("1");
        faxMainMenu.add("2");
        faxMainMenu.add("3");
        faxMainMenu.add("9");
        faxMainMenu.add("0");

    }

    public   void sayMainMenu() throws Exception {

        String Choice=null;



        while ((MainMenuCount<3)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTools().sayMenu(faxMainMenu,"007_");
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
            case "3":
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

    private  void fax30Transaction(){

    }

}
