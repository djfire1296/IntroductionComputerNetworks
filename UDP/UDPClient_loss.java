// UDPClient_loss.java
// Author: Chia-Tse, Wang
// Time: 03/04/2017
// Last Update: 07/04/2017

// This program is used for testing server status on local machine. Client will lost some packet in purpose which 
// is generated randomly by GenerateLostList.

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

class UDPClient_loss{
	public static void main(String[] args) throws Exception{
		int testnum, lossnum;
		int port = 9000; // defualt port = 9000
		Scanner scanner = new Scanner(System.in);

		if(args.length == 1){
			testnum = Integer.parseInt(args[0]);
		}else{
			System.out.print("Please enter a port number(9000 - 9100): ");
			port = scanner.nextInt(); // Input port number
		}

		System.out.print("Please enter total test number: ");
		testnum = scanner.nextInt(); // Input test number

		System.out.print("Please enter lost number: ");
		lossnum = scanner.nextInt(); // Inpute lost number

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress serverIP = InetAddress.getByName("127.0.0.1");

		ArrayList<Integer> lost = new ArrayList<Integer>();
		lost = GenerateLostList(testnum, lossnum);

		long st_time = System.currentTimeMillis();

		for(int i=0; i<testnum; i++){
			if(!lost.contains(i)){
				String sentence = "Hello, test: #" + i;
				byte[] bytes = sentence.getBytes();

				DatagramPacket sendPkt = new DatagramPacket(bytes, bytes.length, serverIP, port);
				clientSocket.send(sendPkt);

				// Suspend for 1us
				Thread.sleep(1); // Prevent client side buffer overflow
			}
		}

		long end_time = System.currentTimeMillis();
		System.out.printf("%d test with %d lost are done in %.4f seconds\n",
						 			testnum, lossnum, (end_time - st_time)/1000.0);
		clientSocket.close();
	}

	// Generate a random list of packet id to lost.
	public static ArrayList<Integer> GenerateLostList(int testnum, int lossnum){
		Random rand = new Random();
		ArrayList<Integer> lost = new ArrayList<Integer>();

		while(lost.size() != lossnum){
			int l = rand.nextInt(testnum);
			if(lost.contains(l)){
				continue;
			}else{
				lost.add(l);
			}
		}

		return lost;
	}

}