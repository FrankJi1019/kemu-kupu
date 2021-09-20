package application;

import java.util.concurrent.FutureTask;

import javafx.application.Platform;

/*
 * This class it to clear the result label
 */
public class ResultLabelCleaner implements Runnable {
	
	// this field contains what task should be completed, in this case is to clear the result label
	private FutureTask<String> query;
	
	// the amount of time to wait until the label shoul be clear
	private int waitTime = 2000;
	
	public ResultLabelCleaner(FutureTask<String> query) {
		this.query = query;
	}

	@Override
	public void run() {
		
		// wait for a specified amount of time and then run the task
		try {
			Thread.sleep(this.waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(query);
		
	}
	
}
