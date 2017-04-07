// UDPClient.java
// Author: Chia-Tse, Wang
// Time: 03/04/2017
// Last Update: 07/04/2017

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

class UDPClient{
	public static void main(String[] args) throws Exception{
		int testnum;
		int port = 9000; // defualt port = 9000
		boolean suspend = false;
		Scanner scanner = new Scanner(System.in);

		if(args.length == 1){
			port = Integer.parseInt(args[0]);
		}else if(args.length == 2){
			if(args[1].equals('-s')){
				suspend = true;
			}
		}else{
			System.out.print("Please enter a port number(9000 - 9100): ");
			port = scanner.nextInt();
		}

		System.out.print("Please enter total test number: ");
		testnum = scanner.nextInt();

		DatagramSocket clientSocket = new DatagramSocket();

		// Send the sentence to Server 100 times continously
//		byte[] ip = new byte[]{(byte)114, (byte)24, (byte)29, (byte)223};
		byte[] ip = new byte[]{(byte)127, (byte)0, (byte)0, (byte)1};
		InetAddress serverIP = InetAddress.getByAddress(ip);
//		String ipString = "127.0.0.1";
//		ipString = ChangeIp();

//		InetAddress serverIP = InetAddress.getByName(ipString);
		long st_time = System.currentTimeMillis();

		for(int i=0; i<testnum; i++){
			String sentence = "Hello, test: #" + i;
			byte[] bytes = sentence.getBytes();

			DatagramPacket sendPkt = new DatagramPacket(bytes, bytes.length, serverIP, port);
			clientSocket.send(sendPkt);

			// Suspend for 1us
			if(suspend){
				Thread.sleep(1); // Prevent client side buffer overflow
			}
		}
		
		long end_time = System.currentTimeMillis();
		System.out.printf("%d test are done in %.4f seconds\n", testnum, (end_time - st_time)/1000.0);

		clientSocket.close();
	}

	// Ask for change ip or not
	// TODO: Need to check ip valid or not.
	public static String ChangeIp(){
		Scanner sc = new Scanner(System.in);
		String ipString = null;

        while(true){
        	System.out.print("Default IP is 127.0.0.1. Do you want change ip address? (Y/N): ");
			String YorN = sc.next();

			if(YorN == null){
				System.out.println("Please type Y or N.");
			}else if(YorN.toUpperCase().equals("Y")){
				ipString = sc.next();
				return ipString;
			}else if(YorN.toUpperCase().equals("N")){
				return "127.0.0.1";
			}else{
				System.out.println("Please type Y or N.");
			}
        }
	}
}