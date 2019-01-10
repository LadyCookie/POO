package network;

import java.net.*;
import java.util.ArrayList;

import model.PacketFile;
import model.PacketMessage;
import model.PacketUserList;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class TCPClient {
	    private Socket socket;
	    
	    public TCPClient(InetAddress serverAddr, int serverPort) throws Exception {
	        this.socket = new Socket(serverAddr, serverPort);
	    }
	    
	  //permet de convertir un objet java en byte[] pour l'envoi
		public static byte[] serialize(Object pack) throws IOException {
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ObjectOutputStream os = new ObjectOutputStream(out);
		    os.writeObject(pack);
		    byte [] data = out.toByteArray();
		    return data;
		}
	    
	    public void sendTxt(String message) throws IOException {
	    	PacketMessage packet = new PacketMessage(message);
	    	byte[] serialized_msg = serialize(packet);
	    	
	    	PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
	    	
	    	OutputStream os = this.socket.getOutputStream();
	        os.write(serialized_msg,0,serialized_msg.length);
	        os.flush();
	        os.close();	        
	        this.socket.close();
	    }
	    
	    public void sendFile(String path) throws IOException {
	    	//on recup l'extension du fichier
	    	String extension = "";
	    	int i = path.lastIndexOf('.');
	    	if (i > 0) {
	    	    extension = path.substring(i+1);
	    	}
	    	
	        File myFile = new File (path);
	        byte [] byte_file  = new byte [(int)myFile.length()];
	        FileInputStream fis = new FileInputStream(myFile);
	        BufferedInputStream bis = new BufferedInputStream(fis);
	        bis.read(byte_file,0,byte_file.length);
	        
	        PacketFile packet = new PacketFile(byte_file,extension);
	        byte[] serialized_file = serialize(packet);
	        
	        OutputStream os = this.socket.getOutputStream();
	        os.write(serialized_file,0,serialized_file.length);
	        os.flush();
	        bis.close();
	        os.close();	        
	        this.socket.close();
	    }
}