package services;

import model.Call;
import util.Const;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 7/5/2017.
 */
public class ServiceTopUp {

    private Call call;
    private Set topUpMenu = new HashSet ();
    private int topUpMenuCount =0;
    private String firstChoice;
    private String mobileNumber="";



    public ServiceTopUp(Call call){

      this.call=call;
    }

    public  void execute() throws Exception {
        call.setServiceTopUp (this);

        createMainMenu ();
        sayMainMenu ();
    }

    private void createMainMenu(){
        /*
            first say balance
            1:MCI
            2:MTN
            0:exit
            9:return pre menu
       */
        topUpMenu.add("1");
        topUpMenu.add("2");
        topUpMenu.add("9");
        topUpMenu.add("0");

    }

    public  void sayMainMenu() throws Exception {

        String Choice="";
        while ((topUpMenuCount < Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(topUpMenu,Const.MENU_PREFIX_TOP_UP);
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
            topUpMenuCount++;
        }
        exit ();
    }

    private void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case  "1":
                break;
            case  "2":
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
      call.getServiceBillPayment ().sayMainMenu ();
    }

    private boolean isNumber(String entrance){
        try{
            Long.parseLong(entrance);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean  mobileIsCorrect(String mobileNumber){
        if (isNumber (mobileNumber)){
            if (mobileNumber.length ()==Const.MOBILE_NUMBER_LEN){
                return true;
            }
        }
        return false;
    }



}
