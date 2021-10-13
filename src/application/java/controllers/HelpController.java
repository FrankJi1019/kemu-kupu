package application.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import application.java.models.Topic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HelpController implements Initializable{

	private Stage stage;
	private Scene scene;
	@FXML ImageView imageInFocus;
	@FXML AnchorPane carouselPane;
	@FXML ToggleGroup imageSelectGroup;
	@FXML Label caption;

	private static int imageSelectedID = 0;
	
	private static final String IMAGES_DIRECTORY_PATH = "application/resources/images/";

	private ArrayList<Image> images = new ArrayList<Image>();
	private ArrayList<String> captions = new ArrayList<String>();

	/**
	 * This method switches back to the main menu when the return home button is pressed
	 */
	public void returnHome(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void nextImage() {
		imageSelectedID++;
		List<Toggle> imageRadioToggles = imageSelectGroup.getToggles();
		if(imageSelectedID > imageRadioToggles.size()-1) {
			imageSelectedID = 0;
		}
		((ToggleButton) imageRadioToggles.get(imageSelectedID)).fire();
		
	}
	public void prevImage() {
		imageSelectedID--;
		List<Toggle> imageRadioToggles = imageSelectGroup.getToggles();
		if(imageSelectedID < 0) {
			imageSelectedID = imageRadioToggles.size()-1;
		}
		((ToggleButton) imageRadioToggles.get(imageSelectedID)).fire();

	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialiseImageMap();
		setToggleActions();
	}

	private void setToggleActions() {
		List<Toggle> imageRadioToggles = imageSelectGroup.getToggles();
		for (Toggle toggle : imageRadioToggles) {
			ToggleButton toggleButton = (ToggleButton) toggle;
			int toggleID = imageRadioToggles.indexOf(toggle);
			// If we've for some reason got more toggles than images, we don't want an error - so don't give any extra toggles functionality
			if(toggleID < images.size()) {
				toggleButton.setOnAction(e -> {
					imageInFocus.setImage(images.get(toggleID));
					caption.setText(captions.get(toggleID));
				});
			}
		}
		
		((ToggleButton) imageRadioToggles.get(0)).fire();
	}

	private void initialiseImageMap() {
		addImage("back-btn.png", "This is to show that description I can use images");
		addImage("home-btn.png", "This is to show that description I can use images");
		addImage("back-btn.png", "This is to show that descriptione I can use images");
		addImage("home-btn.png", "This is to show that description I can use images");
		addImage("home-btn.png", "This is to show that descripteion I can use images");
		addImage("home-btn.png", "This is to show that descriptione I can use images");
	}


	public void addImage(String filename, String description) {
		images.add(createImageFile(filename));
		captions.add(description);
	}
	
	public Image createImageFile(String filename) {
		return new Image(IMAGES_DIRECTORY_PATH + filename, imageInFocus.getFitWidth(), imageInFocus.getFitHeight(), true, true);
	}
}

