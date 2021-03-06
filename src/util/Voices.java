package util;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Set;

public class Voices extends BaseAgiScript {

    //you should copy the persian folder that contain Sound files and folder,into /var/lib/asterisk/sounds/en

    private String  CheckStar(String S){
        int Len=S.length();
        if (Len!=0){

            if ("*".equals(S.substring(Len-1))) S=S.substring(0,Len-1);
        }
        return S;
    }
    private String  RepairDigit(String S){
        String Temp="";
        for (int i=0;i<S.length();i++){
            Temp=Temp+S.charAt(i);
            i++;
        }
        return Temp;
    }
    public  void    PlayFile(String S) throws AgiException{
        streamFile(Util.VoicePath+"Pmsgs/"+S);

    }
    public  void    PlayConfFile(String S) throws AgiException, IOException {
        streamFile(S);
    }
    public  void    Beep() throws AgiException{
        streamFile(Util.VoicePath+"Pmsgs/BEEP");
    }
    public  boolean sayDate(String D) throws AgiException{
        D=D.trim();
        String Year="";
        String Month="";
        String Day="";
        if (D.length()==6){
             Year=D.substring(0,2);
             Month=D.substring(2,4);
             Day=D.substring(4,6);
        }
        else if (D.length()==8){
             Year=D.substring(2,4);
             Month=D.substring(4,6);
             Day=D.substring(6,8);
        }
        //  util.Util.printMessage("Year:"+Year,false);
        //  util.Util.printMessage("Month:"+Month,false);
        // util.Util.printMessage("Day:"+Day,false);
        char temp='!';
        temp=streamFile(Util.VoicePath+"DATE/A"+Day, "0123456789*#");
        if (temp!='!'){
            temp=streamFile(Util.VoicePath+"DATE/B"+Month, "0123456789*#");
            if (temp!='!'){

                streamFile(Util.VoicePath+"DATE/C"+Year, "0123456789*#");
            }
        }
        return false;
    }
    private  boolean isNumber(String number){
        try{
            BigInteger n=new BigInteger(number);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private int     ZeroResult;
    private boolean HaveZero(String SayNow){
        ZeroResult =0;
        if ("0".equals(SayNow.substring(2,3))) ZeroResult =1;
        if ("0".equals(SayNow.substring(1,2))) ZeroResult = ZeroResult +10;
        if ("0".equals(SayNow.substring(0,1))) ZeroResult = ZeroResult +100;
        if (ZeroResult !=0) return true;
        else return false;
    }

    public  boolean saySeparateDigits (String Digit) throws AgiException{
        try{
            int len=Digit.length();
            //util.Util.printMessage("len is:"+String.valueOf(len),false);
            int LeftIndex=len %3;
            if (len>2){
                String SayNOW=Digit.substring(0,3);
                Digit=Digit.substring(3);
                //util.Util.printMessage("Say now:"+SayNOW,false);
                //util.Util.printMessage("Say later:"+Digit,false);
                say3DigitSeparate(SayNOW);
                saySeparateDigits (Digit);
            }else{
                if (len==0) return true;
                else if (len>=1){
                    //util.Util.printMessage("in here more then 1:"+Digit,false);
                    say3DigitSeparate(Digit);
                    return true;
                }

            }
        }catch(NumberFormatException ex){
            return true;
        }
        return false;
    }
    public  boolean sayMobileNo(String Digit) throws AgiException{
        try{
            int len=Digit.length();
            if (Digit.length()==11){
               Digit=Digit.substring(1);
                say3DigitSeparate("0");
                saySeparateDigits (Digit);
            }else{
                int LeftIndex=len %3;
                if (len>2){
                    String SayNOW=Digit.substring(0,3);
                    Digit=Digit.substring(3);
                    say3DigitSeparate(SayNOW);
                    saySeparateDigits (Digit);
                }else{
                    if (len==0) return true;
                    else if (len>=1){
                        say3DigitSeparate(Digit);
                        return true;
                    }

                }

            }
        }catch(NumberFormatException ex){
            return true;
        }

        return false;
    }
    private boolean say3DigitSeparate(String SayNow) throws AgiException {
        boolean IsZero=false;
        //   util.Util.printMessage("Say 3 digit:"+SayNow,false);

        if ((SayNow.length()==1)&& Integer.valueOf(SayNow)!=0){
            streamFile(Util.VoicePath+"NUM/"+SayNow);
            return true;
        }
        if (SayNow.length()==1) {
            IsZero=true;
            SayNow="00"+SayNow;
        }
        if (SayNow.length()==2){
           try
           {

               if (Integer.valueOf(SayNow)<10){
                   streamFile(Util.VoicePath+"NUM/0");
                   streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2));
               }
               else if ((Integer.valueOf(SayNow)>=10) &&(Integer.valueOf(SayNow)<=20)){

                   streamFile(Util.VoicePath+"NUM/"+SayNow);
               }
               else{

                   streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"0o");
                   streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2));
               }
           }catch (Exception e){

           }

        }else if (HaveZero(SayNow)){
            //   util.Util.printMessage("Say 3 digit:"+SayNow+" have zero result is:"+String.valueOf(ZeroResult),false);
            if (ZeroResult ==1){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==10){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }else if (ZeroResult ==11){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00");
            }else if (ZeroResult ==100){
                if (Integer.valueOf(SayNow)<20 ){
                    //streamFile(util.Util.VoicePath+"NUM/0");
                    //util.Util.printMessage("in say less than 20:",false);
                    streamFile(Util.VoicePath+"NUM/0");
                    //streamFile(util.Util.VoicePath+"NUM/0");
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
               // }else{
                   // streamFile(util.Util.VoicePath+"NUM/0");
                   // streamFile(util.Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                  //  streamFile(util.Util.VoicePath+"NUM/"+SayNow.substring(2,3));
                }else{
                    streamFile(Util.VoicePath+"NUM/0");
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
                }

            }else if (ZeroResult ==101){
                streamFile(Util.VoicePath+"NUM/0");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==110){
                streamFile(Util.VoicePath+"NUM/0");
                streamFile(Util.VoicePath+"NUM/0");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }else if (ZeroResult ==111){
                if (IsZero){
                    streamFile(Util.VoicePath+"NUM/0");
                }else{
                    streamFile(Util.VoicePath+"NUM/0");
                    streamFile(Util.VoicePath+"NUM/0");
                    streamFile(Util.VoicePath+"NUM/0");
                }

            }
        }else{
            if (Integer.parseInt(SayNow.substring(1,3))<20 ){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,3));
            }else{
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }


        }
        return true;

    }
    public  boolean sayAmount (String Digit) throws AgiException{
        try{
            if (Integer.valueOf(Digit) ==0) {

                PlayFile(Util.VoicePath+"NUM/"+Digit);
                return true;
            }
            int len=Digit.length();
            int LeftIndex=len %3;
            if ((LeftIndex==0)&&(len>=3)) LeftIndex=3;
            if (len>2){
                String SayNOW=Digit.substring(0,LeftIndex);
                say3Digits(SayNOW);
                String DigitTemp=Digit.substring(LeftIndex);
                if (0==Long.parseLong(DigitTemp)){

                    PlayConfFile(setConnectionFile(Digit.length(), 0));
                }else{
                    PlayConfFile(setConnectionFile(Digit.length(), 1));
                }
                Digit=Digit.substring(LeftIndex);
                sayAmount (Digit);

            }else{
                if (len==0) return true;
                else if (len>=1){
                    say3Digits(Digit);
                    return true;
                }

            }
        }catch(Exception ex){

        }
        return false;
    }
    private boolean say3Digits(String SayNow) throws AgiException{

        if (SayNow.length()==1) SayNow="00"+SayNow;
        if (SayNow.length()==2) SayNow="0"+SayNow;
        if (HaveZero(SayNow)){

            if (ZeroResult ==1){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==10){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }else if (ZeroResult ==11){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00");
            }else if (ZeroResult ==100){
                //util.Util.printMessage("in say less than 20:" + SayNow.substring(1, 3), false);
                if (Integer.parseInt(SayNow)<=20){
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,3));
                    //util.Util.printMessage("in say less than 20:"+SayNow.substring(1,3),false);
                }else{
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                    streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
                }
            }else if (ZeroResult ==101){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0");
            }else if (ZeroResult ==110){
                //util.Util.printMessage("110:"+SayNow.substring(1,3),false);
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }else if (ZeroResult ==111){

            }
        }else{
            if (Integer.parseInt(SayNow.substring(1,3))<=20 ){
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,3));
            }else{
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(0,1)+"00o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(1,2)+"0o");
                streamFile(Util.VoicePath+"NUM/"+SayNow.substring(2,3));
            }


        }
        return true;
    }

    private String  setConnectionFile(int Len, int Kind){
        String Part1=Util.VoicePath+"NUM/";
        if (Kind==0){
            switch (Len){
                case 1:return "";
                case 2:return "";
                case 3:return "";
                case 4:return Part1+"1000";
                case 5:return Part1+"1000";
                case 6:return Part1+"1000";
                case 7:return Part1+"1000000";
                case 8:return Part1+"1000000";
                case 9:return Part1+"1000000";
                case 10:return Part1+"1000000000";
                case 11:return Part1+"1000000000";
                case 12:return Part1+"1000000000";
                case 13:return Part1+"1000000000000";
                case 14:return Part1+"1000000000000";
                case 15:return Part1+"1000000000000";
                default:return "";
            }
        }else{
            switch (Len){
                case 1:return "";
                case 2:return "";
                case 3:return "";
                case 4:return Part1+"1000o";
                case 5:return Part1+"1000o";
                case 6:return Part1+"1000o";
                case 7:return Part1+"1000000o";
                case 8:return Part1+"1000000o";
                case 9:return Part1+"1000000o";
                case 10:return Part1+"1000000000o";
                case 11:return Part1+"1000000000o";
                case 12:return Part1+"1000000000o";
                case 13:return Part1+"1000000000000o";
                case 14:return Part1+"1000000000000o";
                case 15:return Part1+"1000000000000o";
                default:return "";
            }

        }
    }
    public  String  getStringFromAsterisk(String File,int TimeOut) throws AgiException{
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while ((digit = waitForDigit(TimeOut)) != 0)
            {

                if (digit == '#' || digit == '*')
                {
                    break;
                }
                Result=Result+String.valueOf(digit);
            }
        }

        return  Result;
    }
    public  String  getStringFormASK(String File, int timeOUT) throws AgiException, IOException {
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");


        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+Integer.toString(timeOUT), "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0)
                {
                    break;
                }else Result=Result+String.valueOf(digit);

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  getStringFormASKWithLen(String File,int timeOUT,int Len) throws AgiException{
        char digit= '@';
        int counter=1;
        String Result="";
        digit=streamFile(File, "0123456789*#");
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#' )
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+Integer.toString(timeOUT), "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0||counter<Len)
                {
                    break;
                }else{
                    Result=Result+String.valueOf(digit);
                    counter++;
                }

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  getStringFormASKSilent(int timeOUT) throws AgiException{
        char digit= '@';
        String Result="";
        digit=streamFile("", "0123456789*#");
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+Integer.toString(timeOUT), "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0)
                {
                    break;
                }else Result=Result+String.valueOf(digit);

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  getStringFormASKTinyTimeOut_(String File,String timeOUT) throws AgiException, IOException {
        String Result="";
        Result=String.valueOf(streamFile(File, "0123456789*#"));
        Util.printMessage(Result,false);
        Result=Result.trim();
        return  Result;
    }
    public  String  getStringFormASKTinyTimeOut(String File, String timeOUT) throws AgiException, IOException {
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");
       // util.Util.printMessage("Stream file is :"+File,false);
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+timeOUT, "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0)
                {
                    break;
                }else Result=Result+String.valueOf(digit);

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  getCharacterFormASKTinyTimeOut(String File,String timeOUT) throws AgiException{
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");
        Result=Result+String.valueOf(digit);

        if (digit!='*' && digit!='#')
        {
            while (digit!='*' && digit!='#')
            {
                //igit="";
                digit=streamFile(Util.VoicePath+"null/"+timeOUT, "0123456789*#");
                if (digit == '#' || digit == '*'||digit==0x0)
                {
                    break;
                }else Result=Result+String.valueOf(digit);

            }
        }
        Result=Result.trim();
        return  Result;
    }
    public  String  getStringFormASKseperateMenu(String File,int timeOUT) throws AgiException{
        char digit= '@';
        String Result="";
        digit=streamFile(File, "0123456789*#");
        Result=Result+String.valueOf(digit);
        Result=Result.trim();
        return  Result;
    }
    public  String  convertEntranceToDigit(String e){
        int v=-1;
        try {
            v=Integer.valueOf(e);
        }catch (Exception var1){
            v=-1;
        }
        return String.valueOf(v);
    }
   //--------------------------------------------------
    public  String  sayMenu(Set MenuSet, String PrefixMessages) throws AgiException, IOException {
        //util.Util.printMessage("in say for bill",false);

        String element=null;
        String element2=null;
        String LastElement=null;
        String Choice="-1";
        Set tempSet=MenuSet;
        String tempElement=null;
        boolean IsInMenu=false;
        for(Object object : MenuSet)
            LastElement = (String) object;
        Choice= getStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/" + "P006", "0.25");
        Choice= convertEntranceToDigit(Choice);
        if (Choice.equals("-1")){
            for(Object object : MenuSet) {

                element = (String) object;
               // util.Util.printMessage(util.Util.VoicePath +"Pmsgs/"  + PrefixMessages + element,false);
                if (element.equals(LastElement))
                {
                    Choice= getStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + PrefixMessages + element, "3");
                    Choice= convertEntranceToDigit(Choice);
                }
                else Choice= getStringFormASKTinyTimeOut(Util.VoicePath +"Pmsgs/"  + PrefixMessages + element, "0.25");
                Choice= convertEntranceToDigit(Choice);
                if (!Choice.equals("-1")){
                    for(Object object2 : tempSet) {
                        tempElement = (String) object2;
                        if (Choice.equals(tempElement)) return Choice;
                    }
                    break;
                }
            }

        }else{
            for(Object object3 : MenuSet) {
                element2 = (String) object3;
                if (Choice.equals(element2)) return Choice;
            }
        }
        return "-1";

    }


    // _______________________Ghavamin____Messages__________________________________

    public String  playMainMenu() throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "mainMenuMessage" , 5).trim();
    }
    public String  enterAccountOrPanNumber() throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh107" , 5).trim();
    }
    public  boolean by() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh502", "0.25").trim().equals("")) return true;
        else return false;

    }

    public  boolean notClear() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh502", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  void byAndHangup() throws Exception {
        getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh502", "0.25").trim().equals("");
        System.gc();
        hangup();

    }
    public String  ramzeCardRaVaredNamaeid () throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "009" , 5).trim();
    }

    public String  ramzeHesabRaVaredNamaeid () throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "002" , 5).trim();
    }
    public String  tarikheShoroRaVaredNamaeid () throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "P032" , 5).trim();
    }
    public String  tarikheEntehaRaVaredNamaeid () throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "P033" , 5).trim();
    }
    public String  shomareHesabeMaghsadRaVaredNamaeid () throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh10p" , 5).trim();
    }
    public String  shomareHesabePardakhteGhestRaVaredNamaeid () throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh51p" , 5).trim();
    }
    public String  lotfanMablaghRaVaredNamaeid() throws Exception {
        return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "SH11p" , 5).trim();
    }
    public String  agarSahihAstAdade5 () throws Exception {
            return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh46" , 5).trim();
        }
    public String  ramzeJadidRaVaredNamaeid () throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "P023T" , 5).trim();
    }
    public String  ramzeJadidRaMojadadanVaredNamaeid () throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "P024T" , 5).trim();
    }
    public String  mablagheGhestRaVaredNamaeid () throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh01T" , 5).trim();
    }
    public String  shomareMobileRaVaredKonid () throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "menu71condition1part3T" , 5).trim();
    }
    public String  shomareSerialChekRaVaredKonid () throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh209T" , 5).trim();
    }
    public String  cardGheyreFaalMishavadAddade5 () throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "SH80T" , 5).trim();
    }
    public String  shenaseGhabzRaVaredNamaeid () throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh1t" , 5).trim();
    }
    public String  shenasePardakhtRaVaredNamaeid () throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "Sh3t" , 5).trim();
    }
    public String  shomareCardRaVaredNamaeid() throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "sh107-1" , 5).trim();
    }
    public String  shomareHesabRaVaredNamaeid() throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "AccEnter" , 5).trim();
    }
    public String  codeShobeRaVaredNamaeid() throws Exception {
       return getStringFormASK(Util.VoicePath +"Pmsgs/"  + "BranchEnter" , 5).trim();
    }



    public  boolean passNotValid() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E003", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean playActionCode(String actionCode) throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + actionCode, "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean advertisement() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh17", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean mojodieHesabeShoma() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P003", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean mibashad() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P017", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean bedehkarMibashad() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P037", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean gardesheHesabeShoma() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P008", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean varize() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P010", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean bardashte() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P009", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean mablaghe() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P011", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean sayCurrency(String currencyCode) throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/cur/" + currencyCode, "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean beTarikhe() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P014", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean gardeshiMojodNist() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E014", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean khataDarErtebatBaServer() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E011", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean dateNotValid() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E016", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean rial() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P027", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean rialMibashad() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh12", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean bardashVaBeHesabe() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh44", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean varizKhahadShod() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh45", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean montaghelKhahadshod() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh14", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean zemnanShomarePeygirieshoma() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH21", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean dastresiBeMaghsadMaghdorNist() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E053", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean saghfePardakhtPorAst() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH35", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean hesabSabtNashodeAst() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH23", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean pleaseWait() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "P019", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean baArzePoozesh() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E011", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean error() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "error", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean inDoShomareMotabeghatNadarad() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH30", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean shomareOboreMotabarNist() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E007", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean ramzTaghirKard() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH28", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean ramzTaghirNakard() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH29", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean mablagheGhesteInDore() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH04", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean mandeBedehiShoma() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh05", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean mobileDorostNist() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "menu71condition1part4", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean baMovafaghiatSabtShod() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SuccessfulRegisterMobile", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean baMovafaghiatHazfShod() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SuccessfulDeleteMobile", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean chekeMoredeNazarEshtebahAst() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh200", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean chekeShomare() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "nChqShomare", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean darTarikhe() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh1080", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean chequeBaMablaghe() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "nChqBaMablagh", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean sayChequeStatus(char status) throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "ChequeStatus_"+status, "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean cardMasdodNashod() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh62", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean cardMasdodShod() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh61", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean shomareHesabSahihNist () throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E002_1", "0.25").trim().equals("")) return true;
        else return false;

    }
    public  boolean panEntryInvalid() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E002", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean rialBabateGhabze() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh37", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean ab() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "G01", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean bargh() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "G02", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean gaz() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "G03", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean telephone() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "G04", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean mobile() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "G05", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean shahrDari() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "G06", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean motefareghe() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "G08", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean jarimehRahnemaeiVaRanandegi() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "G09", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean sayBillKind(int kind) throws Exception {

        switch (kind){
            case 1:return ab ();
            case 2:return bargh ();
            case 3:return gaz ();
            case 4:return telephone ();
            case 5:return mobile ();
            case 6:return shahrDari ();
            case 7:return shahrDari ();
            case 8:return motefareghe ();
            case 9:return jarimehRahnemaeiVaRanandegi ();
            default:return false;
        }

    }
    public  boolean vaShenaseGhabze() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh67", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean bardashtKhahadShod() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh43", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean shomareCardSahihNist() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "E002", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean pardakhtAnjamShode() throws Exception {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "SH32", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean vaBaShomarePeygirie () throws IOException, AgiException {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh109", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean padakhtShodeAst () throws IOException, AgiException {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "sh110", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean etelaatYaftNashod () throws IOException, AgiException {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "GhabzNotFound", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean mobileVaredShodeBarabarAstBa () throws IOException, AgiException {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Mobile_Conf", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean mobileChargeKhahadShod () throws IOException, AgiException {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "ShargeTelOK", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean shabaDescription () throws IOException, AgiException {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh501", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean shomareShabaieshoma () throws IOException, AgiException {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh502", "0.25").trim().equals("")) return true;
        else return false;
    }
    public  boolean ir () throws IOException, AgiException {
        if (!getStringFormASKTinyTimeOut(Util.VoicePath + "Pmsgs/" + "Sh503", "0.25").trim().equals("")) return true;
        else return false;
    }

    // _______________________Ghavamin____Messages__________________________________

    //---------------Tejarat---------------------------------
    @Override
    public  void    service(AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {

    }
}
