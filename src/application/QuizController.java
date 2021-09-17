package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Random;

public class QuizController implements Initializable {
	
	@FXML
	private Label scoreLabel;
	@FXML
	private Label wordCountLabel;
	@FXML
	private Label letterCountLabel;
	@FXML
	private Label resultLabel;
	@FXML
	private Label speedLabel;
	@FXML
	private Slider speedSlider;
	@FXML
	private TextField userAnswerTextField;
	
	// all words from file
	private static List<String> allWords;
	private List<String> words = new ArrayList<String>();
	
	private int score = 0;
	private int wordLetterCount = -1;
	private List<Word> wordStats = new ArrayList<Word>();
	
	private int attemptTimes = 1;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// get the five words
		Random random = new Random();
		if (allWords.size() <= 5) {
			words = allWords;
			FileIO.speakMaori(this.words.get(1), 1);
		}
		
		while (this.words.size() < 5) {
			if (QuizController.allWords.size() <= 5) break;
			String word = QuizController.allWords.get(random.nextInt(QuizController.allWords.size()));
			if (!this.words.contains(word)) {
				this.words.add(word);
			}
		}
		
		// init score the letter count
		wordLetterCount = this.words.get(0).length();
		int temp = wordLetterCount;
		
		
		// set word letter count
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < temp; i++) {
			if (this.words.get(0).charAt(i) == ' ') {
				sb.append("  ");
				wordLetterCount--;
			} else {
				sb.append("_ ");
			}
		}
		letterCountLabel.setText(String.format("%s(%d letters)", sb.toString(), wordLetterCount));
		
		// set which number is being tested
		wordCountLabel.setText(Integer.toString(6 - this.words.size()));
		
	}
	
	public static void setWords(List<String> words) {
		QuizController.allWords = words;
	}
	
	public void hearAgain() {
		System.out.println("hear again");
	}
	
	public void submit() {
		String userAnswer = userAnswerTextField.getText();
		if (this.checkWordMatch(userAnswer)) {
			this.wordStats.add(new Word(this.words.get(0), 1 / this.attemptTimes));
			this.attemptTimes = 1;
			this.words.remove(0);
		} else {
			
		}
	}
	
	public void dontKnow() {
		System.out.println("Don't know");
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			this.submit();
		}
	}
	
	public void ajustSpeed() {
		System.out.println("Speed is " + speedSlider.getValue());
	}

	public boolean checkWordMatch(String userAnswer) {
		return userAnswer.equalsIgnoreCase(this.words.get(0));
	}
	
}

class Word {
	String word;
	double score;

	public Word(String word, double score) {
		this.word = word;
		this.score = score;
	}

}

