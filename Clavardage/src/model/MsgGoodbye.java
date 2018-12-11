package model;

public class MsgGoodbye extends Message{
	
	private String usernameSrc;
	private String usernameDest;
	
	public MsgGoodbye (String userSrc, String userDest) {
		this.usernameSrc = userSrc;
		this.usernameDest = userDest;
	}

	public String getUsernameSrc() {
		return this.usernameSrc;
	}
	
	public String getUsernameDest() {
		return this.usernameDest;
	}
}
