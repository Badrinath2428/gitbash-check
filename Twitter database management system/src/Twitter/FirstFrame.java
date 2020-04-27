package Twitter;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class abc extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	JMenuBar mnubar;
	JMenu m1,m2,m3;
	JMenuItem inu,upu,delu,su,lo,followers,following,compose,you,search;
	String uid,pass;
	Connection con;
	Statement stmt;
	JPanel jp1;
	ArrayList<JPanel> ap;
	ArrayList<String> sui,ttid;
	ResultSet rs2,rl;
	JTextField sea;
	GridBagConstraints gb;
	TextArea message;
	JScrollPane sp;
	int val;
	public abc() {
		mnubar=new JMenuBar();
		search=new JMenuItem("Search");
		gb=new GridBagConstraints();
		gb.fill=GridBagConstraints.HORIZONTAL;
		m1=new JMenu("USERS");
		m2=new JMenu("SETTINGS");
		m3=new JMenu("TWEET");
		inu=new JMenuItem("SIGN UP");
		upu=new JMenuItem("UPDATE DETAILS");
		delu=new JMenuItem("DELETE ACCOUNT");
		lo=new JMenuItem("LOGOUT");
		su=new JMenuItem("LOGIN");
		followers=new JMenuItem("FOLLOWERS");
		following=new JMenuItem("FOLLOWING");
		you=new JMenuItem("TWEETS");
		compose=new JMenuItem("COMPOSE TWEET");
		sea=new JTextField(15);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				int a=JOptionPane.showConfirmDialog(new JFrame(),"Are you sure?");  
				if(a==JOptionPane.YES_OPTION){  
				    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
					}
			}
		});
		following.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Following(uid);
			}
		});
		followers.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Followers(uid);
			}
		});
		mnubar.add(m1);
		m1.add(inu);
		m1.add(su);
		//mnubar.add(m2);
		m2.add(upu);
		m2.add(search);
		m2.add(followers);
		m2.add(following);
		m2.add(delu);
		m2.add(lo);
		//mnubar.add(m3);
		m3.add(compose);
		m3.add(you);
		setJMenuBar(mnubar);
		getContentPane().setBackground(Color.BLUE);
		setSize(2000,1000);
		setTitle("Twitter Database Management System");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new FlowLayout());
		setVisible(true);
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");     
			con=DriverManager.getConnection(  
			"jdbc:oracle:thin:@localhost:1521:ORCL","Project","Project");    
			stmt=con.createStatement();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		inu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ins) {
				// TODO Auto-generated method stub
				new InsertUser();
			}
		});
		su.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent sud) {
				// TODO Auto-generated method stub
				userLogin ul = new userLogin();
				uid=ul.getUserID();
				pass=ul.getPassword();
				try {
					val = stmt.executeUpdate("select * from users where user_id="+"'"+uid+"'"+
				"and password="+"'"+pass+"'");
					if(val!=0)
					{
						su.setEnabled(false);
						jp1=new JPanel();
						jp1.setSize(1000,100);
						jp1.setVisible(true);
						jp1.setLayout(new GridLayout(2, 1));
						sp=new JScrollPane(jp1);
						add(sp);
						ResultSet rs1=stmt.executeQuery("select t.message,ut.user_id,t.tweet_id from tweet t LEFT OUTER JOIN users_tweet ut on t.tweet_id=ut.tweet_id where ut.user_id in(" + 
								"select uf.f_user_id from users u,users_following uf where u.user_id=uf.user_id and u.user_id='"+uid+"')" + 
								"order by ut.p_date DESC");
						ap=new ArrayList<JPanel>();
						sui=new ArrayList<String>();
						ttid=new ArrayList<String>();
						addComponents(rs1);
						setJMenuBar(mnubar);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(val);
			}
		});
		upu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new UpdateUser(uid);
			}
		});
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Search(uid);
			}
		});
		compose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel p1=new JPanel(new GridLayout(2, 1));
				JPanel p2=new JPanel(new FlowLayout());
				JFrame compo=new JFrame("TWEET");
				compo.setVisible(true);
				compo.setLayout(new GridLayout(3, 1, 3, 4));
				compo.setLocation(650,250);
				message=new TextArea("Enter message:", 10, 30,Scrollbar.VERTICAL);
				compo.add(message);
				JTextField cat=new JTextField("Category",30);
				p1.add(cat);
				JTextField pol=new JTextField("Polarity(P/N)", 30);
				p1.add(pol);
				compo.setSize(100,40);
				compo.pack();
				RandomString rands=new RandomString();
				String mess=rands.getString(10);
				JButton post=new JButton("POST");
				post.setSize(10,2);
				p2.add(post);
				compo.add(p1);
				compo.add(p2);
				post.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							stmt.execute("insert into tweet values('"+ message.getText() +"','"+pol.getText()+"',"
									+0+",'"+mess+"','"+cat.getText()+"')");
							stmt.execute("insert into users_tweet values('"+uid+"',"+mess+"',Sysdate)");
							compo.dispose();
						} catch (SQLException e1) {
							displaySQLErrors(e1);
						}	
					}
				});
			}
		});
		lo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getContentPane().removeAll();
				m1.setVisible(true);
				m2.setVisible(false);
				m3.setVisible(false);
				su.setEnabled(true);
				invalidate();
				validate();
				repaint();
			}
		});
		you.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DisplayTweets(uid);
			}
		});
		delu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent dela) {
				try {
					int a=JOptionPane.showConfirmDialog(new JFrame(),"Are you sure?");  
					if(a==JOptionPane.YES_OPTION){  
						stmt.execute("delete from users_following where user_id='"+uid+"'");
						stmt.execute("delete from users_tweet where user_id='"+uid+"'");
						stmt.execute("delete from users_following where f_user_id='"+uid+"'");
						stmt.execute("delete from following where f_user_id='"+uid+"'");
						stmt.execute("delete from users where user_id='"+uid+"'");
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	void displaySQLErrors(SQLException e) 
	{
		System.out.println("\nSQLException: " + e.getMessage() + "\n");
		System.out.println("SQLState:     " + e.getSQLState() + "\n");
		System.out.println("VendorError:  " + e.getErrorCode() + "\n");
	}
	private void addComponents(ResultSet rs1) {
		try {
			while(rs1.next())
			{
				GridBagConstraints gbc=new GridBagConstraints();
				JPanel ajp=new JPanel(new GridBagLayout());
				String str=rs1.getString(2);
				sui.add(str);
				ttid.add(rs1.getString(3));
				String str2=rs1.getString(1);
				JLabel tid=new JLabel("@"+str);
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=1;
				gbc.gridy=0;
				ajp.add(tid,gbc);
				TextArea tta=new TextArea(str2);
				tta.setEditable(false);
				tta.setSize(50, 30);
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=0;
				gbc.gridy=1;
				ajp.add(tta,gbc);
				JButton like=new JButton("Like");
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=0;
				gbc.gridy=2;
				like.addActionListener(this);
				ajp.add(like,gbc);
				JButton comment=new JButton("Comment");
				gbc.gridx=1;
				gbc.gridy=2;
				comment.addActionListener(this);
				ajp.add(comment,gbc);
				JButton reply=new JButton("Reply");
				gbc.gridx=3;
				gbc.gridy=2;
				reply.addActionListener(this);
				ajp.add(reply,gbc);
				ap.add(ajp);
				jp1.add(ajp);
			}
			for(int i=0;i<sui.size();i++)
			{
				rs2=stmt.executeQuery("select user_name from users where user_id='"+sui.get(i)+"'");
				if(rs2.next())
				{
					GridBagConstraints gbc=new GridBagConstraints();
					JLabel tun=new JLabel(rs2.getString(1));
					gbc.fill=GridBagConstraints.HORIZONTAL;
					gbc.gridx=0;
					gbc.gridy=0;
					ap.get(i).add(tun,gbc);
				}
			}
			for(int i=0;i<ttid.size();i++) {
				JButton retweet=new JButton();
				GridBagConstraints gbc=new GridBagConstraints();
				int ret=stmt.executeUpdate("select * from users_tweet where user_id='"+uid+"' and tweet_id='"+ttid.get(i)+"'");
				if(ret!=0) {
					retweet.setText("Retweeted");
				}
				else {
					retweet.setText("Retweet");
				}
				gbc.gridx=2;
				gbc.gridy=2;
				retweet.addActionListener(this);
				ap.get(i).add(retweet,gbc);
			}
		} catch (HeadlessException | SQLException e) {
			e.printStackTrace();
		}
		add(sp);
		mnubar.add(m2);
		mnubar.add(m3);
		m1.setVisible(false);
		invalidate();
		validate();
		repaint();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton bu=(JButton) e.getSource();
		for(int i=0;i<ap.size();i++)
		{
			Component[] comp=(ap.get(i)).getComponents();
			for(Component comp1:comp)
			{
				if(comp1 instanceof JButton)
				{
					JButton but=(JButton) comp1;
					if(but==bu)
					{
						if(but.getText().equals("Like")){
							try {
								stmt.execute("UPDATE TWEET set likes=likes+1 WHERE TWEET_ID='"+ttid.get(i)+"'");
								but.setText("DisLike");
								but.setBackground(Color.blue);
								JOptionPane.showMessageDialog(new JFrame(),"You have liked a tweet");
								break;
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
						else if(but.getText().equals("DisLike")){
							try {
								stmt.execute("UPDATE TWEET set likes=likes-1 WHERE TWEET_ID='"+ttid.get(i)+"'");
								but.setText("Like");
								but.setBackground(Color.white);
								JOptionPane.showMessageDialog(new JFrame(),"You have Disliked a tweet");
								break;
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
						else if(but.getText().equals("Retweet")) {
							try {
								RandomString rst=new RandomString();
								String ranid=rst.getString(10);
								stmt.execute("insert into users_tweet values('"+uid+"','"+ttid.get(i)+"',Sysdate)");
								stmt.execute("insert into response values('"+uid+"',Sysdate,'"+but.getText()+"','','"+ranid+"')");
								String ch=JOptionPane.showInputDialog(new JFrame(), "Are you supporting it?(p/n)");
								stmt.execute("insert into tweet_response values('"+ttid.get(i)+"','"+ranid+"','"+ch+"')");
								but.setText("Retweeted");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								displaySQLErrors(e1);
							}
							
						}
						else if(but.getText().equals("Retweeted")) {
							try {
								stmt.execute("delete from  users_tweet where user_id='"+uid+"' and tweet_id='"+ttid.get(i)+"'");
								ResultSet rsm=stmt.executeQuery("select r.message_id from response r left outer join "
										+ "tweet_response tr on r.message_id=tr.message_id where r.r_user_id='"+uid+"'"
												+ "and r.category='Retweet' and tr.tweet_id='"+ttid.get(i)+"'");
								if(rsm.next()) {
									String mesid=rsm.getString(1);
									stmt.execute("delete from tweet_response where message_id='"+mesid+"'");
									stmt.execute("delete from response where message_id='"+mesid+"'");
									but.setText("Retweet");
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								displaySQLErrors(e1);
							}
							
						}
						else {
							RandomString rst=new RandomString();
							String ranid=rst.getString(10);
							String tid=ttid.get(i);
							JTextArea mess=new JTextArea("Enter here",10, 30);
							JPanel jpa1=new JPanel(new GridLayout(1,2));
							JPanel jpa2=new JPanel(new FlowLayout());
							JLabel polarity=new JLabel("POLARITY(P/N):");
							JTextField pola=new JTextField(10);
							JButton subm=new JButton("Submit");
							jpa2.add(subm);
							jpa1.add(polarity);
							jpa1.add(pola);
							JFrame mss=new JFrame(but.getText());
							mss.setSize(400,400);
						    mss.setLocation(700,300);
							mss.setLayout(new GridLayout(3,1));
							mss.setVisible(true);
							mss.add(mess);
							mss.add(jpa1);
							mss.add(jpa2);
							subm.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									try {
										stmt.execute("insert into response values('"+uid+"',sysdate,'"+
									but.getText()+"','"+mess.getText()+"','"+ranid+"')");
										stmt.execute("insert into tweet_response values('"+tid+"','"+ranid+
												"','"+pola.getText()+"')");
										System.out.println("Entry successfull");
										mss.dispose();
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										displaySQLErrors(e1);
									}
								}
							});
						}
					}
				}
			}
		}
	}
}

public class FirstFrame {
	public static void main(String a[]) {
			new abc();
	}
}
