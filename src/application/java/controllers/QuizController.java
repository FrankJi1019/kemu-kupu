package application.java.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import application.java.models.FileIO;
import application.java.models.Word;
import application.java.models.WordPlayer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

import application.java.models.WordTimer;

public class QuizController implements Initializable {
	
	// these are used to switch scene
	private Stage stage;
	private Scene scene;
	
	@FXML private Label scoreLabel;
	@FXML private Label wordCountLabel;
	@FXML private Label letterCountLabel;
	@FXML private Label letterNumberLabel;
	@FXML private Label resultLabel;
	@FXML private Label speedLabel;
	@FXML private Slider speedSlider;
	@FXML private TextField userAnswerTextField;
	@FXML private Button submitButton;
	@FXML private Button hearAgainButton;
	@FXML private Button idkButton;
	@FXML private Button nextButton;
	@FXML private AnchorPane macronInfo;
	@FXML private Rectangle feedbackRect;
	@FXML private AnchorPane macronButtons;
	@FXML private Button infoButton;
	@FXML private Label addition;
	@FXML private Label totalWordCountLabel;
	@FXML private Label timerLabel;
	@FXML private ProgressBar scoreBar;
	
	// the list of buttons that will be disabled while a word is being read out
	private Button[] disableButtons = null;

	// this is a list of all words in the file
	private static List<String> allWords;
	
	// constants for displaying messages
	
	private static String CORRECT_MESSAGE = "Correct  " + new String(Character.toChars(0x1F603));
	private static String FIRST_INCORRECT_MESSAGE = "Not quite, have another go!";
	private static List<String> SECOND_INCORRECT_MESSAGE = new ArrayList<>(Arrays.asList(
			"Incorrect, don't worry, learning is a process...",
			"Incorrect, good luck next time!",
			"Incorrect, failure is the mother of success!",
			"Incorrect, don't give up, keep going!"));
	private static String SKIPPED_MESSAGE = "Word Skipped...";
	
	private static boolean isInNextButtonScene;
	
	
	// This is a list of five words that will be tested
	private List<String> testWords = new ArrayList<String>();
	
	private int score = 0;
	
	// This int stores how many letters are in the word that is currently being assessed
	private int wordLetterCount = -1;
	
	// This records the statistics of the user answers, will be passed to complete screen to display
	private List<Word> wordStats = new ArrayList<Word>();
	
	// records how many times that the user has attempted, recall that the user have max 2 attemps
	public static int attemptTimes = 1;
	
	// the speed of word being read out
	private double speedOfSpeech = 1;
	
	// the total amount of word assessed in this round
	private int totalWordsCount = -1;
	
	private WordTimer wordTimer = null;

