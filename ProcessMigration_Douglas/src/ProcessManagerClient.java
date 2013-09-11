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
	//private static final String SPACE = " ";
	public static void main(String[] arg) throws IOException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		System.out.println("ProcessManagerClient is running...");
		ProcessManagerClient pmc = new ProcessManagerClient(arg[1], arg[2]);
		Thread heatbeatSender = new Thread(new HeartbeatSender(pmc));
		heatbeatSender.start();
		Thread socketListener = new Thread(new ClientSocketHandler(pmc));
		socketListener.start();
//		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//		String s;
//		while(true) {
//			System.out.print("==>");
//			s = in.readLine();
//			try {
//				String processName = s.split(SPACE)[0];
//				String[] arg = new String[s.split(SPACE).length];
//				for(int i = 0; i < arg.length; i++)
//					arg[i] = s.split(SPACE)[1+i];
//				Class pro = Class.forName(processName);
//				Object process = pro.newInstance();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				System.err.println("The Migratable Process Input Not Found!");
//			}


			
			
			
			
//			 NewJobCommand njc = (NewJobCommand) command;
//			    String cmd = njc.getInput();
//			    int jobid = njc.getJobId();
//			    /* parse the class name and arguments */
//			    int spaceIndex = cmd.indexOf(' ');
//			    String className = "";
//			    String[] arguments = null;
//
//			    if (spaceIndex == -1) {
//			      className = cmd;
//			    } else {
//			      className = cmd.substring(0, spaceIndex);
//			      arguments = cmd.substring(spaceIndex + 1).split(" ");
//			    }
//			    Object[] objargs = { arguments };
//			    Class theClass = null;
//			    /* start the process */
//			    try {
//			      logger.info("CLASSNAME: "+className);
//			      theClass = Class.forName(className);
//			      MigratableProcess process = (MigratableProcess) theClass.getConstructor(String[].class)
//			              .newInstance(objargs);
//			      Thread thread = new Thread(process);
//			      thread.setName(jobid+" "+process.toString());
//			      _context.processes.put(thread, process);
//			      thread.start();
//			      logger.info("JOB: "+cmd+" STARTED!");
//			    } catch (ClassNotFoundException e) {
//			      e.printStackTrace();
//			    } catch (IllegalArgumentException e) {
//			      e.printStackTrace();
//			    } catch (SecurityException e) {
//			      e.printStackTrace();
//			    } catch (InstantiationException e) {
//			      e.printStackTrace();
//			    } catch (IllegalAccessException e) {
//			      e.printStackTrace();
//			    } catch (InvocationTargetException e) {
//			      e.printStackTrace();
//			    } catch (NoSuchMethodException e) {
//			      e.printStackTrace();
//			    }
		}
	}


