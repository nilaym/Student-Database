package student;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class NewUserDialog extends JDialog implements ActionListener, PropertyChangeListener {
	private JTextField nameField;
	private JPasswordField passField;
	
	private String username = null, password = null;
	private boolean cancelled = false;

	private JOptionPane optionPane;

	private String buttonString1 = "Enter";
	private String buttonString2 = "Cancel";

	public NewUserDialog(JFrame parentFrame) {
		super(parentFrame, true); //parentFrame is the anchor component, the boolean parameter indicates modality - should the dialog block the user from interacting with its parent frame?
		setTitle("Login");

		nameField = new JTextField("Enter your name");
		passField = new JPasswordField("Password");

		//Create an array of the text and components to be displayed.
		String msgString1 = "Please enter your name and a password";
		Object[] items = {msgString1, nameField, passField};

		//Create an array specifying the number of dialog button and their text.
		Object[] options = {buttonString1, buttonString2};
		
		optionPane = new JOptionPane(items, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
		//Make dialog display optionPane
		setContentPane(optionPane);

		//Ensure the username field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				nameField.requestFocusInWindow();
			}
		});

		passField.addActionListener(this); 
		optionPane.addPropertyChangeListener(this);
	}

	private boolean checkValidStudentName(String studentName){
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
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void createNewUser(String name, String pass){
		File file = new File("login.txt");
		FileWriter fw;
		BufferedWriter bw;
		PrintWriter writer;
		try{
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			writer = new PrintWriter(bw, true);
			
			writer.println(name + "  " + Cipher.encrypt(pass)); //encrypt the password and store it by calling Cipher.encrypt
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void display(){
		pack();
		setVisible(true);
	}
	
	public boolean checkIfUserCancelled(){
		return cancelled;
	}

	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(buttonString1); //respond to user hitting enter button
	}

	public void propertyChange(PropertyChangeEvent e) {
		String property = e.getPropertyName();

		if (isVisible() && (e.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(property) || JOptionPane.INPUT_VALUE_PROPERTY.equals(property))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE)
				return; //ignore reset and get out of method

			//Reset the JOptionPane's value. If you don't do this, then if the user
			//presses the same button next time, no property change event will be fired.
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			String name = "";
			String password = "";

			if (buttonString1.equals(value)) { //user hit "enter"
				name = nameField.getText();
				password = passField.getText();
				if (checkValidStudentName(name)) {
					username = nameField.getText();
					password = passField.getText();
					createNewUser(username, password);
					dispose();
				}else{
					//invalid login credentials
					nameField.selectAll();
					JOptionPane.showMessageDialog(this, "Sorry, no such student exists in class", "Invalid Login", JOptionPane.ERROR_MESSAGE);
					nameField.requestFocusInWindow();
				}
			}else { //user hit "cancel" 
				cancelled = true;
				dispose();
			}
		}
	}

	public void destroyDialog() {
		dispose();
	}
}
