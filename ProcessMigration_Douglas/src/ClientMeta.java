/*
 * the meta data of clients
 */
public class ClientMeta {
	private int _port;
	private String _hostName;
	//private boolean _isAlive;
	
	public ClientMeta() {
		//
	}
	
	public ClientMeta(int pt, String hn) {
		this._port = pt;
		this._hostName = hn;
		//this._isAlive = a;
	}
	
	public int getPort() {
		return this._port;
	}
	
	public String getHostName() {
		return this._hostName;
	}
	
//	public boolean getAliveState() {
//		return this._isAlive;
//	}
	
	public void PrintClientMeta() {
		System.out.println("Port: "+this._port+" HostName: "+this._hostName);
	}
	
}
