package Twitter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class userLogin extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel un,pss,va;
	JTextField u,ps;
	JButton c;
	JPanel p2,p3;
	String s,p;
	public userLogin() {
		s=JOptionPane.showInputDialog(new JFrame(), "Enter userID");
		if(!s.isEmpty())
		{
			p=JOptionPane.showInputDialog(new JFrame(), "Enter Password");
		}
	}
	public String getUserID()
	{
		return s;
		
	}
	public String getPassword()
	{
		return p;
	}

}
