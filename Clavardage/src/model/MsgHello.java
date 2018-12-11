package model;

public class MsgHello extends Message{

	private String usernameSrc;
	private String usernameDest;
	private boolean ack;
	private boolean connect;
	
	public MsgHello (String userSrc, String userDest, boolean a, boolean b) {
		this.usernameSrc = userSrc;
		this.usernameDest = userDest;
		this.ack = a;
		this.connect = b;
	}
	
	public String getUsernameSrc(){
		return this.usernameSrc;
	}
	
	public String getUsernameDest(){
		return this.usernameDest;
	}
	
	public boolean getAck(){
		return this.ack;
	}
	
	public boolean getConnect(){
		return this.connect;
	}
	
}
