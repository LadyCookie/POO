package model;

public class MsgCheck extends Message {

	private String usernameSrc;
	private String usernameDest; 
	private boolean checked;
	
	//Constructeur
	public MsgCheck(String uSrc, String uDest,boolean c) {
		usernameSrc = uSrc;
		usernameDest = uDest;
		checked = c;
	}
	
	
}
