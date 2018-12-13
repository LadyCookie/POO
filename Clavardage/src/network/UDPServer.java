package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.util.*;
import data.*;
import model.*;


public class UDPServer extends Thread{
	
	private DatagramSocket socket;
	private InetAddress address;
	private byte[] buf = new byte[1024];
	
	public static ModelData CurrentData;
	
	public UDPServer() {
		//on connecte une socket à internet et on recupère notre adresse IP
		try (final DatagramSocket socket = new DatagramSocket()){
			socket.setBroadcast(true);
			socket.connect(InetAddress.getByName("8.8.8.8"),10002);
			this.address = socket.getLocalAddress();
			this.socket= new DatagramSocket(4445);
		}catch (UnknownHostException| SocketException e) {
			System.out.println("No internet");
		}
	}
	
	//permet de convertir un objet java en byte[] pour l'envoi
	public static byte[] serialize(UserListPacket usrlPacket) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(usrlPacket);
	    return out.toByteArray();
	}
	
	public void run() {
		boolean running = true;
		while(running) {
			DatagramPacket incomingPacket = new DatagramPacket(buf,buf.length);
			try {
				System.out.println("Serveur : J'ecoute\n");
				//on attend de recevoir un message
				this.socket.receive(incomingPacket);
				System.out.println("Serveur : J'ai recu un msg\n");
				//On recupere le port et l'adresse distants
				InetAddress dstAddress = incomingPacket.getAddress();
				int port= incomingPacket.getPort();
				
				//on récupère le pseudo de l'envoyeur et on l'ajoute aux utilisateurs connectés
				String pseudo = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
				System.out.println("Server: son pseudo est: "+pseudo+"\n");
				
				//Si on ne reçoit pas un pseudo mais la commande de fin, on ferme le socket
				if(pseudo.equals("end")) {
					running=false;
				} else {
					User newUser = new User(pseudo,dstAddress);
					CurrentData.addUser(newUser);
					
					//on prend notre propre liste de Connected Users, on la transforme en bytes et on met dans buf
					UserListPacket usrlPacket = new UserListPacket(CurrentData.getLocalUser().getUser(),newUser,CurrentData.usersConnected());
					byte[] data = serialize(usrlPacket);
					buf = data;
					
					//on renvoi à l'envoyeur
					incomingPacket = new DatagramPacket(buf,buf.length,dstAddress,port);
					this.socket.send(incomingPacket);
					System.out.println("Server : J'ai envoyé ma liste\n");
				}
			} catch (IOException e) {
				System.out.println("Probleme UDPServer");
			}
		}
		this.socket.close();
	}
}
