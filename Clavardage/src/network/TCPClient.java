package network;

import java.net.*;	
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
	    private Socket socket;
	    
	    private TCPClient(InetAddress serverAddr, int serverPort) throws Exception {
	        this.socket = new Socket(serverAddr, serverPort);
	    }
	    
	    public void sendTxt(String message) throws IOException {
	    	PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true); //PrintWriter --> OutPutStream socket
	        out.println(message);
	        out.flush();
	    }

}
