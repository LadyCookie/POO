package data;

import network.*;
import java.util.*;
import java.net.*;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class main {
	UDPClient client;
	UDPServer server;
	
	@Before
	public void setup() {
		new UDPServer().start();
		client = new UDPClient();
	}
	
	@Test
	public void test() {
		client.sendBroadcast("Bob");		
	}
	
	@After
	public void tearDown() {
		client.sendBroadcast("end");
		client.close();
	}
}
