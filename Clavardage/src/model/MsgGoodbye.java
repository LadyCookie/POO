package model;

public class MsgGoodbye extends Message{
	
	private String usernameSrc;
	private String usernameDest;
	
	public MsgGoodbye (String userSrc, String userDest) {
		usernameSrc = userSrc;
		usernameDest = userDest;
	}

}
