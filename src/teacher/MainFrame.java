package teacher;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements WindowListener{
	private JPanel view;
	
	public MainFrame(int width, int height){
		super();
		setSize(width, height);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("Java GradeBook");
		
		this.addWindowListener(this);
		
		view = new GradeView();
		getContentPane().add(view);
	}
	
	public void display(){
		setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (!((GradeView)view).hasSavedSinceLastChange()){
			int confirmed = JOptionPane.showConfirmDialog(this, "Exit without saving changes?", "Save Changes?", JOptionPane.YES_NO_OPTION); //make sure user doesn't accidentally quit without saving
			if (confirmed == JOptionPane.YES_OPTION){
				dispose(); //destroy JFrame, application will still continue running - calls JFrame.windowClosed()
			}
		} else{
			dispose(); //destroy JFrame, application will still continue running - calls JFrame.windowClosed()
		}
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
