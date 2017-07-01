package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 7/1/2017.
 */
public  class PersianDateTime {
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
        Calendar calendar = new GregorianCalendar ();
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

        Date Time=new Date();
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
    public  String getShamsiDateWithoutSeperator (){
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
    public String getPreviousDay(int days){
        JDN-=days;
        JDNToIranian();
        JDNToJulian();
        JDNToGregorian();
        return getIranianDateWithoutSeperator ();
    }
    public  int     getYearOfNow(){
        Calendar calendar = new GregorianCalendar();
        setGregorianDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH));
        return irYear;
    }
    public  int     getMonthOfNow(){
        Calendar calendar = new GregorianCalendar();
        setGregorianDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH));
        return irMonth;
    }
    public  int     getDayOfNow(){
        Calendar calendar = new GregorianCalendar();
        setGregorianDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH));
        return irMonth;
    }
    public  String  getShamsiDateForFax(String date) {

        try{
            date=date.substring(2);
            date=date.substring(0,2)+"/"+date.substring(2,4)+"/"+date.substring(4,6);
        }catch (Exception e){

        }

        return date;
    }
    public  String  getNowTime(){

        Date  Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("HH:mm:ss");
        String Now=DateFormat.format(Time);
        return Now;

    }
}