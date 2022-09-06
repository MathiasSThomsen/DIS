package opg3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TCPClient {

	public static void main(String[] args) throws Exception, IOException {
		
		SendThread sendThread;
		ModtagThread modtagThread;
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		Socket clientSocket = new Socket("localhost", 6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		outToServer.writeBytes("Vil du lege med mig" + '\n');
		System.out.println(inFromServer.readLine());

		sendThread = new SendThread(clientSocket, inFromUser);
		sendThread.start();

		modtagThread = new ModtagThread(clientSocket);
		modtagThread.start();

		while(true) {

			if(modtagThread.getDead()) {
				modtagThread = new ModtagThread(clientSocket);
				modtagThread.start();
			}

			if(sendThread.getDead()) {
				sendThread = new SendThread(clientSocket, inFromUser);
				sendThread.start();
			}
		}
			
	}

}
