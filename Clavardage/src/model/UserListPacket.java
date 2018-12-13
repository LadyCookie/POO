package model;
import java.util.*;
import data.*;

public class UserListPacket extends Packet{

	private static final long serialVersionUID = 1L;
	
	private ArrayList<User> usrl;
	
	public UserListPacket (User source, User dest, ArrayList<User> userlist) {
		super(source,dest);
		this.usrl=userlist;
	}
	
	public ArrayList<User> getUserList() {
		return this.usrl;
	}
	
	public void setUserList(ArrayList<User> usrl) {
		this.usrl=usrl;
	}
	
}
