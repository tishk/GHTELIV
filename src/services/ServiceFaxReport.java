package services;

import model.Call;
import model.Transaction;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import util.Const;
import util.PersianDateTime;
import util.Util;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Hamid on 6/5/2017.
 */
public class ServiceFaxReport extends BaseAgiScript{

    private Call call;
    private Set faxMainMenu = new HashSet();
    private int    MainMenuCount=0;
    private String firstChoice="";
    private String endDate ;
    private String startDate;
    private int    faxType;
    private int    faxCount;
    private PersianDateTime persianDateTime=new PersianDateTime ();

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
            case "1":fax30Transaction ();
                break;
            case "2":faxOneMonthTransaction ();
                break;
            case "3":faxDateToDate ();
                break;
            case "9":call.getPlayVoiceTools ().byAndHangup ();
                break;
            case "0":call.getPlayVoiceTools ().byAndHangup ();
                break;
            case "-1":call.getPlayVoiceTools().notClear();
                break;
            default:call.getPlayVoiceTools().notClear();
                break;

        }
    }

    private  void fax30Transaction() throws Exception {
        init30TransactionParameters ();
        startSendFax ();
    }

    private  void faxOneMonthTransaction() throws Exception {
        initOneMonthTransactionParameters ();
        startSendFax ();

    }

    private  void faxDateToDate () throws Exception {
       if (getDateISOK ()){
           initDateToDateTransactionParameters ();
           startSendFax ();
       }
    }

    private void init30TransactionParameters () {
        endDate = "13940101";
        startDate = "13940101";
        faxType=1;
        faxCount=30;
    }

    private void initOneMonthTransactionParameters () {
        endDate = getTodayDate ();
        startDate = getLastMonthsDate ();
        faxType=2;
        faxCount=100;
    }

    private void initDateToDateTransactionParameters () {
        faxType=3;
        faxCount=100;
    }

    private String getTodayDate(){
         return persianDateTime.getShamsiDateWithoutSeperator ();
    }
    private String getLastMonthsDate(){
        return persianDateTime.getPreviousDay (30);
    }

    private  boolean getDateISOK() throws Exception {
        int getDateCount=0;
        boolean getStartDateIsOK=false;
        boolean getEndDateIsOK=false;
        while ((!getStartDateIsOK) && (getDateCount<2)){
            startDate=call.getPlayVoiceTools ().enterStartDate();
            if (isNumber(startDate)){
                if (startDate.length()!=0) {
                    if (startDate.length()==6) {
                        if (entranceDateIsOK(startDate)) {
                            getStartDateIsOK=true;
                        }
                    }
                }
            }else {
                call.getPlayVoiceTools ().dateNotValid();
                getDateCount++;
            }
        }
        if (getStartDateIsOK){
            getDateCount=0;
            while ((!getEndDateIsOK) && (getDateCount<2)){
                endDate=call.getPlayVoiceTools ().enterEndDate().trim();
                if (isNumber(endDate)) {
                    if (endDate.length() != 0) {
                        if (endDate.length() == 6) {
                            if (entranceDateIsOK(endDate)) {
                                getEndDateIsOK = true;
                            }
                        }
                    }
                }else {
                    call.getPlayVoiceTools ().dateNotValid();
                    getDateCount++;
                }
            }
        }

        return getStartDateIsOK&&getEndDateIsOK;
    }
    private  boolean isNumber(String entrance){
        try{
            Long.parseLong(entrance);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    private  boolean entranceDateIsOK(String date){

        int yy=0;int mm=0;int dd=0;
        boolean res=false;
        try{
            dd=Integer.valueOf(date.substring(0, 2));
            mm=Integer.valueOf(date.substring(2, 4));
            yy=Integer.valueOf(date.substring(4, 6));
            if (yy<77 && yy>persianDateTime.getYearOfNow()) return false;
            if (mm<1  && mm>persianDateTime.getMonthOfNow()) return false;
            if (dd<1  && mm>persianDateTime.getDayOfNow())   return false;
            return true;
        }catch (Exception e){
            return false;
        }
    }
    private  void    startSendFax() throws Exception {

        initAndSendTransactionRequest ();

        String actionCode=call.getAccount ().getActionCode ();
        if (actionCode.equals(Const.SUCCESS)){
            String FaxFile=CreatePDFFile(createHTMLFaxFile());
            if (FaxFile!=null){
                boolean resultOfSendFax=SendFax(FaxFile);
            }
        }else{
            call.getPlayVoiceTools ().playActionCode (actionCode);
        }
    }

    private void initAndSendTransactionRequest () {
        call.getAccount ().setFaxCount (faxCount);
        call.getAccount ().setStartDateOfFax (startDate);
        call.getAccount ().setEndDateOfFax (endDate);
        call.getAccount ().setKindOfFax (faxType);
        call.getAccountFacade ().getTransactions (call.getAccount ());
    }

    private String getTypeOfReport(){
        if (faxType==1){
           return "صورتحساب 30گردش آخر";
        }else if (faxType==2){
           return "صورتحساب یک ماهه";
        }else if (faxType==3){
           return "صورتحساب بازه زمانی مشخص";
        }else{
            return "صورتحساب 30گردش آخر";
        }
    }
    private String getِDateTimeOfReport(){
       return persianDateTime.getIranianDate ()+" "+persianDateTime.getNowTime ();
    }

    public  String   createHTMLFaxFile() throws IOException {

        ArrayList<String> Temp = new ArrayList<String>();
        List<Transaction> transactions=call.getAccount ().getTransactions ();


        int countOfTrans=transactions.size ();

        Writer out = null;
        for (int i=countOfTrans-1;i>=0;i--){

            if (faxCount==30){
                if (i==0) endDate=persianDateTime.getShamsiDateForFax (transactions.get (i).getDate ());
                if (i==countOfTrans-1) startDate=persianDateTime.getShamsiDateForFax (transactions.get (i).getDate ());
            }
        }
        String line;
        try {
            InputStream FileName = new FileInputStream (Util.FaxFile_Base + ".html");
            InputStreamReader InFile = new InputStreamReader(FileName, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(InFile);
            String T=null;
            File fileDir = null;

            try
            {

                fileDir = new File(Util.FaxFile_Base+call.getCallerID()+"-"+call.getUniQID()+".html");
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));

                while ((line = br.readLine()) != null) {
                    if (line.contains("a1"))  {T=line.replace("a1","سيستم تلفنبانک بانک قوامين");}
                    else if (line.contains("a2"))  {T=line.replace("a2","«مبالغ به ريال مي باشد»");}
                    else if (line.contains("a3"))  {T=line.replace("a3",":نوع گزارش");}
                    else if (line.contains("a4"))  {T=line.replace("a4",getTypeOfReport());}
                    else if (line.contains("a5"))  {T=line.replace("a5","تاریخ گزارش");}
                    else if (line.contains("a6"))  {T=line.replace("a6",getِDateTimeOfReport ());}
                    else if (line.contains("a7"))  {T=line.replace("a7","مانده حساب");}
                    else if (line.contains("a8"))  {T=line.replace("a8","بدهکار");}
                    else if (line.contains("a9"))  {T=line.replace("a9","بستانکار");}
                    else if (line.contains("b1")) {T=line.replace("b1","شعبه عامل");}
                    else if (line.contains("b2")) {T=line.replace("b2","شرح سند");}
                    else if (line.contains("b3")) {T=line.replace("b3","شماره سند");}
                    else if (line.contains("b4")) {T=line.replace("b4","تاریخ سند");}
                    else if (line.contains("b5")) {T=line.replace("b5","ردیف");}
                    else if (line.contains("d1")) {T=line.replace("d1",persianDateTime.getShamsiDateForFax());}
                    else if (line.contains("acc")) {T=line.replace("acc",call.getAccount());}
                    else if (line.contains("c1")) {T=line.replace("c1",call.getBranch());}
                    else if (line.contains("c2")) {T=line.replace("c2",call.getNameAndFamily());}
                    else if (line.contains("c3")) {T=line.replace("c3",endDate);}
                    else if (line.contains("c4")) {T=line.replace("c4",startDate);}
                    else T=line;
                    out.append(T);
                }
            }catch (Exception e){
                Util.printMessage("e2:"+e.toString(),false);
            }
            int j=countOfTrans-1;
            String credit="";
            String debit="";
            String tblHTmlCmdPart1="<td class=\"style2\"><div align=\"center\" NoWrap=\"NoWrap\">&nbsp;" ;
            String tblHTmlCmdPart2="</div></td>";
            int rowCount=1;
            while (j>=0)
            {
                credit="";
                debit="";
                if (statementMessage[j].getCreditDebit().equals("C")) credit=statementMessage[j].getAmount();
                else if (statementMessage[j].getCreditDebit().equals("D")) debit=statementMessage[j].getAmount();

                out.append("<tr>");
                out.append(tblHTmlCmdPart1+ fixLenNumber(statementMessage[j].getLastAmount()) + tblHTmlCmdPart2);
                out.append(tblHTmlCmdPart1+ fixLenNumber(debit) +tblHTmlCmdPart2 );
                out.append(tblHTmlCmdPart1+ fixLenNumber(credit) + tblHTmlCmdPart2);
                out.append(tblHTmlCmdPart1+ statementMessage[j].getBranchName() + tblHTmlCmdPart2);
                out.append(tblHTmlCmdPart1+ statementMessage[j].getShpInf() + tblHTmlCmdPart2);
                out.append(tblHTmlCmdPart1+ statementMessage[j].getTransDocNo()+ tblHTmlCmdPart2);
                out.append(tblHTmlCmdPart1+ persianDateTime.getShamsi_Date_ForFax(statementMessage[j].getTransDate()) +tblHTmlCmdPart2);
                out.append(tblHTmlCmdPart1+ String.valueOf(rowCount) +tblHTmlCmdPart2);
                out.append("<tr>"+"\n");
                j--;
                rowCount++;

            }

            out.append("<tr>");
            out.append(" <td class=\"style6\"><div align=\"right\" >  </div></td>");
            out.append("<td class=\"style6\"><div align=\"right\" >" + call.getBalance() + "</div></td>");
            out.append("<td class=\"style6\"><div align=\"right\" >" + "موجودی" + "</div></td>");
            out.append("<tr>"+"\n");


            out.append("</table>"+"\n");
            out.append("</td>"+"\n");
            out.append("</tr>"+"\n");
            out.append("</table>"+"\n");
            out.append("</font>"+"\n");
            out.append("</bodsy>"+"\n");
            out.append("</html>"+"\n");
            out.flush();
            out.close();


        } catch (FileNotFoundException e) {
            Util.printMessage("e3"+e.toString(),false);
        } catch (IOException e) {
            Util.printMessage("e4"+e.toString(),false);
        } catch (Exception e){
            Util.printMessage("e5"+e.toString(),false);
        }

        try
        {
           Util.printMessage(Util.FaxFile+call.getCallerID()+"-"+call.getUniQID()+".html",false);
            File fileDir = new File(Util.FaxFile+call.getCallerID()+"-"+call.getUniQID()+".html");
             out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
            int j=0;
            while (j<Temp.size()){
                out.append(Temp.get(j)).append("\r\n");
                j++;
            }
            out.flush();
            out.close();
            Util.printMessage("file creted...",false);

            return Util.FaxFile_Base+call.getCallerID()+"-"+call.getUniQID();
        }catch (Exception e){
            Util.printMessage("e1"+e.toString(),false);
            return null;
        }
        return null;
    }

    public  String   CreatePDFFile(String FaxFile) throws IOException {

        String faxFile_PDF=getMainPathOfFaxFile()+call.getCallerID()+"-"+call.getUniQID()+".pdf";
        String faxFile_HTML=FaxFile+".html";

        if (FaxFile!=null){

            String command = "/usr/local/bin/wkhtmltopdf --page-size A4 --dpi 2000 "+faxFile_HTML+" "+faxFile_PDF;

            InputStreamReader isr =null;
            try
            {
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
            }catch (Exception e){
                Util.printMessage(command,false);
                Util.printMessage("errorrrrr"+e.toString(),false);
                return null;
            }

            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null)
                if (line!="Done"){
                    return null;
                }


            clearFootPrint(faxFile_HTML);

            return faxFile_PDF;
        }
        else return null;
    }

    public  boolean  clearFootPrint(String faxfile){
        File file=new File(faxfile);
        return file.delete();

    }
    public  boolean  SendFax(String FaxFile) throws AgiException, InterruptedException, SQLException, IOException {

        exec(Const.ASTERISK_PARK_COMMAND);
        Util.sendFax(FaxFile);
        return true;

    }

    public  String   getMainPathOfFaxFile(){

        String path=Util.FaxFile+getTodayDate();
        File file=new File(path);
        if (file.exists()) return path+"/";
        else {

            file.mkdir();
            return path+"/";
        }

    }
    @Override
    public void service (AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {

    }
}
