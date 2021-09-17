package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class QuizController {
	
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
