package model;

import java.util.ArrayList;
import java.util.ListIterator;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import data.*;
import network.*;

public class Controller implements PropertyChangeListener{

	private ModelData Data;
	
	public Controller(){
		//appel de la fenetre de login, pour tenter de donner le pseudo, d'instancier Data
	}

	//on ecoute si des changements sont fait à ModelData
	public void propertyChange(PropertyChangeEvent evt) {
		//si on a changé la liste d'utilisateur on met à jour la notre
		if(evt.getPropertyName().equals("userList")) {
			this.Data.setUserConnected((ArrayList<User>) evt.getNewValue());
		}
	}
	
	public ModelData getModelData() {
		return this.Data;
	}
	
	//Tente de se connecter avec un pseudo donné, renvoi true ou false selon si ça a reussi
	public boolean PerformConnect(String pseudo,int portsrc, int portdist) {
		//on verifie si le pseudo est dans la liste des pseudo interdits
		if(pseudo.equals("ListRQ") || pseudo.equals("end") || pseudo.equals("disconnect")) {
			return false;
		}
		
		//on ouvre un nouveau client UDP
		UDPClient client = new UDPClient();
		ArrayList<User> list = client.sendBroadcastListRequest(portsrc,portdist);
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
			client.sendBroadcastPseudo(pseudo,portdist);
			client.close();
			
			LocalUser lU = new LocalUser(pseudo);
			list.add(lU.getUser());		//on l'ajoute dans sa propre liste
			this.Data = new ModelData(lU,list);  //On instancie la ModelData
			
			this.Data.getLocalUser().setConnected(true);
			this.Data.addPropertyChangeListener(this); //on s'ajoute aux changements
			
			UDPServer server= new UDPServer(this.Data,portsrc); //on crée un server UDP pour ce client
			this.Data.addPropertyChangeListener(server); //on l'ajoute à la liste des listeners
			server.start();   //on lance le server UDP
			return true;
		} else {
			return false;
		} 
		
	}
	
	//tente de se deconnecter, change LocalUser connected si ça reussit
	public boolean PerformDisconnect(int portsrc,int portdist) {
		//on ouvre un nouveau client UDP
		UDPClient client = new UDPClient();
		if (client.sendDisconnect(this.Data.usersConnected(),portsrc, portdist)) {
			this.Data.getLocalUser().setConnected(false);
			client.close();
			this.Data.setUserConnected(new ArrayList<User>()) ; //vide notre liste
			return true;
		} 
		client.close();
		return false;
	}
}
