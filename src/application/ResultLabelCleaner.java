package application;

import java.util.concurrent.FutureTask;

import javafx.application.Platform;

public class ResultLabelCleaner implements Runnable {
	
	private FutureTask<String> query;
	private int waitTime = 2000;
	
	public ResultLabelCleaner(FutureTask<String> query) {
		this.query = query;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(this.waitTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Platform.runLater(query);
		
	}
	
}
