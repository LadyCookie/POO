package tests;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

import org.junit.Test;

import data.User;
import model.Controller;

public class TestFileTCP {
	
	Controller Cont;

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
		System.out.println();
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
		
		//boucle pour choisir un pseudo valide
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
		
		//boucle pour choisir avec qui on veut clavarder
		while(!other_pseudo_ok) {
			Scanner keyboard1 = new Scanner(System.in);
			System.out.println("Entrez un pseudo de personne à qui envoyer un message ");
			other_pseudo = keyboard1.nextLine();
			
			other_pseudo_ok = isinList(Cont.getModelData().usersConnected(),other_pseudo,pseudo);
		}
		
		String path;
		
		if(!other_pseudo.equals("DISCONNECT")) {
					
			Scanner keyboard2 = new Scanner(System.in);
			System.out.println("Entrez un chemin ");
			path = keyboard2.nextLine();
			keyboard2.close();
			
			Cont.sendFile(other_pseudo, path, 2000);
			
		}
		
		Scanner keyboard3 = new Scanner(System.in);
		System.out.println("Appuyez sur entrer pour finir ");
		path = keyboard3.nextLine();
		keyboard3.close();
		
		//on se déconnecte
		Cont.PerformDisconnect(4445, 4445);
	}
	
}
