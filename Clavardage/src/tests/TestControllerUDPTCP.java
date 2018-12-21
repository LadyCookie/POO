package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.*;
import network.*;
import model.*;

public class TestControllerUDPTCP {
	Controller Cont;
	
	public static void afficherSession(ArrayList<MessageChat> list) {
		if(list.isEmpty() || list.equals(null)) {
			System.out.println("Historique vide");
		}else {
			System.out.println("Il y a "+list.size()+" message(s)");
			ListIterator<MessageChat> j= list.listIterator();
			while(j.hasNext()) {
				MessageChat localm = j.next();
				System.out.print("Author: "+localm.getAuthor());
				System.out.print(" | Date: "+localm.getDate());
				System.out.print(" | Content: "+localm.getContent()+"\n");
			}
		}
	}
	
	public static void afficherList(ArrayList<User> list,String pseudo) {
		if(list.isEmpty() || list.equals(null)) {
			System.out.println("UserList vide");
		}else {
			//on cherche si le pseudo est déja dans la liste
			ListIterator<User> i= list.listIterator();
			while(i.hasNext()) {
				User local=i.next();
				if(!local.getUsername().equals(pseudo)) {
					if(i.hasNext()) {
						System.out.print(local.getUsername()+" , ");
					} else {
						System.out.print(local.getUsername()+"\n");
					}
				}
			}
		}
	}
	
	public static boolean isinList(ArrayList<User> list,String pseudo,String localpseudo) {
		if(list.isEmpty() || list.equals(null) || pseudo.equals(localpseudo)) {
			return false;
		}else {
			//on cherche si le pseudo est déja dans la liste
			ListIterator<User> i= list.listIterator();
			while(i.hasNext()) {
				User local=i.next();
				if(local.getUsername().equals(pseudo)) {
					return true;
				}
			}
			return false;
		}
	}
	
	@Test		
	public void test() {
		Cont = new Controller();
		String pseudo="";
		boolean pseudo_ok = false;
		
		while(!pseudo_ok) {
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Entrez un pseudo ");
			pseudo = keyboard.nextLine();
			
			pseudo_ok=Cont.PerformConnect(pseudo, 4445, 4445, 2000);
		}
		
		System.out.println("Bienvenue "+pseudo);
		afficherList(Cont.getModelData().usersConnected(),pseudo);
		
		String other_pseudo="";
		boolean other_pseudo_ok = false;
		
		while(!other_pseudo_ok) {
			Scanner keyboard1 = new Scanner(System.in);
			System.out.println("Entrez un pseudo de personne à qui envoyer un message ");
			other_pseudo = keyboard1.nextLine();
			
			other_pseudo_ok = isinList(Cont.getModelData().usersConnected(),other_pseudo,pseudo);
		}
		
		String message;
		
		for(int i=0;i<5;i++) {
			
			Scanner keyboard2 = new Scanner(System.in);
			System.out.println("Entrez un message ");
			message = keyboard2.nextLine();
			
			Cont.sendMessage(other_pseudo, message, 2000);
		}
		
		Scanner keyboard3 = new Scanner(System.in);
		System.out.println("Appuyez sur entrer pour finir ");
		message = keyboard3.nextLine();
		
		afficherSession(Cont.getModelData().getHistoric(other_pseudo));
	
	}
	
	@After
	public void tearDown() {
		//on arrête les deux threads ServerUDP
		UDPClient client = new UDPClient() ;
		client.sendBroadcastPseudo("end",4445);
		client.close();
	}

}
