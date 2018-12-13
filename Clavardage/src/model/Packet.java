package model;

import java.io.Serializable;
import java.util.Date;
import data.*;

public abstract class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected User source;
	protected User dest;
	protected Date timeStamp;
	
	
	public Packet(User source, User dest) {
		this.dest=dest;
		this.source=source;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public Date getTimeStamp() {
		return this.timeStamp;
	}
	
	public User getSource() {
		return this.source;
	}
	
	public User getDest() {
		return this.dest;
	}
	
	public void setSource(User source) {
		this.source=source;
	}
	
	public void setDest(User dest) {
		this.dest=dest;
	}
}
