package opg4;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendThread extends Thread {
    private final Socket connectionSocket;
    private final BufferedReader inFromUser;
    private Boolean dead;

    public SendThread(Socket connectionSocket, BufferedReader inFromUser) {
        this.connectionSocket = connectionSocket;
        this.inFromUser = inFromUser;
        dead = false;
    }

    @Override
    public void run() {
        try {
            DataOutputStream outToReceiver = new DataOutputStream(connectionSocket.getOutputStream());

            String sentence = inFromUser.readLine();
            outToReceiver.writeBytes(sentence + '\n');

            dead = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean getDead() {
        return dead;
    }
}
