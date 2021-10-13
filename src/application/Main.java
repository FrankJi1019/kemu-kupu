package application;
	
import java.io.IOException;
import java.util.HashMap;

import application.java.models.FileIO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource("resources/views/Main.fxml")); 
		primaryStage.setTitle("Kēmu Kupu");
		Scene scene = new Scene(root);
		String css = this.getClass().getResource("resources/css/application.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
