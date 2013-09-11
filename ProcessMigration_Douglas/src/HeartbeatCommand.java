


public class HeartbeatCommand extends Command{
	//public HeartbeatCommand() {}
	
	private int _workLoad;
	
	
	public HeartbeatCommand( int port, String hn, ProcessManagerClient pmc) {
		super.SetHostName(hn);
		super.SetPort(port);
		super.SetType(cmdType.HEART_BEAT);
		this._workLoad = pmc.GetProcessList().size();
	}
	public int GetWorkload() {
		return this._workLoad;
	}
	
	@Override
	public void PrintCommand() {
		System.out.println("HostName: "+ super.GetHostName() + " Port: "+super.GetPort() + " Type: Heartbeat"
				+ " Workload: " + this._workLoad);
	}
}
