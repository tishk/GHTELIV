package util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ShowStatus {

    public ShowStatus () throws InterruptedException {
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
    private  void StartReceiver() throws InterruptedException {
        runServer();
        while (true){
            if (!ResultOfRunning) prepareSocket();
            Thread.sleep(3000);
        }


    }
    private static ShowStatus server;
    private static PersianDateTime PDT=new PersianDateTime();
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
            serverSocket = new ServerSocket(1300);
           // System.out.println("after---------");
            ResultOfRunning=true;
            Socket s =null;
            while (true) {
                try {
                     s = serverSocket.accept();
                    executorService.submit(new ServiceRequest(s));

                } catch (IOException ioe) {
                    ResultOfRunning=false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

        private Socket socket;
        public  String  GetNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }
        InputStream inputStream = null;
        byte[] content = new byte[ 2048 ];
        public   String   createPDFFile() throws IOException {

            String faxFile_PDF="/ivr/Fax/Fax2188803601-1456988406.1.pdf";
            String faxFile_HTML="/ivr/Fax/Fax2188803601-1456988406.1.html";


            //String command = "convert "+FaxFile+".jpg "+FaxFile+".pdf";
            String command = "/usr/local/bin/wkhtmltopdf --page-size A4 --dpi 2000 "+faxFile_HTML+" "+faxFile_PDF;
            // Util.printMessage(command,false);
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

        public   void  sendFax() throws IOException, InterruptedException {

            //sendfax -d 85569999  /ivr/Fax/FaxVega200.tiff
            String command = "sendfax -G -n -T1 -t1 -d 5865865869 /ivr/Fax/Fax2188803601-1456988406.1.pdf ";
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


                try {
                    if (messageString.equals("r")) {sendFax();}else
                        printMesseges(messageString);


                } catch (Exception e) {
                }


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
    }
}