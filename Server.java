import chat.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;

public class Server {
	public static void main(String args[]) {
		try{
			// initializing ORB
			ORB orb = ORB.init(args,null);

			// getting reference to POA
			org.omg.CORBA.Object obj =
				orb.resolve_initial_references("RootPOA");
			POA rootpoa = POAHelper.narrow(obj);
			// getting reference to POA manager
			POAManager manager = rootpoa.the_POAManager();
			// activating manager 
			manager.activate();

			// getting NameService
			obj = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef =
				org.omg.CosNaming.NamingContextExtHelper.narrow(obj);

			// creating servant
			ChatServerImpl cs = new ChatServerImpl();
			// connecting servant to ORB 
			ChatServer chatserver = cs._this(orb);
			// binding servant reference to NameService
			ncRef.rebind(ncRef.to_name("chatserver_yzioaw"), chatserver);

			System.out.println("Object activated");
			// starting orb
			orb.run();

		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
}



