package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class PracticeController implements Initializable {
	
	private List<String> words = new ArrayList<String>();
	private String currentWord = "";
	private double speedOfSpeech = 1;
	private int attempts = 1;
	
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
		List<String> wordFiles = LinuxCommand.executeCommand("ls ./words");
		
		for (String file: wordFiles) {
			List<String> words = FileIO.getContentFromFile(file.replace(".txt", ""));
			this.words.addAll(words);
		}
		
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
	
	public void submit() {
		if (isAnswerCorrect() || this.attempts == 2) {
			this.attempts = 1;
			this.nextWord();
			this.updateLetterCount();
			this.readCurrentWord();
		} else if (this.attempts == 1) {
			this.attempts++;
			this.readCurrentWord();
			this.giveHint();
		}
	}
	
	public void hearAgain() {
		readCurrentWord();
	}
	
	public void idk() {
		this.nextWord();
		this.updateLetterCount();
		this.readCurrentWord();
	}
	
	public void addMacronisedVowel(ActionEvent event) {
		textField.setText(textField.getText() + ((Button)event.getSource()).getText());
	}
	
	public void resetSpeed() {
		this.speedSlider.setValue(-1.0);
	}
	
	private void readCurrentWord() {
		new Thread(new WordPlayer(this.currentWord, speedOfSpeech, true)).start();
		System.out.println(this.currentWord);
	}
	
	private void updateLetterCount() {
		StringBuilder sb = new StringBuilder();
		int maxCharPerLine = 50; // IMPORTANT: if you want to change this value, make sure also change the one in giveHint
		char[] letters = this.currentWord.toCharArray();
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
		boolean b = this.textField.getText().equals(currentWord);
		this.textField.clear();
		return b;
	}
	
	private void nextWord() {
		int wordIndex = new Random().nextInt(this.words.size());
		currentWord = this.words.get(wordIndex);
		this.words.remove(wordIndex);
	}
	
	private void giveHint() {
		double displayRatio = 0.5;
		int letterCount = (int)(this.currentWord.length() * displayRatio);
		Set<Integer> indexes = new HashSet<Integer>();
		Random random = new Random();
		char[] letters = this.currentWord.toCharArray();
		
		while (indexes.size() < letterCount) {
			int i = random.nextInt(this.currentWord.length());
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

}
