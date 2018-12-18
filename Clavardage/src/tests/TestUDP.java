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
	
	@Before
	public void setup() {
		//On crée un utilisateur
		Cont1 = new Controller();
		if (!Cont1.PerformConnect("Bob")) {
			System.out.println("La connection du premier Controller a raté");
		} else {
			System.out.println("La connection du premier Controller, Bob, a reussi");
		}
	}
	
	@Test	
	public void test() {
		Cont2= new Controller();
		//TEST 1
		System.out.println("Test 1:\nOn tente de se connecter avec un pseudo déjà utilisé");
		boolean success= Cont1.PerformConnect("Bob");
		System.out.println("Réponse attendu: false \nRéponse obtenue: "+success);
		
		//TEST 2
		System.out.println("Test 1:\nOn tente de se connecter avec un pseudo valide");
		success= Cont1.PerformConnect("Claude");
		System.out.println("Réponse attendu: true \nRéponse obtenue: "+success);
		System.out.println("La liste des utilisateurs connectés de Cont1 est: "+success);
		/*
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
		*/
	}
	
	@After
	public void tearDown() {
		Cont1.PerformDisconnect();
	}

}
