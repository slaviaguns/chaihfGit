import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;


public class ClientCmdHandler extends CommandHandler{
	private Socket _socket;
	private ProcessManagerClient _client;
	//public Integer ClientListMutex = 0;
	
	public ClientCmdHandler(Socket s, ProcessManagerClient c) {
		this._socket = s;
		this._client = c;
	}
	
	public void HandleProcessMigration(ProcessMigrationCommand p) {
		p.PrintCommand();
		int migNum = p.GetMigNum();
		MigratableProcess mig = null;
		while (migNum != 0) { 
			synchronized(this._client.ClientListMutex) {
				Iterator iter = this._client.GetProcessList().entrySet().iterator(); 
				if(iter.hasNext()) {
				  Map.Entry entry = (Map.Entry) iter.next(); 				
				  mig = (MigratableProcess)entry.getValue();
				  iter.remove();					  
				}
			}
			if(mig != null) {
				System.out.println("&&&&&&&");
				mig.suspend();
				System.out.println("^^^^^^");
				System.out.println(p.GetDesHostName() + " " + p.GetDesPort());
				Thread t = new Thread(new CommandSender(new ProcessCommand(p.GetDesHostName(), p.GetDesPort(), mig)));			
				t.start();
				migNum--;
			} else {
				System.out.println("@@@@@@@@@@@@@@@@");
			}
		}
	}
		

	
	public void HandleProcessInstance(StartNewJobCommand p) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		p.PrintCommand();
		Class theClass = null;
		 try {
			theClass = Class.forName(p.GetClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Object[] obj = {p.GetClassArg()};
	      MigratableProcess process = (MigratableProcess) theClass.getConstructor(String[].class)
	              .newInstance(obj);
	      Thread thread = new Thread(process);
	      synchronized(this._client.ClientListMutex) {
	    	  this._client.GetProcessList().put(thread, process);
	      }	      
	      
	      thread.start();
	}
	
	public void HandlePrintProcess(PrintProcessCommand p) {
		p.PrintCommand();
	}
	
	
	public void HandleComingProcess(ProcessCommand p) {
		p.PrintCommand();
		MigratableProcess mp  =p.GetMigratableProcess();
		Thread t = new Thread(mp);
		System.out.println("start running the new coming process!");
		synchronized(this._client.ClientListMutex) {
			this._client.GetProcessList().put(t, mp);
		}
		t.start();

	}
	@Override
	public  void handle() {
		ObjectInputStream br = null;
		try {
			br = new ObjectInputStream(this._socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
       Command cmd=null;
       try {
		if((cmd=(Command)br.readObject())!=null){
			   switch(cmd.GetType()) {
				   case PROCESS_MIGRATION:
					   HandleProcessMigration((ProcessMigrationCommand)cmd);
					   break;
				   case NEW_PROCESS_INSTANCE:
						try {
							HandleProcessInstance((StartNewJobCommand)cmd);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					   break;
				   case PRINT_PROCESS:
					   HandlePrintProcess((PrintProcessCommand)cmd);
					   break;
				   case PROCESS_COMING:
					   HandleComingProcess((ProcessCommand)cmd);
					   break;
				   default:
					  break;
			   }
		   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
