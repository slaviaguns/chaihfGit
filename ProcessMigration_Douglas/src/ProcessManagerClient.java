import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author hefuchai
 *ProcessManagerClient setup as a dummy client node, which execute the commands from server.
 *The client provides two threads: 1. Listen to the any socket connection. 2. Send heartbeat to server periodically
 *
 */

public class ProcessManagerClient {
	public static int MigratableProcessPid = 0; //The pid for new spawn processes
	
	private Map<Thread, MigratableProcess> ProcessList = new HashMap<Thread, MigratableProcess>(); //list of process
	private ClientMeta clientMeta; //The meta data of client
	public Integer ClientListMutex = 0; //mutex for critical area of client
	
	public ProcessManagerClient(String hn, String port) {
		this.clientMeta = new ClientMeta(Integer.parseInt(port), hn);
	}
	
	public void handleNewJob() {
		
	}
	
	public Map<Thread, MigratableProcess> GetProcessList() {
		return ProcessList;
	}
	
	public ClientMeta GetClientMeta() {
		return clientMeta;
	}
	
	public ProcessManagerClient() {}
	
	public static void main(String[] arg) throws IOException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
		ProcessManagerClient pmc = new ProcessManagerClient(arg[1], arg[2]);
		System.out.println("ProcessManagerClient "+ pmc.GetClientMeta().getHostName()+":" +pmc.GetClientMeta().getPort()
				+" is running...");
		Thread heatbeatSender = new Thread(new HeartbeatSender(pmc));
		heatbeatSender.start();
		Thread socketListener = new Thread(new ClientSocketHandler(pmc));
		socketListener.start();
		}
	}


