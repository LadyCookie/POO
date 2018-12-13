package data;

import network.*;
import java.util.*;
import java.net.*;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class main {
	UDPClient client;
	
	@Before
	public void setup() {
		new UDPServer().start();
		client = new UDPClient();
	}
	
	@Test
	public void test() {
		String echo= client.sendTXT("helloserver");
		assertEquals("helloserver", echo);
		echo= client.sendTXT("helloworking");
		assertFalse(echo.equals("helloserver"));
		
	}
	
	@After
	public void tearDown() {
		client.sendTXT("end");
		client.close();
	}
}
