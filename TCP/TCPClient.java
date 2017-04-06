// TCPClient.java
// Author: Chia-Tse, Wang
// Time: 02/04/2017


import java.io.*;
import java.net.*;

class TCPClient{
	public static void main(String[] args) throws Exception{
		Socket clientSocket = new Socket("127.0.0.1", 9090);

		// Read a sentence from User input
		System.out.print("INPUT: ");
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String sentence = inFromUser.readLine();


		// Write the sentence to Server
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		outToServer.writeBytes(sentence + '\n');

		// Read the modified sentence from Server
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String modifiedSentence = inFromServer.readLine();

		System.out.println("FROM SERVR: " + modifiedSentence);
		clientSocket.close();
	}

}