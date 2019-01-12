package network;

import java.net.*;
import java.io.*;
import model.*;
import data.*;
import java.util.*;

public class UDPClient{

	private DatagramSocket socket;
	private byte[] buf = new byte[65535];
	private InetAddress localAddr;
	public static InetAddress broadcastAddr;
	
	public UDPClient () {
		//on connecte une socket à internet et on recupère notre adresse IP
		try (final DatagramSocket socket = new DatagramSocket()){
			socket.setBroadcast(true);
			socket.connect(InetAddress.getByName("8.8.8.8"),10002);
			this.localAddr = socket.getLocalAddress();
			socket.close();
			this.socket= new DatagramSocket();
			//on recupere l'adresse de broadcast
			NetworkInterface ni=NetworkInterface.getByInetAddress(localAddr);
			for (int i =0; i<ni.getInterfaceAddresses().size();i++) {
				if(ni.getInterfaceAddresses().get(i).getBroadcast() != null) {
					broadcastAddr=ni.getInterfaceAddresses().get(i).getBroadcast();
				}
			}
		}catch (UnknownHostException| SocketException e) {
			//System.out.println("No internet");
		}
	}
	
	//permet d'extraire un UserListPacket d'une série de byte
	private PacketUserList deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
		return (PacketUserList) is.readObject();
	}
	    
	//fonction d'envoi d'un broadcast pour demander les listes
	public ArrayList<User> sendBroadcastListRequest(int portsrc, int portdist) {
		buf= "ListRQ".getBytes();
		try {
			//on fabrique le packet UDP à envoyer
			DatagramPacket packet = new DatagramPacket(buf,buf.length,broadcastAddr,portdist);
			//System.out.println("Client : Paquet demande liste fabriqué");
			this.socket.send(packet);
			//System.out.println("Client : J'ai envoye ma demande de list en broadcast");
			
			//On attend une reponse
			byte[] data = new byte [65535];
			DatagramPacket incomingPacket = new DatagramPacket(data,data.length);
			incomingPacket.setPort(portdist);
			this.socket.setSoTimeout(5000); //on laisse le socket ouvert 30s sinon il lance une exception
			this.socket.receive(incomingPacket);
			//System.out.println("Client : J'ai reçu ma liste "+incomingPacket.getLength());
			
			//On recupere la donnee et on en extrait la liste d'utilisateurs connectes
			//System.out.println("Client : Taille de la donnée "+data.length);
			PacketUserList Packetlist=deserialize(data);
			ArrayList<User> list = Packetlist.getUserList();
			
			//System.out.println("Client : J'ai deserialized ma liste");
			return list;
		} catch (SocketTimeoutException e) {	
			//si il n'a rien reçu venant du réseau, il assume qu'il est seul et renvoi la liste vide
			//System.out.println("Client: Aucune liste reçu, je suis seul sur le reseau");
			return new ArrayList<User>();
		}catch (IOException e) {
			//System.out.println("Client: IOException");
		} catch	(ClassNotFoundException e) {
			//System.out.println("Client : ClassNotFoundException");
		}
		return null;
	}
	
	//fonction d'envoi du pseudo en broadcast
	public boolean sendPseudo(ArrayList<User> list, String pseudo, int portdist) {
		buf= pseudo.getBytes();
		try {
			//on fabrique le packet UDP à envoyer
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			packet.setPort(portdist);
			
			//on parcourt la liste et on envoi à chaque utilisateur
			ListIterator<User> i= list.listIterator();
			while(i.hasNext()) {
				User local=i.next();
				if(!local.getAddr().equals(localAddr)) {
					packet.setAddress(local.getAddr());
					this.socket.send(packet);
				}
			}	
			
			return true;
		}catch (IOException e) {
			System.out.println("ClientUDP: IOException");
			return false;
		} 
	}

	//fonction d'envoi d'un disconnect aux utilisateurs connectés
	public boolean sendDisconnect(ArrayList<User> list, int portsrc,int portdst) {
		buf= "disconnect".getBytes();
		try {
			//on fabrique le packet UDP à envoyer
			DatagramPacket packet = new DatagramPacket(buf,buf.length);
			packet.setPort(portdst);
			
			//on parcourt la liste et on envoi à chaque utilisateur
			ListIterator<User> i= list.listIterator();
			while(i.hasNext()) {
				User local=i.next();
				if(!local.getAddr().equals(localAddr)) {
					packet.setAddress(local.getAddr());
					this.socket.send(packet);
				}
			}		
			
			//on envoi au server l'ordre de s'eteindre
			buf= "end".getBytes();
			DatagramPacket packetServer = new DatagramPacket(buf,buf.length,localAddr,portsrc);
			this.socket.send(packetServer);
			return true;
		}catch (IOException e) {
				//System.out.println("Client: IOException : " +e.toString());
				return false;
		} 
	}
	
	public void close(){
		this.socket.close();
	}
}