package application.java.models;

/** this class is a data class for easier implementation for tracking stats for 
 	the completion screen by creating a array of this type in the QuizController.java
 	class
 */

public class Word {
	
	public final static int CORRECT_FIRST = 1;
	public final static int CORRECT_SECOND = 2;
	public final static int INCORRECT = 3;
	public final static int SKIP = 4;
	
	String word;
	int score;
	String result;

	
	/** constructor
	 * 
	 * @param word
	 * @param score
	 */
	

	public Word(String word, int score, int result) {
		this.word = word;
		this.score = score;
		switch(result) {
		case Word.CORRECT_FIRST:
			this.result = "Correct on first attempt";
			break;
		case Word.CORRECT_SECOND:
			this.result = "Correct on second attempt";
			break;
		case Word.INCORRECT:
			this.result = "Incorrect";
			break;
		case Word.SKIP:
			this.result = "Skipped";
			break;
		default:
			this.result = "Unknown result";
		}
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
	
	public int getScore() {
		return this.score;
	}
	
	public String getResult() {
		return this.result;
	}
	
	/**
	 * developer feature method
	 */
	public String toString() {
		return this.word + "," + this.score + "," + this.result;
	}

}