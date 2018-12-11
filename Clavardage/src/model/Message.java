package model;
import java.util.Date;

public class Message {
	private Date time;
	
	public Message() {
		this.time=new Date();
	}
	
	public Date getTime() {
		return this.time;
	}
}
