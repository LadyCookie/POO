package model;

import java.util.ArrayList;
import java.util.ListIterator;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import data.*;
import network.*;

public class Controller implements PropertyChangeListener{

	public volatile ModelData Data;
	
	private UDPServer server;
	
	//pour tracker les changements faits à ModelData
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public Controller(){
		//appel de la fenetre de login, pour tenter de donner le pseudo, d'instancier Data
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		//System.out.println("Controller : un Listener a été ajouté");
		pcs.addPropertyChangeListener(listener);
	 }

	//on ecoute si des changements sont fait à ModelData
	public void propertyChange(PropertyChangeEvent evt) {
		//si on a changé la liste d'utilisateur on met à jour la notre
		if(evt.getPropertyName().equals("userList")) {
			//System.out.println("Controller: la liste d'utilisateur a changé");
			ArrayList<User> oldlist = new ArrayList<User>(this.Data.usersConnected());
			this.Data.setUserConnected((ArrayList<User>) evt.getNewValue());
			if (oldlist.equals((ArrayList<User>) evt.getNewValue())) {
				//System.out.println("Controller : user change fired");
			}
			pcs.firePropertyChange("userList",oldlist , (ArrayList<User>) evt.getNewValue());
		} else if(evt.getPropertyName().equals("sessionList")) {
			ArrayList<Session> oldlist = new ArrayList<Session>(this.Data.getSessionlist());
			//System.out.println("Controller: la liste de session a changé");
			this.Data.setSessionList((ArrayList<Session>) evt.getNewValue());
			if (oldlist.equals((ArrayList<User>) evt.getNewValue())) {
				System.out.println("Controller : session change fired");
			}
			pcs.firePropertyChange("sessionList",oldlist , (ArrayList<Session>) evt.getNewValue());
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
			
			this.server= new UDPServer(this.Data,portsrc); //on crée un server UDP pour ce client
			addPropertyChangeListener(this.server); //on l'ajoute à la liste des listeners
			this.server.addPropertyChangeListener(this); //on s'ajoute aux listerners du server
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
