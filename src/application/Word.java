package application;

public class Word {
	String word;
	double score;

	public Word(String word, double score) {
		this.word = word;
		this.score = score;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public double getScore() {
		return this.score;
	}

}