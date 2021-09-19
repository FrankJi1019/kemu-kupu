package application;


public class MyRunnable implements Runnable {
	
	private String word;
	private double speed;
	
	private static Object lock = new Object();
	static boolean reading = false;
	private boolean isImportant;
	
	public MyRunnable(String word, double speed, boolean isImportant) {
		this.word = word;
		this.speed = speed;
		this.isImportant = isImportant;
	}

	@Override
	public void run() {
		if (MyRunnable.reading && !this.isImportant) return;
		// if (MyRunnable.reading) return;
		MyRunnable.reading = true;
		synchronized (lock) {
			FileIO.speakMaori(this.word, this.speed);
		}
		MyRunnable.reading = false;
	}

}





