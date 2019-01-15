package network;

import model.*;
import data.*;
import java.net.*;
import java.util.Date;
import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class TCPServer extends Thread implements PropertyChangeListener{
		
	private ServerSocket socket;
	private ModelData Data;
	private ArrayList<InetAddress> activesessionList;
	private boolean running;
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public TCPServer(ModelData Data,int port) throws Exception{
		this.Data=Data;
		this.activesessionList = new ArrayList<InetAddress>();
		this.socket = new ServerSocket(port,1,this.Data.getLocalUser().getUser().getAddr());
		this.running = true;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		////System.out.println("Server : un Listener a �t� ajout�");
		pcs.addPropertyChangeListener(listener);
	 }
	
	public void propertyChange(PropertyChangeEvent evt) {
		//si on a chang� la liste d'utilisateur/session on met � jour la notre
		if(evt.getPropertyName().equals("userList")) {
			this.Data.setUserConnected((ArrayList<User>) evt.getNewValue());
			//System.out.println("TCP Server: la liste d'utilisateurs a chang�");
		} else if(evt.getPropertyName().equals("sessionList")) {
			this.Data.setSessionList((ArrayList<Session>) evt.getNewValue());
			//System.out.println("TCP Server: la liste de session a chang�");
		} else if(evt.getPropertyName().equals("activesessionList")) {
			this.activesessionList = ((ArrayList<InetAddress>) evt.getNewValue());
			//System.out.println("TCP Server: la liste de active session a chang�");
		} 
	}
	
	public void stopServer() {
		this.running=false;
		try {
			this.socket.close();
		}catch (Exception e) {
			System.out.println("Socket non ferm�");
		}
	}
	
	//permet d'extraire un UserListPacket d'une s�rie de byte
	private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
		return (Object) is.readObject();
	}
	
	public void run() {
		while(this.running) {
			try {
		        this.socket.setSoTimeout(1000);
		        Socket client = this.socket.accept();
		        InetAddress clientAddr = client.getInetAddress();
		        String pseudo = this.Data.getPseudo(clientAddr); //on verifie si il est dans notre liste
		        
		        if(!this.activesessionList.contains(clientAddr)) {
		        	ArrayList<InetAddress> oldlist = new ArrayList<InetAddress>(this.activesessionList);
		        	this.activesessionList.add(clientAddr);
		        	pcs.firePropertyChange("activesessionList", oldlist, this.activesessionList);
		        }
				
		        int bytesRead;
		        int current = 0;
		        // receive data
		        byte [] byte_data = new byte [6022386];
		        InputStream is = client.getInputStream();
		          
		        bytesRead = is.read(byte_data,0,byte_data.length);
		        current = bytesRead;

		        do {
		           bytesRead = is.read(byte_data, current, (byte_data.length-current));
		           if(bytesRead >= 0) current += bytesRead;
		        } while(bytesRead > -1);

		        Object data = deserialize(byte_data);
		        Class c = data.getClass();
		        
		        //si c'est un message
		        if (c.getCanonicalName().equals(PacketMessage.class.getCanonicalName())) {
		        	PacketMessage packet_msg = (PacketMessage) data;
		        	String msg = packet_msg.getMessage();
		        	System.out.println("\r\nServerTCP : Message from " + pseudo + ": " + msg);
			        MessageChat message = new MessageChat(pseudo, new Date(),msg);
			        this.Data.addMessage(message,pseudo);
			        pcs.firePropertyChange("sessionList", new ArrayList<Session>(), this.Data.getSessionlist());
		        } else if(c.getCanonicalName().equals(PacketFile.class.getCanonicalName())) {
		        	PacketFile packet_file = (PacketFile) data;
		        	String name = packet_file.getName();
		        	byte[] byte_file = packet_file.getBytes();
		        	
		        	//System.out.println("\r\nServer : File from " + pseudo + ": "+name);
		        	String filePath = new File("").getAbsolutePath();
		        	
			        FileOutputStream fos = new FileOutputStream(filePath+"\\file_reception\\"+name);
			        BufferedOutputStream bos = new BufferedOutputStream(fos);
			        bos.write(byte_file, 0 , byte_file.length);
			        bos.flush();
			        fos.close();
			        bos.close();
			        
			        MessageChat message = new MessageChat(pseudo, new Date(),"Envoi du fichier "+name+" (Clavardage/file_reception");
			        this.Data.addMessage(message,pseudo);
			        pcs.firePropertyChange("sessionList", new ArrayList<Session>(), this.Data.getSessionlist());
		        }
		        
	        }catch (Exception e) {
	        	//System.out.println(e.toString());
	        }
		}
	}
	
    public InetAddress getSocketAddress() {
        return this.socket.getInetAddress();
    }
    
    public int getPort() {
        return this.socket.getLocalPort();
    }

}
