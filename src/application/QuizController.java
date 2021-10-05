package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import java.util.Random;

public class QuizController implements Initializable {
	
	// these are used to switch scene
	private Stage stage;
	private Scene scene;
	
	@FXML
	private Label scoreLabel;
	@FXML
	private Label wordCountLabel;
	@FXML
	private Label letterCountLabel;
	@FXML
	private Label letterNumberLabel;
	@FXML
	private Label resultLabel;
	@FXML
	private Label speedLabel;
	@FXML
	private Slider speedSlider;
	@FXML
	private TextField userAnswerTextField;
	@FXML
	private Button submitButton;
	@FXML
	private Button hearAgainButton;
	@FXML
	private Button idkButton;
	@FXML
	private Button nextButton;
	
	// this is a list of all words in the file
	private static List<String> allWords;
	
	private static String CORRECT_MESSAGE = "Correct";
	private static String FIRST_INCORRECT_MESSAGE = "Incorrect, Try Again";
	private static String SECOND_WRONG_MESSAGE = "Wrong Again";
	private static String SKIPPED_MESSAGE = "Skipped";
	
	private static int startTimer;
	private static int endTimer;
	
	private static boolean isInNextButtonScene;
	
	// This is a list of five words that will be tested
	private List<String> testWords = new ArrayList<String>();
	
	private double score = 0;
	
	// This int stores how many letters are in the word that is currently being assessed
	private int wordLetterCount = -1;
	
	// This records the statistics of the user answers, will be passed to complete screen to display
	private List<Word> wordStats = new ArrayList<Word>();
	
	// records how many times that the user has attempted, recall that the user have max 2 attemps
	private int attemptTimes = 1;
	
	// the speed of word being read out
	private double speedOfSpeech = 1;
	
	// the score reduction time in millisecond for every 0.01 point 
	private static int TIME_MS_EVERY_POINT_01_DEDUCTED = 500;
	

