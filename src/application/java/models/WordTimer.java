package application.java.models;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class WordTimer {
	
	private Label timerLabel = null;
	private double time = 0;
	private Timer timer = new Timer();
	private TimerTask tt = null;
	private int score = 1000;
	
	// the time increases after [precision] seconds
	private double precision = 0.01;
	
	public WordTimer(Label timerLabel) {
		this.timerLabel = timerLabel;
		
	}
	
	public void start() {
		this.timer.cancel();
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
	
	public int scoreCalculation(int wordLength,int startTime,int endTime) {
		
		// speaking time(in seconds):       time = 0.1274(word_length) + 1.1665s
		// user typing time(in seconds):    time = 0.4(word_length) + 0.8s
		
		int durationMs = endTime-startTime;
		int TIME_MS_EVERY_POINT_DEDUCTED = 200;
		
//		int speakingTime = 127*wordLength + 1166;
		int typingTime = 400*wordLength + 800;
		int thinkingTime = durationMs - typingTime;
		//System.out.println(thinkingTime);
		int score = 100;
	
		int scoreDeductionRate = thinkingTime/TIME_MS_EVERY_POINT_DEDUCTED;
		if (scoreDeductionRate <= 0) {
			scoreDeductionRate = 0;
		}
		if (scoreDeductionRate > 50) {
			scoreDeductionRate = 50;
		}
		
		score = score - 1 * scoreDeductionRate;
		
		return score;
	}

}
