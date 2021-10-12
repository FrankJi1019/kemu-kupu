package application.java.models;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class WordTimer {
	
	private Label timerLabel = null;
	private double time = 0;
	private Timer timer = null;
	private TimerTask tt = null;
	
	// the time increases after [precision] seconds
	private double precision = 0.01;
	
	public WordTimer(Label timerLabel) {
		this.timerLabel = timerLabel;
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
	}
	
	public void start() {
		this.timer.scheduleAtFixedRate(tt, 0, (int)(1000 * this.precision));
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
