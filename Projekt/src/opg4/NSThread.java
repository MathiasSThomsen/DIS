package opg4;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class NSThread extends Thread{
    private Map<String, String> ipListe;
    private Socket connectionSocket;

    public NSThread(Map<String, String> ipListe) throws IOException {

        this.ipListe = ipListe;

    }

    @Override
    public void run() {
        try {
            ServerSocket welcomeSocket = new ServerSocket(6789);

            connectionSocket = welcomeSocket.accept();

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

    public Boolean inUse() {
        return connectionSocket != null;
    }
}
