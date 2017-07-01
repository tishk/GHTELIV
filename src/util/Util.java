package util;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class Util {
    static StringUtils strutils=new StringUtils();
    public static  enum Action{
        AccountLogin("AccountLogin"),
        AccountBalance("AccountBalance"),
        AccountLast3Transaction("AccountLast3Transaction"),
        Fax30Transaction("Fax30Transaction"),
        Fax1Month("FaxOneMonth"),
        FaxFromDateTo("FaxFromDateTo"),
        FaxNTransaction("FaxNTransaction"),
        ChangePIN1("ChangePIN1"),
        SMS("SMS"),


        ;

        private Action(String acc){
            this.action = acc;
        }

        private String action;

        private  String getAction(){
            return this.action;
        }

        private   void   setAction(String actionstring){
            this.action = actionstring;
        }

        public String  toString(){
            return this.action;
        }
    }
    public static class PropertiesUtil {

        PropertiesUtil(){
            try
            {
                readConfig();;
            }catch (Exception e){

            }
        }

        public  void readConfig() throws Exception {

           File FileOfSettings = new File(Util.class.getProtectionDomain().getCodeSource().getLocation().getPath());
           String filePath=FileOfSettings.toString();
            //printMessage("@@@@@"+filePath,false);
           String part1Path=strutils.leftString(filePath,filePath.length()-11 );
           String Path =part1Path +"telbank.properties";
          // String Path="D:\\GatewayBackups\\Gateway\\out\\production\\telbank.properties";
           setPath(part1Path);
           Properties props = new Properties();

            //----------------------------------


            try
            {
                props.load(new FileInputStream(Path));
                setHostIP(props.getProperty("HostIP"));
                setFaxPort(props.getProperty("FaxPort"));
                setPrintMessagePort(props.getProperty("PrintMessagePort"));
                setGatewayIP(props.getProperty("GatewayIP"));
                setGatewayPort(props.getProperty("GatewayPort"));
                setClientNo(props.getProperty("ClientNo"));
                setMonitoringServicePort(props.getProperty("MonitoringServicePort"));
                setMonitoringServerPort(props.getProperty("MonitoringServerPort"));
                setMonitoringServerIP(props.getProperty("MonitoringServerIP"));
                setAsteriskResetTime(props.getProperty("AsteriskResetTime"));
                setIvrPath(props.getProperty("IVRPath"));
                setMacAddress(props.getProperty("MacAddress"));
                setDataBaseDriver(props.getProperty("DataBaseDriver"));
                setDataBaseURL(props.getProperty("DataBaseURL"));
                setDataBaseName(props.getProperty("DataBaseName"));
                setDataBaseUser(props.getProperty("DataBaseUser"));
                setDataBasePassword(props.getProperty("DataBasePassword"));

            }
            catch(IOException e)
            {
                e.printStackTrace();
            }


        }
        private  String AsteriskResetTime=null;
        public  void   setAsteriskResetTime(String AsteriskResetTime){
            this.AsteriskResetTime=AsteriskResetTime;
        }
        public   String getAsteriskResetTime(){
            return AsteriskResetTime;
        }

        private String Path=null;
        public  void   setPath(String path){
            Path=path;
        }
        public  String getPath(){
            return Path;
        }

        private  String GatewayIP=null;
        public  void   setGatewayIP(String gatewayIP){
            GatewayIP=gatewayIP;
        }
        public   String getGatewayIP(){
            return GatewayIP;
        }

        private  String GatewayPort=null;
        public  void   setGatewayPort(String gatewayPort){
            GatewayPort=gatewayPort;
        }
        public   String getGatewayPort(){
            return GatewayPort;
        }

        private  String HostIP=null;
        public  void   setHostIP(String hostIP){
            HostIP=hostIP;
        }
        public   String getHostIP(){
            return HostIP;
        }

        private  String ivrPath=null;
        public  void   setIvrPath(String ivrPath){
            this.ivrPath=ivrPath;
        }
        public   String getIvrPath(){
            return this.ivrPath;
        }

        private  String macAddress=null;

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        private  String FaxPort=null;
        public  void   setFaxPort(String faxPort){
            FaxPort=faxPort;
        }
        public   String getFaxPort(){
            return FaxPort;
        }

        private  String PrintMessagePort=null;
        public  void   setPrintMessagePort(String printMessagePort){
            PrintMessagePort=printMessagePort;
        }
        public   String getPrintMessagePort(){
            return PrintMessagePort;
        }

        public  String ClientNo=null;
        public  void   setClientNo(String clientNo){
            ClientNo=clientNo;
        }
        public   String getClientNo(){
            return ClientNo;
        }

        public  String ChannelNo=null;

        public String getChannelNo() {
            return ChannelNo;
        }

        public void setChannelNo(String channelNo) {
            ChannelNo = channelNo;
        }

        private  String MonitoringServicePort=null;
        public  void   setMonitoringServicePort(String monitoringServicePort){
            MonitoringServicePort=monitoringServicePort;
        }
        public   String getMonitoringServicePort(){
            return MonitoringServicePort;
        }

        private  String MonitoringServerIP=null;
        public  void   setMonitoringServerIP(String monitoringServerIP){
            MonitoringServerIP=monitoringServerIP;
        }
        public   String getMonitoringServerIP(){
            return MonitoringServerIP;
        }

        private  String MonitoringServerPort=null;
        public  void   setMonitoringServerPort(String monitoringServerPort){
            MonitoringServerPort=monitoringServerPort;
        }
        public   String getMonitoringServerPort(){
            return MonitoringServerPort;
        }

        private  String DataBaseDriver=null;
        private  String DataBaseURL=null;
        private  String DataBaseName=null;
        private  String DataBaseUser=null;
        private  String DataBasePassword=null;

        public String getDataBaseDriver() {
            return DataBaseDriver;
        }

        public void setDataBaseDriver(String dataBaseDriver) {
            DataBaseDriver = dataBaseDriver;
        }

        public String getDataBaseURL() {
            return DataBaseURL;
        }

        public void setDataBaseURL(String dataBaseURL) {
            DataBaseURL = dataBaseURL;
        }

        public String getDataBaseName() {
            return DataBaseName;
        }

        public void setDataBaseName(String dataBaseName) {
            DataBaseName = dataBaseName;
        }

        public String getDataBaseUser() {
            return DataBaseUser;
        }

        public void setDataBaseUser(String dataBaseUser) {
            DataBaseUser = dataBaseUser;
        }

        public String getDataBasePassword() {
            return DataBasePassword;
        }

        public void setDataBasePassword(String dataBasePassword) {
            DataBasePassword = dataBasePassword;
        }
    }
    public static final PersianDateTime persianDateTime= new PersianDateTime();
    private static final PropertiesUtil prob= new PropertiesUtil();
    private static PersianDateTime PDT=new PersianDateTime();



    public static final void  printMessage(String S,boolean isForLogToFile) throws IOException {

        Socket socket = null;
        try {
            socket = new Socket(IP ,1300 );
        } catch (IOException e) {
           // System.out.print();
        }
        PrintWriter out =null;
        try {
             out = new PrintWriter(socket.getOutputStream(), true);
            out.print(S);
            out.flush();
            Thread.sleep(100);
            out=null;
            socket.close();
        } catch (IOException ioe) {
            out=null;
            socket.close();
        } catch (InterruptedException e) {
            out=null;
            socket.close();
        }
        if (isForLogToFile ){

//             loggerToFileTelBank.getInstance().logInfo(S);
        }
    }
    public static final void  printMessage(Object object) throws IOException {

        Socket socket = null;
        try {
            socket = new Socket(IP ,1300 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter out =null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.print(String.valueOf(object));
            out.flush();
            Thread.sleep(100);
            out=null;
            socket.close();
        } catch (IOException ioe) {
            out=null;
            socket.close();
        } catch (InterruptedException e) {
            out=null;
            socket.close();
        }

    }
    public static synchronized final void  sendFax(String faxFile) throws IOException {

        Socket socket = null;

        PrintWriter out =null;
        try {
            socket = new Socket(IP,Integer.valueOf (Util.faxPort));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.print(faxFile);
            out.flush();
            Thread.sleep(100);

        } catch (Exception e) {
            out.close ();
            socket.close();
        }finally {
            out.close ();
            socket.close();
        }
    }
    public static String  message="";
    public static boolean faxServiceOpened=true;

    public static final String IP= prob.getHostIP();
    public static final String faxPort= prob.getFaxPort();
    public static final String MonitoringPort= prob.getMonitoringServicePort();
    public static final String MonitoringServerPort= prob.getMonitoringServerPort();
    public static final String MonitoringSerevrIP= prob.getMonitoringServerIP();
    public static final String printMessagePort= prob.getPrintMessagePort();
    public static final String GatewayIP= prob.getGatewayIP();
    public static final String GatewayPort= prob.getGatewayPort();
    public static final String clientNo= prob.getClientNo();
    public static final String channelNo=prob.getChannelNo();
    public static final String dataBaseDriver=prob.getChannelNo();
    public static final String dataBaseURL=prob.getChannelNo();
    public static final String dataBaseName=prob.getChannelNo();
    public static final String dataBaseUser=prob.getChannelNo();
    public static final String dataBasePassword=prob.getChannelNo();
    public static final String AsteriskResetTime= prob.getAsteriskResetTime();
    public static final String ivrPath= prob.getPath();
    public static final String FaxFile= ivrPath+"Fax/sent/";
    public static final String FaxFile_Base= ivrPath+"Fax/Fax";
    public static final String FaxFile_Need= ivrPath+"Fax/";
    public static final String FaxFileBill= "/ivr/Fax/FaxBill";
    public static final String VoicePath= "PERSIAN/";
    public static final String macAddress = "PERSIAN/";
    public static  boolean faxInUS=false;


    public static ManagerConnectionFactory factory =null;
    public static AsteriskServer asteriskServer =null;
    public static ManagerConnection managerConnections[]=new ManagerConnection[Integer.valueOf(30)];
    public static boolean ConnectedToSipServer(){
        try{
            factory = new ManagerConnectionFactory("127.0.0.1",
                    "admin",
                    "Tb4tej@rat");
            return true;
        }catch (Exception e){
            //logger.getInstance().logError(logger.getClassName(), logger.getLineNumber(), e.toString(), "PromptPAyment");
            return false;
        }

    }
    public static boolean CreatedServerListener(){
        try{
            asteriskServer = new DefaultAsteriskServer("127.0.0.1",
                    "admin",
                    "Tb4tej@rat");
        }catch (Exception var1){

        }
        return true;
    }



}
