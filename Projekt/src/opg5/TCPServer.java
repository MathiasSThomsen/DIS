package opg5;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;

public class TCPServer {

    public static void main(String[] args) throws Exception {

        SendThread sendThread;
        ModtagThread modtagThread;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        ServerSocket welcomSocket = new ServerSocket(6789);


        // DNS-------------------------------------------------

        DatagramSocket dnsClientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("10.10.139.75");
        byte[] sendData;
        byte[] receiveData = new byte[1024];

        System.out.println("Write 'Opret:' and a name you want to call this server:");
        String sentence = inFromUser.readLine();
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        dnsClientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        dnsClientSocket.receive(receivePacket);

        String acknowledgement = new String(receivePacket.getData());
        System.out.println("FROM DNS:" + acknowledgement.trim());
        dnsClientSocket.close();

        // DNS-------------------------------------------------


        while (true) {
            Socket connectionSocket = welcomSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String startBesked = inFromClient.readLine();
            System.out.println(startBesked);
            System.out.println("skriv J for ja");
            String svar = inFromUser.readLine();

            if (svar.equals("J")) {

                outToClient.writeBytes("Jeg vil gerne lege med dig" + '\n');
                sendThread = new SendThread(connectionSocket, inFromUser);
                sendThread.start();

                modtagThread = new ModtagThread(connectionSocket);
                modtagThread.start();

                while (true) {

                    if (modtagThread.getDead()) {
                        modtagThread = new ModtagThread(connectionSocket);
                        modtagThread.start();
                    }

                    if (sendThread.getDead()) {
                        sendThread = new SendThread(connectionSocket, inFromUser);
                        sendThread.start();
                    }
                }
            } else System.out.println("du gad ikke at snakke, ok det er da også fint så ☻");
        }

    }

}
