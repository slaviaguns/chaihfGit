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


public class ProcessManagerServer {
	private static Map<String, Integer> clientList = new HashMap<String, Integer>();
	
	private int _port;
	
	private String _hostName;
	
	private String LS = "ls";
	
	private String PS = "ps";
	
	private String RUN = "run";
	
	private String MIG = "migrate";
	//the pool size of the thread pool for handling sockets
//	private final int POOL_SIZE = 10;
//	
//	private ExecutorService _executorService;
	
	public static Map<String, Integer> GetClientList() {
		return clientList;
	}
	
	public ProcessManagerServer() {}
	
	public ProcessManagerServer(String hn, String p) throws IOException {
//		ServerSocket serSoc = new ServerSocket(10001);
//		while(true) {
//			Socket soc = serSoc.accept();
//			BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
//			PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
//		}
		
		this._hostName = hn;
		this._port = Integer.parseInt(p);
	//	this._executorService=Executors.newFixedThreadPool(POOL_SIZE);
		
	}
	
	public int GetPort() {
		return this._port;
	}
	
//	public void socketListening() {
//		ServerSocket serversck = null;
//		boolean listen = true;
//		try {
//			System.out.println("Server is running...");
//			System.out.println("Waiting for connection...");
//			serversck = new ServerSocket(this._port);
//			while(listen) {
//				Socket socket = serversck.accept();
//				_executorService.execute(new ServerSocketHandler(socket));
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				serversck.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
	public void ConsoleHandler() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input;
		System.out.print("==>");
		while((input = in.readLine())!=null) {
			//System.out.print("==>");
			//input = in.readLine();
			if(input.split(" ")[0].toLowerCase().equals(LS)) 
				LSCommand();
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
	
	//migrate (src)localhost:10005 (des)localhost:10004 1
	public void MigrateCommand(String input) {
		boolean flag = false;
		if(input.split(" ").length == 4) {
			String SrchostAndPort = input.split(" ")[1];
			String DeshostAndPort = input.split(" ")[2];
			
			synchronized(ServerSocketHandler.ClientListMutex) {
				if(ProcessManagerServer.GetClientList().containsKey(SrchostAndPort));
					flag = true;
			}
			if(flag) {
				Thread cmdSender = new Thread(new CommandSender(new ProcessMigrationCommand(SrchostAndPort.split(":")[0], 
						Integer.parseInt(SrchostAndPort.split(":")[1]), 
						DeshostAndPort.split(":")[0], 
						Integer.parseInt(DeshostAndPort.split(":")[1]), Integer.parseInt(input.split(" ")[3]))));
				cmdSender.start();
			}
			//start a commandsender thread to send command to the specific client
		} else {
			System.out.println("Usage: ps hostname:port");
			return;
		}
	}
	
	//ls
	public void LSCommand() {
		synchronized(ServerSocketHandler.ClientListMutex) {
			Iterator iter = ProcessManagerServer.GetClientList().entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next(); 
			    System.out.println("Host: "+entry.getKey()+"\tNumber of Process "+entry.getValue()); 
			   // Object val = entry.getValue(); 
			} 
		}
	}
	
	//ps localhost:10001
	public void PSCommand(String input) {
		boolean flag = false;
		if(input.split(" ").length >= 2) {
			String hostAndPort = input.split(" ")[1];
			
			synchronized(ServerSocketHandler.ClientListMutex) {
				if(ProcessManagerServer.GetClientList().containsKey(hostAndPort));
					flag = true;
			}
			if(flag) {
				Thread cmdSender = new Thread(new CommandSender(new PrintProcessCommand(hostAndPort.split(":")[0], 
						Integer.parseInt(hostAndPort.split(":")[1]))));
				cmdSender.start();
			}
			//start a commandsender thread to send command to the specific client
		} else {
			System.out.println("Usage: ps hostname:port");
			return;
		}
		

	}
	
	//run localhost:10005 processName arg1 arg2 ...
	public void RunCommand(String input) {
		boolean flag = false;
		String[] arg = input.split(" ");
		if(arg.length >= 3) {
			String hostAndPort = arg[1];
			
			synchronized(ServerSocketHandler.ClientListMutex) {
				if(ProcessManagerServer.GetClientList().containsKey(hostAndPort));
					flag = true;
			}
			if(flag) {
				Thread cmdSender = new Thread(new CommandSender(new StartNewJobCommand(hostAndPort.split(":")[0], 
						Integer.parseInt(hostAndPort.split(":")[1]), arg, arg.length)));
				cmdSender.start();
			} else {
				System.out.println("Client not found");
			}
		} else {
			System.out.println("Usage: ps hostname:port");
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
		
		//pms.socketListening();
	}
}
