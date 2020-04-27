package Twitter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class Search extends JFrame {
	private static final long serialVersionUID = 1L;
	JList<String> lis;
	DefaultListModel<String> uids;
	JButton sea;
	JTextField sear;
	JPanel jp;
	JToolBar tb;
	Connection con;
	Statement stmt;
	ResultSet rs;
	public void loadResults() {
		
	}
	public Search(String uid) {
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");     
			con=DriverManager.getConnection(  
			"jdbc:oracle:thin:@localhost:1521:ORCL","Project","Project");    
			stmt=con.createStatement();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		uids=new DefaultListModel<String>();
		sear=new JTextField(25);
		sea=new JButton("Search");
		tb=new JToolBar();
		tb.add(sear);
		tb.add(sea);
		jp=new JPanel();
		add(jp,BorderLayout.CENTER);
		sear.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				uids.clear();
				jp.removeAll();
				invalidate();
				validate();
				repaint();
				try {
					rs=stmt.executeQuery("select distinct user_name,user_id from users where user_name like '"+
				sear.getText()+"%' or user_id like '"+sear.getText()+"%'");
					while(rs.next()) {
						uids.addElement(rs.getString(1)+"(@"+rs.getString(2)+")");
					}
					lis=new JList<String>(uids);
					jp.add(lis);
					invalidate();
					validate();
					repaint();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		sea.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String select=(String) lis.getSelectedValue();
				sear.setText(select);
				StringTokenizer st=new StringTokenizer(select,"@)");
				st.nextToken();
				String userid=st.nextToken();
				ResultSet rs4;
				System.out.println(select);
				try {
					rs4=stmt.executeQuery("select privacy from users where user_id='"+userid+"'");
					if(rs4.next()) {
						if(rs4.getString(1).equals("Y")) {
							int k=stmt.executeUpdate("select * from users_following where user_id='"+uid+"' and f_user_id='"+userid+"'");
							if(k==0) {
								int ver=JOptionPane.showConfirmDialog(new JFrame()," This account is private!\n"+"Do you want to follow "+userid);
								if(ver==JOptionPane.YES_OPTION) {
									stmt.execute("insert into users_following values('"+uid+"','"+userid+"', Sysdate)");
									new Display(userid);
								}
							}
							else {
								new Display(userid);
							}
						}
						else {
							new Display(userid);
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		add(tb,BorderLayout.NORTH);
		setSize(2000, 1000);
		setVisible(true);
		setLayout(new BorderLayout());
	}
}
