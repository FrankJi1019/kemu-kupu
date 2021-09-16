package application;

public class FileIO {

	public static String openWavFile() {
		String temp;
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
	
}
