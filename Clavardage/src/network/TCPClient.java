package network;

import java.net.*;	
import java.io.IOException;
import java.io.PrintWriter;

public class TCPClient {
	    private Socket socket;
	    
	    public TCPClient(InetAddress serverAddr, int serverPort) throws Exception {
	        this.socket = new Socket(serverAddr, serverPort);
	    }
	    
	    public void sendTxt(String message) throws IOException {
	    	PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true); //PrintWriter --> OutPutStream socket
	        out.println(message);
	        out.flush();
	        this.socket.close();
	    }

}
