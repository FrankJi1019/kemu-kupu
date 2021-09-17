package application;

public class MyRunnable implements Runnable {
	
	private String word;
	private double speed;
	
	public MyRunnable(String word, double speed) {
		this.word = word;
		this.speed = speed;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		FileIO.speakMaori(this.word, this.speed);
	}

}
