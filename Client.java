import chat.*;
import java.io.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;

public class Client implements Runnable {
	public void run() {
		try {
			// getting reference to POA
			org.omg.CORBA.Object obj =
			orb.resolve_initial_references("RootPOA");
			POA rootpoa = POAHelper.narrow(obj);
			// getting reference to POA manager
			POAManager manager = rootpoa.the_POAManager();
			// activating manager 
			manager.activate();
			orb.run();
		} catch (Exception e) {}
	}

	protected static ORB orb;

	public static void main(String args[]) {
		try {
			// initializing ORB
			orb = ORB.init(args,null);
			// getting NameService
			org.omg.CORBA.Object obj = 
			orb.resolve_initial_references("NameService");
			NamingContextExt ncRef =
			org.omg.CosNaming.NamingContextExtHelper.narrow(obj);

			// resolving servant name 
			obj = ncRef.resolve_str("chatserver_yzioaw");
			ChatServer chatserver = ChatServerHelper.narrow(obj);

			// creating servant
			ChatClientImpl cc = new ChatClientImpl();
			// connecting servant to ORB 
			ChatClient chatclient = cc._this(orb);
			Thread t = new Thread(new Client());
			String id = chatserver.subscribe("test", chatclient);
			try {
				System.out.println("Connected with ID " + id);
				System.out.println("Type /quit to exit");
				t.start();
				BufferedReader br =
					new BufferedReader(new InputStreamReader(System.in));
				while (true) {
					String s = br.readLine();
					if (s.equals("/quit")) break;
					chatserver.comment(id, s);
				}
			} finally {
				System.out.print("Unsubscribing...");
				chatserver.unsubscribe(id);
				System.out.println(" done");
				orb.destroy();
				t.join();
			}
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
}



