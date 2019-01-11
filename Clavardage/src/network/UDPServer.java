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
		//System.out.println("Server : un Listener a été ajouté");
		pcs.addPropertyChangeListener(listener);
	 }
	
	//fonction appelée lorsque une modification est faites à Data
	public void propertyChange(PropertyChangeEvent evt) {
		//si on a changé la liste d'utilisateur on met à jour la notre
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
				  			
					//on renvoi à l'envoyeur
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
							System.out.println("Server: message de deconnection de "+local.getUsername());
							ArrayList<User> oldlist = new ArrayList<User>(this.Data.usersConnected()); 
							Data.removeUser(local);
							pcs.firePropertyChange("userList", oldlist, Data.usersConnected());		
							trouve=true;
						}
					}
				} else {
					//on recupère le pseudo
					System.out.println("Server: Nouvelle connection de "+msg);
					ListIterator<User> i= Data.usersConnected().listIterator();
					User local=i.next();
					boolean trouve = false;
					Date date = new Date();
					ArrayList<User> oldlist = new ArrayList<User>(this.Data.usersConnected());
					while(i.hasNext() && !trouve) {
						if(local.getAddr().equals(dstAddress)) {
							Data.removeUser(local);
							System.out.println("Server: "+msg+" est le nouveau pseudo de "+local.getUsername());
							trouve=true;
							date = local.getDate(); //on stocke sa date
						}
						local = i.next();
					}
					
					User newUser = new User(msg,dstAddress);
					
					if(trouve) {
						newUser.setDate(date); 
					}
					
					this.Data.addUser(newUser);
					pcs.firePropertyChange("userList", oldlist, this.Data.usersConnected());
				}	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//System.out.println("UDPServer: Je ferme mon socket");
		this.socket.close();
	}
}
