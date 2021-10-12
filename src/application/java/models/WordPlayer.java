package application.java.models;

/*
 * This is a class to read the word out to the user in another thread
 */
public class WordPlayer implements Runnable {
	
	private String word;
	private double speed;
	
	// the lock that ensures only one thread is being executed at a time (no two word speaking at the 
	// same time)
	// to ensure that there is only one instance of lock, I made this statis
	private static Object lock = new Object();
	
	// if there is a word being read at the moment and another thread wants to start, it will start
	// after the current thread finishes only if the next thread is marked as important
	static boolean reading = false;
	private boolean isImportant;
	
	private static double readingTimeSeconds = -1;
	
	// set what the word is, how fast should it be read, and if it is important
	public WordPlayer(String word, double speed, boolean isImportant) {
		this.word = word;
		this.speed = speed;
		this.isImportant = isImportant;
	}

	@Override
	public void run() {
		
		long start = System.currentTimeMillis();
		
		// if another thread is executing currently and this thread is not important, then ignore this 
		if (WordPlayer.reading && !this.isImportant) return;
		// if (MyRunnable.reading) return;
		
		// set the reading to true because this thread starts to read the word
		WordPlayer.reading = true;
		
		// this block ensures only one word being read at a time
		synchronized (lock) {
			FileIO.speakMaori(this.word, this.speed);
		}
		
		// after reading, set it to false
		WordPlayer.reading = false;
		
		long end = System.currentTimeMillis();
		
		WordPlayer.readingTimeSeconds = (end - start) / 1000;
	}
	
	public static double getReadingTime() {
		return WordPlayer.readingTimeSeconds;
	}

}





