package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 9/22/2015.
 */
public class StringUtils {

    public  String   getDecodedTime(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =

                new SimpleDateFormat ("HHmmss");
        String Now=DateFormat.format(Time);
        return Now;

    }
    public  String   getDecodedDate() {

        Calendar cal = Calendar.getInstance();
        int Day = cal.get(Calendar.DAY_OF_MONTH);
        int Month = cal.get(Calendar.MONTH)+1;
        int Year = cal.get(Calendar.YEAR);

        if(Month < 3 && Day < 21)
            Year -= 622;
        else
            Year -= 621;

        switch (Month) {
            case 1:
                if(Day < 21)
                {
                    Month=10;
                    Day+=10;
                }
                else
                {
                    Month=11;
                    Day-=20;
                }
                break;

            case 2:
                if(Day < 20)
                {
                    Month=11;
                    Day+=11;
                }
                else
                {
                    Month=12;
                    Day-=19;
                }
                break;

            case 3:
                if(Day < 21)
                {
                    Month=12;
                    Day+=9;
                }
                else
                {
                    Month=1;
                    Day-=20;
                }
                break;

            case 4:
                if(Day < 21)
                {
                    Month=1;
                    Day+=11;
                }
                else
                {
                    Month=2;
                    Day-=20;
                }
                break;

            case 5:
            case 6:
                if(Day < 22)
                {
                    Month-=3;
                    Day+=10;
                }
                else
                {
                    Month-=2;
                    Day-=21;
                }
                break;

            case 7:
            case 8:
            case 9:
                if(Day < 23)
                {
                    Month-=3;
                    Day+=9;
                }
                else
                {
                    Month-=2;
                    Day-=22;
                }
                break;

            case 10 :
                if(Day < 23)
                {
                    Month=7;
                    Day+=8;
                }
                else
                {
                    Month=8;
                    Day-=22;
                }
                break;

            case 11:
            case 12:
                if(Day < 22)
                {
                    Month-=3;
                    Day+=9;
                }
                else
                {
                    Month-=2;
                    Day-=21;
                }
                break;
        }
        String mm=null;
        String dd=null;
        if (Month<10) mm="0"+String.valueOf(Month); else mm=String.valueOf(Month);
        if (Day<10) dd="0"+String.valueOf(Day); else dd=String.valueOf(Day);
        return String.valueOf(Year)+ mm+dd;

    }
    public  String   getFixedDateToFax() {

        Calendar cal = Calendar.getInstance();
        int Day = cal.get(Calendar.DAY_OF_MONTH);
        int Month = cal.get(Calendar.MONTH)+1;
        int Year = cal.get(Calendar.YEAR);

        if(Month < 3 && Day < 21)
            Year -= 622;
        else
            Year -= 621;

        switch (Month) {
            case 1:
                if(Day < 21){Month=10;Day+=10;}
                else{Month=11;Day-=20;}
                break;
            case 2:
                if(Day < 20)
                {
                    Month=11;
                    Day+=11;
                }
                else
                {
                    Month=12;
                    Day-=19;
                }
                break;

            case 3:
                if(Day < 21)
                {
                    Month=12;
                    Day+=9;
                }
                else
                {
                    Month=1;
                    Day-=20;
                }
                break;

            case 4:
                if(Day < 21)
                {
                    Month=1;
                    Day+=11;
                }
                else
                {
                    Month=2;
                    Day-=20;
                }
                break;

            case 5:
            case 6:
                if(Day < 22)
                {
                    Month-=3;
                    Day+=10;
                }
                else
                {
                    Month-=2;
                    Day-=21;
                }
                break;

            case 7:
            case 8:
            case 9:
                if(Day < 23)
                {
                    Month-=3;
                    Day+=9;
                }
                else
                {
                    Month-=2;
                    Day-=22;
                }
                break;

            case 10 :
                if(Day < 23)
                {
                    Month=7;
                    Day+=8;
                }
                else
                {
                    Month=8;
                    Day-=22;
                }
                break;

            case 11:
            case 12:
                if(Day < 22)
                {
                    Month-=3;
                    Day+=9;
                }
                else
                {
                    Month-=2;
                    Day-=21;
                }
                break;
        }
        String mm=null;
        String dd=null;
        if (Month<10) mm="0"+String.valueOf(Month); else mm=String.valueOf(Month);
        if (Day<10) dd="0"+String.valueOf(Day); else dd=String.valueOf(Day);
        return String.valueOf(Year)+"/"+ mm+"/"+dd;

    }
    public  String   getFixedTimeToFax() {

        Date Time=new Date();
        SimpleDateFormat DateFormat =

                new SimpleDateFormat ("HH:mm:ss");
        String Now=DateFormat.format(Time);
        return Now;
    }
    public  String   createZeroString(int Count){
        String res="";
        for (int i=0;i<Count;i++) res=res+"0";
        return res;
    }
    public  String   fixLengthWithZero(String Str,int length){
        String Temp=Str;
        for (int i=0;i<length-Str.length();i++) Temp="0"+Temp;
        return Temp;
    }
    public  String   fixLengthWithStar(String Str,int length){
        String Temp=Str;
        for (int i=0;i<length-Str.length();i++) Temp="*"+Temp;
        return Temp;
    }
    public  String   midString(String str,int start,int count){
        try{
            return str.substring(start-1,count+start-1);
        }catch (Exception e){
            return "!";
        }

    }
    public  String   DecToBin32(long n){

        char ST[]=new char[32];
        byte i;
        for ( i=0;i<32;i++) ST[i]='0';
        i=31;
        while (n!=0){

            ST[i]=(char)(n % 2 +(int)('0'));
            n=n / 2;
            i--;
        }
        return String.valueOf(ST);

    }
    public  long     HexToDec(String s) {

        long p=0;
        int  c,i;
        for(i=0;i<s.length();i++){
            switch (s.charAt(i)) {
                case '0':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '1':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '2':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '3':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '4':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '5':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '6':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '7':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '8':c=(int)s.charAt(i)-(int)'0';
                    break;
                case '9':c=(int)s.charAt(i)-(int)'0';
                    break;
                case 'a':c=(int)s.charAt(i)-(int)'a'+10;
                    break;
                case 'b':c=(int)s.charAt(i)-(int)'a'+10;
                    break;
                case 'c':c=(int)s.charAt(i)-(int)'a'+10;
                    break;
                case 'd':c=(int)s.charAt(i)-(int)'a'+10;
                    break;
                case 'e':c=(int)s.charAt(i)-(int)'a'+10;
                    break;
                case 'f':c=(int)s.charAt(i)-(int)'a'+10;
                    break;
                case 'A':c=(int)s.charAt(i)-(int)'A'+10;
                    break;
                case 'B':c=(int)s.charAt(i)-(int)'A'+10;
                    break;
                case 'C':c=(int)s.charAt(i)-(int)'A'+10;
                    break;
                case 'D':c=(int)s.charAt(i)-(int)'A'+10;
                    break;
                case 'E':c=(int)s.charAt(i)-(int)'A'+10;
                    break;
                case 'F':c=(int)s.charAt(i)-(int)'A'+10;
                    break;
                default:return -1;
            }
            p=p*16+c;
        }
        return p;
    }
    public  String   rightString(String str,int index){
        try{
            return str.substring(str.length()-index,str.length());
        }catch (Exception e){
            return "!";
        }

    }
    public  String   leftString(String str,int index){
        try{
            return str.substring(0,index);
        }catch (Exception e){
            return "!";
        }

    }
    public  String   deleteFromString(String str,int start,int count){

        try{

            String before=str.substring(0,start-1);
            String after=str.substring(start+count-1,str.length());
            String Result=before+after;
            return Result;
        }catch (Exception e){
            return "!";
        }

    }
    public  String   Tokenize(String str ,char id){
        int len=0;
        String Result=null;
        try{
            if (id=='1'){
                len= Integer.valueOf(midString(str,1,3));
                str=deleteFromString(str,1,3);
                Result=midString(str,1,len);
                str=deleteFromString(str,1,len);

            }else Result="";
            return Result;

        }catch (Exception e){
            return "";
        }
    }
    public  String   SplitAndCutString(String str,char SplitParameter,int position){
        int positionCount=1;
        int perposition=0;
        try{
            for (int i=0;i<=str.length()-1;i++){
                if (str.charAt(i)==SplitParameter) {
                    if (position==positionCount){
                        return str.substring(perposition,i);
                    }
                    else{
                        positionCount++;
                        perposition=i+1;
                    }
                }
            }
        }catch (Exception e){

        }
        return null;
    }
    public  String   insertSeparator(String str){
        String Temp="";
        try{
            int len=str.length();
            while (len>0){
                Temp=","+right3DigitString(str)+Temp;
                str=deleteRight3DigitFromString(str);
                len=str.length();
            }
        }catch (Exception e){
            return "!";
        }


        return Temp.substring(1);
    }
    public  String   right3DigitString(String str){
        try{
            if (str.length()<3) return str;
            else return str.substring(str.length()-3,str.length());
        }catch (Exception e){
            return "!";
        }

    }
    public  String   deleteRight3DigitFromString(String str){

        try{
            if (str.length()<3) return "";
            else {
                return str.substring(0, str.length() - 3);
            }
        }catch (Exception e){
            return "!";
        }

    }

