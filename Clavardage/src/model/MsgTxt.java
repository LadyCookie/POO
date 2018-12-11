package model;

public class MsgTxt extends Message{
	
	private String usernameSrc;
	private String usernameDest;
	private String content;
	
	public MsgTxt (String userSrc, String userDest, String mess) {
		this.usernameSrc = userSrc;
		this.usernameDest = userDest;
		this.content = mess;
	}
	
	public String getUsernameSrc() {
		return this.usernameSrc;
	}
	
	public String getUsernameDest() {
		return this.usernameDest;
	}

	public String getContent() {
		return this.content;
	}
}
