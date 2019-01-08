package model;

import java.io.Serializable;
import java.io.File;
import data.User;

public class PacketFile implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	private File file;
	
	public PacketFile(File f) {
		this.file=f;		
	}
	
	public File getFile(){
		return this.file;
	}	
}
