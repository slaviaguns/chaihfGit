/*
 * Tilte: A process to make every line of input file to become 'Title'
 * for example:
 * 	"hello have a good day" goes to "Hello Have A Good Day"
 * Uasge: 
 *  Title <fileUntitled> <fileTitled>
 *
 */
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;

//import org.apache.commons.lang.WordUtils;

@SuppressWarnings("serial")
public class Title implements MigratableProcess{
	private TransactionalFileInputStream inFile;
	private TransactionalFileOutputStream outFile;
	private int pid;
	
	private volatile boolean suspending;
	//private String[] args;

	public Title(String args[]) throws Exception{
		//this.args = args;
		if (args.length != 2){
			System.out.println("Uasge: Title <fileUntitled> <fileTitled>");
			throw new Exception("Invalid Arguments");
		}

		inFile = new TransactionalFileInputStream(args[0]);
		outFile = new TransactionalFileOutputStream(args[1], false);
	}

	@Override
	public int GetPid() {
		return this.pid;
	}
	
	public void SetPid(int p) {
		this.pid = p;
	}
	
	public void run(){
		PrintStream titleOut = new PrintStream(outFile);
		DataInputStream titleIn = new DataInputStream(inFile);
		try{
			while (!suspending){
				@SuppressWarnings("deprecation")
				String line = titleIn.readLine();
				if (line == null) {
					break;
				} 
				//String titleLine = WordUtils.capitalize(line);
				String[] tokens = line.split("\\s");
				for (String token : tokens)
					if (token.length() > 0)
						//every word in line to become capitalized
						titleOut.print(token.substring(0, 1).toUpperCase() + token.substring(1).toLowerCase() + " ");
				titleOut.println();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
						// ignore it
				}
			}
		} catch (EOFException e){
			// end of file
		} catch (IOException e){
			System.out.println("Title Error: " + e);
		}

		suspending = false;

	}

	@Override
	public void suspend(){
		suspending = true;
		while (suspending)
			;
	}

	public String toString(){
		String str = this.getClass().getName();
		/* For debug use: 
		if (this.args == null){
			str += "";
		}
		else{
			for (String arguments : args){
				str += arguments;
			}
		}*/
		//We only need the process name when running
		return str;
	}

}