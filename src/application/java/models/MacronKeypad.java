package application.java.models;

import java.io.IOException;

import application.java.controllers.MacronKeypadController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class MacronKeypad extends Region {
	
	private MacronKeypadController macronKeypadController;

	public MacronKeypad(TextField textField) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/views/MacronKeypad.fxml"));
		try {
			AnchorPane macronKeypadPane = loader.load();
			
			macronKeypadController = loader.getController();
			macronKeypadController.initialise(textField);
			this.getChildren().add(macronKeypadPane);

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public MacronKeypad(TextField textField, AnchorPane parentPane) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/views/MacronKeypad.fxml"));
		try {
			AnchorPane macronKeypadPane = loader.load();
			macronKeypadController = loader.getController();
			macronKeypadController.initialise(textField);
			parentPane.getChildren().add(this);
			this.getChildren().add(macronKeypadPane);

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public MacronKeypad(TextField textField, AnchorPane parentPane, double layoutX, double layoutY) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/views/MacronKeypad.fxml"));
		try {
			AnchorPane macronKeypadPane = loader.load();
			macronKeypadController = loader.getController();
			macronKeypadController.initialise(textField);
			this.setLayoutX(layoutX);
			this.setLayoutY(layoutY);
			parentPane.getChildren().add(this);
			this.getChildren().add(macronKeypadPane);

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
