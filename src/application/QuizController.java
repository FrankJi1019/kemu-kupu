package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Random;

public class QuizController implements Initializable {
	
	private Stage stage;
	private Scene scene;
	
	@FXML
	private Label scoreLabel;
	@FXML
	private Label wordCountLabel;
	@FXML
	private Label letterCountLabel;
	@FXML
	private Label resultLabel;
	@FXML
	private Label speedLabel;
	@FXML
	private Slider speedSlider;
	@FXML
	private TextField userAnswerTextField;
	
	// all words from file
	private static List<String> allWords;
	private List<String> words = new ArrayList<String>();
	
	private double score = 0;
	private int wordLetterCount = -1;
	private List<Word> wordStats = new ArrayList<Word>();
	
	private int attemptTimes = 1;
	
	private double speedOfSpeech = 1;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// initialize a changeListener for the slider
		this.speedSlider.valueProperty().addListener(c ->{
			this.speedOfSpeech = this.speedSlider.getValue();
			String roundedSpeed= String.format("%.2f", speedOfSpeech);
			if(roundedSpeed.equals("1.00")) {
				this.speedLabel.setText(roundedSpeed+ " (default)");
			} else {
				this.speedLabel.setText(roundedSpeed);
			}

		});
		
		
		
		// get the five words
		Random random = new Random();
		if (allWords.size() <= 5) {
			words = allWords;
		}
		
		while (this.words.size() < 5) {
			if (QuizController.allWords.size() <= 5) break;
			String word = QuizController.allWords.get(random.nextInt(QuizController.allWords.size()));
			if (!this.words.contains(word)) {
				this.words.add(word);
			}
		}
		
		// init the letter count
		wordLetterCount = this.words.get(0).length();
		int temp = wordLetterCount;
		
		
		// set word letter count
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < temp; i++) {
			if (this.words.get(0).charAt(i) == ' ') {
				sb.append("  ");
				wordLetterCount--;
			} else {
				sb.append("_ ");
			}
		}
		letterCountLabel.setText(String.format("%s(%d letters)", sb.toString(), wordLetterCount));
		
		// set which number is being tested
		wordCountLabel.setText(Integer.toString(6 - this.words.size()));
		
		// speak the word
		new Thread(new MyRunnable(this.words.get(0), speedOfSpeech)).start();
		
	}
	
	public static void setWords(List<String> words) {
		QuizController.allWords = words;
	}
	
	public void hearAgain() {
		// FileIO.openWavFile();
		new Thread(new MyRunnable(this.words.get(0), speedOfSpeech)).start();
	}
	
	public void submit(ActionEvent e) throws IOException {
		
		String userAnswer = userAnswerTextField.getText();
		
		// if user gets it correct (could be the 1st time or the 2nd time)
		if (this.checkWordMatch(userAnswer)) {
			this.wordStats.add(new Word(this.words.get(0), (double)1 / this.attemptTimes));
			score = score + (double)1 / this.attemptTimes;
			this.attemptTimes = 1;
			this.words.remove(0);
			if (this.words.size() == 0) {
				this.switchToComplete(e);
				return;
			}
			resultLabel.setText("Correct");
			// FileIO.speakMaori(this.words.get(0), 1);
			new Thread(new MyRunnable(this.words.get(0), speedOfSpeech)).start();
			
			scoreLabel.setText(Double.toString(score));
			
			// init the letter count
			wordLetterCount = this.words.get(0).length();
			int temp = wordLetterCount;
			
			
			// set word letter count
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < temp; i++) {
				if (this.words.get(0).charAt(i) == ' ') {
					sb.append("  ");
					wordLetterCount--;
				} else {
					sb.append("_ ");
				}
			}
			letterCountLabel.setText(String.format("%s(%d letters)", sb.toString(), wordLetterCount));
			
			// set which number is being tested
			wordCountLabel.setText(Integer.toString(6 - this.words.size()));
			
		// user gets wrong in the 2nd time
		} else if (this.attemptTimes == 2) {
			this.wordStats.add(new Word(this.words.get(0), score = 0));
			this.attemptTimes = 1;
			this.words.remove(0);
			if (this.words.size() == 0) {
				this.switchToComplete(e);
				return;
			}
			resultLabel.setText("Incorrect");
			new Thread(new MyRunnable(this.words.get(0), speedOfSpeech)).start();
			
			// init the letter count
			wordLetterCount = this.words.get(0).length();
			int temp = wordLetterCount;
			
			// set word letter count
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < temp; i++) {
				if (this.words.get(0).charAt(i) == ' ') {
					sb.append("  ");
					wordLetterCount--;
				} else {
					sb.append("_ ");
				}
			}
			letterCountLabel.setText(String.format("%s(%d letters)", sb.toString(), wordLetterCount));
			
			// set which number is being tested
			wordCountLabel.setText(Integer.toString(6 - this.words.size()));
			
		// user gets wrong in the 1st time
		} else {
			char c = this.words.get(0).charAt(1);
			int index = 2;
			if (c == ' ') {
				c = this.words.get(0).charAt(2);
				index = 4;
			}
			
			String s = letterCountLabel.getText();
			char[] chars = s.toCharArray();
			chars[index] = c;
 			letterCountLabel.setText(new String(chars));
			
			this.attemptTimes++;
			resultLabel.setText("Incorrect");
			// FileIO.openWavFile();
			new Thread(new MyRunnable(this.words.get(0), speedOfSpeech)).start();
		}
		
		this.userAnswerTextField.clear();
	}
	
	public void dontKnow(ActionEvent e) throws IOException {
		this.wordStats.add(new Word(this.words.get(0), score = 0));
		this.attemptTimes = 1;
		this.words.remove(0);
		if (this.words.size() == 0) {
			this.switchToComplete(e);
			return;
		}
		
		// FileIO.speakMaori(this.words.get(0), 1);
		new Thread(new MyRunnable(this.words.get(0), 1)).start();
		
		
		// init the letter count
		wordLetterCount = this.words.get(0).length();
		int temp = wordLetterCount;
		
		
		// set word letter count
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < temp; i++) {
			if (this.words.get(0).charAt(i) == ' ') {
				sb.append("  ");
				wordLetterCount--;
			} else {
				sb.append("_ ");
			}
		}
		letterCountLabel.setText(String.format("%s(%d letters)", sb.toString(), wordLetterCount));
		
		// set which number is being tested
		wordCountLabel.setText(Integer.toString(6 - this.words.size()));
		
		// clear , because we skipped
		resultLabel.setText("");
		
	}
	
	public void keyPressed(KeyEvent e) throws IOException {
		if (e.getCode() == KeyCode.ENTER) {
			ActionEvent event = new ActionEvent();
			this.submit(event);
		}
	}

	public boolean checkWordMatch(String userAnswer) {
		return userAnswer.trim().equalsIgnoreCase(this.words.get(0)); // trim removes whitespace on ends
	}
	
	public void switchToComplete(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Completed.fxml"));
		Parent root = loader.load();
		
		CompletedController controller = loader.getController();
		controller.setDataToTable(this.wordStats);
		
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}



