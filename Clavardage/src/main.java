import network.*;

public class main {

	void EnvoyerMessageUDP (String message) {
		String usernameSrc = "Jac";
		String usernameDest = "Bob";
		UDPSender Co = new UDPSender(); //lance en tant que Sender UDP (socket random)
		Co.sendMessageNormalAll(message,usernameSrc,usernameDest);
		
	}
	
}
