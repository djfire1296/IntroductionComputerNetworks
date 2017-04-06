# IntroductionComputerNetworks
## 105-2 Introduction Computer Networks Course

## UDP
Simply type make in terminal will generate three files which are UDPServer.class, UDPClient.class and UDPClient_loss.class.

### Usage:
* Both Client and Server on Local Machine:
	(1) Execute UDPServer first.
	 ` java UDPServer ` or ` java UDPServer 9000 `, 9000 is the port number.

	(2) Type the amount of testing packet.

	(3) Execute UDPClient or UDPClient_loss. Both of them have to type port number then total test number. Please enter same number as you enter in UDPServer. The main differnce is UDPClient_loss will not transmit all packets in purpose. You can decide how many packet you want to lost and enter the amount. In order to check whether server act well or not when packet loss. Since, you shouldn't loss any packet at all when you test on local machine.

* Client and Server in Different Subnet:
	(1) Check the router's ip, and set up router's port forwarding. 
	First, check your computer's LAN IP address which is assigned by router. You can check it by type ` ifconfig ` in terminal, it will show in en0, inet. Next, set protocol will be UDP. Then, choose two port number you like which are for router's port number and destination port such as 9000, 9050. While you connect to router's ip, 9000, it will forward to your LAN IP, 9050.

	(2) Same step above. Notice that ip address in UDPClient and UDPClient_loss need to set router's ip and the same port you set in router's setting and server port must match the destination port.

## TCP
