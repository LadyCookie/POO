package network;

import model.*;
import data.*;
import java.net.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.ListIterator;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TCPServer extends Thread implements PropertyChangeListener{
		
	private ServerSocket socket;
	private ModelData Data;
	private String localUsername;
	private ArrayList<InetAddress> activesessionList;
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public TCPServer(ModelData Data,int port) throws Exception{
		this.Data=Data;
		this.activesessionList = new ArrayList<InetAddress>();
		this.socket = new ServerSocket(port,1,this.Data.getLocalUser().getUser().getAddr());
		this.localUsername = this.Data.getLocalUser().getUser().getUsername();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		//System.out.println("Server : un Listener a été ajouté");
		pcs.addPropertyChangeListener(listener);
	 }
	
	public void propertyChange(PropertyChangeEvent evt) {
		//si on a changé la liste d'utilisateur/session on met à jour la notre
		if(evt.getPropertyName().equals("userList")) {
			this.Data.setUserConnected((ArrayList<User>) evt.getNewValue());
		} else if(evt.getPropertyName().equals("sessionList")) {
			this.Data.setSessionList((ArrayList<Session>) evt.getNewValue());
			System.out.println("TCP Server: la liste de active session a changé");
		} else if(evt.getPropertyName().equals("activesessionList")) {
			this.activesessionList = ((ArrayList<InetAddress>) evt.getNewValue());
		}
	}
	

	public void run() {
		boolean running=true;
		while(running) {
			try {
		        String data = null;
		        Socket client = this.socket.accept();
		        InetAddress clientAddr = client.getInetAddress();
		        System.out.println("\r\nServer "+this.localUsername+": New connection from " + clientAddr.toString());
		        String pseudo = this.Data.getPseudo(clientAddr); //on verifie si il est dans notre liste
		        if(!this.activesessionList.contains(clientAddr)) {
		        	ArrayList<InetAddress> oldlist = new ArrayList<InetAddress>(this.activesessionList);
		        	this.activesessionList.add(clientAddr);
		        	pcs.firePropertyChange("activesessionList", oldlist, this.activesessionList);
		        }
		        
			    //Read the data coming into the socket
			    BufferedReader in = new BufferedReader( new InputStreamReader(client.getInputStream()));  
			    
			    //on ajoute le message à la session
			    while ( (data = in.readLine()) != null ) {
			        System.out.println("\r\nServer "+this.localUsername+": Message from " + pseudo + ": " + data);
			        ArrayList<Session> oldlist = new ArrayList<Session>(Data.getSessionlist());
			        MessageChat message = new MessageChat(pseudo, new Date(),data);
			        Data.addMessage(message,pseudo);
			        pcs.firePropertyChange("sessionList", oldlist, Data.getSessionlist());
			    }
	        }catch (Exception e) {
	        	System.out.println(e.toString());
	        }
		}
    }
	
    public InetAddress getSocketAddress() {
        return this.socket.getInetAddress();
    }
    
    public int getPort() {
        return this.socket.getLocalPort();
    }

}
