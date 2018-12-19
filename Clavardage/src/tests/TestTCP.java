package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.*;
import network.*;
import model.*;

public class TestTCP {
	Controller Cont1;
	Controller Cont3;
	
	public static void afficherSession(ArrayList<MessageChat> list) {
		if(list.isEmpty() || list.equals(null)) {
			System.out.println("Historique vide");
		}else {
			ListIterator<MessageChat> j= list.listIterator();
			while(j.hasNext()) {
				MessageChat localm = j.next();
				System.out.print("Author: "+localm.getAuthor());
				System.out.print("Date: "+localm.getDate());
				System.out.print("Content: "+localm.getContent());
			}
		}
	}
	
	public static void afficherList(ArrayList<User> list) {
		if(list.isEmpty() || list.equals(null)) {
			System.out.println("UserList vide");
		}else {
			//on cherche si le pseudo est déja dans la liste
			ListIterator<User> i= list.listIterator();
			while(i.hasNext()) {
				User local=i.next();
				if(i.hasNext()) {
					System.out.print(local.getUsername()+" , ");
				} else {
					System.out.print(local.getUsername()+"\n");
				}
			}
		}
	}
	
	@Before
	public void setup() {
		//On crée un utilisateur
		Cont1 = new Controller();
		if (!Cont1.PerformConnect("Bob",4445,4446)) {
			System.out.println("La connection du premier Controller a raté");
		} else {
			System.out.println("La connection du premier Controller, Bob, a reussi");
		}
	}
	
	@Test		
	public void test() {
		Cont3 = new Controller();
		boolean success= Cont3.PerformConnect("Claude",4446,4445);
		System.out.println("Claude a pu se connecter: "+success);
		User User1 = Cont1.getModelData().getLocalUser().getUser();
		User User3 = Cont3.getModelData().getLocalUser().getUser();
		System.out.println("On a donc: "+ User1.getUsername()+"    Adresse: "+User1.getAddr().toString());
		System.out.println("           "+ User3.getUsername()+" Adresse: "+User3.getAddr().toString());
		
		//on construit les server TCP
		try {
			TCPServer Server1 = new TCPServer(User1.getAddr(),Cont1.getModelData(),2001);
			Cont1.addPropertyChangeListener(Server1);
			TCPServer Server2 = new TCPServer(User3.getAddr(),Cont3.getModelData(),2000);
			Cont3.addPropertyChangeListener(Server2);
			
			TCPClient Client1 = new TCPClient(User3.getAddr(),2000);
			TCPClient Client2 = new TCPClient(User1.getAddr(),2001);
			
			//Server1.start();
			Server2.start();
			
			//on essaie d'envoyer un message a Server2
			Client1.sendTxt("Hello");
			//Client2.sendTxt("Hello back");
			
			//afficherSession(Cont1.getModelData().getHistoric("Claude"));
			//Server1.interrupt();
			Server2.interrupt();
		}catch(IOException e1) {
			System.out.println("D'envoi du message "+e1.toString());
		} catch (Exception e) {
			System.out.println("Exception de connexion "+e.toString());
		}
				
	}
	
	@After
	public void tearDown() {
		//on arrête les deux threads ServerUDP
		UDPClient client = new UDPClient() ;
		client.sendBroadcastPseudo("end",4445);
		client.sendBroadcastPseudo("end",4446);
		client.close();
	}

}
