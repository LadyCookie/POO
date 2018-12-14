package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.*;
import java.util.*;
import data.*;
import model.*;


public class UDPServer extends  Thread{
	
	private DatagramSocket socket;
	private byte[] buf = new byte[1024];
	
	public UDPServer() {
		try {
			this.socket = new DatagramSocket(4445);
		} catch (SocketException e) {
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
				System.out.println("Serveur : J'ecoute");
				//on attend de recevoir un message
				this.socket.receive(incomingPacket);
				System.out.println("Serveur : J'ai recu un msg");
				
				//on extrait l'adresse et le msg
				InetAddress dstAddress = incomingPacket.getAddress();
				String msg = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
				
				if(msg.equals("ListRQ")) {
					System.out.println("Server : Il me demande ma liste ");
					//On recupere le port distant
					int port= incomingPacket.getPort();
					
					//on prend notre propre liste de Connected Users, on la transforme en bytes et on met dans buf
					PacketUserList PacketList = new PacketUserList(Controller.Data.usersConnected());
					System.out.println("Server : Je demarre la serialisation");
				    
					byte[] data = serialize(PacketList);
				  			
					//on renvoi à l'envoyeur
					DatagramPacket outgoingPacket = new DatagramPacket(data,data.length,dstAddress,port);
					System.out.println("Server : j'envoi un paquet de "+ outgoingPacket.getLength());
					this.socket.send(outgoingPacket);
					System.out.println("Server : J'ai envoyé ma liste");
				} else if(msg.equals("end")) {
					running=false;
					System.out.println("Server: J'ai recu l'ordre de m eteindre");
				} else {
					//on recupère le pseudo
					System.out.println("Server: son pseudo est: "+msg+"");
					ListIterator<User> i= Controller.Data.usersConnected().listIterator();
					User local=i.next();
					boolean trouve = false;
					while(i.hasNext() && !trouve) {
						System.out.println("Server: Je cherche si son pseudo est dans ma liste");
						if(local.getAddr().equals(dstAddress)) {
							System.out.println("Server: J'ai deja cette addr dans ma liste, je le retire");
							Controller.Data.removeUser(local);
							trouve=true;
						}
						local = i.next();
					}
					User newUser = new User(msg,dstAddress);
					Controller.Data.addUser(newUser);
					System.out.println("Server: J'ai ajouté "+msg+" a ma liste");
				}	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		this.socket.close();
	}
}
