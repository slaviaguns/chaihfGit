
public class PrintProcessCommand extends Command{
	
	public PrintProcessCommand(String hn, int port) {
		super(hn, port, cmdType.PRINT_PROCESS);
	}
	
	@Override 
	public void PrintCommand() {
		System.out.println("HostName: "+ super.GetHostName() + " Port: "+super.GetPort() + " Type: Print_Process");
	}
}
