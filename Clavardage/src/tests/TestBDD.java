package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.*;
import network.*;
import model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestBDD {
	database BDD;
	
	@Test
	public void test() {
		Connection conn;
	
	try {
		System.out.println("begin");
		conn = BDD.newConnection();
		System.out.println("conn créé");
		BDD.loadDriver();
		System.out.println("driver chargé");
		Statement st = conn.createStatement();
		
		System.out.println("Connexion à la base établie");
		//on crée une table
		st.executeUpdate("CREATE TABLE User (idPers INTEGER NOT NULL, nom VARCHAR(255) NOT NULL);");
		System.out.println("Table User créée");
		
		
		
		ResultSet rs=st.executeQuery("SELECT * FROM User");
		System.out.println("requête executée");
		while (rs.next()) {
			System.out.printf("User : %s\n", rs.getString("nom"));
		}
		
		rs.close();
		System.out.println("fermeture du resultset");
		st.close();
		System.out.println("fermeture su statement");
		
		if (conn!=null) conn.close();
		System.out.println("fermeture de la connexion");
	}catch(Exception e){
		System.out.println(e);	
	}
	}
}
