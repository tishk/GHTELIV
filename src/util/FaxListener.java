package util;

import util.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 1/22/2016.
 */
public class FaxListener {
    public static final AtomicInteger faxExt = new AtomicInteger(900);
    public static int faxExtension = 900;

    public static final Object countLock = new Object();
    public static void resetFaxExtension(){
        synchronized (countLock) {
           faxExtension=900;
        }
    }
    public FaxListener(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    StartReceiver();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }
   // public static Object faxExtension=900;
    private  void StartReceiver() throws InterruptedException {
        runServer();
        while (true){
            if (!ResultOfRunning) prepareSocket();
            Thread.sleep(3000);
        }


    }
    private static Util.PersianDateTime PDT=new Util.PersianDateTime();
    public static boolean ResultOfRunning=false;

    private ServerSocket serverSocket;

    private void prepareSocket(){
        try{
            stopServer();
        } catch (Exception e) {

        }

        runServer();
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private void runServer() {


        try {

            //System.out.println("before----------");
            serverSocket = new ServerSocket(Integer.valueOf(Util.faxPort));
            // System.out.println("after---------");
            ResultOfRunning=true;
            Socket s =null;
            while (true) {
                try {
                    s = serverSocket.accept();
                    executorService.submit(new ServiceRequest(s));

                } catch (IOException ioe) {
                    ResultOfRunning=false;
                    ioe.printStackTrace();
                }
               /* try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        //Stop the executor service.
        executorService.shutdownNow();
        try {
            //Stop accepting requests.
            serverSocket.close();
            serverSocket=null;
        } catch (IOException e) {
            //  System.out.println("Error in server shutdown");
            //  e.printStackTrace();
        }
        //System.exit(0);
    }


    class ServiceRequest implements Runnable {
        String faxFile="";
        private Socket socket;
        InputStream inputStream = null;
        byte[] content = new byte[ 2048 ];
        public   String   createPDFFile() throws IOException {

            String faxFile_PDF="/ivr/Fax/Fax2188803601-1456988406.1.pdf";
            String faxFile_HTML="/ivr/Fax/Fax2188803601-1456988406.1.html";


            //String command = "convert "+FaxFile+".jpg "+FaxFile+".pdf";
            String command = "/usr/local/bin/wkhtmltopdf --page-size A4 --dpi 2000 "+faxFile_HTML+" "+faxFile_PDF;
            // util.Util.printMessage(command,false);
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

            return null;
        }


        public String    getNextFaxNO_(){
            if (faxExt.get()==950) faxExt.set(900);
            System.out.println("in (getNextFaxNO) fax exten is before inc:"+faxExt.get());
            String faxExten=String.valueOf(faxExt.incrementAndGet());
            System.out.println("in (getNextFaxNO) fax exten is after inc:"+faxExt);
            return faxExten;
        }
        public String    getNextFaxNO(){

                synchronized (countLock) {
                    faxExtension++;
                    if (faxExtension==921) faxExtension=901;
                    return String.valueOf(faxExtension);
                }

        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        public ServiceRequest(Socket connection) throws SocketException {
            this.socket = connection;
            //socket.setSoTimeout(2000);
        }
        public void  printMesseges(String S){
            S=PDT.Shamsi_Date()+" "+PDT.GetNowTime()+" --> "+S;

            for (int i=0;i<S.length();i++) System.out.print("_");
            System.out.println("_");
            for (int i=0;i<S.length()+1;i++) System.out.print(" ");
            System.out.println("|");
            System.out.println(S+" |");
            for (int i=0;i<S.length();i++) System.out.print("_");
            System.out.println("_|");


        }
        public void run() {
            byte[] messageByte = new byte[1000];
            DataInputStream in = null;
            int LenOfMessage = 0;
            int RealLenOfMessage = 0;

            boolean end = false;
            String messageString = "";
            String Reqq = GetNowTimeWithSeparator();
            int bytesRead = -1;
            int Req = 0;
            try {
                in = new DataInputStream(socket.getInputStream());
                end = false;

                bytesRead = in.read(messageByte);
                messageString += new String(messageByte, 0, bytesRead);
                processMessage(messageString);
                messageString = "";
                messageByte = null;
                in = null;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                        in = null;
                    } catch (IOException ex) {
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                        socket = null;
                    } catch (IOException ex) {
                    }
                }
            }
        }

        public  String  GetNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }
        public int getKindOfMessage(final String msg){

            try {
                if (msg.substring(4,8).equals("0110")) return  0;
                else return  -1;
            } catch (Exception e) {
                return -1;
            }
        }
        private  void processMessage(final String RCVMessage){

            //  ExecutorService executorService = Executors.newSingleThreadExecutor();
            // Future future = executorService.submit(new Runnable() {
            // public void run() {
            // if (getDataFromStringIsOK(RCVMessage)) {
            try {
                faxFile = RCVMessage;
                Util.printMessage("RCV Fax File="+RCVMessage);
                //if (util.Util.faxServiceOpened)
                sendFax();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // }
            //  }
            //  });

        }
        public   void  sendFax() throws IOException, InterruptedException {
            //http://www.hylafax.org/man/current/faxsetup.1m.html
            //sendfax -d 85569999  /ivr/Fax/FaxVega200.tiff
            System.out.println("in send fax function system.out.print:fax File is:"+faxFile);
            Util.printMessage("in send fax function",false);
            String faxNO= getNextFaxNO();
            String command = "sendfax -G -n -T1 -t1 -d "+faxNO+"  "+faxFile;


            //http://linux.die.net/man/1/sendfax
            //Thread.sleep(2000);
            InputStreamReader isr =null;
            try
            {
                System.out.println("start send block");
               // util.Util.printMessage("command sent to hylafax:"+command,false);
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                line = br.readLine();
               // util.Util.printMessage(line,false);
                System.out.println("end send block");
                // while ((line = br.readLine()) != null) util.Util.printMessage(line,false);
            }catch (Exception e){
                Util.printMessage("Error Sendfax:"+e.toString(),false);
                System.out.println("error send block");
            }


        }
        public   void  sendFax__() throws IOException, InterruptedException {
            //http://www.hylafax.org/man/current/faxsetup.1m.html
            //sendfax -d 85569999  /ivr/Fax/FaxVega200.tiff
            faxFile="/ivr/Fax/7777777.pdf";
            //String command = "sendfax -G -n -T1 -t1 -d "+getNextFaxNo()+"  "+faxFile;
            String command="sendfax -G -n -T1 -t1 -d 803  "+faxFile;
            // String[] cmd = {"/bin/bash","-c","echo Tb4tej@rat| sudo -S "+command};

            //http://linux.die.net/man/1/sendfax
            //Thread.sleep(2000);
            InputStreamReader isr =null;
            try
            {
                Util.printMessage("command sent to hylafax:"+command,false);
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                while ((line = br.readLine()) != null) Util.printMessage(line,false);
            }catch (Exception e){
                Util.printMessage("Error Sendfax:"+e.toString(),false);
            }


        }
        public   boolean getDataFromStringIsOK(String rcv){
            try
            {
                Util.printMessage("Recieve Fax Command:"+rcv,false);
                //String[] parts = rcv.split("@");
                //faxFile = parts[0]; // 004
                // faxNumber= parts[1]; // 034556
                faxFile = rcv;
                Thread.sleep(2000);
                return true;
            }catch (Exception e){
                return false;
            }
        }
    }
}
