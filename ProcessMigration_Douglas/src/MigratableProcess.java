import java.io.Serializable;

/*
 * interface MigratableProcess provide the interface for constructing process
 * that can be migrated.
 */

public interface MigratableProcess extends Runnable, Serializable{

	void SetPid(int p);
	
	int GetPid();
	
	String toString();
	
	void suspend();
}
