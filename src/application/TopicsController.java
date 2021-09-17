package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class TopicsController implements Initializable{

	@FXML GridPane grid;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Topic> topicList = generateListOfTopics();
		int col = 1;
		int row = 0;
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

	private List<Topic> generateListOfTopics() {
		List<String> list = currentFiles();
		List<Topic> listOfTopics = new ArrayList<>();
		Topic topic;
		for(String fileName : list) {
			topic = new Topic(fileName);
			
			//here we can add an image as well
			// i.e. we could first check if fileName.png exists, 
			//		if yes, 
			//         topic.setIconSrc(fileName+".png");
			//      otherwise we leave it as the default image
			//
			
			listOfTopics.add(topic);
		}
		return listOfTopics;
		
	}

	private List<String> currentFiles() {
		List<String> list = new ArrayList<>();
		list.add("No");
		return list;
	}
	
}
