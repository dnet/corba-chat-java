import org.omg.CORBA.*;
import chat.*;

public class ChatClientImpl extends chat.ChatClientPOA {

	public void update(String nick, String text) {
		System.out.println("<" + nick + "> " + text);
	}
}

