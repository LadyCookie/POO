package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

public class UDPServer extends Thread{
	private DatagramSocket socket;
	private byte[] buf = new byte[1024];
	
	public UDPServer(DatagramSocket socket) {
		this.socket=socket;
	}
	
	public void run() {
		boolean running = true;
		while(running) {
			DatagramPacket incomingPacket = new DatagramPacket(buf,buf.length);
			try {
				this.socket.receive(incomingPacket);
			}
			catch (IOException e) {
				return;
			}
			
			InetAddress address = incomingPacket.getAddress();
			int port= incomingPacket.getPort();
			incomingPacket = new DatagramPacket(buf,buf.length,address,port);
			String received = new String(incomingPacket.getData(),0,incomingPacket.getLength());
			if(received.equals("end")) {
				running=false;
				continue;
			}
			try {
			this.socket.send(incomingPacket);
			} catch (IOException e) {
				return;
			}
		}
		this.socket.close();
	}
}
