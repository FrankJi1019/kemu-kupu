package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
	
	private static List<String> allWords;
	private List<String> words = new ArrayList<String>();
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Random random = new Random();
		if (allWords.size() <= 5) {
			words = allWords;
			return;
		}
		while (this.words.size() < 5) {
			String word = QuizController.allWords.get(random.nextInt(QuizController.allWords.size()));
			if (!this.words.contains(word)) {
				this.words.add(word);
			}
		}
		System.out.println(this.words);
	}
	
	public static void setWords(List<String> words) {
		QuizController.allWords = words;
	}
	
	
	
	public void hearAgain() {
		System.out.println("hear again");
	}
	
	public void submit() {
		System.out.println("submit");
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

	

}
