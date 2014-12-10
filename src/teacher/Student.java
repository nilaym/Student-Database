package teacher;
import java.util.ArrayList;

public class Student {
	private String name;
	private ArrayList <Integer> scores;
	
	public Student(){
		name = "";
		scores = new ArrayList <Integer> (0);
	}
	
	public Student(String name, int...scores){
		this.name = name;
		this.scores = new ArrayList <Integer> (scores.length);
		for (int score : scores){
			this.scores.add(score);
		}
	}
	
	
	public void setName(String nm){
		name = nm;
	}
	
	public String getName(){
		return name;
	}
	
	public void setScore(int i, int score){
		scores.set(i, score);
	}
	
	public int getScore(int i){
		return scores.get(i);
	}
	
	public void addScore(int score){
		scores.add(score);
	}
	
	public int[] getScores(){
		int[] allScores = new int [scores.size()];
		for (int i = 0; i < scores.size(); i++){
			allScores[i] = scores.get(i);
		}
		return allScores;
	}
	
	public int getNumOfScores(){
		return scores.size();
	}
	
	public int getAverage(){
		int sum = 0;
		for (int score : scores){
			sum += score;
		}
		
		int average = (int) Math.round((double)sum/scores.size());
		return average;
	}
	
	public int getHighScore(){
		int highscore = 0;
		for (int score : scores){
			if (score > highscore)
				highscore = score;
		}
		
		return highscore;
	}
	
	public String toString(){
		String str;
		str = "Name: " + name + "\n" + "Scores: ";
		for (int score : scores){
			str += score + "  ";
		}
		
		str += "\n" + "Average: " + getAverage();
			
		return str;
	}
}
