import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author hefuchai
 *The ProcessManagerServer setup as a server, which connect to every client.
 *The server provides three thread: 1. main thread handles console input, 2. Listen to the heartbeat from clients
 *3. Check the workload of clients periodically
 */
public class ProcessManagerServer {
	private static Map<String, Integer> clientList = new HashMap<String, Integer>();
	
	private int _port;
	
	private String _hostName;
	
	//Commands from console input
	private String LS = "ls";
	
	private String PS = "ps";
	
	private String RUN = "run";
	
	private String MIG = "migrate";

	//The list of clients
	public static Map<String, Integer> GetClientList() {
		return clientList;
	}
	
	public ProcessManagerServer() {}
	
	public ProcessManagerServer(String hn, String p) throws IOException {
		this._hostName = hn;
		this._port = Integer.parseInt(p);
	//	this._executorService=Executors.newFixedThreadPool(POOL_SIZE);
		
	}
	
	public int GetPort() {
		return this._port;
	}
	
	
	public void ConsoleHandler() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input;
		System.out.print("==>");
		while((input = in.readLine())!=null) {
			if(input.split(" ")[0].toLowerCase().equals(LS)) 
				LSCommand(input);
			else 
				if(input.split(" ")[0].toLowerCase().equals(PS))
					PSCommand(input);
				else
					if(input.split(" ")[0].toLowerCase().equals(RUN))
						RunCommand(input);
					else
						if(input.split(" ")[0].toLowerCase().equals(MIG))
							MigrateCommand(input);
						else
							System.out.println("Command not found");
			
			//System.out.println(s);
			System.out.print("==>");
		}
	}
	

	public boolean isNumeric(String str){
		   for(int i=str.length();--i>=0;){
		      int chr=str.charAt(i);
		      if(chr<48 || chr>57)
		         return false;
		   }
		   return true;
		} 
	
	/*Send migrate process command to Clients
	 * migrate (src)localhost:10005 (des)localhost:10004 1
	 */
	public void MigrateCommand(String input) {
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		
		if(input.split(" ").length == 4 && isNumeric(input.split(" ")[3])) {
			String SrchostAndPort = input.split(" ")[1];
			String DeshostAndPort = input.split(" ")[2];
			
			synchronized(ServerSocketHandler.ClientListMutex) {
				if(ProcessManagerServer.GetClientList().containsKey(SrchostAndPort)) {
					flag = true;
				}
				if(ProcessManagerServer.GetClientList().containsKey(DeshostAndPort))
					flag1 = true;
				if(ProcessManagerServer.GetClientList().get(SrchostAndPort) > Integer.parseInt(input.split(" ")[3])) {
					flag2 = true;
				}
			}
			if(flag&&flag1) {
				Thread cmdSender = new Thread(new CommandSender(new ProcessMigrationCommand(SrchostAndPort.split(":")[0], 
						Integer.parseInt(SrchostAndPort.split(":")[1]), 
						DeshostAndPort.split(":")[0], 
						Integer.parseInt(DeshostAndPort.split(":")[1]), Integer.parseInt(input.split(" ")[3]))));
				cmdSender.start();
			} else 
				if(!flag)
					System.out.println("Source Hostname or port no found");
				else
					if(!flag1) 
						System.out.println("Destination Hostname or port no found");
					else
						if(!flag2)
							System.out.println("The input number is larger than the number of exist process");
									
			//start a commandsender thread to send command to the specific client
		} else {
			if(input.split(" ").length != 4) {
				System.out.println("Usage: migrate sourcehostname:sourceport deshostname:desport number");
				return;
			} else
				System.out.println("Require a number");
		}
	}
	
	/* List the clients connecting to server
	 * ls
	 */
	public void LSCommand(String input) {
		if(input.split(" ").length != 1)
			System.out.println("Usage: ls");
		else {
			synchronized(ServerSocketHandler.ClientListMutex) {
				Iterator iter = ProcessManagerServer.GetClientList().entrySet().iterator(); 
				while (iter.hasNext()) { 
				    Map.Entry entry = (Map.Entry) iter.next(); 
				    System.out.println("Host: "+entry.getKey()+" \t Number of Process: "+entry.getValue()); 
				   // Object val = entry.getValue(); 
				} 
			}
		}
	}
	
	/* Send printing process list command to Clients
	 * ps localhost:10001
	 */
	public void PSCommand(String input) {
		boolean flag = false;
		if(input.split(" ").length == 2) {
			String hostAndPort = input.split(" ")[1];
			
			synchronized(ServerSocketHandler.ClientListMutex) {
				if(ProcessManagerServer.GetClientList().containsKey(hostAndPort))
					flag = true;
			}
			if(flag) {
				Thread cmdSender = new Thread(new CommandSender(new PrintProcessCommand(hostAndPort.split(":")[0], 
						Integer.parseInt(hostAndPort.split(":")[1]))));
				cmdSender.start();
			} else {
				System.out.println("Client or port not found");
			}
			//start a commandsender thread to send command to the specific client
		} else {
			System.out.println("Usage: ps hostname:port");
			return;
		}
		

	}
	
	/* Send instantiate a new process command to Clients, including the arguements
	 * run localhost:10005 processName arg1 arg2 ...
	 */
	public void RunCommand(String input) {
		boolean flag = false;
		String[] arg = input.split(" ");
		if(arg.length >= 3) {
			String hostAndPort = arg[1];
			
			synchronized(ServerSocketHandler.ClientListMutex) {
				if(ProcessManagerServer.GetClientList().containsKey(hostAndPort))
					flag = true;
			}
			if(flag) {
				Thread cmdSender = new Thread(new CommandSender(new StartNewJobCommand(hostAndPort.split(":")[0], 
						Integer.parseInt(hostAndPort.split(":")[1]), arg, arg.length)));
				cmdSender.start();
			} else {
				System.out.println("Client or port not found");
			}
		} else {
			System.out.println("Usage: run hostname:port arg1 arg2 ...");
			return;
		}
		
		//start a commandsender thread to send command to the specific client
		
	}
	
	public static void main(String[] arg) throws IOException, InterruptedException {
		if(arg == null || arg.length != 3) {
			System.err.println("Usage: ProcessManagerServer hostname port");
		}
		ProcessManagerServer pms = new ProcessManagerServer(arg[1], arg[2]);
		
		Thread socketListener = new Thread(new ServerSocketListener(pms.GetPort()));
		socketListener.start();
		//
		Thread workloadChecker = new Thread(new ServerWorkloadChecker());
		workloadChecker.start();
		//workloadChecker.join();
		pms.ConsoleHandler();

		//socketListener.join();
		
	}
}
