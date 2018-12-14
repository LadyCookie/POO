package data;

import java.util.*;

public class ModelData {

	private LocalUser user;
	private ArrayList<User> userList;
	private ArrayList<Session> sessionList;
	
	public ModelData (LocalUser user,ArrayList<User> l) {
		this.user=user;
		this.userList=l;
		this.sessionList=new ArrayList<Session>();
	}
	
	public void addMessage(MessageChat message,String OtherUser){
		ListIterator<Session> i= this.sessionList.listIterator();
		boolean trouve=false;
		while(i.hasNext() && !trouve) {
			Session local=i.next();
			if(local.getOtherUser()==OtherUser) {
				local.addMessage(message);
				trouve=true;
			}
		}
		if(!trouve) {
			Session local=new Session(OtherUser);
			local.addMessage(message);
			this.sessionList.add(local);
		}
		
	}
	
	
	public ArrayList<MessageChat> getHistoric(String Username){
		ListIterator<Session> i= this.sessionList.listIterator();
		ArrayList<MessageChat> result=new ArrayList<MessageChat>();
		while(i.hasNext()) {
			Session local=i.next();
			if(local.getOtherUser()==Username) {
				result=local.getMessageChat();
			}
		}		
		return result;
	}
	
	public ArrayList<User> usersConnected() {
		return this.userList;
	}
	
	public LocalUser getLocalUser() {
		return this.user;
	}
	
	public void addUser(User U) {
		this.userList.add(U);
	}

	public void removeUser(User U) {
		this.userList.remove(U);
	}
	
	
}
