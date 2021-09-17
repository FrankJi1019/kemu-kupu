package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


// this is the class for passing in different linux commands;
public class LinuxCommand {

	public static List<String> executeCommand(String command){
		
		List<String> list = new ArrayList<>();
		
		try {
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					list.add(line);
					
				}
			} else {
				while ((stderr.readLine()) != null) {
				}
			}
			
			} catch (Exception e) {
			e.printStackTrace();
			}
	
		return list;
	}
	
	
	
	public static int getErrorCode(String command) {
		int errorCode = -1;
		
	try {
		
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

		Process process = pb.start();

		int exitStatus = process.waitFor();
		
		if (exitStatus == 0) {
			errorCode = 0;
		} else {
			errorCode = 1;
		}
		 
		} catch (Exception e) {
		e.printStackTrace();
		}
	
	return errorCode;
	}
	

	public static List<String> getTopic(){
		
		List<String> temp = new ArrayList<>();
		
		if ((LinuxCommand.getErrorCode("test -d words")) == 1) {
			LinuxCommand.executeCommand("mkdir ./words");
			// can rework error message to display on GUI.
			System.out.println("Directory doesn't exist, created new directory");
			//
			return temp;
		} else {
			
			temp = executeCommand("ls ./words | cut  -d \".\" -f -1");
			
			//check if there are any file in directory.
			if (temp.isEmpty()) {
				//can rework error message to display on GUI.
				System.out.println("There are no wordlists in the directory.");
				return temp;
			} else {
				return temp;
			}
			
		}
	}
	
	public static List<String> getFileNameFromDirectory(String dir){

		List<String> temp = new ArrayList<>();
		
		if ((LinuxCommand.getErrorCode("test -d"+dir)) == 1) {
			// can rework error message to display on GUI.
			System.out.println("Directory doesn't exist");
			//
			return temp;
		} else {
			
			temp = executeCommand("ls "+dir+" | cut  -d \".\" -f -1");
			
			//check if there are any file in directory.
			if (temp.isEmpty()) {
				//can rework error message to display on GUI.
				System.out.println("There are no files in the directory.");
				return temp;
			} else {
				return temp;
			}
			
		}
	}
	
}
