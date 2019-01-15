package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.util.*;
import data.*;
import model.*;


public class UDPServer extends  Thread implements PropertyChangeListener {
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	private DatagramSocket socket;
	private byte[] buf = new byte[65535];
	private ModelData Data;
	
	public UDPServer(ModelData d,int portsrc) throws Exception{
			this.Data=d;
			this.socket = new DatagramSocket(portsrc);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		//System.out.println("Server : un Listener a �t� ajout�");
		pcs.addPropertyChangeListener(listener);
	 }
	
	//fonction appel�e lorsque une modification est faites � Data
	public void propertyChange(PropertyChangeEvent evt) {
		//si on a chang� la liste d'utilisateur on met � jour la notre
		if(evt.getPropertyName().equals("userList")) { 
			this.Data.setUserConnected((ArrayList<User>) evt.getNewValue());
		}
	}

	
	//permet de convertir un objet java en byte[] pour l'envoi
	private static byte[] serialize(PacketUserList ListUser) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(ListUser);
	    byte [] data = out.toByteArray();
	    os.close();
	    out.close();
	    return data;
	}
	
	private boolean isLastConnected () {
		Date localUserDate = Data.getLocalUser().getUser().getDate();
		ListIterator<User> i= Data.usersConnected().listIterator();
		
		while(i.hasNext()) {
			User local=i.next();
			Date localDate = local.getDate();
			
			if(localUserDate.before(localDate)) {
				return false;
			}
		}
		return true;
	}
	
	public void run() {
		boolean running = true;
		while(running) {
			try {
				DatagramPacket incomingPacket = new DatagramPacket(buf,buf.length);
				//System.out.println("Serveur : J'ecoute "+Thread.currentThread().getName());
				//on attend de recevoir un message
				this.socket.receive(incomingPacket);
				//System.out.println("Serveur : J'ai recu un msg ");
				
				//on extrait l'adresse et le msg
				InetAddress dstAddress = incomingPacket.getAddress();
				String msg = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
				
				if(msg.equals("ListRQ") && isLastConnected() ) {
					//On recupere le port distant
					int port= incomingPacket.getPort();
					
					//on prend notre propre liste de Connected Users, on la transforme en bytes et on met dans buf
					PacketUserList PacketList = new PacketUserList(Data.usersConnected());
					//System.out.println("Server : Je demarre la serialisation");
				    
					byte[] data = serialize(PacketList);
				  			
					//on renvoi � l'envoyeur
					DatagramPacket outgoingPacket = new DatagramPacket(data,data.length,dstAddress,port);
					this.socket.send(outgoingPacket);
				} else if(msg.equals("end")) {
					running=false;
				} else if (msg.equals("disconnect")){
					
					//on cherche cet utilisateur dans la liste
					ListIterator<User> i= Data.usersConnected().listIterator();
					
					boolean trouve = false;
					while(i.hasNext() && !trouve) {
						User local=i.next();
						//System.out.println("Server: Je cherche si son addr est dans ma liste");
						if(local.getAddr().equals(dstAddress)) {
							//System.out.println("Server: J'ai cette addr dans ma liste, je le retire");
							//System.out.println("Server: message de deconnection de "+local.getUsername());
							ArrayList<User> oldlist = new ArrayList<User>(this.Data.usersConnected()); 
							Data.removeUser(local);
							String notif = local.getUsername()+" has logged off";
							pcs.firePropertyChange("ConnectionStatus",new String() , notif);
							pcs.firePropertyChange("userList", oldlist, Data.usersConnected());		
							trouve=true;
						}
					}
				} else if(!msg.equals("ListRQ")){
					//on recup�re le pseudo
					ListIterator<User> i= Data.usersConnected().listIterator();
					boolean trouve = false;
					Date date = new Date();
					User newUser = new User(msg,dstAddress);
					ArrayList<User> oldlist = new ArrayList<User>(this.Data.usersConnected());
					while(i.hasNext() && !trouve) {
						User local = i.next();
						if(local.getAddr().equals(dstAddress)) {
							this.Data.removeUser(local);
							String notif = local.getUsername()+" changed his username to "+msg;
							pcs.firePropertyChange("Pseudo",new String() , notif);
							trouve=true;
							newUser.setDate(local.getDate()); 
						}
					}
					
					if(!trouve) {
						String notif2 = msg + " has logged on";
						pcs.firePropertyChange("ConnectionStatus",new String() , notif2);
					}
					
					this.Data.addUser(newUser);
					pcs.firePropertyChange("userList", oldlist, this.Data.usersConnected());
				}	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		this.socket.close();
	}
}
