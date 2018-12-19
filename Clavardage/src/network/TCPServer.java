package network;

import model.*;
import data.*;
import java.net.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.ListIterator;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TCPServer extends Thread implements PropertyChangeListener{
		
	private ServerSocket socket;
	private ModelData Data;
		/*public MyServerSocket(String ipAddress) throws Exception {
	        if (ipAddress != null && !ipAddress.isEmpty()) 
	          this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
	        else 
	          this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());
	    }*/
	
	public TCPServer(InetAddress addr,ModelData Data,int port) throws Exception{
		this.Data=Data;
		this.socket = new ServerSocket(port,1,addr);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		//si on a changé la liste d'utilisateur/session on met à jour la notre
		if(evt.getPropertyName().equals("userList")) {
			this.Data.setUserConnected((ArrayList<User>) evt.getNewValue());
		} else if(evt.getPropertyName().equals("sessionList")) {
			this.Data.setSessionList((ArrayList<Session>) evt.getNewValue());
		}
	}
	
	private String findpseudo(InetAddress addr) throws Exception{
		if(!this.Data.usersConnected().isEmpty() && !this.Data.usersConnected().equals(null)) {
			//on cherche si le pseudo est déja dans la liste
			ListIterator<User> i= this.Data.usersConnected().listIterator();
			while(i.hasNext()) {
				User local=i.next();
				if (local.getAddr().equals(addr)){
					return local.getUsername();
				}
			}
		}
		throw new NullPointerException("La liste est vide OU on ne connait pas cet utilisateur");
	}
	
	public void run() {
		boolean running=true;
		while(running) {
			try {
		        String data = null;
		        Socket client = this.socket.accept();
		        InetAddress clientAddr = client.getInetAddress();
		        System.out.println("\r\nNew connection from " + clientAddr.toString());
		        String pseudo = findpseudo(clientAddr);
			    //Read the data coming into the socket
			    BufferedReader in = new BufferedReader( new InputStreamReader(client.getInputStream()));  
			        
			    //on ajoute le message à la session
			    while ( (data = in.readLine()) != null ) {
			        System.out.println("\r\nMessage from " + clientAddr.toString() + ": " + data);
			        MessageChat message = new MessageChat(pseudo, new Date(),data);
			        Data.addMessage(message,pseudo);
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
