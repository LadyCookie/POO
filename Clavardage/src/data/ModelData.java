package data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;


public class ModelData{

	private LocalUser user;
	private ArrayList<User> userList;
	private ArrayList<Session> sessionList;
	
	//pour tracker les changements faits à ModelData
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public ModelData (LocalUser user,ArrayList<User> l) {
		this.user=user;
		this.userList=l;
		this.sessionList=new ArrayList<Session>();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	 }
	
	public void addMessage(MessageChat message,String OtherUser){
		ArrayList<Session> ancientList = this.sessionList; 
		
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
		//envoi une notification de changement
		pcs.firePropertyChange("sessionList", ancientList, this.sessionList);
	}
	
	public void addUser(User U) {
		ArrayList<User> ancientList= this.userList;
		this.userList.add(U);
		pcs.firePropertyChange("userList", ancientList, this.userList);
	}

	public void removeUser(User U) {
		ArrayList<User> ancientList= this.userList;
		this.userList.remove(U);
		pcs.firePropertyChange("userList", ancientList, this.userList);
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
	
	public ArrayList<User> usersConnected() {
		return this.userList;
	}
	
	public LocalUser getLocalUser() {
		return this.user;
	}	
	
}
