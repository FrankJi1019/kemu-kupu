package application.java.models;

public class User {
	
	String username;
	int score;
	
	public User(String s, int i) {
		this.username = s;
		this.score = i;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getScore() {
		return this.score;
	}

}
