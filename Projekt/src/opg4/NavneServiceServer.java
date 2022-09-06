package opg4;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NavneServiceServer {
    public static void main(String[] args) throws Exception {

        Map<String, String> ipListe = new HashMap<>();
        ipListe.put("David", "10.10.139.188");
        ipListe.put("Camilla", "10.10.131.201");
        ipListe.put("Mathias", "10.10.139.224");


        /*
        NSThread nsThread = new NSThread(ipListe);
        nsThread.start();

        while(true) {
            if(!nsThread.inUse()) {
                nsThread = new NSThread(ipListe);
                nsThread.start();
            }
        }
        */
        try {
            ServerSocket welcomeSocket = new ServerSocket(6890);

            Socket connectionSocket = welcomeSocket.accept();

            DataOutputStream outToReceiver = new DataOutputStream(connectionSocket.getOutputStream());
            BufferedReader inFromSender = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            outToReceiver.writeBytes("Skriv et navn\n");

            String clientSentence = inFromSender.readLine();

            System.out.println(clientSentence);

            if(ipListe.containsKey(clientSentence)) {
                String ip = ipListe.get(clientSentence);
                outToReceiver.writeBytes(ip + '\n');
            }else outToReceiver.writeBytes("404");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
