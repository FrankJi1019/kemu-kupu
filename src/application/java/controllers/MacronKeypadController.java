package application.java.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class MacronKeypadController {

	private TextField textField;
	private int lastRecordedCaretPosition = 0;
	@FXML private AnchorPane macronInfo;
	@FXML private Button infoButton;

	
	public void initialise(TextField textField) {
		this.setTextField(textField);
		addCaretListener();
		addShortcutKeyListener();
		// hide help pane to start off with
		macronInfo.setVisible(false);
		
	}
	private void addCaretListener() {
		// get the caret position 
		textField.caretPositionProperty().addListener(c -> {
			if (textField.isFocused()) {
				this.lastRecordedCaretPosition = textField.getCaretPosition();
			}
		});
		
	}

	// sets one character to the left of caret position to macronised Vowel, if it is
			// a vowel already, by pressing the left ALT key on keyboard.
	private void addShortcutKeyListener() {
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				// convert text field String into char array.
				char[] textFieldToChars = textField.getText().toCharArray();

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
	}
	
	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	public boolean isTextFieldEmpty() {
		if(textField == null) {
			System.err.println("You didn't initialise the textField");
			return true;
		}
		return false;
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
		textField.setText(textFieldCharsToString);
		// set caret position to current position.
		textField.positionCaret(index+1);
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
	 * Tells the user how to user macron button
	 */
	public void showInfo() {
		if(macronInfo.isVisible()) {
			macronInfo.setVisible(false);
			infoButton.setText("?");
		} else {
			macronInfo.setVisible(true);
			infoButton.setText("X");
		}
	}
	
	

}
