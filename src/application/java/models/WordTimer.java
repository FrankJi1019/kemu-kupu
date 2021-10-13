package application.java.models;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class WordTimer {
	
	private Label timerLabel = null;
	private int time = 0;
	private Timer timer = new Timer();
	private TimerTask tt = null;
	private int score = 100;
	
	
	// the time increases after [precision] milliseconds
	private int precision = 200;
	
	public WordTimer(Label timerLabel) {
		this.timerLabel = timerLabel;
		
	}
	
	public void start(int wordLengthTime) {
		this.timer.cancel();
		this.timer = new Timer();
		this.tt = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						time = time + precision;
						if (time > wordLengthTime) {
							if (score <= 51) {
								score = 51;
							}
							score--;
							//timerLabel.setText(new Integer(time - wordLengthTime).toString());
							timerLabel.setText("Score: "+score);
							
						}
					}
				});
			}
		};
		this.timer.scheduleAtFixedRate(tt, 0, (int)(this.precision));
		score = 100;
	}
	
	public void stop() {
		timer.cancel();
		this.timer = new Timer();
		this.tt = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timerLabel.setText(String.format("Time: %.2f", time = time + precision));
					}
				});
			}
		};
		this.time = 0;
	}
	
}
