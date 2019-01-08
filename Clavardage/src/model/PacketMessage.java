package model;

import java.io.Serializable;

import data.User;

public class PacketMessage implements Serializable{

private static final long serialVersionUID = 1L;
	
	private String msg;
	
	public PacketMessage(String msg) {
		this.msg=msg;		
	}
	
	public String getMessage(){
		return this.msg;
	}	
}
