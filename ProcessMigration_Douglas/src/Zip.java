/*
 * Zip: A process to zip or unzip file.
 * Uasge: 
 *  to zip file: Zip -zip <fileUnzipped> <fileZipped>
 *  to unzip file: Zip -unzip <fileZipped> <fileUnzipped>
 *
 */

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@SuppressWarnings({ "serial" })
public class Zip implements MigratableProcess{
	private TransactionalFileInputStream inFile;
	private TransactionalFileOutputStream outFile;
	private int pid;
	//flag controls whether zip or unzip
	private boolean flag;

	//name of the file to be zip
	private String fileName;

	private volatile boolean suspending;
	/*private String[] args;*/

	//the buffer to store read bytes
	private byte[] b = new byte[128];

	@Override
	public int GetPid() {
		return this.pid;
	}
	
	public void SetPid(int p) {
		this.pid = p;
	}
	public Zip(String args[]) throws Exception{
		//this.args = args;
		if (args.length != 3){
			System.out.println("Uasge: Zip -zip <fileUnzipped> <fileZipped>");
			System.out.println("    or Zip -unzip <fileZipped> <fileUnzipped>");
			throw new Exception("Invalid Arguments");
		}

		if (args[0].equals("-zip")) 
			flag = true;
		else if (args[0].equals("-unzip"))
			flag = false;
		else{
			System.out.println("Uasge: Zip -zip <fileUnzipped> <fileZipped>");
			System.out.println("    or Zip -unzip <filezipped> <fileUnzipped>");
			throw new Exception("Invalid Arguments");
		}
		
		inFile = new TransactionalFileInputStream(args[1]);
		outFile = new TransactionalFileOutputStream(args[2], false);

		fileName = args[1];
	}

	public void run(){
		try {
			if (flag == true) { //zip file
				ZipOutputStream zipOut = new ZipOutputStream(outFile);
				zipOut.putNextEntry(new ZipEntry(fileName));
				while (!suspending) {
					int result;
					if ((result = inFile.read(b, 0, b.length)) > 0) {
						zipOut.write(b, 0, result);
					} else
						break;
					try {
						//sleep 5s, to make it easier to test without using large files
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// ignore it
					}
				}
				zipOut.closeEntry();
				zipOut.close();
				inFile.close();
				outFile.close();
			} else { //unzip file
				ZipInputStream zipIn = new ZipInputStream(inFile);
				if (zipIn.getNextEntry() != null) {
					while (!suspending) {
						int result;
						if ((result = zipIn.read(b)) > 0) {
							outFile.write(b, 0, result);
						} else
							break;
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// ignore it
						}
					}
					zipIn.closeEntry();
					outFile.close();
					zipIn.close();
					inFile.close();
				}
			}
		} catch (EOFException e) {
			// ignore it
		} catch (IOException e) {
			System.out.println("Zip Error: " + e);
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