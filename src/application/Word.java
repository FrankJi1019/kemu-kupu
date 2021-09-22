package application;

// this class is a data class for easier implementation for tracking stats for 
// the completion screen by creating a array of this type in the QuizController.java
// class
public class Word {
	String word;
	double score;

	
	// constructor
	
	public Word(String word, double score) {
		this.word = word;
		this.score = score;
	}
	
	//getter and setter methods	
	
	public String getWord() {
		return this.word;
	}
	
	public double getScore() {
		return this.score;
	}

}