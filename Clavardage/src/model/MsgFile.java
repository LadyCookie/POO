package model;

public class MsgFile extends Message{

	private String usernameSrc;
	private String usernameDest;
	private byte[] file;
	
	public MsgFile (String userSrc, String userDest, byte[] f) {
		usernameSrc = userSrc;
		usernameDest = userDest;
		file = f;
	}
}
