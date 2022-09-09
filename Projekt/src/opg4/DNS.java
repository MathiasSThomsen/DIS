package opg4;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class DNS {
    public static void main(String[] args) throws Exception {

        Map<String, String> ipListe = new HashMap<>();
        ipListe.put("David", "10.10.139.75");
        ipListe.put("Camilla", "10.10.131.221");
        ipListe.put("Mathias", "10.10.139.56");


        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        byte[] sendData;

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            String ipRequest = ipListe.get(sentence.trim());

            sendData = ipRequest.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}
