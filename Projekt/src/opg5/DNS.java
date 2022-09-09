package opg5;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class DNS {
    public static void main(String[] args) throws Exception {

        Map<String, String> ipListe = new HashMap<>();

        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData;
        byte[] sendData;

        while (true) {
            receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            if (sentence.contains("Opret:")) {
                String name = sentence.split(":")[1].trim();
                String ip = IPAddress.toString().replace("/", "");

                sendData = opretServer(name, ip, ipListe).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

                System.out.println(ipListe);

            } else if (sentence.contains("List")) {
                StringBuilder sb = new StringBuilder();
                for (String navn : ipListe.keySet()) {
                    sb.append(navn).append(", ");
                }
                sendData = sb.toString().getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            } else {
                String ipRequest = ipListe.get(sentence.trim());
                System.out.println(ipRequest);
                sendData = ipRequest.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }
        }

    }

    private static String opretServer(String nytNavn, String ip, Map<String, String> ipListe) {
        if (ipListe.containsValue(ip)) {
            String key = "";
            for (String k : ipListe.keySet()) {
                if (ipListe.get(k).equals(ip)) {
                    key = k;
                    break;
                }
            }
            ipListe.remove(key);
        }

        if (ipListe.containsKey(nytNavn)) {
            return "Navn er taget";
        }

        ipListe.put(nytNavn, ip);
        return "Oprettet";
    }
}
