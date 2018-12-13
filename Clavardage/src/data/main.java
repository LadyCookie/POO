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
		LocalUser User1 = new LocalUser("Bob");
		ModelData md1 = new ModelData(User1);
		new UDPServer(md1).start();
		client = new UDPClient();
	}
	
	@Test
	public void test() {
		String pseudo_test = "Bob";
		ArrayList<User> list = client.sendBroadcastListRequest();
		boolean trouve=false;
		//si la liste n'est pas vide on cherche si le pseudo est déja dans la liste
		if (list != null) {
			System.out.println("Sa liste n'est pas vide");
			ListIterator<User> i= list.listIterator();
			while(i.hasNext() && !trouve) {
				User local=i.next();
				System.out.println("Elle contient pseudo"+local.getUsername());
				if(local.getUsername().equals("pseudo_test")) {
					trouve=true;
				}
			}
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
