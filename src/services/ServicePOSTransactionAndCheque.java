package services;

import model.Call;
import model.TransactionPOS;
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
 * Created by Hamid on 6/9/2017.
 */
public class ServicePOSTransactionAndCheque extends BaseAgiScript {

    private Call call;
    private Set posAndChequeMenu = new HashSet ();
    private int posAndChequeMenuCount =0;
    private String firstChoice;
    private PersianDateTime persianDateTime=new PersianDateTime ();
    private String startDate="";
    private String endDate="";
    private String faxType="";
    private String chequeSerialNumber ="";
    private int faxCount;


    public ServicePOSTransactionAndCheque (Call call) {
        this.call=call;
    }

    public void execute() throws Exception {
        call.setServicePOSTransactionAndCheque (this);
        createMainMenu ();
        sayMainMenu ();
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
        while ((posAndChequeMenuCount <Const.MAX_TEL_BANK_MENU_COUNT)) {

            if (firstChoice.equals("")) Choice = call.getPlayVoiceTool ().sayMenu(posAndChequeMenu,"pos_");
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
            posAndChequeMenuCount++;
        }
        exit ();
    }

    private void selectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case  "1":get30POSTransactions ();
                break;
            case  "2":getDateToDatePOSTransactions ();
                break;
            case  "3":getChequeStatus ();
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

    private void get30POSTransactions() throws Exception {

        init30TransactionRequestParameters ();
        startSendFax ();

    }

    private void init30TransactionRequestParameters () {
        faxType="1";
        faxCount=30;
    }

    private void initDateToDateTransactionParameters () {
        faxType="2";
    }

    private void getDateToDatePOSTransactions() throws Exception {
        if (getDateISOK ()){
            initDateToDateTransactionParameters ();
            startSendFax ();
        }
    }

    private void getChequeStatus() throws Exception {

       if (getSerialNumberOfChequeOK ()){
           sendChequeStatusRequest ();
          if (isSuccessChequeStatus ()){
              playStatus ();
          }else{
              playChequeNumberIsIncorrect ();
          }
       }

    }

    private boolean isSuccessChequeStatus () {
        return call.getAccount ().getActionCode ().equals (Const.SUCCESS);
    }

    private void playStatus () throws Exception {
        call.getPlayVoiceTool ().chekeShomare ();
        call.getPlayVoiceTool ().sayPersianDigitsSeparate (call.getAccount ().getChequeSerialNumber ());
        call.getPlayVoiceTool ().darTarikhe ();
        call.getPlayVoiceTool ().sayDate (correctDateForPlay (call.getAccount ().getChequeDate ()));
        call.getPlayVoiceTool ().chequeBaMablaghe ();
        call.getPlayVoiceTool ().sayPersianDigit (correctNumberForPlay (call.getAccount ().getChequeAmount ()));
        call.getPlayVoiceTool ().rial ();
        call.getPlayVoiceTool ().sayChequeStatus (call.getAccount ().getChequeStatusCodeChar ());
    }

    private void sendChequeStatusRequest () {
        call.getAccount ().setChequeSerialNumber (chequeSerialNumber);
        call.getAccountFacade ().getChequeStatus (call.getAccount ());
    }

    private void playChequeNumberIsIncorrect () throws Exception {
        call.getPlayVoiceTool ().chekeMoredeNazarEshtebahAst ();
    }

    private boolean getSerialNumberOfChequeOK () throws Exception {
        int count=0;
        boolean serialNumberEntered=false;
        while (!serialNumberEntered && count<3){
            getSerialNumberFromCustomer ();
            if (chequeSerialNumber.length ()>0){
                serialNumberEntered=true;
            }else{
                inputError ();
                count++;
            }
        }
        return serialNumberEntered;
    }

    private void getSerialNumberFromCustomer () throws Exception {
        chequeSerialNumber =call.getPlayVoiceTool ().shomareSerialChekRaVaredKonid ();
    }

