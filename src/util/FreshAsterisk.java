package util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 6/12/2016.
 */
public class FreshAsterisk {

    int c=0;
    Timer timer=null;
    public FreshAsterisk(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    checkIfNeedReboot();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void    checkIfNeedReboot() throws IOException {

        int delay = 1000*60*60;
        Util.printMessage("Start fresh Asterisk thread" , false);
        int period = 1000*60*10;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (getNowHour()==Integer.valueOf(Util.AsteriskResetTime)) try {
                    Util.faxServiceOpened=false;
                    Thread.sleep(1000*60*5);
                    freshAsteriskOperations();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);
    }
    public  int     getNowHour(){
        Date Time=new Date();
        SimpleDateFormat DateFormat =new SimpleDateFormat ("HH");
        String Now=DateFormat.format(Time);
        return Integer.valueOf(Now);
    }
    public  void    freshAsteriskOperations() throws InterruptedException, IOException {
        deleteSentFaxOfToday();
        FaxListener.resetFaxExtension();
        Util.printMessage("System reset done!",true);
        //runScript("/ivr/reStartTelbank.sh");
        runCommand("amportal stop");
        Thread.sleep(10000);
        runCommand("reboot");

    }
    public  void    freshInternalAsteriskOperations() throws InterruptedException, IOException {
        //Util.faxServiceOpened=false;

        // Thread.sleep(1000*1*5);
        // c++;
        // Util.printMessage("fresh Asterisk Count:"+String.valueOf(c) , true);
        // runCommand("amportal restart");
        //Thread.sleep(1000 * 5 * 1);
        // runCommand("faxsetup");
        // Thread.sleep(1000 * 5 * 1);
        Util.faxServiceOpened=false;
        Thread.sleep(1000*60*3);
        deleteSentFaxOfToday();
        // FaxListener.faxExtension=(Object)900;
        Util.printMessage("fresh Asterisk :Asterisk Restarted...", false);
       // runScript("/ivr/reStartTelbank.sh");
        Util.printMessage("by by ...", false);
        System.exit(0);
        Thread.sleep(1000 * 10);
        //Util.faxServiceOpened=true;

    }

    private  static void  runCommand(String command) throws IOException {
        try {
            //  String command = " java -cp /ivr/*:. org.asteriskjava.fastagi.DefaultAgiServer";
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);



        } catch (Throwable t) {
            Util.printMessage(t.getMessage(),false);
        }
    }
    private  static void  runScript(String command) throws IOException {
        try {
            //  String command = " java -cp /ivr/*:. org.asteriskjava.fastagi.DefaultAgiServer";
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) ;System.out.println(line);

        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }
    private  static void  deleteSentFaxOfToday() {
        try {
            FileUtils.deleteDirectory(new File(Util.FaxFile));
            FileUtils.forceMkdir(new File(Util.FaxFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private  static void  clearHylafaxLog() throws IOException {
        deleteContentOdDirectory("/var/spool/hylafax/doneq");
        deleteContentOdDirectory("/var/spool/hylafax/recvq");
        deleteContentOdDirectory("/var/spool/hylafax/log");
        deleteContentOdDirectory("/var/spool/hylafax/sendq");
        deleteContentOdDirectory("/var/spool/hylafax/docq");
        deleteContentOdDirectory("/var/spool/hylafax/pollq");
    }
    private  static void  deleteContentOdDirectory(String dir) throws IOException {
       runCommand("rm -rf "+dir+"/*");
    }

}
