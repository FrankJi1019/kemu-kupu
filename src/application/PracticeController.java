package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class PracticeController implements Initializable {
	
	private List<String> words = new ArrayList<String>();
	private String currentWord = "";
	private double speedOfSpeech = 1;
	
	@FXML
	private Label hintLabel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// obtain all the words from all the word list
		List<String> wordFiles = LinuxCommand.executeCommand("ls ./words");
		
		for (String file: wordFiles) {
			List<String> words = FileIO.getContentFromFile(file.replace(".txt", ""));
			this.words.addAll(words);
		}
		
		// choose a random word and remove from the word list
		int wordIndex = new Random().nextInt(this.words.size());
		currentWord = this.words.get(wordIndex);
		this.words.remove(wordIndex);
		
		// speak the word
		readCurrentWord();
		
		updateLetterCount();

	}
	
	public void submit() {
		System.out.println("SUbmit");
	}
	
	public void hearAgain() {
		System.out.println("hear again");
	}
	
	public void idk() {
		System.out.println("skip");
	}
	
	private void readCurrentWord() {
		new Thread(new WordPlayer(this.currentWord, speedOfSpeech, true)).start();
		System.out.println(this.currentWord);
	}
	
	private void updateLetterCount() {
		StringBuilder sb = new StringBuilder();
		int maxCharPerLine = 50;
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

}
