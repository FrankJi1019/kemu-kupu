package application.java.models;

import java.io.IOException;

import application.java.controllers.SpeedToggleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class SpeedToggle extends Region {
	private SpeedToggleController  speedToggleController;
	public SpeedToggle() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/views/SpeedToggle.fxml"));
		try {
			AnchorPane togglePane = loader.load();
			 speedToggleController = loader.getController();
		
			 this.getChildren().add(togglePane);
			 
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public SpeedToggle(AnchorPane parentPane) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/views/SpeedToggle.fxml"));
		try {
			AnchorPane togglePane = loader.load();
			 speedToggleController = loader.getController();
			 this.getChildren().add(togglePane);
			parentPane.getChildren().add(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public SpeedToggle(AnchorPane parentPane, double layoutX, double layoutY) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/views/SpeedToggle.fxml"));
		try {
			AnchorPane togglePane = loader.load();
			 speedToggleController = loader.getController();
			 this.setLayoutX(layoutX);
			 this.setLayoutY(layoutY);
			 this.getChildren().add(togglePane);
			parentPane.getChildren().add(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Unsure
	 */
	public double getSpeed() {
		return this.speedToggleController.getSpeed();
	}
	
}
