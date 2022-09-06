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

		ServerSocket welcomSocket = new ServerSocket(6789);
		Socket connectionSocket = welcomSocket.accept();
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){

			clientSentence = inFromClient.readLine();
			System.out.println("FROM CLIENT: " + clientSentence);
			sentence = inFromUser.readLine();
			outToClient.writeBytes(sentence + '\n');
		}

	}

}