	private int lastRecordedCaretPosition = 0;
	/*
	 * This is method is call when a controller instance has been created
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.scoreBar.setProgress(1);
		
		// define which button to disable when TTS system reads out word
		this.disableButtons = new Button[]{
			submitButton,
			hearAgainButton,
			idkButton,
			infoButton
		};
		
		// create a new timer instance
		this.wordTimer = new WordTimer(this.timerLabel, this.scoreBar);
		
		// display the speed of speech, clearly indicating whether the current speed is the default
		this.speedSlider.valueProperty().addListener(c ->{
			this.speedOfSpeech = 0 - (this.speedSlider.getValue());
			String roundedSpeed= String.format("%.2f", 1/speedOfSpeech);
			if(roundedSpeed.equals("1.00")) {
				this.speedLabel.setText(roundedSpeed+ "x "+ " (default)");
			} else {
				this.speedLabel.setText(roundedSpeed + "x ");
			}
			

		});
		
		// get the caret position 
		userAnswerTextField.caretPositionProperty().addListener(c -> {
			if (userAnswerTextField.isFocused()) {
				this.lastRecordedCaretPosition = userAnswerTextField.getCaretPosition();
		    }
		});
		
		// sets one character to the left of caret position to macronised Vowel, if it is
		// a vowel already, by pressing the left ALT key on keyboard.
		
		userAnswerTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				// convert text field String into char array.
				char[] textFieldToChars = userAnswerTextField.getText().toCharArray();

				if (event.getCode() == KeyCode.ALT) {
					if (lastRecordedCaretPosition > 0) {
						// set the one char left to the caret the selected char position.
						int charPosition = lastRecordedCaretPosition-1;
						char vowelChar = textFieldToChars[charPosition];
						setMacronWithKeyboard(vowelChar, charPosition, textFieldToChars);
					}
				}
			}
		});
		
		// get the five words that will be tested, but if there are less than 5 words in the file
		// then put all the word in the list
		nextButton.setVisible(false);
		macronInfo.setVisible(false);
		
		// check if wordlist size < 5 then add all words to current round
		Random random = new Random();
		if (allWords.size() <= 5) {
			testWords.addAll(allWords);
		}
		// if wordlist size > 5 then find 5 random non repeating words from it.
		while (this.testWords.size() < 5) {
			if (QuizController.allWords.size() <= 5) break;
			String word = QuizController.allWords.get(random.nextInt(QuizController.allWords.size()));
			if (!this.testWords.contains(word)) {
				this.testWords.add(word);
			}
		}
		
		this.totalWordsCount = this.testWords.size();
		this.totalWordCountLabel.setText(String.format("Word    of %d", this.testWords.size()));
		
		// tell the user how many letters are in the word
		this.setWordAndLetterCount();
		
		// set which number is being tested, in the initialize method, it is always the first one
		wordCountLabel.setText(Integer.toString(this.totalWordsCount + 1 - this.testWords.size()));
		
		// speak the word in another thread so it won't freezes the window
		this.timerLabel.setText("Score: 100");
		this.scoreBar.setProgress(1);
		new Thread(new WordPlayer(this.testWords.get(0), speedOfSpeech, true, this.disableButtons, wordTimer)).start();
		
		//set isInNextButtonScene to false
		isInNextButtonScene = false;
		
		//initialise score countdown to 100.
		WordTimer.finalScore = 100;
		
		
	}
	
	/*
	 * This method is used outside this class to pass the entire word list to this controller.
	 * Note that the list of allWords is needed in the initialize method which means this method must
	 * be executed before an instance of quiz controller being created, therefore it is a static method
	 * and the field to store all words in the file is a static field
	 */
	public static void setWords(List<String> words) {
		QuizController.allWords = words;
	}
	
	/*
	 * allow the user to hear the word again
	 * this is executed in another thread so that the main thread will not freeze
	 */
	public void hearAgain() {
		new Thread(new WordPlayer(this.testWords.get(0), speedOfSpeech, false, this.disableButtons)).start();
		
		// disable some buttons while the word is being read
//		this.toggleButtons(true);
//		while (WordPlayer.reading) {}
//		this.toggleButtons(false);
	}
	
	/*
	 * This is the action for submit button
	 */
	public void submit(ActionEvent e) throws IOException, InterruptedException {
		
		// get the user answer from the text field
		String userAnswer = userAnswerTextField.getText();
		
		// if the user did not enter anything and submit, only proceed after the user has confirmed 
		// they want to submit, otherwise do not submit
		if (userAnswer.isEmpty()) {
			if (!this.showMessageWhenNoAnswer()) {
				return;
			}
		}
		
		// if user gets it correct (could be the 1st time or the 2nd time)
		if (this.checkWordMatch(userAnswer)) {
			this.timerLabel.setText("Score: 100");
			this.scoreBar.setProgress(1);
			// stop the timer
			this.wordTimer.stop();
			
			// no matter this is the 1st or 2nd try, this word has been completed, so add this word to 
			// statistics, update score and reset the number of attempts
			

			int finalThisRoundScore = WordTimer.finalScore;
			this.wordStats.add(new Word(this.testWords.get(0), finalThisRoundScore, 
					attemptTimes == 1? Word.CORRECT_FIRST : Word.CORRECT_SECOND));

			score = score + finalThisRoundScore;
			
			attemptTimes = 1;
			
			// remove the word that has finished, this means the first word in the list is the next word
			this.testWords.remove(0);
			
			// tell the user the result of their submit in the label, also play a sound to let them know
			resultLabel.setText(CORRECT_MESSAGE);
			feedbackRect.setFill(Color.web("#00b24c"));
			FileIO.openGeneralWavFile("correct");
			hideAllButtonsShowNextButton();
			isInNextButtonScene = true;
			
			
			// if there is no next word, the program should switch complete screen
			if (this.testWords.size() == 0) {
				this.switchToComplete(e);
				return;
			}
			
			// clear dashes
			clearFieldsAfterSubmit();
	
			// update the score
			playScoreIncreaseAnimation(String.valueOf(finalThisRoundScore),String.valueOf(score));
			//scoreLabel.setText(Double.toString(score));
			
			WordTimer.finalScore = 100;
			
		// user gets wrong in the 2nd time
		} else if (attemptTimes == 2) {
			this.timerLabel.setText("Score: 100");
			this.scoreBar.setProgress(1);
			this.wordTimer.stop();
			
			// the user only has two attempts so the current word has been completed, so add it to 
			// the statistics and update the attempt times
			// note that in this case there is no need to update the score
			this.wordStats.add(new Word(this.testWords.get(0), 0, Word.INCORRECT));
			attemptTimes = 1;
			
			// move to the next word
			this.testWords.remove(0);
			
			setEncouragingMessage();
			feedbackRect.setFill(Color.web("#f87676"));
			FileIO.openGeneralWavFile("wrong");
			hideAllButtonsShowNextButton();
			isInNextButtonScene = true;
			
			// if there is no next word, then switch to the complete scene
			if (this.testWords.size() == 0) {
				this.switchToComplete(e);
				return;
			}
			
			// clear dashes
			clearFieldsAfterSubmit();
			
			WordTimer.finalScore = 100;
			
		// user gets wrong in the 1st time, in this case, the user have another change
		} else {
			
			// show the hint, for assignment 3 the hint is to display the second letter
			this.showHint();
			
			attemptTimes++;
			
			// inform the user the result of there submit and play the word again
			resultLabel.setText(FIRST_INCORRECT_MESSAGE);
			feedbackRect.setFill(Color.web("#f87676"));

			FileIO.openGeneralWavFile("wrong");
			this.wordTimer.stop();
			new Thread(new WordPlayer(this.testWords.get(0), speedOfSpeech, true, this.disableButtons, this.wordTimer)).start();
			this.timerLabel.setText("Score: 50");
			this.scoreBar.setProgress(0.5);
			
			// automatically set focus to the text field.
			userAnswerTextField.requestFocus();
			
			WordTimer.finalScore = 50;
		}
		
		// clear the result label that shows corrent, incorrect or skipped
		this.userAnswerTextField.clear();
		
		// this is just to let the system know to clear the label, will not clear immidiately
		//this.clearResultLabel();
		
	}
	
