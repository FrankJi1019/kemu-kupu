package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.animation.FadeTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PracticeController implements Initializable {
	
	private static String INCORRECT_MESSAGE = "Incorrect, Try Again";
	private List<String> words = new ArrayList<String>();
	public static String currentWord = "";
	public static String userAnswer = "";
	public static boolean isCorrect;
	private double speedOfSpeech = 1;
	private int attempts = 1;
	private int lastRecordedCaretPosition = 0;

	
	
	private Stage stage;
	private Scene scene;
	
	@FXML private Label hintLabel;
	@FXML private Label speedLabel;
	@FXML private TextField textField;
	@FXML private Slider speedSlider;
	@FXML private AnchorPane macronInfo;
	@FXML private Button infoButton;
    @FXML private Button submitButton;
    @FXML private Rectangle feedbackRect;
    @FXML private Label resultLabel;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// obtain all the words from all the word list
		this.words = FileIO.getAllWordsFromWordsDirectory();
		
		// choose a random word and remove from the word list
		nextWord();
		readCurrentWord();
		updateLetterCount();
		
		// set the slider action - change the speed of speech
		this.speedSlider.valueProperty().addListener(c ->{
			this.speedOfSpeech = 0 - (this.speedSlider.getValue());
			String roundedSpeed= String.format("Speed: x%.1f", 1/speedOfSpeech);
			if(roundedSpeed.equals("1.0")) {
				this.speedLabel.setText(roundedSpeed+ " (default)");
			} else {
				this.speedLabel.setText(roundedSpeed);
			}
		});
		
		// get the caret position 
		textField.caretPositionProperty().addListener(c -> {
			if (textField.isFocused()) {
				this.lastRecordedCaretPosition = textField.getCaretPosition();
			}
		});

		// hide help pane to start off with
		macronInfo.setVisible(false);
	}
	
	/*
	 * This is the action for the submit button
	 */
	public void submit(ActionEvent e) {
		
		PracticeController.userAnswer = this.textField.getText();
		
		// if the user gets the word correct, note that this could be either the first or the second attemp
		if (isAnswerCorrect()) {
			this.attempts = 1;
			FileIO.openGeneralWavFile("correct");
			PracticeController.isCorrect = true;
			this.switchScene("PracticeComplete", e);
			
		// if the user gets the word wrong for the first time
		} else if (this.attempts == 1) {
			FileIO.openGeneralWavFile("wrong");
			this.attempts++;
			feedbackRect.setFill(Color.web("#f87676"));
			resultLabel.setText(INCORRECT_MESSAGE);
			this.readCurrentWord();
			this.giveHint();
			
		// if the user gets the word wrong for the second time
		} else {
			FileIO.openGeneralWavFile("wrong");
			PracticeController.isCorrect = false;
			this.switchScene("PracticeComplete", e);
		}
	}
	
	/*
	 * Pressing enter on the keyboard is the same as submit action
	 */
	public void keyPressed(KeyEvent e) throws IOException, InterruptedException {
		if (e.getCode() == KeyCode.ENTER) {
			ActionEvent event = new ActionEvent(this.submitButton, this.submitButton);
			this.submit(event);
		}
	}
	
	/*
	 * This is the action for the hear again button
	 */
	public void hearAgain() {
		readCurrentWord();
	}
	
	/*
	 * This is the action to skip the current word
	 */
	public void idk(ActionEvent e) {
		
		// play the animation - the text "skipped" fades out
		resultLabel.setText("Skipped");
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(3000), resultLabel);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0);	
		fadeTransition.setOnFinished(event -> {
			resultLabel.setText("");
			resultLabel.setOpacity(1.0);
		});
		fadeTransition.play();
		feedbackRect.setFill(Color.web("#d0d0d0"));		
		// get the next word, read it and show the letter counts
		this.nextWord();
		this.readCurrentWord();
		this.updateLetterCount();
	}
	
	/*
	 * This method allows user to add macron in the answer
	 */
	public void addMacronisedVowel(ActionEvent event) {
		textField.requestFocus();
		textField.insertText( lastRecordedCaretPosition, ((Button)event.getSource()).getText());
		textField.positionCaret(lastRecordedCaretPosition);
	}
	
	/*
	 * This method resets the speed of speech
	 */
	public void resetSpeed() {
		this.speedSlider.setValue(-1.0);
	}
	
	/*
	 * This method leads the user back to the home screen
	 */
	public void returnHome(ActionEvent e) {
		switchScene("Main", e);
	}
	
	/*
	 * Read the current word in a new thread
	 */
	private void readCurrentWord() {
		new Thread(new WordPlayer(PracticeController.currentWord, speedOfSpeech, true)).start();
		// developer feature
		//System.out.println(PracticeController.currentWord);
	}
	
	/*
	 * Based on the current word, generate the underscores as letter count
	 */
	private void updateLetterCount() {
		StringBuilder sb = new StringBuilder();
		
		// the max number of characters that can be displayed on one line
		// note that this is not the max number of underscores
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
	
	/*
	 * THis is method access the user's answer in the text field and returns true if it is correct
	 */
	private boolean isAnswerCorrect() {
		boolean b = this.textField.getText().trim().equalsIgnoreCase(currentWord);
		this.textField.clear();
		return b;
	}
	
	/*
	 * Get the next word
	 * This method is used when the user skip the current one - user gets a new word if he/she doesn't know
	 */
	private void nextWord() {
		int wordIndex = new Random().nextInt(this.words.size());
		currentWord = this.words.get(wordIndex);
//		this.words.remove(wordIndex);
	}
	
	/*
	 * This method provides hint to user if they get the word wrong in the first time
	 */
	private void giveHint() {
		
		// the ratio of the letters that will be displayed
		double displayRatio = 0.5;
		
		// blank space will not be displayed as hint
		String temp = PracticeController.currentWord.replace(" ", "");
		int letterCount = (int)(temp.length() * displayRatio);
		
		// generate random letters that will be displayed
		// note that, there is no repeated elements in Set
		Set<Integer> indexes = new HashSet<Integer>();
		Random random = new Random();
		char[] letters = PracticeController.currentWord.toCharArray();
		while (indexes.size() < letterCount) {
			int i = random.nextInt(PracticeController.currentWord.length());
			if (letters[i] != ' ') {
				indexes.add(i);
			}
		}
		
		// re-build the underscores
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
		
		// display the newly generated underscores to the user
		this.hintLabel.setText(new String(hint));
	}
	
	/*
	 * This method is used to switch between different scenes
	 */
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
	

	/*
	 * Tells the user how to user macron button
	 */
	public void showInfo() {
		if(macronInfo.isVisible()) {
			macronInfo.setVisible(false);
		} else {
			macronInfo.setVisible(true);
		}
	}

}
