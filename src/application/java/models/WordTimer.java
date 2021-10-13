package application.java.models;

import java.util.Timer;
import java.util.TimerTask;

import application.java.controllers.QuizController;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class WordTimer {
	
	private Label timerLabel = null;
	private int time = 0;
	private Timer timer = new Timer();
	private TimerTask tt = null;
	private int score = 100;
	
	public static int finalScore = 100/QuizController.attemptTimes;
	
	
	// the time increases after [precision] milliseconds
	private int precision = 200;
	
	public WordTimer(Label timerLabel) {
		this.timerLabel = timerLabel;
		
	}
	
	public void start(int wordLengthTime) {
		this.timer.cancel();
		this.timer = new Timer();
		if (QuizController.attemptTimes == 1) {
			score = 100;
		} else if (QuizController.attemptTimes == 2) {
			score = 50;
			precision = 500;
		}
		this.tt = new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						time = time + precision;
						if (time > wordLengthTime) {
							if (score <= 50/QuizController.attemptTimes + 1) {
								score = 50/QuizController.attemptTimes + 1;
							}
							score--;
							//timerLabel.setText(new Integer(time - wordLengthTime).toString());
							timerLabel.setText("Score: "+score);
							finalScore = score;
							
						}
					}
				});
			}
		};
		
		this.timer.scheduleAtFixedRate(tt, 0, (int)(this.precision));
		
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
