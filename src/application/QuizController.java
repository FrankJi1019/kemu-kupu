package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
	
	private static List<String> words;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println(words);
	}
	
	public static void setWords(List<String> words) {
		QuizController.words = words;
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
