package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TopicsController implements Initializable{

	@FXML GridPane grid;
	@FXML Button returnButton;

	
	private Stage stage;
	private Scene scene;

	
	// This is method is call when a controller instance has been created.
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Topic> topicList = generateListOfTopics();
		int col = 1;
		int row = 0;
		
		// adding topics names into the GridPane.
		for(Topic item : topicList) {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Topic.fxml"));
			try {
				AnchorPane anchorPane = loader.load();
				TopicController  topicController = loader.getController();
				
				topicController.setData(item);
				
				if(col==4) {
					col = 1;
					row++;
				}
				grid.add(anchorPane, col++, row);
				GridPane.setMargin(anchorPane, new Insets(10));
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		
	}

	// this methods gets the filename as a list and icon names as list and match
	// filename with icon names.
	private List<Topic> generateListOfTopics() {
		List<String> list = LinuxCommand.getFileNameFromDirectory("./words");
		List<Topic> listOfTopics = new ArrayList<>();
		List<String> imageList = LinuxCommand.getFileNameFromDirectory("./data/images");
		Topic topic;
		for(String fileName : list) {
			String formattedfileName = fileName.replace("-", " ");
			// create Topic object with default icon
			topic = new Topic(formattedfileName);
			// if we have an icon for it, then update the icon
			if (imageList.contains(fileName)){
				topic.setIconSrc(fileName+".png");
			} 
			listOfTopics.add(topic);
		}
		return listOfTopics;
		
	}
	
	// this method will return to Main game page when called.
	public void returnHome(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
}
