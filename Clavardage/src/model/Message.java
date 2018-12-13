package model;
import data.*;

public class Message extends Packet {
	
	private static final long serialVersionUID = 1L;
	
	private String content;
	
	public Message(User source, User dest,String content) {
		super(source,dest);
		this.content=content;
	}

	public String getContent() {
		return this.content;
	}
	
}