    public  String[]     extractText(String aText,String aSeparator){
        String[] Result= aText.split(aSeparator);
        return Result;

    }
    public  List<String> extractChequeData(String aText,String identifier){

        List<String> result=new ArrayList<String>();
        if (aText.trim().equals("")) return result;
        String s="";
        String param="";
        try {
            aText=deleteFromString(aText,1,16);
            int sizeOfIdentifier=identifier.length();
            for(int i=0;i<sizeOfIdentifier;i++){
                if (identifier.charAt(i)=='1'){
                    int lenOfParam=Integer.valueOf(midString(aText,1,3));
                    aText=deleteFromString(aText,1,3);
                    param=midString(aText,1,lenOfParam);
                    aText=deleteFromString(aText,1,lenOfParam);
                }else param="";
                result.add(param);
                param="";
            }

        }catch (Exception var1){

        }
        return result;
    }
    public  String[]     extractTextPos(String aText,int[] aPos){

        List<String> result=new ArrayList<>();
        try{
           if ((aText.trim()=="")||(aPos.length==0)){
               result.add(aText);
               return  result.toArray(new String[result.size()]);
           }
            int s=1;
            String tempString="";
            for (int i=0;i<aPos.length;i++){
                tempString=midString(aText,s,aPos[i]);
                result.add(tempString);
                aText=deleteFromString(aText,1,aPos[i]);
           }
            return result.toArray(new String[result.size()]);
        }catch (Exception e){

        }
         return result.toArray(new String[result.size()]);
    }

}
