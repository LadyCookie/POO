package data;

import java.util.*;

public class Session {
	private String OtherUser;
	private ArrayList<MessageChat> ConvList;
	
	public Session(String OtherUser) {
		this.OtherUser=OtherUser;
		this.ConvList=new ArrayList<MessageChat>();
	}
	
	public ArrayList<MessageChat> getMessageChat(){
		return this.ConvList;
	}
	
	public String getOtherUser() {
		return this.OtherUser;
	}
	
	public void addMessage(MessageChat message) {
		ConvList.add(message);
	}
	
}
