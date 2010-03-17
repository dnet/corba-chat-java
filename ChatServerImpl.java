import org.omg.CORBA.*;
import java.util.*;
import chat.*;

public class ChatServerImpl extends chat.ChatServerPOA {

	protected class Client {
		public chat.ChatClient chatclient;
		public String nick;
		public Client(String nick, chat.ChatClient chatclient) {
			this.chatclient = chatclient;
			this.nick = nick;
		}
	}

	protected Map<String, Client> clients = new HashMap<String, Client>();
	protected List<String> nicks = new Vector<String>();

	public String subscribe(String nick, chat.ChatClient c)
		throws chat.NameAlreadyUsed {
		if (nicks.contains(nick)) throw new chat.NameAlreadyUsed();
		nicks.add(nick);
		String id = UUID.randomUUID().toString();
		System.out.println("subscribe: " + nick + " -> " + id);
		clients.put(id, new Client(nick, c));
		return id;
	}

	public void unsubscribe(String id) throws chat.UnknownID {
		System.out.println("unsubscribe: " + id);
		Client c = clients.remove(id);
		if (c == null) throw new chat.UnknownID();
		nicks.remove(c.nick);
	}

	public void comment(String id, String text) throws chat.UnknownID {
		Client from = clients.get(id);
		if (from == null) throw new chat.UnknownID();
		System.out.println(
			"comment: " + text + " by " + id+ " [" + from.nick + "]");
		for (Client to : clients.values()) {
			to.chatclient.update(from.nick, text);
		}
	}
}

