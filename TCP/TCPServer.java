// TCPServer.java
// Author: Chia-Tse, Wang
// Time: 02/04/2017


import java.io.*;
import java.net.*;

class TCPServer{
	public static void main(String[] args) throws Exception{
		ServerSocket welcomeSocket = new ServerSocket(9090);
		System.out.println("Server Ready... ");

		while(true){
			Socket socket = welcomeSocket.accept();
			System.out.println("A Client Connect. ");

			// Read a sentence from Client
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String sentence = inFromClient.readLine();
			System.out.println("RECV: " + sentence);

			// Captalize the received sentence
			String modifiedSentence = sentence.toUpperCase() + '\n';
			System.out.println("MODIFY TO: " + modifiedSentence);

			// Write the modified sentence to Client
			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
			outToClient.writeBytes(modifiedSentence);
		}
	}

}