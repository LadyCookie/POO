import java.util.*;

public class UserList {

	private ArrayList<User> l;
	
	public UserList() {
		this.l=new ArrayList<User>();
	}
	
	public void addUser(User U) {
		l.add(U);
	}
	
	public void removeUser(User U) {
		l.remove(U);
	}
	
}
