package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.io.File;
import data.User;

public class PacketFile implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	private byte[] bytes;
	private String extension;
	
	public PacketFile(byte[] b, String ext) {
		this.bytes=b;
		this.extension=ext;
	}
	
	public byte[] getList(){
		return this.bytes;
	}	
	
	public String getExtension(){
		return this.extension;
	}
}
