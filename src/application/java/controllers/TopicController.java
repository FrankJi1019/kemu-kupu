package application.java.controllers;

import java.io.IOException;
import java.util.List;

import application.java.models.FileIO;
import application.java.models.Topic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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


	private Stage stage;
	private Scene scene;
	
	private Button button = null;

	// Image dir exists outside src to give future ability to add topics (and images)
	private static String IMAGE_DIRECTORY = "./data/images/";

	/**
	 * This method switches scene to the quiz and uses the chosen topic to set the word-list
	 * to the correct file.
	 * 
	 * @param event created by Topic Button press
	 * @throws IOException 
	 */
	public void switchToQuiz(ActionEvent event) throws IOException {
		// Set the quiz word-list to the topic selected through the button title
//		String topicSelected = this.btn.getText();
//		String fileName = topicSelected.replace(" ", "-");
//		List<String> words = FileIO.getContentFromFile(fileName);
//		QuizController.setWords(words);
		TopicsScreenController.topicName = this.btn.getText();
		this.button.setText(String.format("Start game with\n%s", this.btn.getText()));

//		// Switch the scene
//		Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Quiz.fxml"));
//		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		stage.setScene(scene);
//		stage.show();

	}

	/**
	 * This method uses a Topic object to set the required button and image element
	 * for display
	 * 
	 * @param topic to use text and image values of 
	 */
	public void setData(Topic topic) {
		btn.setText(topic.getName());
		Image image  = new Image("file:" + IMAGE_DIRECTORY + topic.getIconSrc());
		img.setImage(image);
	}
	
	public void setButton(Button b) {
		this.button = b;
	}
}
