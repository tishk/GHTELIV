package util;

import java.io.*;
import java.net.Socket;

/**
 * Created by Hamid on 6/2/2017.
 */
public class RequestToSwitch {

    public String send(String message) {

        try {
            String serverName = Util.GatewayIP;
            int port = Integer.parseInt(Util.GatewayPort);
            Socket client = new Socket(serverName, port);

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(message);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);


            client.close();
            return in.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
