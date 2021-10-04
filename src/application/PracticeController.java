package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class PracticeController implements Initializable {
	
	private List<String> words = new ArrayList<String>();
	private String currentWord = "";
	private double speedOfSpeech = 1;
	
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
	}

}
