package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class TopicController {

	@FXML Button btn;
	@FXML ImageView img;
	
	private Topic topic;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// when we switch scene, this will be the action event that does so
	public void topicSelected(ActionEvent event) throws IOException {
		String topicSelected = this.btn.getText();
		topicSelected = topicSelected.replace(" ", "-");
		// this is where we set the word list
		
		// System.out.println("You clicked: "+  topicSelected);
		
		// get the word list
		List<String> words = FileIO.getContentFromFile(topicSelected);
		
		// pass the word to the quiz controller + switch the scene
		QuizController.setWords(words);
		
		// switch the scene
		Parent root = FXMLLoader.load(getClass().getResource("/application/Quiz.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	public void setData(Topic topic) {
		this.topic = topic;
		btn.setText(topic.getName());
		Image image  = new Image("file:data/images/"+topic.getIconSrc());
		img.setImage(image);
	}
	
}
