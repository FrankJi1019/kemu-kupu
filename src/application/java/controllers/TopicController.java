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
	
	private Button startButton = null;

	// Image dir exists outside src to give future ability to add topics (and images)
	private static String IMAGE_DIRECTORY = "./data/images/";

	/**
	 * This method sets the selected topic, ready to press p
	 * 
	 * @param event created by Topic Button press
	 * @throws IOException 
	 */
	public void setSelectedTopic(ActionEvent event) throws IOException {

		TopicsScreenController.topicName = this.btn.getText();
		this.startButton.setText(String.format("START >> \nTopic: %s", this.btn.getText()));
		this.startButton.setStyle("-fx-background-color: #91b2eb;");

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
	
	public void setStartButton(Button b) {
		this.startButton = b;
	}
}
