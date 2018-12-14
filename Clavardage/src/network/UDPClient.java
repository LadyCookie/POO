package network;

import java.net.*;
import java.io.*;
import model.*;
import data.*;
import java.util.*;

public class UDPClient {

	private DatagramSocket socket;
	private byte[] buf_envoi = new byte[1024];
	private byte[] buf_recept = new byte[1024];
	private InetAddress localAddr;
	public static InetAddress broadcastAddr;
	
	public UDPClient () {
		//on connecte une socket à internet et on recupère notre adresse IP
		try (final DatagramSocket socket = new DatagramSocket()){
			socket.setBroadcast(true);
			socket.connect(InetAddress.getByName("8.8.8.8"),10002);
			this.localAddr = socket.getLocalAddress();
			this.socket= new DatagramSocket();
			System.out.println("Client : Mon adresse est"+localAddr.toString()+"");
			System.out.println("Client : Mon socket est "+this.socket.toString()+"");
			//on recupere l'adresse de broadcast
			NetworkInterface ni=NetworkInterface.getByInetAddress(localAddr);
			for (int i =0; i<ni.getInterfaceAddresses().size();i++) {
				if(ni.getInterfaceAddresses().get(i).getBroadcast() != null) {
					broadcastAddr=ni.getInterfaceAddresses().get(i).getBroadcast();
				}
			}
			System.out.println("Client : J'ai l'adresse de broacast"+ broadcastAddr.toString() +"");
		}catch (UnknownHostException| SocketException e) {
			System.out.println("No internet");
		}
	}
	
	
	//permet d'extraire un UserListPacket d'une série de byte
	public static ArrayList<User> deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
		return (ArrayList<User>) is.readObject();
	}
	    
	//fonction d'envoi d'un broadcast pour demander les listes
	public ArrayList<User> sendBroadcastListRequest() {
		buf_envoi= "ListRQ".getBytes();
		try {
			//on fabrique le packet UDP à envoyer
			DatagramPacket packet = new DatagramPacket(buf_envoi,buf_envoi.length,broadcastAddr,4445);
			System.out.println("Client : Paquet demande liste fabriqué");
			this.socket.send(packet);
			System.out.println("Client : J'ai envoye ma demande de list en broadcast");
			
			//On attend une reponse
			byte[] data = new byte [1024];
			DatagramPacket incomingPacket = new DatagramPacket(data,data.length);
			this.socket.receive(incomingPacket);
			System.out.println("Client : J'ai reçu ma liste "+incomingPacket.getLength());
			
			//On recupere la donnee et on en extrait la liste d'utilisateurs connectes
			System.out.println("Client : Taille de la donnée "+data.length);
			ArrayList<User> list=deserialize(data);
			
			System.out.println("Client : J'ai deserialized ma liste");
			return list;
			/*
			//On choisit de n'ecouter les paquets que de cette source
			//On boucle tant qu'on a pas recu la liste vide
			while (!(usrl.isEmpty())) {
				//on attend le reste des packets
				InetAddress srcAddr=p.getSource().getAddr();
				User sender = p.getSource();
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
		return null;
	}
	
	//fonction d'envoi du pseudo en broadcast
	public void sendBroadcastPseudo(String pseudo) {
		buf_recept= pseudo.getBytes();
		try {
			//on fabrique le packet UDP à envoyer
			DatagramPacket packet = new DatagramPacket(buf_recept,buf_recept.length,broadcastAddr,4445);
			System.out.println("Client : Paquet pseudo fabriqué ("+pseudo+")"+"");
			this.socket.send(packet);
			System.out.println("Client : J'ai envoye mon pseudo en broadcast");
			
		}catch (IOException e) {
			System.out.println("Client: IOException");
		} 
	}
	
	public void close(){
		socket.close();
	}
}