	/*
	 * action event for the don't know button
	 */
	public void dontKnow(ActionEvent e) throws IOException, InterruptedException {
		// if the user press dont know, it means the current has finished and the score for this word is 0
		this.wordStats.add(new Word(this.testWords.get(0), 0, Word.SKIP));
		attemptTimes = 1;
		
		this.wordTimer.stop();
		
		// move to the next word if exists, otherwise switch to complete screen
		this.testWords.remove(0);
		if (this.testWords.size() == 0) {
			this.switchToComplete(e);
			return;
		}
		
		
		// set result label and make rectangle grey
		resultLabel.setText(SKIPPED_MESSAGE);
		feedbackRect.setFill(Color.web("#d0d0d0"));

		hideAllButtonsShowNextButton();
		isInNextButtonScene = true;
		
		// clear dashes
		clearFieldsAfterSubmit();
		
		// will be executed after some time
		//this.clearResultLabel();
		
		// clear text field for user to enter the next word
		this.userAnswerTextField.clear();

	}
	
	/*
	 * This is the key board event that allows user to hit enter to submit answer
	 * simply invokes the submit method
	 */
	public void keyPressed(KeyEvent e) throws IOException, InterruptedException {
		if (WordPlayer.reading) return;
		if ((e.getCode() == KeyCode.ENTER) && (isInNextButtonScene == false)) {
			ActionEvent event = new ActionEvent(this.submitButton, this.submitButton);
			this.submit(event);
		} else if ((e.getCode() == KeyCode.ENTER) && (isInNextButtonScene == true)){
			ActionEvent event = new ActionEvent(this.nextButton,this.nextButton);
			this.switchToNextWord(event);
		}
	}
	

	/*
	 * This method checks whether user enters the right word
	 */
	public boolean checkWordMatch(String userAnswer) {
		// trim removes whitespace on ends
		return userAnswer.trim().equalsIgnoreCase(this.testWords.get(0)); 
	}
	
	/*
	 * This method will first pass user statistics to the complete screen and then switch to it
	 */
	public void switchToComplete(ActionEvent e) throws IOException, InterruptedException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/views/Completed.fxml"));
		Parent root = loader.load();
		
		// passing data
		CompletedController controller = loader.getController();
		controller.setData(this.wordStats);
		controller.setAnimation();
		
