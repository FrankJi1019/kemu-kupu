package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TopicController {

	@FXML Button btn;
	@FXML ImageView img;
	
	private Topic topic;
	
	// when we switch scene, this will be the action event that does so
	public void topicSelected(ActionEvent event) throws IOException {
		String topicSelected = this.btn.getText();
		topicSelected = topicSelected.replace(" ", "-");
		// this is where we set the word list
		
		System.out.println("You clicked: "+  topicSelected);
		
		
	}
	
	public void setData(Topic topic) {
		this.topic = topic;
		btn.setText(topic.getName());
		Image image  = new Image("file:data/images/"+topic.getIconSrc());
		img.setImage(image);
	}
	
}
