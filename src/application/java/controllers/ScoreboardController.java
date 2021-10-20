package application.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import application.java.models.FileIO;
import application.java.models.SceneManager;
import application.java.models.User;
import application.java.models.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class ScoreboardController implements Initializable {
	
	@FXML private TableView<User> tableView;
	@FXML private TableColumn<User, String> userColumn;
	@FXML private TableColumn<User, Integer> scoreColumn;

	private SceneManager sceneManager = new SceneManager();

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		HashMap<String, Integer> loadedScore = null;
		try {
			loadedScore = FileIO.loadGame();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		ArrayList<User> userList = new ArrayList<User>();
		
		Set<String> keys = loadedScore.keySet();
		for (String name: keys) {
			User user = new User(name, loadedScore.get(name));
			userList.add(user);
		}
		
		ObservableList<User> list = FXCollections.observableArrayList(userList);
		
		this.userColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		this.scoreColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("score"));
		
		this.tableView.setItems(list);
		
	}
	
	public void returnHome(ActionEvent e) {
		sceneManager.switchScene(e, "Main");
	}

	public void reset() {
		// added confirmation alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Clearing scores");
		alert.setHeaderText("Are you sure you want to clear the statistics?");

		// if click ok then quit game
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			FileIO.deleteGame();
			this.initialize(null, null);
		}

		// otherwise return to game, close the alert.
		if (result.get() == ButtonType.CANCEL) {
			alert.close();
		}
		
	}

}
