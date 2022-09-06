package opg3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) throws Exception {

		SendThread sendThread;
		ModtagThread modtagThread;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		ServerSocket welcomSocket = new ServerSocket(6789);

		while(true) {
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

				while(true) {

					if(modtagThread.getDead()) {
						modtagThread = new ModtagThread(connectionSocket);
						modtagThread.start();
					}

					if(sendThread.getDead()) {
						sendThread = new SendThread(connectionSocket, inFromUser);
						sendThread.start();
					}
				}
			}else System.out.println("du gad ikke at snakke, ok det er da også fint så ☻");
		}

	}

}
