package data;

public class LocalUser {
	private User Client;
	private boolean connected;
	
	
	public LocalUser(User Client) {
		this.Client=Client;
		this.connected=false;
	}
	
	public User getUser() {
		return this.Client;
	}
	
	void setConnected(){
		this.connected=true;
	}
}
