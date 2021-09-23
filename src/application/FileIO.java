package application;

import java.util.ArrayList;
import java.util.List;

/** 
 *  this class features any commands that are required file Input/Output.
 */
public class FileIO {

	// directory constants
	private static String WAVE_DIRECTORY = "./data/sounds/";
	
	// Method openWavFile deleted because it is not used.
	
	
	/** this method open and play filename.wav file from ./data/sounds folder.
	 * 	when String filename are passed in.
	 * 
	 * @param filename
	 */
	
	public static String openGeneralWavFile(String filename) {
		String temp;
		
		// testing if file exists, if yes play the wav, if not return String that "no 
		// such file exists"
		if ((LinuxCommand.getErrorCode("test -f "+WAVE_DIRECTORY+filename+".wav")) == 0) {
			LinuxCommand.executeCommand("aplay "+WAVE_DIRECTORY+filename+".wav");
			return null;
		} else {
			temp = "no such file exists";
			return temp;
		}
	}
	
	//  Method deleteWavFile deleted because it is not used.
	
	
	/**
	 * Method for speakingMaori word giving string as word to speak and int as speed for the speech.
	 */
	
	public static void speakMaori(String word, double speed) {
		
		//default speed is 1.0 
		//slow: 2.0
		//fast: 0.5
		
		// create scm file for festival
		LinuxCommand.executeCommand("touch ./data/Maori.scm");
		
		// passing speaking parameter into scm file
		LinuxCommand.executeCommand("echo \"(voice_akl_mi_pk06_cg)\" >> ./data/Maori.scm");
		LinuxCommand.executeCommand("echo \"(Parameter.set 'Duration_Stretch " + speed + ")\" >> ./data/Maori.scm");
		LinuxCommand.executeCommand("echo \"(utt.wave (SayText " + "\\" + "\"" + word + "\\" + "\") " + "\\" + "\"" + 
						"./data/Maori.wav" + "\\" + "\"" + " 'riff)" + "\"" + " >> ./data/Maori.scm");
		
		// can be reinstated if need to be saved as a wave file.
		//LinuxCommand.executeCommand("echo \"(utt.save.wave (SayText " + "\\" + "\"" + word + "\\" + "\") " + "\\" + "\"" + 
		//		"./data/Maori.wav" + "\\" + "\"" + " 'riff)" + "\"" + " >> ./data/Maori.scm");
		
		//speak word from scm file
		LinuxCommand.executeCommand("festival -b ./data/Maori.scm");
		//remove scm file
		LinuxCommand.executeCommand("rm -f ./data/Maori.scm");
		
		
		// Dev feature only
		//System.out.println(word);
	}
	
	
	/**method to speak English given a string as a word.
	 * 
	 * @param word
	 */
	public static void speakEnglish(String word) {
		
		
		LinuxCommand.executeCommand("touch ./data/English.scm");
		
		// passing parameter into scm file
		LinuxCommand.executeCommand("echo \"(voice_cmu_us_slt_cg)\" >> ./data/English.scm");
		LinuxCommand.executeCommand("echo \"(SayText " + "\\" + "\"" + word + "\\" + "\")" + "\"" + " >> ./data/English.scm");
		//speak word from scm file
		LinuxCommand.executeCommand("festival -b ./data/English.scm");
		//remove scm file
		LinuxCommand.executeCommand("rm -f ./data/English.scm");
		
	}
	
	
	/** Method to cat each line from file and output into a list.
	 * 
	 * @return List
	 */
	public static List<String> getContentFromFile (String fileName){
		List<String> temp = new ArrayList<>();
		
		// testing if file exists, if yes then cat words to list, if not return String that "no 
		// such file exists"
		if ((LinuxCommand.getErrorCode("test -f ./words/"+fileName+".txt")) == 0) {
			temp = LinuxCommand.executeCommand("cat ./words/"+fileName+".txt");
			return temp;
			
		} else {
			//can rework this bit to display on GUI.
			System.out.println("No such file exists!");
			return temp;
		}
	}
	
	
}
