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
	Controller Cont1;
	Controller Cont2;
	Controller Cont3;
	
	public static void afficherList(ArrayList<User> list) {
		if(list.isEmpty() || list.equals(null)) {
			System.out.println("vide");
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
		Cont2= new Controller();
		Cont3= new Controller();
		//TEST 1
		System.out.println("\nTest 1: On tente de se connecter avec un pseudo déjà utilisé");
		boolean success= Cont2.PerformConnect("Bob",4446,4445);
		System.out.println("Réponse attendu: false \nRéponse obtenue: "+success);
		
		//TEST 2
		System.out.println("\nTest 2: On tente de se connecter avec un pseudo valide");
		success= Cont3.PerformConnect("Claude",4446,4445);
		System.out.println("Réponse attendu: true \nRéponse obtenue: "+success);
		System.out.print("La liste des utilisateurs connectés de Cont1 est: ");
		afficherList(Cont1.getModelData().usersConnected());
		System.out.print("La liste des utilisateurs connectés de Cont3 est: ");
		afficherList(Cont3.getModelData().usersConnected());
		
		//TEST 3
		System.out.println("\nTest 3: On tente de deconnecter Cont3");
		success= Cont3.PerformDisconnect(4446,4445);
		System.out.println("Réponse attendu: true \nRéponse obtenue: "+success);
		System.out.println("La liste des utilisateurs connectés de Cont1 est: ");
		afficherList(Cont1.getModelData().usersConnected());
		System.out.println("La liste des utilisateurs connectés de Cont3 est: ");
		afficherList(Cont3.getModelData().usersConnected());
		System.out.println("On s'attend à ce qu'elles soient vides toutes les deux, ");
		System.out.println("car on est sur le même ordinateur et la liste se vide par adresse");
	}
	
	@After
	public void tearDown() {
		//on arrête les deux threads
		UDPClient client = new UDPClient() ;
		client.sendBroadcastPseudo("end",4445);
		
	}

}
