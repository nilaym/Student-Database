package student;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import teacher.Student;

public class MainFrame extends JFrame implements WindowListener{
	private JPanel view;
	
	public MainFrame(int width, int height){
		super();
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Java Gradebook");
		
		this.addWindowListener(this);
	}
	
	public void display(){
		setVisible(true);
	}
	
	private Student loadStudentData(String studentName){
		File file = new File("students.txt"); //load the file
		//if the file doesn't exist, then create a blank file called students.txt
		if (!file.exists()){
			System.out.println("ERROR LOADING DATA...MISSING STUDENT.TXT FILE!");
			System.exit(0);
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
				if (name.equals(studentName)){
					int[] scores = new int[info.length-1];
					for (int i = 1; i < info.length; i++){
						scores[i-1] = Integer.parseInt(info[i]);
					}
					
					Student s = new Student(name, scores);
					return s;
				}
			}
		}
		
		return null;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		JOptionPane.showMessageDialog(this, "Welcome to the Java Student Gradebook!!");
		int answer = JOptionPane.showConfirmDialog(this, "Are you a returning user?", "Java Student Database", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION){
			LoginDialog dialog = new LoginDialog(this);
			dialog.display();
			
			if (dialog.checkIfUserCancelled()){
				dispose();
			}else{
				String[] info = dialog.getLoginInfo();
				Student s =  loadStudentData(info[0]);
				view = new PortalView(s);
				
				add(view);
				revalidate();
				repaint();
			}
		}else{
			NewUserDialog dialog = new NewUserDialog(this);
			dialog.display();

			LoginDialog dialog1 = new LoginDialog(this);
			dialog1.display();

			if (dialog1.checkIfUserCancelled()){
				dispose();
			}else{
				String[] info = dialog1.getLoginInfo();
				Student s =  loadStudentData(info[0]);
				view = new PortalView(s);

				add(view);
				revalidate();
				repaint();
			}
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		JOptionPane.showMessageDialog(null, "Thank you for using the Java Gradebook made by Javish Industries LLC");
		System.exit(0); //exit application 
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}
}
