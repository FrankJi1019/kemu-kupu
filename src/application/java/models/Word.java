package application.java.models;

/** this class is a data class for easier implementation for tracking stats for 
 	the completion screen by creating a array of this type in the QuizController.java
 	class
 */

public class Word {
	String word;
	double score;

	
	/** constructor
	 * 
	 * @param word
	 * @param score
	 */
	
	public Word(String word, double score) {
		this.word = word;
		this.score = score;
	}
	
	/**
	 * getter method for word
	 * @return word
	 */
	
	public String getWord() {
		return this.word;
	}
	/**
	 * getter method for score
	 * @return score
	 */
	
	public double getScore() {
		return this.score;
	}
	
	/**
	 * developer feature method
	 */
	public String toString() {
		return this.word + "," + this.score;
	}

}