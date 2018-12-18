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
	
	//Tente de se connecter avec un pseudo donné, renvoi true ou false selon si ça a reussi
	public boolean PerformConnect(String pseudo) {
		//on verifie si le pseudo est dans la liste des pseudo interdits
		if(pseudo.equals("ListRQ") || pseudo.equals("end") || pseudo.equals("disconnect")) {
			return false;
		}
		
		//on ouvre un nouveau client UDP
		UDPClient client = new UDPClient();
		ArrayList<User> list = client.sendBroadcastListRequest();
		boolean trouve=false;
		//si le Client n'a rien reçu il aura reçu la liste vide, donc il peut prendre n'import quel pseudo
		if (!list.isEmpty()) {
			
			//on cherche si le pseudo est déja dans la liste
			ListIterator<User> i= list.listIterator();
			while(i.hasNext() && !trouve) {
				User local=i.next();
				if(local.getUsername().equals(pseudo)) {
					trouve=true;
				}
			}
		}
		
		if (!trouve) {
			client.sendBroadcastPseudo(pseudo);
			client.close();
			
			LocalUser lU = new LocalUser(pseudo);
			list.add(lU.getUser());		//on l'ajoute dans sa propre liste
			Controller.Data = new ModelData(lU,list);  //On instancie la ModelData
			Data.getLocalUser().setConnected(true);
			new UDPServer().start();   //on lance le server UDP
			return true;
		} else {
			return false;
		} 
		
	}
	
	//tente de se deconnecter, change LocalUser connected si ça reussit
	public boolean PerformDisconnect() {
		//on ouvre un nouveau client UDP
		UDPClient client = new UDPClient();
		if (client.sendDisconnect(Data.usersConnected())) {
			Data.getLocalUser().setConnected(false);
			client.close();
			return true;
		} 
		client.close();
		return false;
	}
}
