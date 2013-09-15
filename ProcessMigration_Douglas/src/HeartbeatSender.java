import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * 
 * @author hefuchai
 * HeartbeatSender is used by clients to send heartbeat to server
 */
public class HeartbeatSender implements Runnable{
	 private Socket socket = null;
     private int port = 10001;
     private ProcessManagerClient _processMC;
     public HeartbeatSender(ProcessManagerClient pmc) {
    	 this._processMC = pmc;
     }
	@Override
	public void run() {
         System.out.println("Connecting to server...");                 
            try {
				socket = new Socket("localhost", port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}   
            ObjectOutputStream out = null;
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				while(true) {		
					out.writeObject(new HeartbeatCommand(_processMC.GetClientMeta().getPort(), 
							_processMC.GetClientMeta().getHostName(), _processMC));
					out.flush();
					//pw.println("this is client "+ Thread.currentThread().getName());
					Thread.currentThread().sleep(5000);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(socket != null)
					try {
						out.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
	}

}
