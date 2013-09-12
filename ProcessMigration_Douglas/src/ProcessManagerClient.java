import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcessManagerClient {
	
	private Map<Thread, MigratableProcess> ProcessList = new HashMap<Thread, MigratableProcess>();
	private ClientMeta clientMeta;
	public Integer ClientListMutex = 0;
	
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


