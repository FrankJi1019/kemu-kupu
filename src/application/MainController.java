package application;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
	
	private Stage stage;
	private Scene scene;
	
	@FXML SVGPath koruSvg;
	
	/**
	 * this method is to quit the game when called.
	 */
	public void quit() {
		
		// added confirmation alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quiting Game");
		alert.setHeaderText("Are you sure to Quit?");
		
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
	 * this method switches to Topics scene when called.
	 */
	public void newGame(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Topics.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	
	/**
	 * This methods runs on start up. It is used to add an animation to the start screen.
	 * The animation is loosely based on:
	 * https://stackoverflow.com/questions/36727777/how-to-animate-dashed-line-javafx
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		double strokeArraySize = 1000.0;
		// use a koru svg defined in the FXML to create an animation
		koruSvg.getStrokeDashArray().add(strokeArraySize);
		koruSvg.setStrokeDashOffset(strokeArraySize);

		koruSvg.setStrokeWidth(3);
		
		

		// Create a SVG based animation and play
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(10),new KeyValue(koruSvg.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)),
				new KeyFrame(Duration.ZERO,new KeyValue(koruSvg.strokeDashOffsetProperty(),strokeArraySize,Interpolator.LINEAR))
				);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
        timeline.play();
		
	}

}
