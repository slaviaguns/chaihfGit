import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;


public class TransactionalFileOutputStream extends OutputStream implements Serializable{

		private String _fileName;

	  private long _offset; // offset is not necessary for output stream

	  private boolean _firstWrite;

	  public TransactionalFileOutputStream(String fn) {
	    this(fn, false);
	  }

	  public TransactionalFileOutputStream(String fn, boolean isappend) {
	    this._fileName = fn;
	    this._offset = 0;
	    this._firstWrite = !isappend;
	  }

	  @Override
	  public synchronized void write(byte[] b, int off, int len) throws IOException {
	    // open the stream with append mode
	    FileOutputStream outstream = new FileOutputStream(this._fileName, !this._firstWrite);
	    this._firstWrite = false;

	    outstream.write(b, off, len);
	    outstream.flush();

	    // close the stream
	    outstream.close();
	  }

	  @Override
	  public synchronized void write(byte[] b) throws IOException {
	    // open the stream with append mode
	    FileOutputStream outstream = new FileOutputStream(this._fileName, !this._firstWrite);
	    this._firstWrite = false;

	    outstream.write(b);
	    outstream.flush();

	    // close the stream
	    outstream.close();
	  }

	  @Override
	  public synchronized void write(int b) throws IOException {
	    // open the stream with append mode
	    FileOutputStream outstream = new FileOutputStream(this._fileName, !this._firstWrite);
	    this._firstWrite = false;

	    outstream.write(b);
	    outstream.flush();

	    // close the stream
	    outstream.close();
	  }

}
