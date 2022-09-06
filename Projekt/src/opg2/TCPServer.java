package opg2;

import javax.lang.model.element.NestingKind;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) throws Exception {
		
		String clientSentence;
		String sentence;

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

				outToClient.writeBytes("Jeg vil gerne lege med dig, din tur" + '\n');

				while (true) {

					clientSentence = inFromClient.readLine();
					System.out.println("FROM CLIENT: " + clientSentence);
					sentence = inFromUser.readLine();
					outToClient.writeBytes(sentence + '\n');
				}
			}else System.out.println("du gad ikke at snakke, ok det er da også fint så ☻");
		}

	}

}
