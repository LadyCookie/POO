package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.InetAddress;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import data.*;
import network.*;

public class Controller implements PropertyChangeListener{

	private ModelData Data;
	private TCPServer TCPserver;
	private ArrayList<InetAddress> activesessionList;
	
	
	//pour tracker les changements faits � ModelData
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public Controller(){
		this.activesessionList = new ArrayList<InetAddress>();
		//appel de la fenetre de login, pour tenter de donner le pseudo, d'instancier Data
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public ModelData getModelData() {
		return this.Data;
	}
	
	//on ecoute si des changements sont fait � ModelData
	public void propertyChange(PropertyChangeEvent evt) {
		//si on a chang� la liste d'utilisateur on met � jour la notre
		if(evt.getPropertyName().equals("userList")) {
			//System.out.println("Controller: la liste d'utilisateur a chang�");
			ArrayList<User> oldlist = new ArrayList<User>();
			this.Data.setUserConnected((ArrayList<User>) evt.getNewValue());
			pcs.firePropertyChange("userList",oldlist , (ArrayList<User>) evt.getNewValue());
		} else if(evt.getPropertyName().equals("sessionList")) {
			//System.out.println("Controller : la liste de session a chang�");
			this.Data.setSessionList((ArrayList<Session>) evt.getNewValue());
			pcs.firePropertyChange("sessionList",new ArrayList<Session>() , (ArrayList<Session>) evt.getNewValue());
		} else if(evt.getPropertyName().equals("activesessionList")) {
			//System.out.println("Controller : j'ai re�u un evenement de changement de session active");
			ArrayList<InetAddress> oldlist = new ArrayList<InetAddress>(this.activesessionList);
			this.activesessionList = ((ArrayList<InetAddress>) evt.getNewValue());
			pcs.firePropertyChange("activesessionList",oldlist , (ArrayList<InetAddress>) evt.getNewValue());
		} else if(evt.getPropertyName().equals("NewMessageFrom")) {
			//System.out.println("Controller : j'ai re�u un evenement de changement de session active");
			pcs.firePropertyChange("NewMessageFrom",new String() , (String) evt.getNewValue());
		} 
	}
	
	//Tente de se connecter avec un pseudo donn�, renvoi true ou false selon si �a a reussi
	public boolean PerformConnect(String pseudo,int portsrc, int portdist, int portTCPsrc) {
		//on verifie si le pseudo est dans la liste des pseudo interdits
		if(pseudo.equals("ListRQ") || pseudo.equals("end") || pseudo.equals("disconnect")) {
			return false;
		}
		
		//on ouvre un nouveau client UDP
		UDPClient client = new UDPClient();
		ArrayList<User> list = client.sendBroadcastListRequest(portsrc,portdist);
		boolean trouve=false;
		//si le Client n'a rien re�u il aura re�u la liste vide, donc il peut prendre n'importe quel pseudo
		if (!list.isEmpty() || !list.equals(null)) {
			//on cherche si le pseudo est d�ja dans la liste
			ListIterator<User> i= list.listIterator();
			while(i.hasNext() && !trouve) {
				User local=i.next();
				if(local.getUsername().equals(pseudo)) {
					trouve=true;
				}
			}
		}
		
		if (!trouve) {
						
			LocalUser lU = new LocalUser(pseudo);
			list.add(lU.getUser());		//on l'ajoute dans sa propre liste
			this.Data = new ModelData(lU,list);  //On instancie la ModelData
			
			client.sendPseudo(this.Data.usersConnected(),pseudo,portdist);
			client.close();
			
			try {
				UDPServer server= new UDPServer(this.Data,portsrc); //on cr�e un server UDP pour ce client
				addPropertyChangeListener(server); //on l'ajoute � la liste des listeners
				server.addPropertyChangeListener(this); //on s'ajoute aux listeners du server
				server.start();   //on lance le server UDP
				
				this.TCPserver = new TCPServer(this.Data,portTCPsrc); //port arbitraire
				addPropertyChangeListener(this.TCPserver); //on l'ajoute � la liste des listeners
				this.TCPserver.addPropertyChangeListener(this); //on s'ajoute aux listeners du server
				this.TCPserver.start();   //on lance le server TCP
				
			}catch(Exception e) {
				//System.out.println("Controller: "+e.toString());
			}
			return true;
		}
		client.close();
		return false;
	}
	
	//tente de se deconnecter
	public boolean PerformDisconnect(int portsrc,int portdist) {
		//on ouvre un nouveau client UDP
		UDPClient client = new UDPClient();
		if (client.sendDisconnect(this.Data.usersConnected(),portsrc, portdist)) {
			//System.out.println("J'ai envoy� le message de deconnection");
			client.close();
			//this.TCPserver.interrupt();
			this.TCPserver.stopServer();
			this.Data.setUserConnected(new ArrayList<User>()) ; //On vide notre liste
			return true;
		}  else {
			client.close();
			return false;
		}
	}	
	
	public boolean ChangePseudo (String pseudo) {
		
		boolean trouve=false;
		ArrayList<User> list = this.Data.usersConnected();
		
		if (!list.isEmpty() || !list.equals(null)) {
			//on cherche si le pseudo est d�ja dans la liste
			ListIterator<User> i= list.listIterator();
			while(i.hasNext() && !trouve) {
				User local=i.next();
				if(local.getUsername().equals(pseudo)) {
					trouve=true;
				}
			}
		}
		
		if(!trouve) {
			this.Data.removeUser(this.Data.getLocalUser().getUser()); //on se retire de la liste
			this.Data.getLocalUser().getUser().setUsername(pseudo); //on change notre pseudo en local
			this.Data.addUser(this.Data.getLocalUser().getUser()); //on se rajoute � la liste
			
			pcs.firePropertyChange("userList",new ArrayList<User>() , this.Data.usersConnected());
			//on informe les autres du changement
			UDPClient client = new UDPClient();
			client.sendPseudo(this.Data.usersConnected(),pseudo, 4445);
			client.close();
			return true;
		} else {
			return false;
		}
	}

	public boolean sendMessage(String pseudo, String msg,int portdst) {
		try {
			InetAddress addr = this.Data.getAddresse(pseudo);
			
			if(!this.activesessionList.contains(addr)) {
				ArrayList<InetAddress> oldlist = new ArrayList<InetAddress>(this.activesessionList);
				this.activesessionList.add(addr); //on ajoute � la liste des sessions actives
				pcs.firePropertyChange("activesessionList",oldlist , this.activesessionList);
			}
			
			TCPClient client = new TCPClient(addr,portdst);
			ArrayList<InetAddress> oldlist = new ArrayList<InetAddress>(this.activesessionList);
			
			this.activesessionList.add(addr); //on ajoute � la liste des sessions actives
			pcs.firePropertyChange("activesessionList",oldlist , this.activesessionList);
			
			client.sendTxt(msg);
			if(!pseudo.equals(this.getModelData().getLocalUser().getUser().getUsername())) {			
		        MessageChat message = new MessageChat(this.Data.getLocalUser().getUser().getUsername(), new Date(),msg);
		        this.Data.addMessage(message,pseudo);
		        pcs.firePropertyChange("sessionList", new ArrayList<Session>(), Data.getSessionlist());
		        pcs.firePropertyChange("NewMessageFrom", new String(), pseudo);
			}
	        return true;
		}catch(Exception e){
			System.out.println("Erreur lors de l'envoi du message "+e.toString());
			//l'utilisateur n'est pas connect� ou la connection a �chou�
			//System.out.println("Controller: "+e.toString());
			return false;
		}
	}
	
	public boolean sendFile(String pseudo, String path,int portdst) {
		try {
			InetAddress addr = this.Data.getAddresse(pseudo);
			if(!this.activesessionList.contains(addr)) {
				ArrayList<InetAddress> oldlist = new ArrayList<InetAddress>(this.activesessionList);
				this.activesessionList.add(addr); //on ajoute � la liste des sessions actives
				if (oldlist.equals(this.activesessionList)) {
					//System.out.println("Controller : active user change fired");
				}
				pcs.firePropertyChange("activesessionList",oldlist , this.activesessionList);
			}
			TCPClient client = new TCPClient(addr,portdst);
			ArrayList<InetAddress> oldlist = new ArrayList<InetAddress>(this.activesessionList);
			
			this.activesessionList.add(addr); //on ajoute � la liste des sessions actives
			if (oldlist.equals(this.activesessionList)) {
				//System.out.println("Controller : active session change fired");
			}
			pcs.firePropertyChange("activesessionList",oldlist , this.activesessionList);
			
			client.sendFile(path);
			File myFile = new File (path);
		    String name = myFile.getName();
			MessageChat message = new MessageChat(this.Data.getLocalUser().getUser().getUsername(), new Date(),"Envoi du fichier "+name);
	        this.Data.addMessage(message,pseudo);
	        pcs.firePropertyChange("sessionList", new ArrayList<Session>(), Data.getSessionlist());
	        pcs.firePropertyChange("NewMessageFrom", new String(), pseudo);
			return true;
		}catch(Exception e){
			//System.out.println("Erreur lors de l'envoi du message "+e.toString());
			//l'utilisateur n'est pas connect� ou la connection a �chou�
			return false;
		}
	}
}
