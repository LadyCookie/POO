package model;

public class MsgExt extends Message{
	
	private String usernameSrc;
	private String usernameDest;
	private byte[] file;
	
	public MsgExt (String userSrc, String userDest, byte[] f) {
		usernameSrc = userSrc;
		usernameDest = userDest;
		file = f;
	}
}
