package network;

import java.net.InetAddress;
import java.util.EventListener;

public interface NewMessageListener extends EventListener {
	void aMessageHasBeenReceived(InetAddress address);
}
