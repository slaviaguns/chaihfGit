import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable; 
/**
 * 
 * @author hefuchai
 * TransactionalFileInputStream extends InputStream, which mainly used to record the
 * offset of read files.
 */
public class TransactionalFileInputStream extends InputStream implements Serializable {

	private int _offset;
	
	private String _fileName;
	
	public TransactionalFileInputStream(String s) {
		this._fileName = s;
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		this._offset = 0;
	}
	
	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		RandomAccessFile stream = new RandomAccessFile(this._fileName, "r");
		stream.seek(this._offset);
		int nextByte;
		nextByte = stream.read();
			this._offset++;
			//return nextByte;
	//	}
		stream.close();
		return nextByte;
	}
	
	@Override
	public synchronized int read(byte[] buffer, int off, int len) throws IOException {
		RandomAccessFile stream = new RandomAccessFile(this._fileName, "r");
		stream.seek(this._offset);
		int getBytes = stream.read(buffer, off, len);
		if(getBytes != -1)
			this._offset += getBytes;
		else
			this._offset = -1;
		stream.close();
		return getBytes;
		//int i = 0;
		//byte readByte;
		//while(i<len && readByte = stream.read())
		
	}
	
	@Override
	public synchronized int read(byte[] buffer) throws IOException {
		RandomAccessFile stream = new RandomAccessFile(this._fileName, "r");
		stream.seek(this._offset);
		int getBytes = stream.read(buffer);
		if(getBytes != -1)
			this._offset += getBytes;
		else
			this._offset = -1;
		stream.close();
		return getBytes;
		
	}
}
