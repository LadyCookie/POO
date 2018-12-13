package network;

import java.net.*;
import java.io.*;
import model.*;
import data.*;
import java.util.*;

public class UDPClient {

	private DatagramSocket socket;
	private byte[] buf = new byte[1024];
	private InetAddress address;
	public static InetAddress broadcastAddr;
	
	
	public static ModelData CurrentData;
	
	public UDPClient () {
		try (final DatagramSocket socket = new DatagramSocket()){
			socket.connect(InetAddress.getByName("8.8.8.8"),10002);
			this.address = socket.getLocalAddress();
		}
		catch (UnknownHostException e) {
			System.out.println("No internet");
		}
		catch(SocketException e) {
			return;
		}
	}
	
	
	public static UserListPacket deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
		return (UserListPacket) is.readObject();
	}
	    

	
	public String sendTXT(String msg) {
		buf = msg.getBytes();
		try {
			//on récupère l'adresse de broadcast
			NetworkInterface ni=NetworkInterface.getByInetAddress(this.address);
			for (int i =0; i<ni.getInterfaceAddresses().size();i++) {
				if(ni.getInterfaceAddresses().get(i).getBroadcast() != null) {
					broadcastAddr=ni.getInterfaceAddresses().get(i).getBroadcast();
				}
			}
			
			DatagramPacket packet = new DatagramPacket(buf,buf.length,broadcastAddr,4445);
			this.socket.send(packet);
			
			packet = new DatagramPacket(buf,buf.length);
			this.socket.receive(packet);
			
			byte[] data=packet.getData();
			UserListPacket p=deserialize(data);
		    
			InetAddress srcAddr=p.getSource().getAddr();
			ArrayList<User> usrl=p.getUserList();
			CurrentData.usersConnected().addAll(usrl);
			
			while (!(usrl.isEmpty())) {
				
				this.socket.receive(packet);
				
				data=packet.getData();
				p=deserialize(data);
				
				if(p.getSource().getAddr().equals(srcAddr)) {
					usrl=p.getUserList();
					CurrentData.usersConnected().addAll(usrl);
				}			
			}
			return "";
			
		}catch (IOException e) {
			return "";
		}catch(ClassNotFoundException e) {
			return "";
		}
	}
	
	public void close(){
		socket.close();
	}
}