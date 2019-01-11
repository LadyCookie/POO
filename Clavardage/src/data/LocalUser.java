package data;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class LocalUser {
	private User Client;
	
	public LocalUser(String pseudo) {
		//on cherche l'adresse IP locale
		try (final DatagramSocket socket = new DatagramSocket()){
			socket.setBroadcast(true);
			socket.connect(InetAddress.getByName("8.8.8.8"),10002);
			InetAddress address = socket.getLocalAddress();
			socket.close();
			this.Client=new User(pseudo,address);
		}catch (UnknownHostException| SocketException e) {
			System.out.println("No Internet");
		}
	}
	
	public User getUser() {
		return this.Client;
	}
		
	public void setUser(User user) {
		this.Client = user;
	}
}
	