    private void startSendFax() throws Exception {

        sayPleaseWait ();

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

    private void sayPleaseWait () throws Exception {
        call.getPlayVoiceTool ().pleaseWait ();
    }

    private void initAndSendTransactionRequest () {
        call.getAccount ().setStartDateOfFax (startDate);
        call.getAccount ().setEndDateOfFax (endDate);
        call.getAccount ().setKindOfPOSTransaction (faxType);
        call.getAccountFacade ().getTransactionOfPOS (call.getAccount ());
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

    private  boolean permittedAccountUseChequeService(){
        return call.getAccount ().getAccountType ().equals ("01");
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

    private  boolean  clearFootPrint(String faxfile){
        File file=new File(faxfile);
        return file.delete();

    }

    private  boolean  SendFax(String FaxFile) throws AgiException, InterruptedException, SQLException, IOException {

        exec(Const.ASTERISK_PARK_COMMAND);
        Util.sendFax(FaxFile);
        return true;

    }

    private  String getTodayDate(){
        return persianDateTime.getShamsiDateWithoutSeperator ();
    }

    private  String getTypeOfReport(){
        if (faxType.equals ("1")){
            return "صورتحساب پايانه فروشگاهي 30گردش آخر";
        }else if (faxType.equals ("2")){
            return "صورتحساب پايانه فروشگاهي در بازه زمانی مشخص";
        }else return "";
    }

    private  String getِDateTimeOfReport(){
        return persianDateTime.getIranianDate ()+" "+persianDateTime.getNowTime ();
    }

    private  String createHTMLFaxFile() throws IOException {

        ArrayList<String> Temp = new ArrayList<String>();
        List<TransactionPOS> transactionPOSList=call.getAccount ().getTransactionsPOS ();


        int countOfTrans=transactionPOSList.size ();

        Writer out = null;
        for (int i=countOfTrans-1;i>=0;i--){

            if (faxCount==30){
                if (i==0) endDate=persianDateTime.getShamsiDateForFax (transactionPOSList.get (i).getLocalDate ());
                if (i==countOfTrans-1) startDate=persianDateTime.getShamsiDateForFax (transactionPOSList.get (i).getLocalDate ());
            }
        }
        String line;
        try {
            InputStream FileName = new FileInputStream (Util.FaxFile_Base_POS + ".html");
            InputStreamReader InFile = new InputStreamReader(FileName, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(InFile);
            String T=null;
            File fileDir = null;

            try
            {
                String fileName=Util.FaxFile_Base_POS+call.getCallerID()+"-"+call.getUniQID()+".html";
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
                    else if (line.contains("b6")) {T=line.replace("b6","شماره شبا");}
                    else if (line.contains("b7")) {T=line.replace("b7",call.getAccount ().getShebaNumber());}
                    else if (line.contains("b8")) {T=line.replace("b8","شناسه پايانه ");}
                    else if (line.contains("b9")) {T=line.replace("b9","چرخه واريز ");}
                    else if (line.contains("c1")) {T=line.replace("c1","تاريخ واريز ");}
                    else if (line.contains("c2")) {T=line.replace("c2","شماره پيگيري ");}
                    else if (line.contains("c3")) {T=line.replace("c3","مبلغ");}
                    else if (line.contains("c4")) {T=line.replace("c4","شماره ارجاع ");}
                    else if (line.contains("c5")) {T=line.replace("c5","نوع تراکنش ");}
                    else if (line.contains("c6")) {T=line.replace("c6","نوع پايانه ");}
                    else if (line.contains("c7")) {T=line.replace("c7","تاريخ ");}
                    else if (line.contains("c8")) {T=line.replace("c8","رديف ");}
                    else if (line.contains("c9")) {T=line.replace("c9",".اين صورت حساب صرفاً جنبه اطلاع رساني داشته و فاقد هرگونه ارزش قانوني مي باشد");}
                    else T=line;
                    out.append(T);
                }
            }catch (Exception e){
                Util.printMessage("e2:"+e.toString(),false);
            }
            int j=countOfTrans-1;

            String tableHTMLColumn1="<td width=\"12%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn2="<td width=\"3%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn3="<td width=\"12%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn4="<td width=\"10%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn5="<td width=\"12%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn6="<td width=\"9%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn7="<td width=\"12%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn8="<td width=\"12%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn9="<td width=\"15%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLColumn10="<td width=\"3%\" bgcolor=\"#FFFFFF\"><div align=\"center\"> " ;
            String tableHTMLEndOfColumn="</div></td>";
            int rowCount=1;
            String tempAmount="";
            String correctedAmount="";
            while (j>=0)
            {

                out.append("<tr>");
                out.append(tableHTMLColumn1+ fixLenNumber(transactionPOSList.get (j).getTerminalCode ()) + tableHTMLEndOfColumn);
                out.append(tableHTMLColumn2+ fixLenNumber(transactionPOSList.get (j).getDepositeCircle ()) +tableHTMLEndOfColumn );
                out.append(tableHTMLColumn3+ fixLenNumber(transactionPOSList.get (j).getLocalDate ()) + tableHTMLEndOfColumn);
                out.append(tableHTMLColumn4+ transactionPOSList.get (j).getTraceCode () + tableHTMLEndOfColumn);
                out.append(tableHTMLColumn5+ transactionPOSList.get (j).getAmountShaparak ()+ tableHTMLEndOfColumn);
                out.append(tableHTMLColumn6+ transactionPOSList.get (j).getReferenceCode ()+ tableHTMLEndOfColumn);
                out.append(tableHTMLColumn7+ getProcessTypeName (transactionPOSList.get (j).getProcessType ())+tableHTMLEndOfColumn);
                out.append(tableHTMLColumn8+ getTerminalTypeName (transactionPOSList.get (j).getTerminalType ())+tableHTMLEndOfColumn);
                out.append(tableHTMLColumn8+ transactionPOSList.get (j).getLocalDate ()+" "+transactionPOSList.get (j).getLocalTime ()+tableHTMLEndOfColumn);
                out.append(tableHTMLColumn9+ String.valueOf(rowCount) +tableHTMLEndOfColumn);
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
            return Util.FaxFile_Base_POS+call.getCallerID()+"-"+call.getUniQID();

        }catch (Exception e){
            Util.printMessage("e1"+e.toString(),false);
            return null;
        }

    }

    private  String CreatePDFFile(String FaxFile) throws IOException {

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

    private  String   getMainPathOfFaxFile(){

        String path=Util.FaxFile+getTodayDate();
        File file=new File(path);
        if (file.exists()) return path+"/";
        else {

            file.mkdir();
            return path+"/";
        }

    }

    private  String   getProcessTypeName(String processTypeName){

       if (processTypeName.equals ("pu")){
          return "pu";
       }
       else if (processTypeName.equals ("bi")){
           return "bi";
       }
       else if (processTypeName.equals ("bp")){
           return "bp";
       }else return "" ;//TODO
    }

    private  String   getTerminalTypeName(String terminalTypeName){

       if (terminalTypeName.equals ("pos")){
          return "pos";
       }else if (terminalTypeName.equals ("int")){
           return "int";
       }else if (terminalTypeName.equals ("mob")){
           return "mob";
       }else if (terminalTypeName.equals ("ikt")){
           return "ikt";
       }else return "" ;//TODO
    }

    private  String   fixLenNumber(String no){
        try{
            return String.valueOf(Long.valueOf(no));
        }catch (Exception e){
            return "-";
        }
    }

    private  String  correctNumberForPlay(String number) {
        return String.valueOf(convertToNumber(number));
    }
    private  String  correctDateForPlay(String date) {
        return date.replace ("/","");
    }
    private  Long   convertToNumber(String number){
        try{
            Long longNumber=Long.valueOf(number);
            return longNumber;
        }catch (Exception e){
            return -1L;
        }
    }


    @Override
    public void service (AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {

    }
}
