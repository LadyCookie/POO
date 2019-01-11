package data;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;


public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String Username;
	private InetAddress address;
	private Date date;
	
	public User(String Username, InetAddress address) {
		this.Username=Username;
		this.address=address;
		this.date = new Date();
	}
	
	public String getUsername(){
		return this.Username;
	}
	
	public InetAddress getAddr() {
		return this.address;
	}
	
	public Date getDate() {
		return this.date;
	}
	
}
