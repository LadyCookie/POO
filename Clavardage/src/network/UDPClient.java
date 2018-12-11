package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;

public class UDPClient {

	private DatagramSocket socket;
	private byte[] buf = new byte[1024];
	private InetAddress address;
	
	public UDPClient () {
		try {
		this.socket = new DatagramSocket();
		this.address=InetAddress.getByName("localhost");
		}
		catch (SocketException e) {
			return;
		}
		catch (UnknownHostException e) {
			return;
		}
	}
	
	public String sendTXT(String msg) {
		buf = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buf,buf.length,address,4445);
		try {
			this.socket.send(packet);
		}catch (IOException e) {
			return "";
		}
		packet = new DatagramPacket(buf,buf.length);
		
		try {
			this.socket.receive(packet);
		}
		catch (IOException e) {
			return "";
		}
		
		String received = new String (packet.getData(),0,packet.getLength());
		return received;
		
	}
	
	public void close(){
		socket.close();
	}
}