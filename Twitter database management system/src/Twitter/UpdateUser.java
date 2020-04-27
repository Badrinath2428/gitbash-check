package Twitter;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UpdateUser extends JFrame{
	/**
	 * 
	 */
	Connection con;
	Statement stmt;
	private static final long serialVersionUID = 1L;
	JPanel p1,p2,p3;
	JLabel usn,usid,mob_no,vef,eid,pass;
	JTextField usna,usi,mob,ver,emid,pas;
	JButton update;
	ResultSet rs;
	JTextArea ta;
	public UpdateUser() {	
	}
	void displaySQLErrors(SQLException e) 
	{
		ta.append("\nSQLException: " + e.getMessage() + "\n");
		ta.append("SQLState:     " + e.getSQLState() + "\n");
		ta.append("VendorError:  " + e.getErrorCode() + "\n");
	}
	public UpdateUser(String uid) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@localhost:1521:ORCL","Project","Project");    
			stmt=con.createStatement();
			rs=stmt.executeQuery("select * from users where user_id='"+uid+"'");
			if(rs.next()) {
				p1=new JPanel(new GridLayout(6,2));
				p2=new JPanel(new FlowLayout());
				p3=new JPanel(new FlowLayout());
				usn=new JLabel("USER NAME:");
				usna=new JTextField(rs.getString(1),25);
				usid=new JLabel("USER ID:");
				usi=new JTextField(uid, 25);
				usi.setEditable(false);
				mob_no=new JLabel("MOBILE NUBER:");
				mob=new JTextField(rs.getLong(3)+"",25);
				vef=new JLabel("VERIFICATION FLAG");
				ver=new JTextField(rs.getString(4), 25);
				eid=new JLabel("EMAIL ID:");
				emid=new JTextField(rs.getString(5),25);
				emid.setEditable(false);
				pass=new JLabel("PASSWORD:");
				pas=new JTextField(rs.getString(6), 25);
				p1.add(usn);
				p1.add(usna);
				p1.add(usid);
				p1.add(usi);
				p1.add(mob_no);
				p1.add(mob);
				p1.add(vef);
				p1.add(ver);
				p1.add(eid);
				p1.add(emid);
				p1.add(pass);
				p1.add(pas);
				add(p1);
				update=new JButton("UPDATE");
				update.setBounds(0,0,8,3);
				ta=new JTextArea(30, 30);
				ta.setEditable(false);
				p2.add(update);
				p3.add(ta);
				add(p2);
				add(p3);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			displaySQLErrors(e);
		}
		update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					int i=stmt.executeUpdate("update users set user_name='"+usna.getText()+
							"',mob_no="+mob.getText()+",privacy='"+ver.getText()+"',"+
							"password='"+pas.getText()+"' where user_id='"+uid+"'");
					ta.append("Updated "+i+" rows successfully");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					displaySQLErrors(e1);
				}
			}
		});
		setVisible(true);
		setSize(2000, 10000);
		setLayout(new GridLayout(3, 1, 10, 10));    
		
	}
}
