package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.*;
import network.*;
import model.*;

public class TestUDP {
	UDPClient client;
	UDPServer server;
	
	@Before
	public void setup() {
		try {
		//On crée un utilisateur
		Controller Cont1 = new Controller();
		Perform
		
		
		
		//on lance un client
		client = new UDPClient();
		} catch(UnknownHostException e) {
		}
	}
	
	@Test
	public void test() {
		String pseudo_test = "Claude";
		ArrayList<User> list = client.sendBroadcastListRequest();
		boolean trouve=false;
		//si la liste n'est pas vide on cherche si le pseudo est déja dans la liste
		if ( !( list.isEmpty() ) && (list != null) ) {
			System.out.println("Sa liste n'est pas vide");
			ListIterator<User> i= list.listIterator();
			
			while(i.hasNext() && !trouve) {
				User local=i.next();
				System.out.println("Je cherche si son pseudo est dans la liste");
				System.out.println("Elle contient pseudo "+local.getUsername());
				if(local.getUsername().equals(pseudo_test)) {
					trouve=true;
				}
			}
		} else {
			System.out.println("Sa liste est vide");
		}
		if (trouve==false) {
			System.out.println("Ce pseudo est valide");
			client.sendBroadcastPseudo(pseudo_test);
		} else {
			System.out.println("Try again");
		} 
	}
	
	@After
	public void tearDown() {
		client.sendBroadcastPseudo("end");
		client.close();
	}

}
