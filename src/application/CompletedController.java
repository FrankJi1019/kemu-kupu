package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CompletedController {
	
	private Stage stage;
	private Scene scene;
	
	public void returnHome(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void playAgain(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Quiz.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}