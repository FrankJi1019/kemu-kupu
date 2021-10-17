package application.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
	@FXML Label caption;
	@FXML AnchorPane radioButtons;

	private static int imageSelectedID = 0;
	
	private static final String IMAGES_DIRECTORY_PATH = "/application/resources/images/";

	private ToggleGroup imageSelectGroup = new ToggleGroup();
	private ArrayList<Image> images = new ArrayList<Image>();
	private ArrayList<String> captions = new ArrayList<String>();

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialiseImageData();
		buildToggles();
		setToggleActions();
	}

	
	private void initialiseImageData() {
		addImage("slide1.png", "Kēmu Kupu is a simple and fun way to learn how to spell words in Te Reo Māori! Through great Text-To-Speech Technology, Kēmu Kupu also allows you to begin to make sense of how the Māori language comes together! Clicking the 'Games' module allows you to test what you know and don't know - and can be a great starting point!");
		addImage("slide2.png", "To start a game, select a topic from the available options, and then click START to begin.");
		addImage("slide3.png", "At the top of the game screen, there's a slider to control word-reading-speed and a score counter - which will change depending on how long you take to spell the word, and if you get it correct. The bottom two buttons are super useful when you're a bit stuck. The all important vowels-with-macrons can be added as well, with more information available at the '?' button. ");
		addImage("slide4.png", "Once you complete your game, you'll be able to review your score, and can even save it too!");
		addImage("slide5.png", "In practice mode, you are able to practice your Māori spelling skills. In the topic selection however, you might see a button you didn't see in games mode; this button let's you practice with all the words, randomly picking from each topic - a great tool for building up confidence across lots of topics at once! ");
		addImage("slide6.png", "In practice mode, rather than being given only one letter as a hint, you'll see more of the word - helping you to go further with your Māori spelling skills. ");
	}
	
	/*
	 * This method builds the amount of toggles needed for the amount of images required.
	 * Note that leftMostX , the layoutX position of the leftmost radio toggole can be 
	 * adjusted as required (e.g. if more or less images are used) 
	 */
	private void buildToggles() {
		int spacing = 35;
		int leftMostX = 210;
		for (int i = 0; i < images.size(); i++) {
			RadioButton rb = new RadioButton();
			rb.setToggleGroup(imageSelectGroup);
			rb.setLayoutX(leftMostX + i*spacing);
			radioButtons.getChildren().add(rb);
		}

	}
	
	/*
	 * This method sets the toggle button actions where each toggle corresponds to a different image 
	 * and it's caption. 
	 */
	private void setToggleActions() {
		List<Toggle> imageRadioToggles = imageSelectGroup.getToggles();
		for (Toggle toggle : imageRadioToggles) {
			ToggleButton toggleButton = (ToggleButton) toggle;
			int toggleID = imageRadioToggles.indexOf(toggle);
			toggleButton.setOnAction(e -> {
				imageInFocus.setImage(images.get(toggleID));
				caption.setText(captions.get(toggleID));
				imageSelectedID = toggleID;
			});
		
		}
		// Fire a button to start off with to make sure the screen starts here
		((ToggleButton) imageRadioToggles.get(0)).fire();
	}

	/*
	 * nextImage() changes the image when the right arrow is clicked.
	 * It does so by firing the RadioToggleButton action, effectively 
	 * going to the next image.
	 * 
	 */
	public void nextImage() {
		imageSelectedID++;
		List<Toggle> imageRadioToggles = imageSelectGroup.getToggles();
		// If we're at the last radio toggle, overlap to the start
		if(imageSelectedID > imageRadioToggles.size()-1) {
			imageSelectedID = 0;
		}
		((ToggleButton) imageRadioToggles.get(imageSelectedID)).fire();
		
	}
	
	/*
	 * prevImage() changes the image when the right arrow is clicked.
	 * It does so by firing the RadioToggleButton action, effectively 
	 * going to the next image.
	 * 
	 */
	public void prevImage() {
		imageSelectedID--;
		List<Toggle> imageRadioToggles = imageSelectGroup.getToggles();
		// If we're at the first radio toggle, overlap to the end
		if(imageSelectedID < 0) {
			imageSelectedID = imageRadioToggles.size()-1;
		}
		((ToggleButton) imageRadioToggles.get(imageSelectedID)).fire();

	}

	/*
	 * This method adds a specific image inside the IMAGE_DIRECTORY_PATH to the images ArrayList, and adds
	 * the corresponding caption to the captions ArrayList
	 */
	public void addImage(String filename, String description) {
		images.add(createImageFile(filename));
		captions.add(description);
	}
	
	/*
	 *  This method is used as a helper function to create an Image file for storage.
	 */
	public Image createImageFile(String filename) {
		return new Image(IMAGES_DIRECTORY_PATH + filename, imageInFocus.getFitWidth(), imageInFocus.getFitHeight(), true, true);
	}
	

	/**
	 * This method switches back to the main menu when the return home button is pressed
	 */
	public void returnHome(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/resources/views/Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
