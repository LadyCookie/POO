package model;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.*;

public class database {
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost/dbessai";
	private String user = "bduser";
	private String password = "SECRET";
	
	public void loadDriver() throws ClassNotFoundException {
		Class.forName(driverName);
	}
	
	public Connection newConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(url,user,password);
		return conn;
	}

	public ArrayList<InetAddress> getIP() throws SQLException{
		Connection conn=null;
		try {
			ArrayList<InetAddress> result = new ArrayList<InetAddress>();;
			
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT IP FROM User");
			
			while (rs.next()) {
				result.add( (InetAddress) rs.getObject("IP"));
			}
			return result;
		}finally {
			if (conn!=null) conn.close();
		}
	}
	
	public void addUser (User user) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(
					"INSERT INTO User(IP,nickname) " +
					"VALUES ('" + user.getAddr() + "', " + user.getUsername() + ")"
					);
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	
	
}
