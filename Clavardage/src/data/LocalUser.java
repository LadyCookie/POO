package data;

public class LocalUser {
	private User Client;
	private boolean connected;
	
	
	public LocalUser(User Client) {
		this.Client=Client;
		this.connected=false;
	}
	
	void setConnected(){
		this.connected=true;
	}
}