	/*
	 * This is method is call when a controller instance has been created
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
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
		
		// get the five words that will be tested, but if there are less than 5 words in the file
		// then put all the word in the list
		nextButton.setVisible(false);
		
		Random random = new Random();
		if (allWords.size() <= 5) {
			testWords.addAll(allWords);
		}
		while (this.testWords.size() < 5) {
			if (QuizController.allWords.size() <= 5) break;
			String word = QuizController.allWords.get(random.nextInt(QuizController.allWords.size()));
			if (!this.testWords.contains(word)) {
				this.testWords.add(word);
			}
		}
		
		// tell the user how many letters are in the word
		this.setWordAndLetterCount();
		
		// set which number is being tested, in the initialize method, it is always the first one
		wordCountLabel.setText(Integer.toString(6 - this.testWords.size()));
		
		// speak the word in another thread so it won't freezes the window
		new Thread(new WordPlayer(this.testWords.get(0), speedOfSpeech, true)).start();
		
		//set isInNextButtonScene to false
		isInNextButtonScene = false;
		
		startTimer = (int) System.currentTimeMillis();
		
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
		new Thread(new WordPlayer(this.testWords.get(0), speedOfSpeech, false)).start();
	}
	
	/*
	 * This is the action for submit button
	 */
	public void submit(ActionEvent e) throws IOException, InterruptedException {
		
		endTimer = (int) System.currentTimeMillis();
		//System.out.println(endTimer-startTimer);
		
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
			
			// no matter this is the 1st or 2nd try, this word has been completed, so add this word to 
			// statistics, update score and reset the number of attempts
			
			double thisRoundScore = scoreCalculation(wordLetterCount, startTimer, endTimer);
			double finalThisRoundScore = thisRoundScore / this.attemptTimes;
			String finalThisRoundScoreString = String.format("%.2f", finalThisRoundScore);
			finalThisRoundScore = Double.parseDouble(finalThisRoundScoreString);
			this.wordStats.add(new Word(this.testWords.get(0), finalThisRoundScore));
			score = score + finalThisRoundScore;
			String finalScore = String.format("%.2f", score);
			score = Double.parseDouble(finalScore);
			this.attemptTimes = 1;
			
			// remove the word that has finished, this means the first word in the list is the next word
			this.testWords.remove(0);
			
			// tell the user the result of their submit in the label, also play a sound to let them know
			resultLabel.setText(CORRECT_MESSAGE);
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
			scoreLabel.setText(Double.toString(score));
			
		// user gets wrong in the 2nd time
		} else if (this.attemptTimes == 2) {
			
			// the user only has two attempts so the current word has been completed, so add it to 
			// the statistics and update the attempt times
			// note that in this case there is no need to update the score
			this.wordStats.add(new Word(this.testWords.get(0), 0));
			this.attemptTimes = 1;
			
			// move to the next word
			this.testWords.remove(0);
			
			resultLabel.setText(SECOND_WRONG_MESSAGE);
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
			
			
		// user gets wrong in the 1st time, in this case, the user have another change
		} else {
			
			// show the hint, for assignment 3 the hint is to display the second letter
			this.showHint();
			
			this.attemptTimes++;
			
			// inform the user the result of there submit and play the word again
			resultLabel.setText(FIRST_INCORRECT_MESSAGE);
			FileIO.openGeneralWavFile("wrong");
			new Thread(new WordPlayer(this.testWords.get(0), speedOfSpeech, true)).start();
			
			//restart timer
			startTimer = (int) System.currentTimeMillis();
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
		this.wordStats.add(new Word(this.testWords.get(0), 0));
		this.attemptTimes = 1;
		
		// move to the next word if exists, otherwise switch to complete screen
		this.testWords.remove(0);
		if (this.testWords.size() == 0) {
			this.switchToComplete(e);
			return;
		}
		
		
		// set result label
		resultLabel.setText(SKIPPED_MESSAGE);
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
		if ((e.getCode() == KeyCode.ENTER) && (isInNextButtonScene == false)) {
			ActionEvent event = new ActionEvent(this.submitButton, this.submitButton);
			this.submit(event);
		} else {
			
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
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Completed.fxml"));
		Parent root = loader.load();
		
		// passing data
		CompletedController controller = loader.getController();
		controller.setData(this.wordStats);
		System.out.println(this.wordStats.toString());
		
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
		wordCountLabel.setText(Integer.toString(6 - this.testWords.size()));
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
		userAnswerTextField.setText(userAnswerTextField.getText() + ((Button)event.getSource()).getText());
		userAnswerTextField.positionCaret(userAnswerTextField.getText().length());
	}
	
	
	/**
	 * this method hide all other buttons and shows next button
	 */
	public void hideAllButtonsShowNextButton() {
		submitButton.setVisible(false);
		hearAgainButton.setVisible(false);
		idkButton.setVisible(false);
		nextButton.setVisible(true);
	}
	
	/**
	 * this method hide next button and shows all other button
	 */
	
	public void showAllButtonsHideNextButton() {
		submitButton.setVisible(true);
		hearAgainButton.setVisible(true);
		idkButton.setVisible(true);
		nextButton.setVisible(false);
	}
	
	
	/**
	 * this method resets the speed slider to default
	 */
	public void resetSpeedToDefault() {
		speedSlider.setValue(-1.00);
	}
	
	public void switchToNextWord(ActionEvent event) {
		
		
		
		userAnswerTextField.setVisible(true);
		
		// reset the timer for next word.
		startTimer = (int) System.currentTimeMillis();
		//System.out.println(endTimer-startTimer);
		
		//clear result label
		resultLabel.setText("");
		
		// play the next word
		new Thread(new WordPlayer(this.testWords.get(0), speedOfSpeech, true)).start();
					
		// update the score and letter count
		this.setWordAndLetterCount();
		
		// show all other buttons again
		showAllButtonsHideNextButton();
		
		isInNextButtonScene = false;
		
	}
	
	
	/**
	 * this method clears any unwanted fields after showing NEXT button
	 */
	public void clearFieldsAfterSubmit() {
		letterCountLabel.setText("");
		userAnswerTextField.setVisible(false);
	}
	
	
	/**
	 * 
	 * this method calculate score according how long user took to answer the question.
	 */
	public double scoreCalculation(int wordLength,int startTime,int endTime) {
		
		// speaking time(in seconds):       time = 0.1274(word_length) + 1.1665s
		// user typing time(in seconds):    time = 0.4(word_length) + 0.8s
		
		int durationMs = endTime-startTime;
		
		int speakingTime = 127*wordLength + 1166;
		int typingTime = 400*wordLength + 800;
		int thinkingTime = durationMs - speakingTime - typingTime;
		//System.out.println(thinkingTime);
		double score = 1.0;
	
		int scoreDeductionRate = thinkingTime/TIME_MS_EVERY_POINT_01_DEDUCTED;
		if (scoreDeductionRate <= 0) {
			scoreDeductionRate = 0;
		}
		if (scoreDeductionRate > 50) {
			scoreDeductionRate = 50;
		}
		
		score = score - 0.01*scoreDeductionRate;
		
		return score;
	}
	
	
	
}



