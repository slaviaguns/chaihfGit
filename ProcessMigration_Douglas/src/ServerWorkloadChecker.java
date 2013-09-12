
public class ServerWorkloadChecker implements Runnable{
	
	public ServerWorkloadChecker() {}
	public boolean isRunning = true;
	@Override
	public void run() {
		while(isRunning) {
			synchronized(ServerSocketHandler.ClientListMutex) {
				int a = ProcessManagerServer.GetClientList().size();
				//System.out.println("The number of clients: "+ProcessManagerServer.GetClientList().size());
			}
			
					
			try {
				Thread.currentThread().sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
