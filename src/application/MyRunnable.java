package application;

public class MyRunnable implements Runnable {
	
	private String word;
	private double speed;
	
	private static Object lock = new Object();
	static boolean reading = false;
	
	public MyRunnable(String word, double speed) {
		this.word = word;
		this.speed = speed;
	}

	@Override
	public void run() {
		if (MyRunnable.reading) return;
		MyRunnable.reading = true;
		synchronized (lock) {
			FileIO.speakMaori(this.word, this.speed);
		}
		MyRunnable.reading = false;
	}

}
