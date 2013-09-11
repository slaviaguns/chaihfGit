import java.io.Serializable;

/*
 * interface MigratableProcess provide the interface for constructing process
 * that can be migrated.
 */

public interface MigratableProcess extends Runnable, Serializable{

	String toString();
	
	void suspend();
}