		//developer feature for testing
		//System.out.println(this.wordStats.toString());
		
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * Tell the user how the word is formatted using underscores: when there is a blank space
	 * and tell the user how many letters in total are in the word 
	 */
	private void setWordAndLetterCount() {
		// find the number of letters in the word,note that we delete the word whenever the user has
		// completed it (can be correct, incorrect, or skipped) so the first word of the testWOrds is 
		// always the current word being tested.
		// note that currentlt this count involves empty spaces
		wordLetterCount = this.testWords.get(0).length();
		int temp = wordLetterCount;
		
		// there can be maximum 47 characters in each line of underscores
		int maxStringSize = 47;
		
		// build the underscores, if it is a character then add a underscore, if it is a blank space
		// then add a blank space and minus one from the letter count.
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < temp; i++) {
			if (this.testWords.get(0).charAt(i) == ' ') {
				sb.append("  ");
				wordLetterCount--;
			} else {
				sb.append("_ ");
			}
		}
		String message = sb.toString();
		
		// if the underscores exceed the max length that a single line can hold, then switch to the
		// next line
		if (message.length() > maxStringSize) {
			char[] chars = message.toCharArray();
			for (int i = maxStringSize; i > 0; i--) {
				if (chars[i] == ' ' && chars[i-1] == ' ' && chars[i+1] != ' ') {
					chars[i] = '\n';
					break;
				}
			}
			message = new String(chars);
		}
		
		// set the two labels
		letterCountLabel.setText(message);
		letterNumberLabel.setText(String.format("(%d letters)", wordLetterCount));
		
