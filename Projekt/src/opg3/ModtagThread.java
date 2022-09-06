package opg3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ModtagThread extends Thread{
    private Socket connectionSocket;
    private Boolean dead;

    public ModtagThread(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
        dead = false;
    }

    @Override
    public void run() {
        try {
            BufferedReader inFromSender = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            String clientSentence = inFromSender.readLine();
            System.out.println("Fra afsender: " + clientSentence);
            dead = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean getDead() {
        return dead;
    }
}
