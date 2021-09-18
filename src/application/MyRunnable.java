package application;

public class MyRunnable implements Runnable {
	
	private String word;
	private double speed;
	
	private static Object lock = new Object();
	
	public MyRunnable(String word, double speed) {
		this.word = word;
		this.speed = speed;
	}

	@Override
	public void run() {
		synchronized (lock) {
			FileIO.speakMaori(this.word, this.speed);
		}
		
	}

}
