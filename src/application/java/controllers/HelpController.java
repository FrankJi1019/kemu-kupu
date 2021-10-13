package application.java.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelpController {
	
	private Stage stage;
	private Scene scene;

	/**
	 * This method switches back to the main menu when the return home button is pressed
	 */
	public void returnHome(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
