package model;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Date;

import data.*;

public class database {
	private String driverName = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost/dbClavardage";
	private String user = "bduser";
	private String password = "SECRET";
	
	public void loadDriver() throws ClassNotFoundException {
		Class.forName(driverName);
	}
	
	public Connection newConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(url,user,password);
		return conn;
	}

	
	
	//GETTER
	public ArrayList<InetAddress> getAllUserIP() throws SQLException{
		Connection conn=null;
		ArrayList<InetAddress> result = new ArrayList<InetAddress>();;
		try {
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
	
	
	public ArrayList<String> getAllUserName() throws SQLException{
		Connection conn=null;
		ArrayList<String> result = new ArrayList<String>();;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT nickname FROM User");
			
			while (rs.next()) {
				result.add( rs.getString("nickname"));
			}
			return result;
		}finally {
			if (conn!=null) conn.close();
		}
	}
	
	
	public ArrayList<MessageChat> getHistoric(InetAddress addrIP) throws SQLException{
		Connection conn=null;
		ArrayList<MessageChat> result = new ArrayList<MessageChat>();;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT author,date,content FROM MessageChat WHERE IP=" + addrIP);
			
			while (rs.next()) {
				result.add( new MessageChat(  rs.getString(1) , (Date) rs.getObject(2) , rs.getString(3) ));
			}
			return result;
		}finally {
			if (conn!=null) conn.close();
		}
	}
	
	//ADDER
	
	//penser à regarder les types possible de la BDD, notamment pour le type de IP
	public void addUser (User user) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(
					"INSERT INTO User(IP,nickname) " +
					"VALUES (" + user.getAddr() + ", '" + user.getUsername() + "')"
					);
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	
	
	public void addUser (InetAddress addrIP,String name) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(
					"INSERT INTO User(IP,nickname) " +
					"VALUES (" + addrIP + ", '" + name + "')"
					);
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	
	
	public void addMessage(InetAddress addrIP,java.sql.Timestamp date,String content,String author) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT max(id) as MAX FROM MessageChat");
			int id=rs.getInt("MAX") +1;
			st.executeUpdate(
					"INSERT INTO MessageChat(id,IP,date,content,author) " +
					"VALUES (" + id + "," + addrIP + "," + date + ",'"+ content + ","+ author +"')"
					);
			rs.close();
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	
	public void addMessage(MessageChat msg,InetAddress addrIP) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT max(id) as MAX FROM MessageChat");
			int id=rs.getInt("MAX") +1;
			st.executeUpdate(
					"INSERT INTO MessageChat(id,IP,date,content,author) " +
					"VALUES (" + id + "," + addrIP + "," + (Timestamp) msg.getDate() + ",'"+ msg.getContent() + "," + msg.getAuthor() + "')"
					);
			rs.close();
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	
	//SETTER
	
	public void updateNickname(InetAddress addrIP, String newName) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(
					"UPDATE User" +
					"SET nickname = '" + newName + "'" +
					"WHERE IP =" + addrIP
					);
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}
	}
}
