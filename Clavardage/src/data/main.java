package data;

import network.*;
import java.util.*;
import java.net.*;
import org.junit.*;
import model.*;
import data.*;

public class main {
	UDPClient client;
	UDPServer server;
	
	@Before
	public void setup() {
		try {
		LocalUser User1 = new LocalUser("Bob");
		User connected= new User("Claude",InetAddress.getLocalHost());
		ModelData md1 = new ModelData(User1);
		md1.addUser(connected);
		new UDPServer(md1).start();
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
