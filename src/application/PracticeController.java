package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class PracticeController implements Initializable {
	
	private List<String> words = new ArrayList<String>();
	public static String currentWord = "";
	private double speedOfSpeech = 1;
	private int attempts = 1;
	
	private Stage stage;
	private Scene scene;
	
	@FXML
	private Label hintLabel;
	@FXML
	private Label speedLabel;
	@FXML
	private TextField textField;
	@FXML
	private Slider speedSlider;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// obtain all the words from all the word list
		this.words = FileIO.getAllWordsFromWordsDirectory();
		
		// choose a random word and remove from the word list
		nextWord();
		readCurrentWord();
		updateLetterCount();
		
		this.speedSlider.valueProperty().addListener(c ->{
			this.speedOfSpeech = 0 - (this.speedSlider.getValue());
			String roundedSpeed= String.format("Speed: x%.1f", 1/speedOfSpeech);
			if(roundedSpeed.equals("1.0")) {
				this.speedLabel.setText(roundedSpeed+ " (default)");
			} else {
				this.speedLabel.setText(roundedSpeed);
			}
		});

	}
	
	public void submit(ActionEvent e) {
		if (isAnswerCorrect()) {
			this.attempts = 1;
			FileIO.openGeneralWavFile("correct");
			this.switchScene("PracticeComplete", e);
		} else if (this.attempts == 1) {
			FileIO.openGeneralWavFile("wrong");
			this.attempts++;
			this.readCurrentWord();
			this.giveHint();
		} else {
			FileIO.openGeneralWavFile("wrong");
			this.switchScene("PracticeComplete", e);
		}
	}
	
	public void hearAgain() {
		readCurrentWord();
	}
	
	public void idk(ActionEvent e) {
		this.switchScene("PracticeComplete", e);
	}
	
	public void addMacronisedVowel(ActionEvent event) {
		textField.setText(textField.getText() + ((Button)event.getSource()).getText());
	}
	
	public void resetSpeed() {
		this.speedSlider.setValue(-1.0);
	}
	
	public void returnHome(ActionEvent e) {
		switchScene("Main", e);
	}
	
	private void readCurrentWord() {
		new Thread(new WordPlayer(PracticeController.currentWord, speedOfSpeech, true)).start();
		System.out.println(PracticeController.currentWord);
	}
	
	private void updateLetterCount() {
		StringBuilder sb = new StringBuilder();
		int maxCharPerLine = 50; // IMPORTANT: if you want to change this value, make sure also change the one in giveHint
		char[] letters = PracticeController.currentWord.toCharArray();
		for (char c: letters) {
			if (c == ' ') {
				sb.append("  ");
			} else {
				sb.append("_ ");
			}
		}
		
		char[] hint = sb.toString().toCharArray();
		
		// handle the long word
		if (hint.length > maxCharPerLine) {
			for (int i = maxCharPerLine; i > 0; i--) {
				if (hint[i] == ' ' && hint[i-1] == ' ' && hint[i+1] == ' ') {
					hint[i] = '\n';
					break;
				}
			}
		}
		
		this.hintLabel.setText(new String(hint));
	}
	
	private boolean isAnswerCorrect() {
		boolean b = this.textField.getText().equalsIgnoreCase(currentWord);
		this.textField.clear();
		return b;
	}
	
	private void nextWord() {
		int wordIndex = new Random().nextInt(this.words.size());
		currentWord = this.words.get(wordIndex);
//		this.words.remove(wordIndex);
	}
	
	private void giveHint() {
		double displayRatio = 0.5;
		int letterCount = (int)(PracticeController.currentWord.length() * displayRatio);
		Set<Integer> indexes = new HashSet<Integer>();
		Random random = new Random();
		char[] letters = PracticeController.currentWord.toCharArray();
		
		while (indexes.size() < letterCount) {
			int i = random.nextInt(PracticeController.currentWord.length());
			if (letters[i] != ' ') {
				indexes.add(i);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		int maxCharPerLine = 50;  // IMPORTANT: if you want to change this value, make sure also change the one in updateLetterCount
		
		for (int i = 0; i < letters.length; i++) {
			if (indexes.contains(i)) {
				sb.append(letters[i] + " ");
			} else if (letters[i] != ' ') {
				sb.append("_ ");
			} else {
				sb.append("  ");
			}
		}

		char[] hint = sb.toString().toCharArray();
		
		// handle the long word
		if (hint.length > maxCharPerLine) {
			for (int i = maxCharPerLine; i > 0; i--) {
				if (hint[i] == ' ' && hint[i-1] == ' ' && hint[i+1] == ' ') {
					hint[i] = '\n';
					break;
				}
			}
		}
		
		this.hintLabel.setText(new String(hint));
	}
	
	private void switchScene(String filename, ActionEvent e) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(String.format("/application/%s.fxml", filename)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
