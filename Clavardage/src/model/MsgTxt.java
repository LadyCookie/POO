package model;

public class MsgTxt extends Message{
	
	private String usernameSrc;
	private String usernameDest;
	private String message;
	
	public MsgTxt (String userSrc, String userDest, String mess) {
		usernameSrc = userSrc;
		usernameDest = userDest;
		message = mess;
	}

}
