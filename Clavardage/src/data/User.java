package data;

import java.net.InetAddress;


public class User {

	private String Username;
	private InetAddress address;
	
	public User(String Username, InetAddress address) {
		this.Username=Username;
		this.address=address;
	}
	
	public String getUsername(){
		return this.Username;
	}
	
	public InetAddress getAddr() {
		return this.address;
	}
	
}
