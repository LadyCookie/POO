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
		try {
			InetAddress address = getAddresse(OtherUser);
			boolean trouve=false;
			while(i.hasNext() && !trouve) {
				Session local=i.next();
				if(local.getOtherUserAddress().equals(address)) {
					
					local.addMessage(message);
					trouve=true;
				}
			}
			if(!trouve) { 
				//local est une session
				Session local=new Session(address);
				local.addMessage(message);
				this.sessionList.add(local);
			}
		} catch (Exception e) {
			System.out.println("Cet utilisateur n'est pas connecté");
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
		try {
			InetAddress address = getAddresse(Username);
			while(i.hasNext()) {
				Session local=i.next();
				if(local.getOtherUserAddress().equals(address)) {
					result=local.getMessageChat();
				}
			}		
			return result;
		}catch (Exception e){
			return result;
		}
	}
	
	public ArrayList<MessageChat> getHistoricFromAddress(InetAddress address){
		System.out.println("Je cherche dans ma liste de session ayant une taille de "+this.sessionList.size()+"\n");
		ListIterator<Session> i= this.sessionList.listIterator();
		ArrayList<MessageChat> result=new ArrayList<MessageChat>();
		while(i.hasNext()) {
			System.out.println("Je rentre dans la boucle");
			Session local=i.next();
			System.out.println("Je compare "+local.getOtherUserAddress().toString()+" et "+address.toString());
			if(local.getOtherUserAddress().equals(address)) {
				System.out.println("Je compare "+local.getOtherUserAddress().toString()+" et "+address.toString());
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
				if (local.getAddr().equals(addr)){
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
