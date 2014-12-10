package student;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JDialog implements ActionListener, PropertyChangeListener {
	private JTextField nameField;
	private JPasswordField passField;
	
	private String loginName = null, loginPass = null;
	private boolean cancelled = false;

	private JOptionPane optionPane;

	private String buttonString1 = "Enter";
	private String buttonString2 = "Cancel";

	public LoginDialog(JFrame parentFrame) {
		super(parentFrame, true); //parentFrame is the anchor component, the boolean parameter indicates modality - should the dialog block the user from interacting with its parent frame?
		setTitle("Login");

		nameField = new JTextField("User Name");
		passField = new JPasswordField("Password");

		//Create an array of the text and components to be displayed.
		String msgString1 = "Please enter your username and password";
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

	private boolean isValidLogin(String name, String password){
		File file = new File("login.txt");
		Scanner reader;
		try{
			reader = new Scanner(file);
		}catch(IOException e){
			e.printStackTrace();
			reader = null;
			System.exit(0);
		}
		
		while(reader.hasNext()){
			String line = reader.nextLine();
			String[] info = line.split("  ");
			if (name.equals(info[0]) && password.equals(Cipher.decrypt(info[1])))
				return true;
		}
		
		return false;
	}
	
	public void display(){
		pack();
		setVisible(true);
	}
	
	public String[] getLoginInfo(){
		String[] info = {loginName, loginPass};
		return info;
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
				if (isValidLogin(name, password)) {
					loginName = nameField.getText();
					loginPass = passField.getText();
					dispose();
				}else{
					//invalid login credentials
					nameField.selectAll();
					JOptionPane.showMessageDialog(this, "Sorry, no such username password combination detected", "Invalid Login", JOptionPane.ERROR_MESSAGE);
					nameField.requestFocusInWindow();
				}
			}else { //user hit "cancel" 
				cancelled = true;
				dispose();
			}
		}
	}
}
