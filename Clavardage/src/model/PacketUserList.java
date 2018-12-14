package model;

import model.*;
import data.*;
import java.io.Serializable;
import java.util.*;

public class PacketUserList implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<User> UserList;
	
	public PacketUserList(ArrayList<User> l) {
		this.UserList=l;		
	}
	
	public ArrayList<User> getUserList(){
		return this.UserList;
	}	

}
