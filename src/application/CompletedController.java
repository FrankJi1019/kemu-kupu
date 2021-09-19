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
	
	public void setDataToTable(List<Word> wordStats) {
		
		ObservableList<Word> list = FXCollections.observableArrayList(wordStats);
		
		this.wordColumn.setCellValueFactory(new PropertyValueFactory<Word, String>("word"));
		this.scoreColumn.setCellValueFactory(new PropertyValueFactory<Word, Double>("score"));
		this.summaryTable.setItems(list);
		
		int totalScore = 0;
		for (Word word: wordStats) {
			totalScore += word.getScore();
		}
		this.totalScoreLabel.setText(Integer.toString(totalScore));
		
	}
	
	public void returnHome(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void playAgain(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/application/Quiz.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
