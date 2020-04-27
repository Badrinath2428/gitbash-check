package Twitter;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Display extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection con;
	Statement stmt;
	ResultSet rs;
	JPanel p1;
	JScrollPane sp;
	ArrayList<String> alid;
	ArrayList<JPanel> aljp;
	String usid;
	int k;
	int coun;
	public Display() {	
	}
	public Display(String uid) {
		try {
			usid=uid;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@localhost:1521:ORCL","Project","Project");    
			stmt=con.createStatement();
			k=stmt.executeUpdate("select tweet_id from users_tweet where user_id='"+uid+"'");
			rs=stmt.executeQuery("select t.message,t.polarity,t.likes,t.tweet_id,t.category, "
					+ "ut.p_date from users_tweet ut,"
				+ "tweet t where t.tweet_id=ut.tweet_id and ut.user_id='"+uid+"' order by "
						+ "ut.p_date DESC");
			setVisible(true);
			setLayout(new FlowLayout());
			setSize(2000,1000);
			p1=new JPanel();
			p1.setLayout(new GridLayout(k, 1));
			alid=new ArrayList<String>();
			aljp=new ArrayList<JPanel>();
			while(rs.next())
			{
				coun++;
				GridBagConstraints gbc=new GridBagConstraints();
				JPanel ajp=new JPanel(new GridBagLayout());
				alid.add(rs.getString(4));
				JLabel tid=new JLabel("@"+uid);
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=0;
				gbc.gridy=0;
				ajp.add(tid,gbc);
				JLabel tdate=new JLabel(rs.getString(6));
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=1;
				gbc.gridy=0;
				ajp.add(tdate,gbc);
				JLabel like=new JLabel("	Likes("+rs.getInt(3)+")");
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=2;
				gbc.gridy=0;
				ajp.add(like,gbc);
				TextArea tta=new TextArea(rs.getString(1));
				tta.setEditable(false);
				tta.setSize(50, 30);
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=0;
				gbc.gridy=1;
				ajp.add(tta,gbc);
				JButton comment=new JButton("Comments");
				gbc.gridx=0;
				gbc.gridy=2;
				comment.addActionListener(this);
				ajp.add(comment,gbc);
				JButton retweet=new JButton("Retweets");
				gbc.gridx=1;
				gbc.gridy=2;
				retweet.addActionListener(this);
				ajp.add(retweet,gbc);
				JButton reply=new JButton("Replies");
				gbc.gridx=2;
				gbc.gridy=2;
				reply.addActionListener(this);
				ajp.add(reply,gbc);
				aljp.add(ajp);
				p1.add(ajp);
			}
			if(coun==0) {
				JOptionPane.showMessageDialog(new JFrame(),"No tweets!" );
				this.dispose();
			}
			sp=new JScrollPane(p1);
			add(sp);	
		}catch (HeadlessException e2) {
				e2.printStackTrace();
		}catch (ClassNotFoundException e) {
		e.printStackTrace();
		}catch(SQLException e1) {
			displaySQLErrors(e1);
		}catch(Exception ef) {
			ef.printStackTrace();
		}
	}
	void displaySQLErrors(SQLException e) 
	{
		System.out.println("\nSQLException: " + e.getMessage() + "\n");
		System.out.println("SQLState:     " + e.getSQLState() + "\n");
		System.out.println("VendorError:  " + e.getErrorCode() + "\n");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton bu=(JButton) e.getSource();
		for(int i=0;i<aljp.size();i++) {
			Component comp[]=aljp.get(i).getComponents();{
				for(Component comp1:comp) {
					if(comp1 instanceof JButton)
					{
						JButton but=(JButton) comp1;
						if(but==bu) {
							if(but.getText().equals("Replies")){
								try {
									ResultSet rst=stmt.executeQuery("select r.r_user_id,r.message_id,r.message from response r left outer join  tweet_response tr on r.message_id=tr.message_id\r\n" + 
											"where tr.tweet_id='"+alid.get(i)+"' and r.category='Reply' order by replied_date DESC");
									int siz=0;
									JFrame res=new JFrame("Replies");
									JPanel jpf=new JPanel();
									JScrollPane jsp=new JScrollPane(jpf);
									while(rst.next()) {
										siz++;
										JPanel jp=new JPanel(new FlowLayout());
										TextArea ta=new TextArea(40,40);
										ta.setEditable(false);
										ta.append("@"+rst.getString(1)+"\n");
										ta.append("Replied message:"+"\n");
										ta.append(rst.getString(3));
										jp.add(ta);
										jpf.add(jp);
									}
									if(siz==0) {
										JOptionPane.showMessageDialog(new JFrame(),"Sorry, No replies to display");
									}else {
										res.setVisible(true);
										res.pack();
										res.setSize(400, 300);
										res.add(jsp);
									}
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									displaySQLErrors(e1);
								}
							}
							else if(but.getText().equals("Comments")){
								try {
									ResultSet rst=stmt.executeQuery("select r.r_user_id,r.message_id,r.message from response r left outer join  tweet_response tr on r.message_id=tr.message_id\r\n" + 
											"where tr.tweet_id='"+alid.get(i)+"' and r.category='Comment' order by replied_date DESC");
									int siz=0;
									JFrame res=new JFrame("Comments");
									JPanel jpf=new JPanel();
									JScrollPane jsp=new JScrollPane(jpf);
									while(rst.next()) {
										siz++;
										JPanel jp=new JPanel(new FlowLayout());
										TextArea ta=new TextArea(40,40);
										ta.setEditable(false);
										ta.append("@"+rst.getString(1)+"\n");
										ta.append("Comments:"+"\n");
										ta.append(rst.getString(3));
										jp.add(ta);
										jpf.add(jp);
									}
									if(siz==0) {
										JOptionPane.showMessageDialog(new JFrame(),"Sorry, No Comments to display");
									}else {
										res.setVisible(true);
										res.pack();
										res.setSize(400, 300);
										res.add(jsp);
									}
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									displaySQLErrors(e1);
								}
							}
							else if(but.getText().equals("Retweets")){
								try {
									ResultSet rst=stmt.executeQuery("select r.r_user_id,r.message_id,r.message from response r left outer join  tweet_response tr on r.message_id=tr.message_id\r\n" + 
											"where tr.tweet_id='"+alid.get(i)+"' and r.category='Comment' order by replied_date DESC");
									int siz=0;
									JFrame res=new JFrame("Retwotes");
									JPanel jpf=new JPanel();
									JScrollPane jsp=new JScrollPane(jpf);
									while(rst.next()) {
										siz++;
										JLabel jp=new JLabel(rst.getString(1));
										jpf.add(jp);
									}
									if(siz==0) {
										JOptionPane.showMessageDialog(new JFrame(),"Sorry, No Retweets");
									}else {
										res.setVisible(true);
										res.pack();
										jpf.setLayout(new GridLayout(siz,1));
										res.setSize(400, 300);
										res.add(jsp);
									}
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									displaySQLErrors(e1);
								}
							}
						}
					}
				}
			}
		}
	}
}