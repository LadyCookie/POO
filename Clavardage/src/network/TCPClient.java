package network;

import java.net.*;

import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class TCPClient {
	    private Socket socket;
	    
	    public TCPClient(InetAddress serverAddr, int serverPort) throws Exception {
	        this.socket = new Socket(serverAddr, serverPort);
	    }
	    
	    public void sendTxt(String message) throws IOException {
	    	PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
	        out.println(message);
	        out.flush();
	        this.socket.close();
	    }
	    
	    public void sendFile(String path) throws IOException {
	    	File file = new File(path);
	        FileInputStream fis = new FileInputStream(file);
	        BufferedInputStream bis = new BufferedInputStream(fis);
	    	
	        //Get socket's output stream
	        OutputStream os = this.socket.getOutputStream();
	                
	        //Read File Contents into contents array 
	        byte[] contents;
	        long fileLength = file.length(); 
	        long current = 0;
	         
	        long start = System.nanoTime();
	        
	        while(current!=fileLength){ 
	            int size = 10000;
	            if(fileLength - current >= size)
	                current += size;    
	            else{ 
	                size = (int)(fileLength - current); 
	                current = fileLength;
	            } 
	            contents = new byte[size]; 
	            bis.read(contents, 0, size); 
	            os.write(contents);
	            System.out.print("TCPClient: Sending file ... "+(	current*100)/fileLength+"% complete!");
	        }   
	        
	        os.flush(); 
	        this.socket.close();
	    }

}
