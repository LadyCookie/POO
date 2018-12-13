package network;

import java.net.*;
import java.io.*;
import model.*;
import data.*;
import java.util.*;

public class UDPClient {

	private DatagramSocket socket;
	private byte[] buf = new byte[1024];
	private InetAddress address;
	public static InetAddress broadcastAddr;
	
	
	public static ModelData CurrentData;
	
	public UDPClient () {
		//on connecte une socket à internet et on recupère notre adresse IP
		try (final DatagramSocket socket = new DatagramSocket()){
			socket.setBroadcast(true);
			socket.connect(InetAddress.getByName("8.8.8.8"),10002);
			this.address = socket.getLocalAddress();
			this.socket= new DatagramSocket();
			System.out.println("Client : Mon adresse est"+address.toString()+"\n");
			System.out.println("Client : Mon socket est "+this.socket.toString()+"\n");
		}catch (UnknownHostException| SocketException e) {
			System.out.println("No internet");
		}
	}
	
	//permet d'extraire un UserListPacket d'une série de byte
	public static UserListPacket deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
		return (UserListPacket) is.readObject();
	}
	    

	//fonction d'envoi d'un broadcast
	public void sendBroadcast(String pseudo) {
		//on veut envoyer son pseudo en broadcast
		//buf = CurrentData.getLocalUser().getUser().getUsername().getBytes();
		buf= pseudo.getBytes();
		try {
			//on recupere l'adresse de broadcast
			NetworkInterface ni=NetworkInterface.getByInetAddress(this.address);
			for (int i =0; i<ni.getInterfaceAddresses().size();i++) {
				if(ni.getInterfaceAddresses().get(i).getBroadcast() != null) {
					broadcastAddr=ni.getInterfaceAddresses().get(i).getBroadcast();
				}
			}
			System.out.println("Client : J'ai l'adresse de broacast"+ broadcastAddr.toString() +"\n");
			
			//on fabrique le packet UDP à envoyer
			DatagramPacket packet = new DatagramPacket(buf,buf.length,broadcastAddr,4445);
			System.out.println("Client : Paquet fabriqué\n");
			this.socket.send(packet);
			System.out.println("Client : J'ai envoye ma demande en broadcast\n");
			
			//On attend une reponse
			packet = new DatagramPacket(buf,buf.length);
			this.socket.receive(packet);
			System.out.println("Client : J'ai reçu un paquet\n");
			
			//On recupere la donnee et on en extrait la liste d'utilisateurs connectes
			byte[] data=packet.getData();
			UserListPacket p=deserialize(data);
			
		    //On choisit de n'ecouter les paquets que de cette source
			InetAddress srcAddr=p.getSource().getAddr();
			ArrayList<User> usrl=p.getUserList();
			User sender = p.getSource();
			System.out.println("Server : le pseudo "+ sender.getUsername()+"\n");
			CurrentData.usersConnected().addAll(usrl);
			System.out.println("Client : J'ai mis à jour ma liste\n");
			
			
			/*
			//On boucle tant qu'on a pas recu la liste vide
			while (!(usrl.isEmpty())) {
				//on attend le reste des packets
				this.socket.receive(packet);
				
				data=packet.getData();
				p=deserialize(data);
				
				if(p.getSource().getAddr().equals(srcAddr)) {
					usrl=p.getUserList();
					CurrentData.usersConnected().addAll(usrl);
				}			
			}			*/
			
		}catch (IOException e) {
			System.out.println("Client: IOException");
		} catch	(ClassNotFoundException e) {
			System.out.println("Client : ClassNotFoundException");
		}
	}
	
	public void close(){
		socket.close();
	}
}