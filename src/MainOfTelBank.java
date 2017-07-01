import util.FreshAsterisk;
import util.PersianDateTime;
import util.Util;
import util.ShowStatus;

import java.io.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 12/20/2015.
 */
public class MainOfTelBank {
    public  static Util util = new Util();
    private static PersianDateTime PDT = new PersianDateTime();

    public  static void    main(String args[]) throws Exception {
       startOperations();
    }
    public  static void    startOperations() throws ExecutionException, InterruptedException, IOException {

        Thread.sleep(5000);
        startMessagePrinter();
        IVRStop();
        Thread.sleep(3000);
        startFaxListener();
        Util.printMessage("Starting IVR,Please wait...",false);
        startIVR();
        startAsteriskFrechCheck();

    }
    public  static void    runCommand(String command) throws IOException {
        try {
          //  String command = " java -cp /ivr/*:. org.asteriskjava.fastagi.DefaultAgiServer";
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
          //  while ((line = br.readLine()) != null) ;//util.Util.printMessage(line,false);

        } catch (Throwable t) {
            Util.printMessage(t.getMessage(),false);
        }
    }
    private static void    startFaxListener() throws ExecutionException, InterruptedException {
        new util.FaxListener();
    }
    private static void    startMessagePrinter() throws ExecutionException, InterruptedException {
        new ShowStatus ();
    }
    private static void    startAsteriskFrechCheck(){
        new FreshAsterisk();
    }
    private static boolean startIVR() {


        try {
            String command = " java -cp "+Util.ivrPath+"/*:. org.asteriskjava.fastagi.DefaultAgiServer";
            // Valuesmain.test("Here");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            Util.printMessage("IVR Started..." , false);
            return true;

        } catch (Throwable t) {
            //  ShowMessage("Ivr Started");
            return false;
        }
    }
    private static boolean IVRStop() {
        try {
            String command = "pkill -f org.asteriskjava.fastagi.DefaultAgiServer";
            InputStreamReader isr = null;
            isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            int LineCounter = 0;
            return true;

        } catch (IOException e2) {
            return false;
        }

    }


}

