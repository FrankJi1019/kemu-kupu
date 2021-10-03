package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TopicsController implements Initializable{

	@FXML GridPane grid;
	@FXML Button returnButton;
	@FXML AnchorPane anchorPane;
	
	private Stage stage;
	private Scene scene;

	private static int GRID_WIDTH = 4;
	
	/**
	 * This method occurs upon switching to Topics.fxml. It dynamically loads topic buttons
	 *  (Topic.fxml) based on what exists inside the words folder, and adds them to a 
	 *  Grid Pane.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Topic> topicList = generateListOfTopics();
		
		// If we don't find any topics (so /words is empty), tell the user
		if (topicList.isEmpty()) {
			displayErrorMessage("Uh oh! It doesn't look like you have any files in the words directory");
			return;
		}
		
		int col = 1;
		int row = 0;
		
		// Add each topic button (Topic.fxml) to the Grid Pane
		for(Topic item : topicList) {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Topic.fxml"));
			try {
				AnchorPane anchorPane = loader.load();
				TopicController  topicController = loader.getController();
				
				topicController.setData(item);
				
				// Create another row when max-grid width reached
				if(col==(GRID_WIDTH+1)) {
					col = 1;
					row++;
				}
				
				grid.add(anchorPane, col++, row);
				GridPane.setMargin(anchorPane, new Insets(10));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method gets the filenames inside /words as a list, and creates a 
	 * list of Topic objects with the filenames, using a provided image if it exists.
	 */
	private List<Topic> generateListOfTopics() {
		List<String> list = LinuxCommand.getFileNameFromDirectory("./words");
		List<Topic> listOfTopics = new ArrayList<>();
		List<String> imageList = LinuxCommand.getFileNameFromDirectory("./data/images");
		Topic topic;
		for(String fileName : list) {
			String formattedfileName = fileName.replace("-", " ");
			
			// Create Topic object with default icon
			topic = new Topic(formattedfileName);
			
			// If we have an icon for it, then update the icon
			if (imageList.contains(fileName)){
				topic.setIconSrc(fileName+".png");
			} 
			
			listOfTopics.add(topic);
		}
		return listOfTopics;
		
	}
	
	/**
	 * This method switches back to the main menu when the return home button is pressed
	 */
	public void returnHome(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This method displays a specified message in red to show an error.
	 */
	private void displayErrorMessage(String message) {
		Label errorMessage = new Label(message);
		errorMessage.getStyleClass().add("empty-msg");
		errorMessage.setLayoutX(65);
		errorMessage.setLayoutY(120);
		anchorPane.getChildren().add(errorMessage);
	}
	
}
