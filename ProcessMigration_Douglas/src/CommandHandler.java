/*
 * the abstract command handler for both server and client
 */
public abstract class CommandHandler implements Runnable{
	public abstract void handle();
	
	@Override 
	public final void run() {
		handle();
	}
	
}
