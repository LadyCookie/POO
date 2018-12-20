package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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
			//System.out.println("Server: la liste de session a changé");
			this.Data.setUserConnected((ArrayList<User>) evt.getNewValue());
		}
	}

	
	//permet de convertir un objet java en byte[] pour l'envoi
	public static byte[] serialize(PacketUserList ListUser) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(ListUser);
	    byte [] data = out.toByteArray();
	//   os.close();
	 //   out.close();
	    return data;
	}
	
	public void run() {
		boolean running = true;
		while(running) {
			try {
				DatagramPacket incomingPacket = new DatagramPacket(buf,buf.length);
				//System.out.println("Serveur : J'ecoute "+Thread.currentThread().getName());
				//on attend de recevoir un message
				this.socket.receive(incomingPacket);
				//System.out.println("Serveur : J'ai recu un msg "+Thread.currentThread().getName());
				
				//on extrait l'adresse et le msg
				InetAddress dstAddress = incomingPacket.getAddress();
				String msg = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
				
				if(msg.equals("ListRQ")) {
					//System.out.println("Server : Il me demande ma liste ");
					//On recupere le port distant
					int port= incomingPacket.getPort();
					
					//on prend notre propre liste de Connected Users, on la transforme en bytes et on met dans buf
					PacketUserList PacketList = new PacketUserList(Data.usersConnected());
					//System.out.println("Server : Je demarre la serialisation");
				    
					byte[] data = serialize(PacketList);
				  			
					//on renvoi à l'envoyeur
					DatagramPacket outgoingPacket = new DatagramPacket(data,data.length,dstAddress,port);
					//System.out.println("Server : j'envoi un paquet de "+ outgoingPacket.getLength());
					this.socket.send(outgoingPacket);
					//System.out.println("Server : J'ai envoyé ma liste");
				} else if(msg.equals("end")) {
					running=false;
					//System.out.println("Server: J'ai recu l'ordre de m eteindre");
				} else if (msg.equals("disconnect")){
					//System.out.println("Server: message de deconnection");
					
					//on cherche cet utilisateur dans la liste
					ListIterator<User> i= Data.usersConnected().listIterator();
					
					boolean trouve = false;
					while(i.hasNext() && !trouve) {
						User local=i.next();
						//System.out.println("Server: Je cherche si son addr est dans ma liste");
						if(local.getAddr().equals(dstAddress) &&!local.getUsername().equals(Data.getLocalUser().getUser().getUsername())) {
							//System.out.println("Server: J'ai cette addr dans ma liste, je le retire");
							ArrayList<User> oldlist = new ArrayList<User>(this.Data.usersConnected()); 
							Data.removeUser(local);
							pcs.firePropertyChange("userList", oldlist, Data.usersConnected());		
							trouve=true;
						}
					}
				} else {
					//on recupère le pseudo
					//System.out.println("Server: son pseudo est: "+msg+"");
					ListIterator<User> i= Data.usersConnected().listIterator();
					User local=i.next();
					boolean trouve = false;
					ArrayList<User> oldlist = new ArrayList<User>(this.Data.usersConnected());
					while(i.hasNext() && !trouve) {
						//System.out.println("Server: Je cherche si son addr/pseudo est dans ma liste");
						if(local.getAddr().equals(dstAddress)) {
							//System.out.println("Server: J'ai deja cette addr dans ma liste, je le retire");
							Data.removeUser(local);
							trouve=true;
						}
						local = i.next();
					}
					User newUser = new User(msg,dstAddress);
					this.Data.addUser(newUser);
					pcs.firePropertyChange("userList", oldlist, this.Data.usersConnected());
					//System.out.println("Server: J'ai ajouté "+msg+" a ma liste");
				}	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		this.socket.close();
	}
}
