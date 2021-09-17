package application;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {
	
	private Stage stage;
	private Scene scene;
	
	public void quit() {
		System.exit(0);
	}
	
	public void newGame(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Topics.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}