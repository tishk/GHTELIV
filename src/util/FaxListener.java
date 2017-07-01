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
    private  void StartReceiver() throws InterruptedException {
        runServer();
        while (true){
            if (!ResultOfRunning) prepareSocket();
            Thread.sleep(3000);
        }


    }
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

            serverSocket = new ServerSocket(Integer.valueOf(Util.faxPort));
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

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {

        executorService.shutdownNow();
        try {

            serverSocket.close();
            serverSocket=null;
        } catch (IOException e) {

        }
    }


    class ServiceRequest implements Runnable {
        String faxFile="";
        private Socket socket;


        public String    getNextFaxNO(){

                synchronized (countLock) {
                    faxExtension++;
                    if (faxExtension==921) faxExtension=901;
                    return String.valueOf(faxExtension);
                }

        }

        public ServiceRequest(Socket connection) throws SocketException {
            this.socket = connection;

        }
        public void run() {
            byte[] messageByte = new byte[1000];
            DataInputStream in = null;

            String messageString = "";
            int bytesRead = -1;
            try {
                in = new DataInputStream(socket.getInputStream());
                bytesRead = in.read(messageByte);
                messageString += new String(messageByte, 0, bytesRead);
                processMessage(messageString);
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



        private  void processMessage(final String RCVMessage){

            try {
                faxFile = RCVMessage;
                Util.printMessage("RCV Fax File="+RCVMessage);

                sendFax();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        public   void  sendFax() throws IOException, InterruptedException {

            System.out.println("in send fax function system.out.print:fax File is:"+faxFile);
            Util.printMessage("in send fax function",false);
            String faxNO= getNextFaxNO();
            String command = "sendfax -G -n -T1 -t1 -d "+faxNO+"  "+faxFile;


            InputStreamReader isr =null;
            try
            {
                System.out.println("start send block");
                isr = new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                line = br.readLine();
                System.out.println("end send block");
            }catch (Exception e){
                Util.printMessage("Error Sendfax:"+e.toString(),false);
                System.out.println("error send block");
            }


        }

    }
}
