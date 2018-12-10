package model;

public class MsgHello extends Message{

	private String usernameSrc;
	private String usernameDest;
	private boolean ack;
	private boolean connect;
	
	public MsgHello (String userSrc, String userDest, boolean a, boolean b) {
		usernameSrc = userSrc;
		usernameDest = userDest;
		ack = a;
		connect = b;
	}
	
}
