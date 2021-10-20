package application.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import application.java.models.AnimationManager;
import application.java.models.SceneManager;
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
	
	private String correctAnswer = "";
	private String userAnswer = "";
	
	private SceneManager sceneManager = new SceneManager();

	
	private static List<String> SECOND_INCORRECT_MESSAGE = new ArrayList<>(Arrays.asList(
			"Incorrect, don't worry, learning is a process...",
			"Incorrect, good luck next time!",
			"Incorrect, failure is the mother of success!",
			"Incorrect, don't give up, keep going!"));
		
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
		AnimationManager animationManager = new AnimationManager();

		// display different color (and transition) based on the result of user's answer
		if(isCorrect) {
			animationManager.playColourFadeAnimation(1500, feedbackRect, "#00b24c", "#91b2eb");

			feedbackLabel.setText("Great job! Good practice session!");
		} else {
			animationManager.playColourFadeAnimation(1500, feedbackRect, "#f87676", "#91b2eb");
			setEncouragingMessage();
		}
		
	}
	
	public void playAgain(ActionEvent e) {
		sceneManager.switchScene(e, "Practice");
	}
	
	public void returnHome(ActionEvent e) {
		sceneManager.switchScene(e, "Main");
	}
	
	/*
	 * This method sets random encouraging message when called.
	 */
	
	public void setEncouragingMessage() {
		Random rand = new Random();
		// select a random encouraging message from the message list.
		feedbackLabel.setText(SECOND_INCORRECT_MESSAGE.get((rand.nextInt(SECOND_INCORRECT_MESSAGE.size()))));
	}


}
