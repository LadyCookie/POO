package data;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class LocalUser {
	private User Client;
	private boolean connected;
	
	public LocalUser(String pseudo) {
		//on cherche l'adresse IP locale
		try (final DatagramSocket socket = new DatagramSocket()){
			socket.setBroadcast(true);
			socket.connect(InetAddress.getByName("8.8.8.8"),10002);
			InetAddress address = socket.getLocalAddress();
			this.Client=new User(pseudo,address);
			this.connected=false;
		}catch (UnknownHostException| SocketException e) {
			System.out.println("No internet");
		}
		
	}
	
	public User getUser() {
		return this.Client;
	}
	
	void setConnected(){
		this.connected=true;
	}
}
