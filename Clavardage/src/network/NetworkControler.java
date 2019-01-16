package network;

import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.InetAddress;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import data.*;

public class NetworkControler implements PropertyChangeListener{

	private ModelData Data;
	private TCPServer TCPserver;
	private HTTPServerThread ServerHTTPThread;
	
	//pour tracker les changements faits à ModelData
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public NetworkControler(){
		//called by the InterfaceController
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public ModelData getModelData() {
		return this.Data;
	}
	
	//Listens to any changes or new data fired by TCPServer and UDPServer, and fires them back
	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("userList")) { 										//If the online users list has changed
			ArrayList<User> oldlist = new ArrayList<User>();
			this.Data.setConnectedUsers((ArrayList<User>) evt.getNewValue()); //updates local list
			pcs.firePropertyChange("userList",oldlist , (ArrayList<User>) evt.getNewValue());
		} else if(evt.getPropertyName().equals("sessionList")) {							//if new messages came in
			this.Data.setSessionList((ArrayList<Session>) evt.getNewValue());
			pcs.firePropertyChange("sessionList",new ArrayList<Session>() , (ArrayList<Session>) evt.getNewValue());
		} else if(evt.getPropertyName().equals("NewMessageFrom")) {							//new message notification
			pcs.firePropertyChange("NewMessageFrom",new String() , (String) evt.getNewValue());
		} else if(evt.getPropertyName().equals("NewFileFrom")) {							//new file notification
			pcs.firePropertyChange("NewFileFrom",new String() , (String) evt.getNewValue());
		} else if(evt.getPropertyName().equals("Pseudo")) {									//new username change
			pcs.firePropertyChange("Pseudo",new String() , (String) evt.getNewValue());
		} else if(evt.getPropertyName().equals("ConnectionStatus")) {						//new connection/disconection
			pcs.firePropertyChange("ConnectionStatus",new String() , (String) evt.getNewValue());
		} else if(evt.getPropertyName().equals("OnlineUserListHTTP")) {
			this.Data.setConnectedUsers((ArrayList<User>) evt.getNewValue()); //updates local list
			pcs.firePropertyChange("userList",new ArrayList<User>() , (ArrayList<User>) evt.getNewValue());
		}
	}
	
	//Tries to connect with a given username, returns true if success
	public boolean PerformConnectHTTP(String pseudo) {
		try {
			HTTPServerThread servertoHTTP = new HTTPServerThread(4446);
			ArrayList<User> list = servertoHTTP.sendListRequest(); //ask server for list of connected users
			boolean trouve=false;
			
			//if the list returned isn't empty
			if (!list.isEmpty() || !list.equals(null)) {
				//checks if the username isn't already in the list
				ListIterator<User> i= list.listIterator();
				while(i.hasNext() && !trouve) {
					User local=i.next();
					if(local.getUsername().equals(pseudo)) {
						trouve=true;
					}
				}
			}
			
			//if it isn't
			if (!trouve) {	
				LocalUser lU = new LocalUser(pseudo);
			//	list.add(lU.getUser());					//adds the LocalUser to its own list
				this.Data = new ModelData(lU,list); 	//Instantiates local ModelData
				servertoHTTP.sendPseudo(pseudo); //sends local username to server
				
				this.ServerHTTPThread = servertoHTTP;
				ServerHTTPThread.addPropertyChangeListener(this); 	//adds NetworkController to the ServerHTTPThread's listeners
				ServerHTTPThread.start();
				return true;
			}else { //if the username is already taken
				return false;
			}
		}catch (Exception e) {
			System.out.println("NetworkController : "+e.toString());
			return false;
		}
	}
	
	//Tries to disconnect and informs other users, returns true if success
	public boolean PerformDisconnectHTTP() {
		if (this.ServerHTTPThread.sendPseudo("disconnect")) {	//the client performs the disconnection
			this.ServerHTTPThread.stopServer();
			return true;
		}  else {
			return false;
		}
	}	
	
	//Changes the local username and informs other users
	public boolean ChangePseudoHTTP (String pseudo) {
		
		ArrayList<User> list = this.Data.getConnectedUsers();
		boolean trouve = false;
		
		//if the list returned isn't empty
		if (!list.isEmpty() || !list.equals(null)) {
			//checks if the username isn't already in the list
			ListIterator<User> i= list.listIterator();
			while(i.hasNext() && !trouve) {
				User local=i.next();
				if(local.getUsername().equals(pseudo)) {
					trouve=true;
				}
			}
		}
		
		if(!trouve) {			
			this.Data.getLocalUser().getUser().setUsername(pseudo);
			return this.ServerHTTPThread.sendPseudo(pseudo);
		} else {
			return false;
		}
	}

	//Tries to connect with a given username, returns true if success
		public boolean PerformConnectUDP(String pseudo,int portsrc, int portdist, int portTCPsrc) {
			
			//open an UDPClient
			UDPClient client = new UDPClient();
			ArrayList<User> list = client.sendBroadcastListRequest(portsrc,portdist); //broadcast requests for list of connected users
			boolean trouve=false;
			
			//if the list returned isn't empty
			if (!list.isEmpty() || !list.equals(null)) {
				//checks if the username isn't already in the list
				ListIterator<User> i= list.listIterator();
				while(i.hasNext() && !trouve) {
					User local=i.next();
					if(local.getUsername().equals(pseudo)) {
						trouve=true;
					}
				}
			}
			
			//if it isn't
			if (!trouve) {	
				LocalUser lU = new LocalUser(pseudo);
				list.add(lU.getUser());					//adds the LocalUser to its own list
				this.Data = new ModelData(lU,list); 	 //Instantiates local ModelData
				
				client.sendPseudo(this.Data.getConnectedUsers(),pseudo,portdist); //sends local username to all the people on the list
				client.close();	//close client
				
				try {
					UDPServer server= new UDPServer(this.Data,portsrc); 	//makes UDPServer thread
					addPropertyChangeListener(server); 						//adds UDPServer to listeners
					server.addPropertyChangeListener(this); 				//adds NetworkController to the UDPServer's listeners
					server.start();   										//runs the UDPServer thread
					
					this.TCPserver = new TCPServer(this.Data,portTCPsrc); 	//makes TCPServer thread
					addPropertyChangeListener(this.TCPserver); 				//adds TCPServer to listeners
					this.TCPserver.addPropertyChangeListener(this); 		//adds NetworkController to the TCPServer's listeners
					this.TCPserver.start();  								 //runs the TCPServer thread
					
				}catch(Exception e) {
					System.out.println("NetworkController: "+e.toString());
				}
				return true;
			}else { //if the username is already taken
				client.close();	//closes the client
				return false;
			}
		}
		
		//Tries to disconnect and informs other users, returns true if success
		public boolean PerformDisconnectUDP(int portsrc,int portdist) {
			UDPClient client = new UDPClient();		//opens a new UDPClient
			if (client.sendDisconnect(this.Data.getConnectedUsers(),portsrc, portdist)) {	//the client performs the disconnection
				client.close();
				this.TCPserver.stopServer();	//Stops the TCP server thread
				return true;
			}  else {
				client.close();
				return false;
			}
		}	
		
		//Changes the local username and informs other users
		public boolean ChangePseudoUDP (String pseudo) {
			boolean trouve=false;
			ArrayList<User> list = this.Data.getConnectedUsers();
			
			if (!list.isEmpty() || !list.equals(null)) {			//checks if the username is already in the list
				ListIterator<User> i= list.listIterator();
				while(i.hasNext() && !trouve) {
					User local=i.next();
					if(local.getUsername().equals(pseudo)) {
						trouve=true;
					}
				}
			}
			
			if(!trouve) {
				this.Data.removeConnectedUser(this.Data.getLocalUser().getUser()); 		//takes localUser out of the list of online users
				this.Data.getLocalUser().getUser().setUsername(pseudo); 				//changes local username
				this.Data.addConnectedUser(this.Data.getLocalUser().getUser()); 					//adds the localuser back into the list
				pcs.firePropertyChange("userList",new ArrayList<User>() , this.Data.getConnectedUsers());	//inform others that the list changed
				
				UDPClient client = new UDPClient();										//make new UDPClient()
				client.sendPseudo(this.Data.getConnectedUsers(),pseudo, 4445);			//send the username to the other online users
				client.close();
				return true;
			} else {
				return false;
			}
		}
	
	//sends a message to a specific username
	public boolean sendMessage(String pseudo, String msg,int portdst) {
		try {
			InetAddress addr = this.Data.getAddressFromPseudo(pseudo);	//finds the address corresponding to that username
			TCPClient client = new TCPClient(addr,portdst);				//makes new TCPClient() with that address
			client.sendTxt(msg);										//TCPClient sends the message
			
			if(!pseudo.equals(this.getModelData().getLocalUser().getUser().getUsername())) {	//if the message wasn't to the localuser		
		        MessageChat message = new MessageChat(this.Data.getLocalUser().getUser().getUsername(), new Date(),msg); 
		        this.Data.addMessage(message,pseudo);					//add the new message to the Session
		        pcs.firePropertyChange("sessionList", new ArrayList<Session>(), Data.getSessionlist());	//inform others that the session changed
			}
	        return true;
		}catch(Exception e){
			System.out.println("NetworkController : Message send failed "+e.toString());
			return false;
		}
	}
	
	//sends a file to a specific username
	public boolean sendFile(String pseudo, String path,int portdst) {
		try {
			InetAddress addr = this.Data.getAddressFromPseudo(pseudo);	//finds the address corresponding to that username
			TCPClient client = new TCPClient(addr,portdst);				//makes new TCPClient() with that address
			client.sendFile(path);										//TCPClient sends the message
			
			if(!pseudo.equals(this.getModelData().getLocalUser().getUser().getUsername())) {	//if the message wasn't to the localuser	
				File myFile = new File (path);								//make a file from this path
			    String name = myFile.getName();								//get the file name
				MessageChat message = new MessageChat(this.Data.getLocalUser().getUser().getUsername(), new Date(),"Envoi du fichier "+name);
		        this.Data.addMessage(message,pseudo);				//add the new message to the Session
		        pcs.firePropertyChange("sessionList", new ArrayList<Session>(), Data.getSessionlist());	//inform others that the session changed
			}
			return true;
		}catch(Exception e){
			System.out.println("NetworkController : File send failed "+e.toString());
			return false;
		}
	}
}
