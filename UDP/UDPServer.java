// UDPServer.java
// Author: Chia-Tse, Wang
// Time: 03/04/2017
// Last Update: 06/04/2017

// TODO: Error will occur while the last packet didn't transmit since program can't teriminate!
// However, it can be fixed by add timer in runTest. If server didn't receive packet for a given time, terminate
// program and assume the rest of packet were lost. Furthermore, we can add a timer while no client
// for a long time. Also, detect the client's ip.

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;

class UDPServer{
	public static void main(String[] args) throws Exception{
		int port;
		Scanner scanner = new Scanner(System.in);

		if(args.length == 1){
			port = Integer.parseInt(args[0]);
		}else{
			System.out.print("Please enter a port number(9000 - 9100): ");
			port = scanner.nextInt();
		}

		UDPServer svr = new UDPServer();
		InetAddress localAddress = InetAddress.getLocalHost();

		DatagramSocket serverSocket = new DatagramSocket(port);

		System.out.println("server ready on ip " + localAddress + ", port " + port + "...");

		boolean again = false;
		int testnum = 0;

		do{
			System.out.print("Please enter total test number: ");
			testnum = scanner.nextInt();

			System.out.println("Waiting for the client to connect... ");
			svr.runTest(testnum, serverSocket);

			again = svr.anotherTest();
		}while(again);

		System.out.println();
		System.out.println("Thanks for your help! \n" +
                    "Please remeber to print-screen final results, and have a nice day! \n" + 
                    "Sincerely, Jerry!");

		serverSocket.close();
	}

	// Run test. Receive data from the client.
	public void runTest(int testnum, DatagramSocket serverSocket) throws Exception{
		DatagramPacket rcvdPkt = new DatagramPacket(new byte[128], 128);
		ArrayList<Integer> received_test = new ArrayList<Integer>();
		long st_time = System.currentTimeMillis();

		int count;
		for(count=0; ; count++){
			serverSocket.receive(rcvdPkt);

			// sentence will format like "Hello,this is test: #100"
			String sentence = new String(rcvdPkt.getData());
			System.out.printf("%2d. receive = %s\n", count, sentence);

			String[] split_sentence = sentence.split("#");
			received_test.add(Integer.parseInt(split_sentence[1].trim()));

			if(Integer.parseInt(split_sentence[1].trim()) == testnum -1){
				break;
			}
		}

		long end_time = System.currentTimeMillis();
		System.out.printf("%d test are done in %.4f seconds\n", testnum, (end_time - st_time)/1000000.0);
		finalresult(testnum, count+1, received_test);
	}

	// Print final result of the test.
	public void finalresult(int testnum, int receivenum, ArrayList<Integer> received_test){
		// check point: the size of the arraylist should match the number of received tests
		if(received_test.size() != receivenum){
			System.out.println("The arraylist of received test goes wrong! Check it again!");
			System.exit(1);
		}

		ArrayList<Integer> rcvdInterval = new ArrayList<Integer>();
		ArrayList<Integer> lossInterval = new ArrayList<Integer>();
		int st_idx = 0;
		int end_idx = 0;
		int sumrcvd = 0;
		int sumloss = 0;

		// If enter the following if means some packets were missing
		if(receivenum != testnum){

			// Check whether the first packet have arrived or not
			if(received_test.get(0) != 0){
				lossInterval.add(received_test.get(0));
				st_idx = received_test.get(0).intValue();
			}

			for(int i=0; i<received_test.size()-1; i++){
				if((received_test.get(i+1) - received_test.get(i)) > 1){
					end_idx = received_test.get(i).intValue();
					rcvdInterval.add(end_idx - st_idx + 1);
					lossInterval.add(received_test.get(i+1) - received_test.get(i) - 1);
					st_idx = received_test.get(i+1).intValue();
				}
			}

			rcvdInterval.add(received_test.get(received_test.size()-1) - st_idx + 1);
			
			for(Integer r : rcvdInterval) sumrcvd += r.intValue();

			for(Integer l : lossInterval) sumloss += l.intValue();

			// check point: the sum of rcvdInrerval plus the sum of lossInterval should match the total test number
			if((sumrcvd + sumloss) != testnum || sumrcvd != receivenum){
				System.out.println("Something goes wrong during construct interval arraylist! Check it again!");
				System.out.println("sumrcvd = " + sumrcvd);
				System.out.println("sumloss = " + sumloss);
				System.exit(1);
			}
		}

		float loss_percentage = (receivenum - testnum) * 100 / (float)(testnum);

		System.out.println();
		System.out.println("Final result: ");
		System.out.printf("%-27s = %d\n", "Transmission packet number", testnum);
		System.out.printf("%-27s = %d\n", "Received packet number", receivenum);
		System.out.printf("%-27s = %.2f%%\n", "Packet loss percentage", loss_percentage);

		if(!rcvdInterval.isEmpty() && !lossInterval.isEmpty()){
			int max_rcvdInterval = Collections.max(rcvdInterval).intValue();
			int min_rcvdInterval = Collections.min(rcvdInterval).intValue();
			int max_lossInterval = Collections.max(lossInterval).intValue();
			int min_lossInterval = Collections.min(lossInterval).intValue();
			float average_rcvdInterval = (float)sumrcvd / rcvdInterval.size();
			float average_lossInterval = (float)sumloss / lossInterval.size();

			System.out.printf("%-27s = %d\n", "Max received interval", max_rcvdInterval);
			System.out.printf("%-27s = %d\n", "Min received interval", min_rcvdInterval);
			System.out.printf("%-27s = %d\n", "Max lossed interval", max_lossInterval);
			System.out.printf("%-27s = %d\n", "Min lossed interval", min_lossInterval);
			System.out.printf("%-27s = %.2f\n", "Average received interval", average_rcvdInterval);
			System.out.printf("%-27s = %.2f\n", "Average lossed interval", average_lossInterval);
		}
		System.out.println();
	}


	// Check whether there is another test.
	public boolean anotherTest(){
		Scanner sc = new Scanner(System.in);

        while(true){
        	System.out.print("Do you want another test? (Y/N): ");
			String YorN = sc.next();

			if(YorN == null){
				System.out.println("Please type Y or N.");
			}else if(YorN.toUpperCase().equals("Y")){
				return true;
			}else if(YorN.toUpperCase().equals("N")){
				return false;
			}else{
				System.out.println("Please type Y or N.");
			}
        }
	}
}