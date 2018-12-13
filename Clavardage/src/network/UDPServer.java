package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.*;
import java.util.*;
import data.*;


public class UDPServer extends Thread{
	private DatagramSocket socket;
	private byte[] buf = new byte[1024];
	
	public UDPServer() {
		try {
		this.socket=new DatagramSocket(4445);
		}
		catch ( SocketException e) {
		}
	}
	
	
	//permet de convertir un objet java en byte[] pour l'envoi
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
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
