package teacher;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GradeView extends JPanel implements ActionListener{
	private List<Student> students;
	private Student currentStudent; //student whose information is being currently displayed
	private JPanel topPanel, middlePanel, bottomPanel;
	private JButton saveButton, addNewButton, forwardButton, backButton, addScoreButton, getAverageButton, getClassAverageButton;
	private JTextField nameField;
	private List<JTextField> scoreFields;
	private boolean hasSaved = true; //change to false every time a change is made
	
	public GradeView(){
		students = new ArrayList<Student>();
		loadClassInfo(); //read all students' info from text file
		if (students.size() > 0)
			currentStudent = students.get(0);
		
		setLayout(new GridLayout(3,1)); //split GradeView panel into 3 rows and 1 column
		
		createTopPanel(); //set up top of GradeView, which is the add new student button and the save button
		createMiddlePanel(); //set up the middle or the view with all the scores
		createBottomPanel(); //set up the bottom view which should have the controls for moving between students
	}
	
	private void createMiddlePanel(){
		middlePanel = new JPanel();
		middlePanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10); //external padding
		c.ipadx = 10; //internal padding x
		c.ipady = 5; //internal padding y
		
		if (currentStudent != null){
			JLabel nameLabel = new JLabel("Name: ");
			middlePanel.add(nameLabel, c);
			c.gridwidth = GridBagConstraints.REMAINDER; //tells the layout manager to have each component take up its own row
			nameField = new JTextField(currentStudent.getName());
			nameField.addActionListener(this);
			middlePanel.add(nameField, c);
			
			int[] scores = currentStudent.getScores();
			scoreFields = new ArrayList<JTextField>(scores.length);
			for (int i = 0; i < scores.length; i++){
				c.gridwidth = 1; //reset gridwidth to default
				JLabel label = new JLabel("Test " + (i+1) + ": ");
				middlePanel.add(label, c);
				
				c.gridwidth = GridBagConstraints.REMAINDER; //take up the rest of the row
				JTextField field = new JTextField("" + scores[i]);
				field.addActionListener(this);
				scoreFields.add(field);
				middlePanel.add(field, c);
			}
		}
		
		JScrollPane scrollPane = new JScrollPane(middlePanel); //make the panel scrollable
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //Display scroll bar always
		scrollPane.setSize(300, 300);
		
		this.add(scrollPane);
	}
	
	private void createTopPanel(){
		topPanel = new JPanel();
		addNewButton = new JButton("Add a New Student");
		addNewButton.addActionListener(this);
		topPanel.add(addNewButton);
		
		saveButton = new JButton("Save Changes");
		saveButton.addActionListener(this);
		topPanel.add(saveButton);
		
		addScoreButton = new JButton("Add New Test Score");
		addScoreButton.addActionListener(this);
		topPanel.add(addScoreButton);
		
		getAverageButton = new JButton("Compute Average");
		getAverageButton.addActionListener(this);
		topPanel.add(getAverageButton);
		
		getClassAverageButton = new JButton("Get Class Average");
		getClassAverageButton.addActionListener(this);
		topPanel.add(getClassAverageButton);
		
		this.add(topPanel);
	}
	
	private void createBottomPanel(){
		bottomPanel = new JPanel();
		forwardButton = new JButton(">>");
		backButton = new JButton("<<");
		forwardButton.addActionListener(this);
		backButton.addActionListener(this);
		bottomPanel.add(backButton);
		bottomPanel.add(forwardButton);		
		
		this.add(bottomPanel);
	}
	
	private void updatePanel(){
		//re-create entire gradeview panel each time student is switched
		this.removeAll();
		createTopPanel(); //set up top of GradeView, which is the add new student button and the save button
		createMiddlePanel(); //set up the middle or the view with all the scores
		createBottomPanel(); //set up the bottom view which should have the controls for moving between students
		this.updateUI();
	}
	
	private void loadClassInfo(){
		/*loads the information about each student in the class from a text file called
		 *students.txt
		 */
		
		File file = new File("students.txt"); //load the file
		//if the file doesn't exist, then create a blank file called students.txt
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			Scanner reader; //reader for file
			try{
				reader = new Scanner(file);
			} catch(IOException e){
				reader = null;
				e.printStackTrace();
				System.out.println("ERROR: Shutting down!");
				System.exit(0);
			}
			
			//get the information from the file
			while(reader.hasNext()){
				String line = reader.nextLine();
				String[] info = line.split("  "); //split line of text at every "  "  into array of strings 
				String name = info[0];
				int[] scores = new int[info.length-1];
				for (int i = 1; i < info.length; i++){
					scores[i-1] = Integer.parseInt(info[i]);
				}
				
				Student s = new Student(name, scores);
				students.add(s);
			}
		}
	}
	
	public boolean hasSavedSinceLastChange(){
		return hasSaved;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(addNewButton)){
			String input = (String) JOptionPane.showInputDialog(this, "Input the Students' Name", "Add New Student", JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (input != null){
				Student s = new Student();
				s.setName(input);
				students.add(s);
				currentStudent = s;
				updatePanel();
				hasSaved = false;
			}
		}
		
		else if(event.getSource().equals(saveButton)){
			//in case the user didn't press the Enter key after changing any fields
			//post an action event to each text field so the actionPerformed method can run 
			//and make any changes necessary to currentStudent
			nameField.postActionEvent();
			for (JTextField field : scoreFields){
				field.postActionEvent();
			}
			
			File file = new File("students.txt"); 
			try{
				//save data
				FileWriter fw = new FileWriter(file);
				BufferedWriter writer = new BufferedWriter(fw); //writer for writing to file
				
				for (Student s : students){
					String studentInfo = "";
					studentInfo += s.getName() + "  ";
					for (int score : s.getScores()){
						studentInfo += score + "  ";
					}
					
					writer.write(studentInfo);
					writer.newLine();
				}
				writer.close();
				hasSaved = true;
			} catch(IOException e){
				e.printStackTrace();
			}
		} 
		
		else if(event.getSource().equals(addScoreButton)){
			String input = (String) JOptionPane.showInputDialog(this, "Input New Grade", "Add Score", JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (input != null){
				try{
					int score = Integer.parseInt(input);
					currentStudent.addScore(score);
					updatePanel();
					hasSaved = false;
				}catch(Exception e){
					JOptionPane.showMessageDialog(this, "Illegal argument! Input a valid score", "IllegalArgumentException", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		else if(event.getSource().equals(getAverageButton)){
			JOptionPane.showMessageDialog(this, "" + currentStudent.getName() + "'s Average is " + currentStudent.getAverage());
		}
		
		else if(event.getSource().equals(getClassAverageButton)){
			Object[] options = new String[scoreFields.size()];
			for (int i = 0; i < options.length; i++){
				options[i] = "Test " + (i+1);
			}
			
			String answer = (String) JOptionPane.showInputDialog(this, "Which test would you like to get class average for?", "Class Average", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (answer != null){
				int testIndex = Integer.parseInt(answer.substring(5, 6)) -1;
				
				//calculate class average
				int classSum = 0;
				int numOfStudents = 0;
				for (Student s : students){
					if (testIndex < s.getNumOfScores()){
						classSum += s.getScore(testIndex);
						numOfStudents++;
					}
				}
				
				int classAvg = classSum / numOfStudents;
				JOptionPane.showMessageDialog(this, "Class Average for Test" + (testIndex+1) + " is " + classAvg);
			}
		}
		
		else if (event.getSource().equals(forwardButton)){
			int index = students.indexOf(currentStudent);
			if (index+1 >= students.size()){
				JOptionPane.showMessageDialog(this, "No more students!");
			} else{
				currentStudent = students.get(index+1);
				updatePanel();
			}
		} 
		
		else if (event.getSource().equals(backButton)){
			int index = students.indexOf(currentStudent);
			if (index-1 < 0){
				JOptionPane.showMessageDialog(this, "Already at first student!");
			} else{
				currentStudent = students.get(index-1);
				updatePanel();
			}
		} 
		
		else if (event.getSource().equals(nameField)){
			currentStudent.setName(nameField.getText());
			hasSaved = false;
			
		}
		
		else{
			//check to see if any of the score fields were changed
			for (JTextField field : scoreFields){
				if (event.getSource().equals(field)){
					int index = scoreFields.indexOf(field);
					try{
						currentStudent.setScore(index, Integer.parseInt(field.getText()));
					} catch(Exception e){
						JOptionPane.showMessageDialog(this, "Grades must be numerical and in whole integers for now", "Input Error", JOptionPane.ERROR_MESSAGE);
						field.setText("0");
					}
					
					hasSaved = false;
				}
			}
		}
	}
}
