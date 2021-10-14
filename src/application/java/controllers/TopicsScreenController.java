package application.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
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

import application.java.models.FileIO;
import application.java.models.LinuxCommand;
import application.java.models.Topic;

public class TopicsScreenController implements Initializable{

	@FXML GridPane grid;
	@FXML Button returnButton;
	@FXML AnchorPane anchorPane;
	@FXML Button startButton;
	@FXML Button allTopicsButton;

	private Stage stage;
	private Scene scene;

	private static int GRID_WIDTH = 4;

	public static String topicName = "";

	public static boolean isPractice;

	/**
	 * This method occurs upon switching to Topics.fxml. It dynamically loads topic buttons
	 *  (Topic.fxml) based on what exists inside the words folder, and adds them to a 
	 *  Grid Pane.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		allTopicsButton.setVisible(isPractice);

		List<Topic> topicList = generateListOfTopics();
		topicName = "";

		// If we don't find any topics (so /words is empty), tell the user
		if (topicList.isEmpty()) {
			displayErrorMessage("Uh oh! It doesn't look like you have any files in the words directory");
			return;
		}

		int col = 1;
		int row = 0;

		// Add each topic button (Topic.fxml) to the Grid Pane
		for(Topic item : topicList) {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/Topic.fxml"));
			try {
				AnchorPane anchorPane = loader.load();
				TopicController  topicController = loader.getController();

				topicController.setData(item);
				topicController.setStartButton(this.startButton);

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

		// automatically set focus away from button
		// Attribution: https://stackoverflow.com/questions/12744542/requestfocus-in-textfield-doesnt-work/38900429

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				grid.requestFocus();
			}
		});

		// Attribution End
		this.startButton.setStyle("-fx-background-color: #e4e4e4;");
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
		Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * This methods switch to corresponding scene depends on if Game/Practice module selected.
	 * @param event
	 * @throws IOException
	 */
	public void startGame(ActionEvent event) throws IOException {

		// if Game module is selected
		if (isPractice == false) {

			String topicSelected = TopicsScreenController.topicName;

			// if no topic selected, do not proceed
			if (topicSelected.equals("")) {
				return;
			}

			// sets selected wordlist to the game module.
			String fileName = topicSelected.replace(" ", "-");
			List<String> words = FileIO.getContentFromFile(fileName);
			QuizController.setWords(words);

			// switch scene to Game module to start game.
			Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Quiz.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

			// if Practice module is selected
		} else if (isPractice == true) {
			String topicSelected = TopicsScreenController.topicName;

			// if no topic selected, do not proceed
			if (topicSelected.equals("")) {
				return;
			}

			// sets selected wordlist to the practice module.
			String fileName = topicSelected.replace(" ", "-");
			List<String> words = FileIO.getContentFromFile(fileName);
			PracticeController.wordsSetter(words);

			// switch scene to Practice module to start practice.
			Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Practice.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
	}

	/**
	 * this method is called when all topics button been clicked in the Practice selection scene.
	 * @param event
	 * @throws IOException
	 */
	public void startPracticeAllTopics(ActionEvent event) throws IOException {

		// sets all wordlist to the practice module.
		List<String> words = FileIO.getAllWordsFromWordsDirectory();
		PracticeController.wordsSetter(words);

		// switch scene to Practice module to start practice.
		Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Practice.fxml"));
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
