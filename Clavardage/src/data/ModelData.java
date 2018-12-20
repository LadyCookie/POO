package data;


import java.util.*;
import java.net.InetAddress;


public class ModelData{

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
	
	public void addUser(User U) {
		this.userList.add(U);
	}

	public void removeUser(User U) {
		this.userList.remove(U);
	}
	
	public void setUserConnected(ArrayList<User> l) {
		this.userList = l;
	}
	
	public void setSessionList(ArrayList<Session> l) {
		this.sessionList = l;
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
	
	public ArrayList<Session> getSessionlist() {
		return this.sessionList;
	}
	
	public ArrayList<User> usersConnected() {
		return this.userList;
	}
	
	public LocalUser getLocalUser() {
		return this.user;
	}	
	
	public String getPseudo(InetAddress addr) throws Exception {
		if(!this.userList.isEmpty() && !this.userList.equals(null)) {
			//on cherche si le pseudo est déja dans la liste
			ListIterator<User> i= this.userList.listIterator();
			while(i.hasNext()) {
				User local=i.next();
				if (local.getAddr().equals(addr) && !local.getUsername().equals(this.user.getUser().getUsername())){
					return local.getUsername();
				}
			}
		}
		throw new NullPointerException("La liste est vide OU on ne connait pas cet utilisateur");
	}
	
	public InetAddress getAddresse(String pseudo) throws Exception {
		if(!this.userList.isEmpty() && !this.userList.equals(null)) {
			//on cherche si le pseudo est déja dans la liste
			ListIterator<User> i= this.userList.listIterator();
			while(i.hasNext()) {
				User local=i.next();
				if (local.getUsername().equals(pseudo)){
					return local.getAddr();
				}
			}
		}
		throw new NullPointerException("La liste est vide OU on ne connait pas cet utilisateur");		
	}
	
}