		// set which number is being tested
		wordCountLabel.setText(Integer.toString(this.totalWordsCount + 1 - this.testWords.size()));
	}
	
	/*
	 * if the user enters a wrong on the first try, display the second letter of the word as a hint
	 */
	private void showHint() {
		
		// get what the second letter is, if the second character if a space then display the third char
		char c = this.testWords.get(0).charAt(1);
		int index = 2;
		if (c == ' ') {
			c = this.testWords.get(0).charAt(2);
			index = 4;
		}
		
		// re-build the string and set the underscores again
		String s = letterCountLabel.getText();
		char[] chars = s.toCharArray();
		chars[index] = c;
		letterCountLabel.setText(new String(chars));
	}
	
	/*
	 * When user wants to submit but no answer has been provided, always check with the user if 
	 * they really want to submit
	 * This method returns true if the user wants to submit and returns false if the user do not want
	 * to submit
	 */
	private boolean showMessageWhenNoAnswer() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("No answer provided");
		alert.setHeaderText("You have not entered your answer, are you sure you want to submit?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		}
		
		if (result.get() == ButtonType.CANCEL) {
			return false;
		}
		
		// this statement should never be reached because there are only two options in the message box
		return false;
	}
	
	/**
	 * This method uses a button to add the macronised vowel due to the textField being
	 * unable to read 'Alt + -'
	 */
	public void addMacronisedVowel(ActionEvent event) {
		userAnswerTextField.requestFocus();
		userAnswerTextField.insertText( lastRecordedCaretPosition, ((Button)event.getSource()).getText());
		userAnswerTextField.positionCaret(lastRecordedCaretPosition);

	}
	
	
	/**
	 * this method hide all other buttons and shows next button
	 */
	public void hideAllButtonsShowNextButton() {
		submitButton.setVisible(false);
		hearAgainButton.setVisible(false);
		idkButton.setVisible(false);
		nextButton.setVisible(true);
		macronButtons.setVisible(false);
		infoButton.setVisible(false);
		this.timerLabel.setVisible(false);
		this.scoreBar.setVisible(false);
	}
	
	/**
	 * this method hide next button and shows all other button
	 */
	
	public void showAllButtonsHideNextButton() {
		submitButton.setVisible(true);
		hearAgainButton.setVisible(true);
		idkButton.setVisible(true);
		nextButton.setVisible(false);
		macronButtons.setVisible(true);
		infoButton.setVisible(true);
		this.timerLabel.setVisible(true);
		this.scoreBar.setVisible(true);
	}
	
	
	/**
	 * this method resets the speed slider to default
	 */
	public void resetSpeedToDefault() {
		speedSlider.setValue(-1.00);
	}
	
	/**
	 * this method switch to the Next word when called.
	 */
	public void switchToNextWord(ActionEvent event) {
	
		userAnswerTextField.setVisible(true);
		
		//clear result label
		resultLabel.setText("");
		
		// Set the color of the feedback rectangle back to grey
		feedbackRect.setFill(Color.web("#d0d0d0"));
		
		// play the next word
		this.timerLabel.setText("Score: 100");
		this.scoreBar.setProgress(1);
		new Thread(new WordPlayer(this.testWords.get(0), speedOfSpeech, true, this.disableButtons, wordTimer)).start();
					
		// update the score and letter count
		this.setWordAndLetterCount();
		
		// show all other buttons again
		showAllButtonsHideNextButton();
		isInNextButtonScene = false;
		
		// automatically set focus to the text field.
		userAnswerTextField.requestFocus();
		
//		this.wordTimer.stop();
		
	}
	
	
	/**
	 * this method clears any unwanted fields after showing NEXT button
	 */
	public void clearFieldsAfterSubmit() {
		letterCountLabel.setText("");
		userAnswerTextField.setVisible(false);
	}
	

	/**
	 * this method tells the user how to use the macron button when the ? button is pressed.
	 */
	public void showInfo() {
		if(macronInfo.isVisible()) {
			macronInfo.setVisible(false);
		} else {
			macronInfo.setVisible(true);
		}
	}
	
	/**
	 * this method plays the Score Increase animation after each round of game.
	 * @param roundScoreString
	 * @param newScore
	 */
	public void playScoreIncreaseAnimation(String roundScoreString,String newScore) {
		this.addition.setText("+ " + roundScoreString);
		
		// Animation that shows what we're incrementing by
		FadeTransition fadeAddition = new FadeTransition(Duration.millis(1000), this.addition);
		fadeAddition.setFromValue(1.0);
		fadeAddition.setToValue(0);
		fadeAddition.setOnFinished(event -> {
			this.addition.setText("");
			this.addition.setOpacity(1);
		});
		
		// Animation that gives 'scaling' effect
		ScaleTransition scoreLabelScale= new ScaleTransition(Duration.millis(500), this.scoreLabel);
		
		scoreLabelScale.setByX(0.5);
		scoreLabelScale.setByY(0.5);	
		scoreLabelScale.setCycleCount(2);
		scoreLabelScale.setAutoReverse(true);
		
		// Animation gives an incrementing effect
		ParallelTransition incrementEffect = new ParallelTransition();
		incrementEffect.getChildren().addAll(fadeAddition,scoreLabelScale);
		
		// Play the whole score increment
		SequentialTransition scoreIncreaseAnimation = new SequentialTransition();
		PauseTransition pause = new PauseTransition(Duration.millis(1000));
		pause.setOnFinished(event -> {
			this.scoreLabel.setText(newScore);
		});
		scoreIncreaseAnimation.getChildren().addAll(pause, incrementEffect);
		scoreIncreaseAnimation.play();
	}
	
	/*
	 * This method leads the user back to the home screen
	 */
	public void returnHome(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../../resources/views/Main.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * This method sets random encouraging message when called.
	 */
	
	public void setEncouragingMessage() {
		Random rand = new Random();
		// select a random encouraging message from the message list.
		resultLabel.setText(SECOND_INCORRECT_MESSAGE.get((rand.nextInt(SECOND_INCORRECT_MESSAGE.size()))));
	}
	
	
	/**
	 * this method extract vowel at indicated index in the char array, replace it with macronised vowel
	 * and update in the text field.
	 * @param index
	 * @param textFieldToChars
	 * @param macronisedLetter
	 */
	
	public void replaceVowelToMacron(int index,char[] textFieldToChars,char macronisedLetter) {
		
		// replace vowel to macronised vowel.
		textFieldToChars[index] = macronisedLetter;
		// convert char array to String.
		String textFieldCharsToString = String.valueOf(textFieldToChars);
		userAnswerTextField.setText(textFieldCharsToString);
		// set caret position to current position.
		userAnswerTextField.positionCaret(index+1);
	}
	
	
	/**
	 * this is helper method that sets the Macronised vowel with keyboard.
	 * @param vowelChar
	 * @param charPosition
	 * @param textFieldToChars
	 */
	
	public void setMacronWithKeyboard(char vowelChar, int charPosition,char[] textFieldToChars) {
		switch(vowelChar) {
		case 'a':
		case 'A':
			replaceVowelToMacron(charPosition,textFieldToChars,'ā');
			break;
		case 'e':
		case 'E':
			replaceVowelToMacron(charPosition,textFieldToChars,'ē');
			break;
		case 'i':
		case 'I':
			replaceVowelToMacron(charPosition,textFieldToChars,'ī');
			break;
		case 'o':
		case 'O':
			replaceVowelToMacron(charPosition,textFieldToChars,'ō');
			break;
		case 'u':
		case 'U':
			replaceVowelToMacron(charPosition,textFieldToChars,'ū');
			break;
		}
	}
	
}



