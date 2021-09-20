package application;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CompletedController {
	
	// these two variables are used to change scene
	private Stage stage;
	private Scene scene;
	
	@FXML
	private TableView<Word> summaryTable;
	@FXML
	private TableColumn<Word, String> wordColumn;
	@FXML
	private TableColumn<Word, Double> scoreColumn;
	@FXML
	private Label totalScoreLabel;
	
	/*
	 * This method will be invoked when other scene is switching to complete scene, this is to pass
	 * useful data to this controller, for example, the statistics for user answers
	 * The total score will also be calculated in this method and display on the screen as a label
	 * The parameter wordStats is the statistics of the user answers.
	 */
	public void setData(List<Word> wordStats) {
		
		// write the data to the table
		ObservableList<Word> list = FXCollections.observableArrayList(wordStats);
		
		this.wordColumn.setCellValueFactory(new PropertyValueFactory<Word, String>("word"));
		this.scoreColumn.setCellValueFactory(new PropertyValueFactory<Word, Double>("score"));
		this.summaryTable.setItems(list);
		
		// iterates the list to calculate the total score and set it to the text of label
		double totalScore = 0;
		for (Word word: wordStats) {
			totalScore += word.getScore();
		}
		this.totalScoreLabel.setText(Double.toString(totalScore));
		
	}
	
	public void returnHome(ActionEvent e) {
		this.switchScene(e, "Main");
	}
	
	public void playAgain(ActionEvent e) {
		this.switchScene(e, "Quiz");
	}
	
	
	/*
	 * This method is to switch scenes 
	 * The first parameter e is the ActionEvent that is received when user clicked a button
	 * The second parameter sceneName is the name of the fxml file, this string should not include 
	 * file extension and should be case-sensitive
	 */
	private void switchScene(ActionEvent e, String sceneName) {
		
		try {
			// establish the full relative path using the name of the scene
			// this is relative easy to do because all fxml files are stored in the same package
			String path = String.format("/application/%s.fxml", sceneName);
			
			Parent root = FXMLLoader.load(getClass().getResource(path));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}

}
