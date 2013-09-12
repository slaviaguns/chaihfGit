
public class StartNewJobCommand extends Command{
	private String[] _args = null;
	private int length;
	
	public StartNewJobCommand(String hn, int port, String[] a, int l) {
		super(hn, port, cmdType.NEW_PROCESS_INSTANCE);
		length = l - 2;
		this._args = new String[length];
		for(int i = 0; i<length;i++)
			this._args[i] = a[i+2];
	}
	
	public String GetClassName() {
		return this._args[0];
	}
	
	public String[] GetClassArg() {
		int argLen = length-1;
		String[] a = new String[argLen];
		for(int i = 0; i < argLen; i++)
			a[i] = this._args[i+1];
		return a;
		//return this._args;
	}
	@Override 
	public void PrintCommand() {
		System.out.println("*******************************");
		System.out.println(super.GetHostName() + ":"+super.GetPort() +" receive command. "+ " Type: Start_New_Process");
		System.out.println("Process name and arguements:");
		for(int i = 0; i<length; i++) 
			System.out.print(this._args[i]+" ");
		System.out.println("\n*******************************");
	}
}
