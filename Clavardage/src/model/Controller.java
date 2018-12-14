package model;

import java.util.ArrayList;
import java.util.ListIterator;

import data.*;
import network.*;

public class Controller {

	public static ModelData Data;
	
	public Controller(){
		//appel de la fenetre de login, pour tenter de donner le pseudo, d'instancier Data
	}
	
	//renvoi � reviser
	public boolean PerformConnect(String pseudo) {
		UDPClient client = new UDPClient();
		ArrayList<User> list = client.sendBroadcastListRequest();
		boolean trouve=false;
		//on assume que la liste ne peut pas etre vide (il y a au moins l'envoyeur dedans)
		//on cherche si le pseudo est d�ja dans la liste
		ListIterator<User> i= list.listIterator();
		while(i.hasNext() && !trouve) {
			User local=i.next();
			if(local.getUsername().equals(pseudo)) {
				trouve=true;
			}
		}

		if (!trouve) {
			client.sendBroadcastPseudo(pseudo);
			client.close();
			LocalUser lU = new LocalUser(pseudo);
			list.add(lU.getUser());		//on l'ajoute dans sa propre liste
			Controller.Data = new ModelData(lU,list);  //On instancie la ModelData
			new UDPServer().start();   //on lance le server UDP
			return true;
		} else {
			return false;
		} 
	}
}
