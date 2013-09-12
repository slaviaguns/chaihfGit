


public class ProcessMigrationCommand extends Command{
	private int _migrationNum;
	private String _desHostName;
	private int _desPort;
	
	public ProcessMigrationCommand() {
		
	}
	
	public ProcessMigrationCommand(String srchn, int srcport, String deshn, int desport, int num) {
		super(srchn, srcport, cmdType.PROCESS_MIGRATION);
		this._migrationNum = num;
		this._desHostName = deshn;
		this._desPort = desport;
	}
	public String GetDesHostName() {
		return this._desHostName;
	}
	
	public int GetDesPort() {
		//System.out.println("desport: "+ this._desPort);
		return this._desPort;
		
	}
	
	public int GetMigNum() {
		return this._migrationNum;
	}
	
	@Override 
	public void PrintCommand() {
		System.out.println("From SrcHostName: "+ super.GetHostName() + " SrcPort: "+super.GetPort() + " to "+
	" DesHostName: "+this._desHostName + " Desport: "+this._desPort+" Num: "+this._migrationNum
				+" Type: Migration_process");
	}
}
