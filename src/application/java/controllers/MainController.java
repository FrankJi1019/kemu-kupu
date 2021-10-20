package application.java.controllers;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.java.models.AnimationManager;
import application.java.models.SceneManager;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable{
	
	private SceneManager sceneManager = new SceneManager();

	@FXML SVGPath koruSvg;
	
	/**
	 * this method is to quit the game when called.
	 */
	public void quit() {
		
		// added confirmation alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quiting Game");
		alert.setHeaderText("Are you sure you want to Quit?");
		
		// if click ok then quit game
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			System.exit(0);
		}
		
		// otherwise return to game, close the alert.
		if (result.get() == ButtonType.CANCEL) {
			alert.close();
		}
	}
	
	/**
	 * this method switches to Practice scene when called
	 */
	public void practice(ActionEvent e) throws IOException {
		TopicsScreenController.isPractice = true;
		sceneManager.switchScene(e, "TopicsScreen");

	}
	
	/** 
	 * this method switches to Topics scene when called.
	 */
	public void newGame(ActionEvent e) throws IOException {
		TopicsScreenController.isPractice = false;
		sceneManager.switchScene(e, "TopicsScreen");

	}
	
	/*
	 * This method is the action for score board button, used to switch the scene to the score board
	 */
	public void scoreBoard(ActionEvent e) throws IOException {
		sceneManager.switchScene(e, "Scoreboard");

	}

	
	public void helpScreen(ActionEvent e) throws IOException {
		sceneManager.switchScene(e, "Help");

	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		AnimationManager animationManager = new AnimationManager();
		animationManager.playStrokeAnimation(koruSvg, 1000, 10, 3);
	}

}
