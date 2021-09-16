package application;

public class FileIO {

	public static String openWavFile() {
		String temp;
		// magic number issue with directory (can be refactored later if needed)
		if ((LinuxCommand.getErrorCode("test -f ./data/Maori.wav")) == 0) {
			LinuxCommand.executeCommand("aplay ./data/Maori.wav");
			return null;
		} else {
			temp = "no such file exists";
			return temp;
		}
	}
	
	public static void deleteWavFile() {
		LinuxCommand.executeCommand("rm -f ./data/*.wav");
	}
	
	
	public static void speakMaori(String word, double speed) {
		
		// magic number issue with directory (can be refactored later if needed)
		LinuxCommand.executeCommand("touch ./data/Maori.scm");
		// magic number issue with directory (can be refactored later if needed)
		// passing parameter into scm file
		LinuxCommand.executeCommand("echo \"(voice_akl_mi_pk06_cg)\" >> ./data/Maori.scm");
		LinuxCommand.executeCommand("echo \"(Parameter.set 'Duration_Stretch " + speed + ")\" >> ./data/Maori.scm");
		LinuxCommand.executeCommand("echo \"(utt.save.wave (SayText " + "\\" + "\"" + word + "\\" + "\") " + "\\" + "\"" + 
				"./data/Maori.wav" + "\\" + "\"" + " 'riff)" + "\"" + " >> ./data/Maori.scm");
		//speak word from scm file
		LinuxCommand.executeCommand("festival -b ./data/Maori.scm");
		//remove scm file
		LinuxCommand.executeCommand("rm -f ./data/Maori.scm");
		
		//Wav file for Maori word are kept because it might needed to be replayed.
	}
	
	public static void speakEnglish(String word) {
		
		// magic number issue with directory (can be refactored later if needed)
		LinuxCommand.executeCommand("touch ./data/English.scm");
		// magic number issue with directory (can be refactored later if needed)
		// passing parameter into scm file
		LinuxCommand.executeCommand("echo \"(voice_cmu_us_slt_cg)\" >> ./data/English.scm");
		LinuxCommand.executeCommand("echo \"(SayText " + "\\" + "\"" + word + "\\" + "\")" + "\"" + " >> ./data/English.scm");
		//speak word from scm file
		LinuxCommand.executeCommand("festival -b ./data/English.scm");
		//remove scm file
		LinuxCommand.executeCommand("rm -f ./data/English.scm");
		
	}
	
	
	
	
}
