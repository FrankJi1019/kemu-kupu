package application.java.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/** 
 *  this class features any commands that are required file Input/Output.
 */
public class FileIO {

	// directory constants
	private static String WAVE_DIRECTORY = "./data/sounds/";
	private static String GAMELOG_DIRECTORY = "./data/logs/";

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


	/**
	 * Method to obtain every single word from the words directory and store into List in java
	 * @return List
	 */
	public static List<String> getAllWordsFromWordsDirectory(){

		// obtain all the words from all the word list
		List<String> wordFiles = new ArrayList<>();
		List<String> words = new ArrayList<>();
		wordFiles = LinuxCommand.executeCommand("ls ./words");

		for (String file: wordFiles) {
			words.addAll(FileIO.getContentFromFile(file.replace(".txt", "")));
		}

		return words;
	}
	
	public static void saveGame(HashMap<String,Integer> scoreBoard) {
		LinuxCommand.executeCommand("rm -f "+GAMELOG_DIRECTORY+".gameLog.txt");
		LinuxCommand.executeCommand("touch "+GAMELOG_DIRECTORY+".gameLog.txt");
		
		for (String playerName: scoreBoard.keySet()) {
			int playerScore = scoreBoard.get(playerName);
			
			String writeOut = (playerName + "," + playerScore);
			LinuxCommand.executeCommand("echo "+ writeOut + " >> "+GAMELOG_DIRECTORY+ ".gameLog.txt");
		}
	}
	
	public static HashMap<String,Integer> loadGame() throws InterruptedException, IOException{
		HashMap<String,Integer> scoreBoard = new HashMap<>();
		String command;
		
		if ((LinuxCommand.getErrorCode("cat "+GAMELOG_DIRECTORY+".gameLog.txt")) == 0){
			command = "cat "+GAMELOG_DIRECTORY+".gameLog.txt";
		} else {
			LinuxCommand.executeCommand("touch "+GAMELOG_DIRECTORY+".gameLog.txt");
			return scoreBoard;
		}
		
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		Process process = pb.start();
			
		BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
		int exitStatus = process.waitFor();
			
		if (exitStatus == 0) {
			String line;
			while ((line = stdout.readLine()) != null) {
				List<String> separate = new ArrayList<>(Arrays.asList(line.split(",")));
				String userName = separate.get(0);
				int userScore = Integer.parseInt(separate.get(1));
				scoreBoard.put(userName, userScore);
			}
		}
		
		return sortByValue(scoreBoard);
		
	}
	
	
	// Attribution Credit: https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
	 public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
	    {
	        // Create a list from elements of HashMap
	        List<Map.Entry<String, Integer> > list =
	               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());
	 
	        // Sort the list
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
	            public int compare(Map.Entry<String, Integer> o1,
	                               Map.Entry<String, Integer> o2)
	            {
	                return (o2.getValue()).compareTo(o1.getValue());
	            }
	        });
	         
	        // put data from sorted list to hashmap
	        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
	        for (Map.Entry<String, Integer> aa : list) {
	            temp.put(aa.getKey(), aa.getValue());
	        }
	        return temp;
	    }
	 
	 
	// Attribution ends
	 
	 public static void deleteGame() {
		 LinuxCommand.executeCommand("rm -f "+GAMELOG_DIRECTORY+".gameLog.txt");
	 }

}
