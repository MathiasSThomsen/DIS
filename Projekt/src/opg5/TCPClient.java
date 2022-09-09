package opg5;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;


public class TCPClient {

    public static void main(String[] args) throws Exception {

        SendThread sendThread;
        ModtagThread modtagThread;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        //DNS--------------------------------------------------------

        String ip = "";

        while (ip.equals("")) {
            DatagramSocket dnsClientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("10.10.139.75");
            byte[] sendData;
            byte[] receiveData = new byte[1024];

            System.out.println("Write a name of a server, or 'List' to get a list of server names:");
            String sentence = inFromUser.readLine();
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            dnsClientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            dnsClientSocket.receive(receivePacket);

            ip = new String(receivePacket.getData());
            System.out.println("FROM DNS:" + ip.trim());

            if (!ip.contains("10.10.")) ip = "";

            dnsClientSocket.close();
        }

        //DNS--------------------------------------------------------

        Socket clientSocket = new Socket(ip, 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        outToServer.writeBytes("Vil du lege med mig" + '\n');
        System.out.println(inFromServer.readLine());

        sendThread = new SendThread(clientSocket, inFromUser);
        sendThread.start();

        modtagThread = new ModtagThread(clientSocket);
        modtagThread.start();

        while (true) {

            if (modtagThread.getDead()) {
                modtagThread = new ModtagThread(clientSocket);
                modtagThread.start();
            }

            if (sendThread.getDead()) {
                sendThread = new SendThread(clientSocket, inFromUser);
                sendThread.start();
            }
        }

    }

}
