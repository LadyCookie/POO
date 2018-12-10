
public class User {

	private String Username;
	private int id;
	
	public User(String Username, int id) {
		this.Username=Username;
		this.id=id;
	}
	
	public String getUsername(){
		return this.Username;
	}
	
	public int getId() {
		return this.id;
	}
	
}
