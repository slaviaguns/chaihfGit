import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author hefuchai
 * Each client will setup a listener to listen from other connection.
 * The connection could be from server and other clients.
 * A new thread will be provide to handle the commands.
 */

public class ClientSocketHandler implements Runnable{
	private ProcessManagerClient client;
	public ClientSocketHandler() {
		
	}
	public ClientSocketHandler(ProcessManagerClient c) {
		this.client = c;
	}
	@Override
	public void run() {
		ServerSocket serversck = null;
		boolean listen = true;
		try {
			serversck = new ServerSocket(this.client.GetClientMeta().getPort());
			while(listen) {
				Socket socket = serversck.accept();
				Thread handleCmd = new Thread (new ClientCmdHandler(socket, this.client));
				handleCmd.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
			
		
	}
}
