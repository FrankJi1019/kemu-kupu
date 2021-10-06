package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PracticeCompleteController implements Initializable {
	
	private Stage stage;
	private Scene scene;
	
	private String correctAnswer = "";
	private String userAnswer = "";
	
	@FXML
	private Label answerLabel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.correctAnswer = PracticeController.currentWord;
		this.userAnswer = PracticeController.userAnswer;
		this.answerLabel.setText(String.format("The answer is %s, and your answer is %s", this.correctAnswer, this.userAnswer));
		
	}
	
	public void playAgain(ActionEvent e) {
		this.switchScene("Practice", e);
	}
	
	public void returnHome(ActionEvent e) {
		this.switchScene("Main", e);
	}
	
	private void switchScene(String filename, ActionEvent e) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(String.format("/application/%s.fxml", filename)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}



}
