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
	private static String driverName = "org.postgresql.Driver";
	private static String url = "jdbc:postgresql://localhost:5432/Clavardage";
	private static String user = "ClavardageUser";
	private static String password = "SECRET";
	
	public static void loadDriver() throws ClassNotFoundException {
		Class.forName(driverName);
	}
	
	public static Connection newConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(url,user,password);
		return conn;
	}

	//GETTER
	public static ArrayList<String> getAllUserIP() throws SQLException{
		Connection conn=null;
		ArrayList<String> result = new ArrayList<String>();;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT \"IP\" FROM public.\"Session\"; ");
			
			while (rs.next()) {
				result.add( rs.getString("IP"));
			}
			return result;
		}finally {
			if (conn!=null) conn.close();
		}
	}
	
	
	public static ArrayList<String> getAllUserName() throws SQLException{
		Connection conn=null;
		ArrayList<String> result = new ArrayList<String>();;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT nickname FROM public.\"Session\";");
			
			while (rs.next()) {
				result.add( rs.getString("nickname"));
			}
			return result;
		}finally {
			if (conn!=null) conn.close();
		}
	}
	
	
	public static ArrayList<MessageChat> getHistoric(InetAddress addrIP) throws SQLException{
		Connection conn=null;
		ArrayList<MessageChat> result = new ArrayList<MessageChat>();;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT author,date,content FROM public.\"MessageChat\" WHERE \"IP\"='" + addrIP.toString()+"';");

			while (rs.next()) {
				result.add(new MessageChat(  rs.getString("author") ,new Date(rs.getTimestamp("date").getTime()) , rs.getString("content") ));
			}
			return result;
		}finally {
			if (conn!=null) conn.close();
		}
	}
	
	public static String getNickname(String addrIP) throws SQLException{
		Connection conn=null;
		try {
			conn= newConnection();
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT nickname FROM public.\"Session\" WHERE \"IP\" ='" + addrIP + "';");			
			rs.next();
			
			String result =  rs.getString("nickname");
			
			rs.close();
			st.close();
			return result;
		}finally {
			if (conn!= null) conn.close();
		}
	}
	
	//ADDER
	

	public static void addSession (User user) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT Count(*) FROM public.\"Session\" WHERE \"IP\"='" + user.getAddr().toString() + "';");
			rs.next();
			
			if(rs.getInt("count") == 0) {
				st.executeUpdate(
						"INSERT INTO public.\"Session\"(\"IP\",nickname) " +
						"VALUES ('" + user.getAddr().toString() + "', '" + user.getUsername() + "');"
						);
			}
			else {
				updateNickname(user.getAddr(),user.getUsername());
			}
			rs.close();
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	
	
	public static void addSession (InetAddress addrIP,String name) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT Count(*) FROM public.\"Session\" WHERE \"IP\"='" + addrIP.toString() + "';");
			rs.next();
			
			if(rs.getInt("count") == 0) {
				st.executeUpdate(
						"INSERT INTO public.\"Session\"(\"IP\",nickname) " +
						"VALUES ('" + addrIP.toString() + "', '" + name + "')"
						);
			}
			else {
				updateNickname(addrIP,name);
			}
			rs.close();
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	

	public static void addMessage(InetAddress addrIP,Date date,String content,String author) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Count(*) FROM public.\"MessageChat\";");
			rs.next();
			int id=rs.getInt("count")+1;
			st.executeUpdate(
					"INSERT INTO public.\"MessageChat\"(id,date,content,author,\"IP\") " +
					"VALUES (" + id + ",'" + new Timestamp(date.getTime()) + "','"+ content + "','"+ author +"','" + addrIP.toString() + "');"
					);
			rs.close();
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	
	public static void addMessage(MessageChat msg,InetAddress addrIP) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT Count(*) FROM public.\"MessageChat\"");
			rs.next();
			int id=rs.getInt("count") +1;
			st.executeUpdate(
					"INSERT INTO public.\"MessageChat\"(id,date,content,author,\"IP\") " +
					"VALUES (" + id + ",'" + new Timestamp(msg.getDate().getTime()) + "','"+ msg.getContent() + "','" + msg.getAuthor() + "','"+ addrIP.toString() + "');"
					);
			rs.close();
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}		
	}
	
	//SETTER
	
	public static void updateNickname(InetAddress addrIP, String newName) throws SQLException{
		Connection conn=null;
		try {
			conn = newConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(
					"UPDATE public.\"Session\"" +
					"SET nickname = '" + newName + "'" +
					"WHERE \"IP\" ='" + addrIP.toString() +"';"
					);
			st.close();
		}finally {
			if (conn!=null) conn.close();
		}
	}
}
