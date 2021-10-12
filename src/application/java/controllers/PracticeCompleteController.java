package application.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PracticeCompleteController implements Initializable {
	
	private Stage stage;
	private Scene scene;
	
	private String correctAnswer = "";
	private String userAnswer = "";
		
	@FXML private Label answerLabel;
	@FXML private Rectangle feedbackRect;
	@FXML private Label feedbackLabel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// display the user's result on the label
		boolean isCorrect = PracticeController.isCorrect;
		this.correctAnswer = PracticeController.currentWord.toLowerCase();
		this.userAnswer = PracticeController.userAnswer.toLowerCase();
		this.answerLabel.setText(String.format("The answer is: %s \n You typed: %s", this.correctAnswer, this.userAnswer.trim()));
		
		// display different color (and transition) based on the result of user's answer
		if(isCorrect) {
			feedbackRect.setFill(Color.web("#00b24c"));
			FillTransition fillTransition = new FillTransition(Duration.millis(1500),feedbackRect,Color.web("#00b24c"), Color.web("#91b2eb"));
			feedbackLabel.setText("Great job! Good practice session!");
			fillTransition.play();
		} else {
			FillTransition fillTransition = new FillTransition(Duration.millis(1500),feedbackRect,Color.web("#f87676"), Color.web("#91b2eb"));
			feedbackLabel.setText("No worries, practice makes perfect!");
			fillTransition.play();
		}
		
	}
	
	public void playAgain(ActionEvent e) {
		this.switchScene("Practice", e);
	}
	
	public void returnHome(ActionEvent e) {
		this.switchScene("Main", e);
	}
	
	/**
	 * This method is used to switch between different scenes
	 * @param filename: the filename of the scene, this string does not include the .fxml extension
	 * @param e
	 */
	private void switchScene(String filename, ActionEvent e) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(String.format("../../resources/views/%s.fxml", filename)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}



}
