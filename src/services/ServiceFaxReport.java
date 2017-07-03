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

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(faxMainMenu,"007_");
            else {
                Choice=firstChoice;
                firstChoice="";
            }
            if (!Choice.equals("-1")){
                selectSubMenu(Choice);
            }
            else{
                call.getPlayVoiceTool ().notClear();
            }
            MainMenuCount++;
        }
        call.getPlayVoiceTool ().byAndHangup();
    }

    private  void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case "1":fax30Transaction ();
                break;
            case "2":faxOneMonthTransaction ();
                break;
            case "3":faxDateToDate ();
                break;
            case "9":call.getPlayVoiceTool ().byAndHangup ();
                break;
            case "0":call.getPlayVoiceTool ().byAndHangup ();
                break;
            case "-1":call.getPlayVoiceTool ().notClear();
                break;
            default:call.getPlayVoiceTool ().notClear();
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

    private String getLastMonthsDate(){
        return persianDateTime.getPreviousDay (30);
    }

    private String getTodayDate(){
        return persianDateTime.getShamsiDateWithoutSeperator ();
    }

    private  boolean getDateISOK() throws Exception {
        int getDateCount=0;
        boolean getStartDateIsOK=false;
        boolean getEndDateIsOK=false;
        while ((!getStartDateIsOK) && (getDateCount<2)){
            startDate=call.getPlayVoiceTool ().tarikheShoroRaVaredNamaeid ();
            if (isNumber(startDate)){
                if (startDate.length()!=0) {
                    if (startDate.length()==6) {
                        if (entranceDateIsOK(startDate)) {
                            getStartDateIsOK=true;
                        }
                    }
                }
            }else {
                call.getPlayVoiceTool ().dateNotValid();
                getDateCount++;
            }
        }
        if (getStartDateIsOK){
            getDateCount=0;
            while ((!getEndDateIsOK) && (getDateCount<2)){
                endDate=call.getPlayVoiceTool ().tarikheEntehaRaVaredNamaeid ().trim();
                if (isNumber(endDate)) {
                    if (endDate.length() != 0) {
                        if (endDate.length() == 6) {
                            if (entranceDateIsOK(endDate)) {
                                getEndDateIsOK = true;
                            }
                        }
                    }
                }else {
                    call.getPlayVoiceTool ().dateNotValid();
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

        call.getPlayVoiceTool ().pleaseWait ();

        initAndSendTransactionRequest ();

        String actionCode=call.getAccount ().getActionCode ();
        if (actionCode.equals(Const.SUCCESS)){
            String FaxFile=CreatePDFFile(createHTMLFaxFile());
            if (FaxFile!=null){
                boolean resultOfSendFax=SendFax(FaxFile);
            }
        }else{
            call.getPlayVoiceTool ().playActionCode (actionCode);
            call.getPlayVoiceTool ().baArzePoozesh ();
        }
    }

    private void initAndSendTransactionRequest () {
        call.getAccount ().setFaxCount (faxCount);
        call.getAccount ().setStartDateOfFax (startDate);
        call.getAccount ().setEndDateOfFax (endDate);
        call.getAccount ().setKindOfFax (faxType);
        call.getAccountFacade ().getTransactions (call.getAccount ());
    }

    private  String   getTypeOfReport(){
        if (faxType==1){
           return "صورتحساب 30گردش آخر";
        }else if (faxType==2){
           return "صورتحساب یک ماهه";
        }else if (faxType==3){
           return "صورتحساب در بازه زمانی مشخص";
        }else{
            return "صورتحساب 30گردش آخر";
        }
    }

    private  String   getِDateTimeOfReport(){
       return persianDateTime.getIranianDate ()+" "+persianDateTime.getNowTime ();
    }

    private  String   createHTMLFaxFile() throws IOException {

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
                String fileName=Util.FaxFile_Base+call.getCallerID()+"-"+call.getUniQID()+".html";
                fileDir = new File(fileName);
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));

                while ((line = br.readLine()) != null) {
                    if (line.contains("a1"))  {T=line.replace("a1","سيستم تلفنبانک بانک قوامين");}
                    else if (line.contains("a2"))  {T=line.replace("a2","«مبالغ به ريال مي باشد»");}
                    else if (line.contains("a3"))  {T=line.replace("a3",":نوع گزارش");}
                    else if (line.contains("a4"))  {T=line.replace("a4",getTypeOfReport());}
                    else if (line.contains("a5"))  {T=line.replace("a5","تاریخ گزارش");}
                    else if (line.contains("a6"))  {T=line.replace("a6",getِDateTimeOfReport ());}
                    else if (line.contains("a7"))  {T=line.replace("a7","نام صاحب حساب");}
                    else if (line.contains("a8"))  {T=line.replace("a8",call.getAccount ().getNameAndFamily ());}
                    else if (line.contains("a9"))  {T=line.replace("a9","شماره حساب");}
                    else if (line.contains("b1")) {T=line.replace("b1",call.getAccount ().getAccountNumber ());}
                    else if (line.contains("b2")) {T=line.replace("b2","شماره کارت");}
                    else if (line.contains("b3")) {T=line.replace("b3"," ");}
                    else if (line.contains("b4")) {T=line.replace("b4","مانده کنوني");}
                    else if (line.contains("b5")) {T=line.replace("b5",call.getAccount ().getBalance ());}
                    else if (line.contains("b6")) {T=line.replace("b6","شماره شبا");}
                    else if (line.contains("b7")) {T=line.replace("b7",call.getAccount ().getShetabNumber ());}
                    else if (line.contains("b8")) {T=line.replace("b8","مانده");}
                    else if (line.contains("b9")) {T=line.replace("b9","واريز به حساب");}
                    else if (line.contains("c1")) {T=line.replace("c1","برداشت از حساب");}
                    else if (line.contains("c2")) {T=line.replace("c2","شرح عمليات");}
                    else if (line.contains("c3")) {T=line.replace("c3","کد عمليات");}
                    else if (line.contains("c4")) {T=line.replace("c4","مرکز پرداخت");}
                    else if (line.contains("c5")) {T=line.replace("c5","تاريخ");}
                    else if (line.contains("c6")) {T=line.replace("c6","رديف");}
                    else if (line.contains("c7")) {T=line.replace("c7",".اين صورت حساب صرفاً جنبه اطلاع رساني داشته و فاقد هرگونه ارزش قانوني مي باشد");}
                    else T=line;
                    out.append(T);
                }
            }catch (Exception e){
                Util.printMessage("e2:"+e.toString(),false);
            }
            int j=countOfTrans-1;
            String credit="";
            String debit="";
            String tableHTMLColumn1="<td width=\"14%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn2="<td width=\"12%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn3="<td width=\"12%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn4="<td width=\"24%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn5="<td width=\"6%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn6="<td width=\"14%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn7="<td width=\"15%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn8="<td width=\"3%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLEndOfColumn="</div></td>";
            int rowCount=1;
            String tempAmount="";
            String correctedAmount="";
            while (j>=0)
            {
                tempAmount=transactions.get (j).getAmount ();
                if (isNumber (tempAmount)){
                    correctedAmount=tempAmount.replace ('-',' ').trim ();
                    if (Long.valueOf (tempAmount) > 0) {
                       credit=correctedAmount;
                       debit="";
                    }else{
                       debit=correctedAmount;
                       credit="";
                    }
                }
                out.append("<tr>");
                out.append(tableHTMLColumn1+ fixLenNumber(transactions.get (j).getBalance ()) + tableHTMLEndOfColumn);
                out.append(tableHTMLColumn2+ fixLenNumber(credit) +tableHTMLEndOfColumn );
                out.append(tableHTMLColumn3+ fixLenNumber(debit) + tableHTMLEndOfColumn);
                out.append(tableHTMLColumn4+ transactions.get (j).getDescription () + tableHTMLEndOfColumn);
                out.append(tableHTMLColumn5+ transactions.get (j).getDocumentType ()+ tableHTMLEndOfColumn);
                out.append(tableHTMLColumn6+ transactions.get (j).getBranchCode ()+ tableHTMLEndOfColumn);
                out.append(tableHTMLColumn7+ transactions.get (j).getDate ()+" "+transactions.get (j).getTime () +tableHTMLEndOfColumn);
                out.append(tableHTMLColumn8+ String.valueOf(rowCount) +tableHTMLEndOfColumn);
                out.append("</tr>"+"\n");
                j--;
                rowCount++;

            }

            String endOfFile="</table>\n" +
                    "</td>\n" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<td colspan=\"4\">\n" +
                    "<table width=\"449\"  border=\"0\" align=\"right\" cellpadding=\"5\" cellspacing=\"0\">\n" +
                    "\n" +
                    "</table>\n" +
                    "</td>\n" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<td colspan=\"4\">&nbsp;</td>\n" +
                    "</tr>\n" +
                    "</table>\n" +
                    "</th>\n" +
                    "</tr>\n" +
                    "</table>\n" +
                    "<p style=\"font-size:22px; text-align:center; font-family:B Nazanin; font-weight: bold; \"> c7 </P>\n" +
                    "</body>\n" +
                    "</html>";
            out.append(endOfFile);

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
            return Util.FaxFile_Base+call.getCallerID()+"-"+call.getUniQID();

        }catch (Exception e){
            Util.printMessage("e1"+e.toString(),false);
            return null;
        }

    }

    private  String   CreatePDFFile(String FaxFile) throws IOException {

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

    private  boolean  clearFootPrint(String faxfile){
        File file=new File(faxfile);
        return file.delete();

    }

    private  boolean  SendFax(String FaxFile) throws AgiException, InterruptedException, SQLException, IOException {

        exec(Const.ASTERISK_PARK_COMMAND);
        Util.sendFax(FaxFile);
        return true;

    }

    private  String   getMainPathOfFaxFile(){

        String path=Util.FaxFile+getTodayDate();
        File file=new File(path);
        if (file.exists()) return path+"/";
        else {

            file.mkdir();
            return path+"/";
        }

    }

    private  String   fixLenNumber(String no){
        try{
            return String.valueOf(Long.valueOf(no));
        }catch (Exception e){
            return "-";
        }
    }


    @Override
    public void service (AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {

    }
}
