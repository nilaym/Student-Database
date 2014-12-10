package student;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import teacher.Student;

public class PortalView extends JPanel{
	private Student student;
	
	public PortalView(Student s){
		student = s;
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10); //external padding
		c.ipadx = 10; //internal padding x
		c.ipady = 5; //internal padding y
		
		if (student != null){
			JLabel nameLabel = new JLabel("Name: ");
			add(nameLabel, c);
			c.gridwidth = GridBagConstraints.REMAINDER; //tells the layout manager to have each component take up its own row
			JTextField nameField = new JTextField(student.getName());
			nameField.setEditable(false);
			add(nameField, c);
			
			int[] scores = student.getScores();
			for (int i = 0; i < scores.length; i++){
				c.gridwidth = 1; //reset gridwidth to default
				JLabel label = new JLabel("Test " + (i+1) + ": ");
				add(label, c);
				
				c.gridwidth = GridBagConstraints.REMAINDER; //take up the rest of the row
				JTextField field = new JTextField("" + scores[i]);
				field.setEditable(false);
				add(field, c);
			}
		}
	}
}
