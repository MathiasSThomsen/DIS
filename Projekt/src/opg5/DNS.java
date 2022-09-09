package opg5;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class DNS {
    public static void main(String[] args) throws Exception {

        Map<String, String> ipListe = new HashMap<>();
        ipListe.put("David", "10.10.139.75");
        ipListe.put("Camilla", "10.10.131.221");
        ipListe.put("Mathias", "10.10.139.56");


        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData;
        byte[] sendData;

        while (true) {
            receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            System.out.println(sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            if (sentence.contains("Opret:")) {
                String name = sentence.split(":")[1].trim();
                String ip = IPAddress.toString().replace("/", "");
                ipListe.put(name, ip);

                sendData = "Server Oprettet".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                System.out.println(ipListe);

            } else {
                System.out.println(sentence);
                String ipRequest = ipListe.get(sentence.trim());
                System.out.println(ipRequest);
                System.out.println(ipListe);
                sendData = ipRequest.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }
        }
    }
}
