import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
/**
 * 
 * @author hefuchai
 * TransactionalFileOutputStream extends OutputStream, which mainly used to record the
 * offset of files.
 */

public class TransactionalFileOutputStream extends OutputStream implements Serializable{

	  private String file;

	  private boolean first;

	  public TransactionalFileOutputStream(String fn) {
	    this(fn, false);
	  }

	  public TransactionalFileOutputStream(String fn, boolean isappend) {
	    this.file = fn;
	    this.first = !isappend;
	  }

	  @Override
	  public synchronized void write(byte[] b, int off, int len) throws IOException {
	    FileOutputStream outstream = new FileOutputStream(this.file, !this.first);
	    this.first = false;
	    outstream.write(b, off, len);
	    outstream.flush();
	    outstream.close();
	  }

	  @Override
	  public synchronized void write(byte[] b) throws IOException {
	    FileOutputStream outstream = new FileOutputStream(this.file, !this.first);
	    this.first = false;
	    outstream.write(b);
	    outstream.flush();
	    outstream.close();
	  }

	  @Override
	  public synchronized void write(int b) throws IOException {
	    FileOutputStream outstream = new FileOutputStream(this.file, !this.first);
	    this.first = false;
	    outstream.write(b);
	    outstream.flush();
	    outstream.close();
	  }

}
