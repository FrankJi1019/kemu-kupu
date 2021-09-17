package application;


import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MainController {
	
	private Stage stage;
	private Scene scene;
	
	public void quit() {
		// added confirmation alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quiting Game");
		alert.setHeaderText("Are you sure to Quit?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			System.exit(0);
		}
		
		if (result.get() == ButtonType.CANCEL) {
			alert.close();
		}
	}
	
	public void newGame(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Topics.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
