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
    public static class PersianDateTime_{
        public  String  Shamsi_Date() {

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
            return String.valueOf(Year) + "/" + String.valueOf(Month) + "/" + String.valueOf(Day);

        }
        public  String  GetNowDateTime(){
            Date DateTime=new Date();
            SimpleDateFormat DateFormat =
                    new SimpleDateFormat ("yyyy.MM.dd '@' hh:mm:ss a");
            String Now=DateFormat.format(DateTime);
            return Shamsi_Date()+"  "+Now.substring(13);
        }
        public  String  GetNowDate(){
            Date  Date=new Date();
            SimpleDateFormat DateFormat =
                    new SimpleDateFormat ("yyyy.MM.dd");
            String Now=DateFormat.format(Date);
            return Now;
        }
        public  String  GetNowTime(){

            Date  Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss");
            String Now=DateFormat.format(Time);
            return Now;

        }
        public  int     GetTimeInDate(){

            Date  Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH");
            String Now=DateFormat.format(Time);
            return Integer.valueOf(Now);

        }
        public  int     GetMonthInYear(){
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
            return Month;


        }
        public  String  ConvertDBDate(String date){
            String Result=null;
            try
            {
               Result=date.substring(0,4)+"/"+date.substring(4,6)+"/"+date.substring(6,8);

            }catch (Exception e){
                Result=null;
            }
            return Result;
        }
        public  String  ConvertDBTime(String time){
            String Result=null;
            try
            {
                Result=time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);

            }catch (Exception e){
                Result=null;
            }
            return Result;
        }
    }
    public static class PersianDateTime {
        private int irYear; // Year part of a Iranian date
        private int irMonth; // Month part of a Iranian date
        private int irDay; // Day part of a Iranian date
        private int gYear; // Year part of a Gregorian date
        private int gMonth; // Month part of a Gregorian date
        private int gDay; // Day part of a Gregorian date
        private int juYear; // Year part of a Julian date
        private int juMonth; // Month part of a Julian date
        private int juDay; // Day part of a Julian date
        private int leap; // Number of years since the last leap year (0 to 4)
        private int JDN; // Julian Day Number
        private int march; // The march day of Farvardin the first (First day of jaYear)
        public PersianDateTime(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        public  String  Shamsi_Date(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            return getIranianDate();
        }
        public  String  GetNowTime(){

            Date  Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss");
            String Now=DateFormat.format(Time);
            return Now;

        }
        public  String  GetNowDate(){
            Date  Date=new Date();
            SimpleDateFormat DateFormat =
                    new SimpleDateFormat ("yyyy.MM.dd");
            String Now=DateFormat.format(Date);
            return Now;
        }
        public  int     GetTimeInDate(){

            Date  Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH");
            String Now=DateFormat.format(Time);
            return Integer.valueOf(Now);

        }
        public  int     GetMonthInYear(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            return irMonth;
        }
        public  String  getShamsiDateForFileName(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            return getIranianDateForFileName();
        }
        public  String  Shamsi_Date_WithoutSeperator(){
            Calendar calendar = new GregorianCalendar();
            setGregorianDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            return getIranianDateWithoutSeperator();
        }
        public  int     GetNowTimeInteger(){
            Date  Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm");

            String Now=DateFormat.format(Time);

            String HH=Now.substring(0,2);
            String MM=Now.substring(3,5);

            int R=Integer.valueOf(HH+MM);
            Now=null;
            HH=null;
            MM=null;
            return R;
        }
        public PersianDateTime(int year, int month, int day)
        {
            setGregorianDate(year,month,day);
        }
        public int getIranianYear() {
            return irYear;
        }
        public int getIranianMonth() {
            return irMonth;
        }
        public int getIranianDay() {
            return irDay;
        }
        public int getGregorianYear() {
            return gYear;
        }
        public int getGregorianMonth() {
            return gMonth;
        }
        public int getGregorianDay() {
            return gDay;
        }
        public int getJulianYear() {
            return juYear;
        }
        public int getJulianMonth() {
            return juMonth;
        }
        public int getJulianDay() {
            return juDay;
        }
        public String getIranianDate(){
            if ((irMonth<10)&&(irDay<10)){
                return (irYear+"/0"+irMonth+"/0"+irDay);
            }else if (irMonth<10){
                return (irYear+"/0"+irMonth+"/"+irDay);
            }else if (irDay<10){
                return (irYear+"/"+irMonth+"/0"+irDay);
            }else return (irYear+"/"+irMonth+"/"+irDay);

        }
        public String getIranianDateForFileName(){
            if ((irMonth<10)&&(irDay<10)){
                return (irYear+".0"+irMonth+".0"+irDay);
            }else if (irMonth<10){
                return (irYear+".0"+irMonth+"."+irDay);
            }else if (irDay<10){
                return (irYear+"."+irMonth+".0"+irDay);
            }else return (irYear+"."+irMonth+"."+irDay);
        }
        public String getIranianDateWithoutSeperator(){
            if ((irMonth<10)&&(irDay<10)){
                return (irYear+"0"+irMonth+"0"+irDay);
            }else if (irMonth<10){
                return (irYear+"0"+irMonth+""+irDay);
            }else if (irDay<10){
                return (irYear+""+irMonth+"0"+irDay);
            }else return (irYear+""+irMonth+""+irDay);
        }
        public String getGregorianDate()
        {
            return (gYear+"/"+gMonth+"/"+gDay);
        }
        public String getJulianDate()
        {
            return (juYear+"/"+juMonth+"/"+juDay);
        }
        public String getWeekDayStr() {
            String weekDayStr[]={
                    "Monday",
                    "Tuesday",
                    "Wednesday",
                    "Thursday",
                    "Friday",
                    "Saturday",
                    "Sunday"};
            return (weekDayStr[getDayOfWeek()]);
        }
        public String toString(){
            return (getWeekDayStr()+
                    ", Gregorian:["+getGregorianDate()+
                    "], Julian:["+getJulianDate()+
                    "], Iranian:["+getIranianDate()+"]");
        }
        public int getDayOfWeek()
        {
            return (JDN % 7);
        }
        public void nextDay(){
            JDN++;
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void nextDay(int days) {
            JDN+=days;
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void previousDay(){
            JDN--;
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void previousDay(int days){
            JDN-=days;
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void setIranianDate(int year, int month, int day){
            irYear =year;
            irMonth = month;
            irDay = day;
            JDN = IranianDateToJDN();
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void setGregorianDate(int year, int month, int day){
            gYear = year;
            gMonth = month;
            gDay = day;
            JDN = gregorianDateToJDN(year,month,day);
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        public void setJulianDate(int year, int month, int day){
            juYear = year;
            juMonth = month;
            juDay = day;
            JDN = julianDateToJDN(year,month,day);
            JDNToIranian();
            JDNToJulian();
            JDNToGregorian();
        }
        private void IranianCalendar(){
// Iranian years starting the 33-year rule
            int Breaks[]=
                    {-61, 9, 38, 199, 426, 686, 756, 818,1111,1181,
                            1210,1635,2060,2097,2192,2262,2324,2394,2456,3178} ;
            int jm,N,leapJ,leapG,jp,j,jump;
            gYear = irYear + 621;
            leapJ = -14;
            jp = Breaks[0];
// Find the limiting years for the Iranian year 'irYear'
            j=1;
            do{
                jm=Breaks[j];
                jump = jm-jp;
                if (irYear >= jm)
                {
                    leapJ += (jump / 33 * 8 + (jump % 33) / 4);
                    jp = jm;
                }
                j++;
            } while ((j<20) && (irYear >= jm));
            N = irYear - jp;
// Find the number of leap years from AD 621 to the begining of the current
// Iranian year in the Iranian (Jalali) calendar
            leapJ += (N/33 * 8 + ((N % 33) +3)/4);
            if ( ((jump % 33) == 4 ) && ((jump-N)==4))
                leapJ++;
// And the same in the Gregorian date of Farvardin the first
            leapG = gYear/4 - ((gYear /100 + 1) * 3 / 4) - 150;
            march = 20 + leapJ - leapG;
// Find how many years have passed since the last leap year
            if ( (jump - N) < 6 )
                N = N - jump + ((jump + 4)/33 * 33);
            leap = (((N+1) % 33)-1) % 4;
            if (leap == -1)
                leap = 4;
        }
        public boolean IsLeap(int irYear1){
// Iranian years starting the 33-year rule
            int Breaks[]=
                    {-61, 9, 38, 199, 426, 686, 756, 818,1111,1181,
                            1210,1635,2060,2097,2192,2262,2324,2394,2456,3178} ;
            int jm,N,leapJ,leapG,jp,j,jump;
            gYear = irYear1 + 621;
            leapJ = -14;
            jp = Breaks[0];
// Find the limiting years for the Iranian year 'irYear'
            j=1;
            do{
                jm=Breaks[j];
                jump = jm-jp;
                if (irYear1 >= jm)
                {
                    leapJ += (jump / 33 * 8 + (jump % 33) / 4);
                    jp = jm;
                }
                j++;
            } while ((j<20) && (irYear1 >= jm));
            N = irYear1 - jp;
// Find the number of leap years from AD 621 to the begining of the current
// Iranian year in the Iranian (Jalali) calendar
            leapJ += (N/33 * 8 + ((N % 33) +3)/4);
            if ( ((jump % 33) == 4 ) && ((jump-N)==4))
                leapJ++;
// And the same in the Gregorian date of Farvardin the first
            leapG = gYear/4 - ((gYear /100 + 1) * 3 / 4) - 150;
            march = 20 + leapJ - leapG;
// Find how many years have passed since the last leap year
            if ( (jump - N) < 6 )
                N = N - jump + ((jump + 4)/33 * 33);
            leap = (((N+1) % 33)-1) % 4;
            if (leap == -1)
                leap = 4;
            if (leap==4 || leap==0)
                return true;
            else
                return false;

        }
        private int IranianDateToJDN(){
            IranianCalendar();
            return (gregorianDateToJDN(gYear,3,march)+ (irMonth-1) * 31 - irMonth/7 * (irMonth-7) + irDay -1);
        }
        private void JDNToIranian(){
            JDNToGregorian();
            irYear = gYear - 621;
            IranianCalendar(); // This invocation will update 'leap' and 'march'
            int JDN1F = gregorianDateToJDN(gYear,3,march);
            int k = JDN - JDN1F;
            if (k >= 0)
            {
                if (k <= 185)
                {
                    irMonth = 1 + k/31;
                    irDay = (k % 31) + 1;
                    return;
                }
                else
                    k -= 186;
            }
            else
            {
                irYear--;
                k += 179;
                if (leap == 1)
                    k++;
            }
            irMonth = 7 + k/30;
            irDay = (k % 30) + 1;
        }
        private int julianDateToJDN(int year, int month, int day) {
            return (year + (month - 8) / 6 + 100100) * 1461/4 + (153 * ((month+9) % 12) + 2)/5 + day - 34840408;
        }
        private void JDNToJulian(){
            int j= 4 * JDN + 139361631;
            int i= ((j % 1461)/4) * 5 + 308;
            juDay = (i % 153) / 5 + 1;
            juMonth = ((i/153) % 12) + 1;
            juYear = j/1461 - 100100 + (8-juMonth)/6;
        }
        private int gregorianDateToJDN(int year, int month, int day){
            int jdn = (year + (month - 8) / 6 + 100100) * 1461/4 + (153 * ((month+9) % 12) + 2)/5 + day - 34840408;
            jdn = jdn - (year + 100100+(month-8)/6)/100*3/4+752;
            return (jdn);
        }
        private void JDNToGregorian(){
            int j= 4 * JDN + 139361631;
            j = j + (((((4* JDN +183187720)/146097)*3)/4)*4-3908);
            int i= ((j % 1461)/4) * 5 + 308;
            gDay = (i % 153) / 5 + 1;
            gMonth = ((i/153) % 12) + 1;
            gYear = j/1461 - 100100 + (8-gMonth)/6;
        }
    }
    public static final String  Shamsi_Date() {

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
        return String.valueOf(Year) + "/" + String.valueOf(Month) + "/" + String.valueOf(Day);

    }
    public static final String ConvertDBNumber(String Number){
        String No=null;
        try{
          // No=BigInteger.valueOf(Long.valueOf(Number));
            No =String.valueOf(Long.valueOf(Number));
        }catch (Exception e){

           No="?";
        }
        return No;
    }
    public static final String ConvertDBFloatNumber(String Number){
        String No=null;
        try{
            // No=BigInteger.valueOf(Long.valueOf(Number));
            No =String.valueOf(Math.round(Float.valueOf(Number)));
        }catch (Exception e){

            No="?";
        }
        return No;
    }
    public static final String ConvertDBSign(String Sign){

        try{
            String No=null;
            No=ConvertDBNumber(Sign);
            int N=Integer.valueOf(No);
            if (N==1) return "واریز";
            else if (N==0) return "برداشت";
            else return "نامعلوم";
        }catch (Exception e){

             return "نامعلوم";
        }




    }
    public static final BigInteger ConvertToBigNumber(String No){
        BigInteger R=new BigInteger("0");
        try{
            R=BigInteger.valueOf(Long.valueOf(No));
        }catch (NumberFormatException e ){
        }
        return R;
    }
    public static final int ConvertToNumber(String No){
        int R=0;
        try{
            R=Integer.valueOf(String.valueOf(No));
        }catch (NumberFormatException e ){
        }
        return R;
    }
    public static final Boolean IsEqualStrings(String Par1,String Par2){
        //Values.test(Par1+"   "+Par2);
        int result = Par1.compareTo(Par2);
        if (result == 0) {
            return true;
        } else if (result > 0) {
           return false;
        } else {
            return false;
        }
    }
    public static final Boolean IsEqualStringsNo(String Par1,String Par2){
        BigInteger P1=new BigInteger("1");
        BigInteger P2=new BigInteger("0");


        try{


            P1= Util.ConvertToBigNumber(Par1);
          //  util.Util.ShowMessage("P1 :" + P1);
            P2= Util.ConvertToBigNumber(Par2);
          //  util.Util.ShowMessage("P2 :" + P2);
          //  util.Util.ShowMessage(String.valueOf(String.valueOf(P1) == String.valueOf(P2)));
        }catch (Exception e){
           // util.Util.ShowMessage(e.toString());
        }
        return String.valueOf(P1)== String.valueOf(P2);
    }
    public static final String GetTransactionKind(String Debit,String Creditor){
      int D=-1,C=-1;
        try{
          D=Integer.valueOf(Debit);
        }catch (Exception e){
            D=-1;
        }
        try{
            C=Integer.valueOf(Creditor);
        }catch (Exception e){
            C=-1;
        }
        if ((D!=-1) && (D!=0)) return "1";
        else if ((C!=-1) && (C!=0)) return "0";
        else return "0";

    }
    public static final String GetTransactionCash(String Debit,String Creditor){
        int D=-1,C=-1;
        try{
            D=Integer.valueOf(Debit);
        }catch (Exception e){
            D=-1;
        }
        try{
            C=Integer.valueOf(Creditor);
        }catch (Exception e){
            C=-1;
        }
        if ((D!=-1) && (D!=0)) return String.valueOf(D) ;
        else if ((C!=-1) && (C!=0)) return String.valueOf(C) ;
        else return "0";

    }
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
    public static synchronized final void  sendFax(String FAXFile) throws IOException {

        Socket socket = null;
        try {
            socket = new Socket(IP,1301);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter out =null;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.print(FAXFile);
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
