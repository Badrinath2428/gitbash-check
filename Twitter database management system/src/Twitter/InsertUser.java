package Twitter;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InsertUser extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel jl;
	JPanel p1,p2,p3,p4,p5;
	JLabel un,uid,mn,pas,flag,eid,otp;
	JTextField u,ui,m,pa,fla,ei,ot;
	TextArea ta;
	JButton in;
	Random rand,rand2;
	int o,val;
	String usid,userid;
	void displaySQLErrors(SQLException e) 
	{
		ta.append("\nSQLException: " + e.getMessage() + "\n");
		ta.append("SQLState:     " + e.getSQLState() + "\n");
		ta.append("VendorError:  " + e.getErrorCode() + "\n");
	}

	public InsertUser()
	{
		rand =new Random();
		rand2=new Random();
		jl=new JLabel("Enter Following details ");
		p1=new JPanel();
		p2=new JPanel();
		p3=new JPanel();
		p4=new JPanel();
		p5=new JPanel();
		p1.add(jl);
		p1.setLayout(new FlowLayout());
		un=new JLabel("User Name(no spaces instead use underscore,minimum of 6 letters):");
		uid=new JLabel("User ID(auto generated):");
		mn=new JLabel("Mobile NO:");
		pas=new JLabel("Password");
		flag=new JLabel("PRIVACY:");
		eid=new JLabel("Email ID:");
		otp=new JLabel("OTP");
		u=new JTextField(20);
		ui=new JTextField(20);
		ui.setEditable(false);
		m=new JTextField(20);
		pa=new JTextField(20);
		fla=new JTextField(20);
		ei=new JTextField(20);
		ot=new JTextField(20);
		
		ta=new TextArea(20,100);
		ta.setEditable(false);
		in=new JButton("CONFIRM & GENERATE OTP");
		p2.add(un);
		p2.add(u);
		p2.add(uid);
		p2.add(ui);
		p2.add(mn);
		p2.add(m);
		p2.add(flag);
		p2.add(fla);
		p2.add(eid);
		p2.add(ei);
		p2.add(pas);
		p2.add(pa);
		p2.setLayout(new GridLayout(6,2));
		p3.add(in);
		p4.add(otp);
		p4.add(ot);
		p5.add(ta);
		add(p1);
		add(p2);
		add(p3);
		add(p4);
		add(p5);
		p3.setLayout(new FlowLayout());
		p4.setLayout(new GridLayout(1, 2));
		setLayout(new GridLayout(5,1));
		setVisible(true);
		setTitle("ADD USER");
		setSize(2000,1000);
		o=rand.nextInt(10000);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		in.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{  
					Class.forName("oracle.jdbc.driver.OracleDriver");   
					  
					Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@localhost:1521:ORCL","Project","Project");  
					    
					Statement stmt=con.createStatement();  
					String host="smtp.gmail.com";  
					final String user="javacode2428@gmail.com"; 
					final String password="Badri1234"; 
					    
					  String to=(ei.getText());  
					  ta.append("Please check your email,OTP has been generated");
					  
					   Properties props = new Properties();  
					   props.put("mail.smtp.host",host);  
					   props.put("mail.smtp.auth", "true"); 
					   props.put("mail.smtp.port", "587"); 
					   props.put("mail.smtp.starttls.enable", "true"); 
					     
					   Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {  
					      protected PasswordAuthentication getPasswordAuthentication() {  
					    return new PasswordAuthentication(user,password);  
					      }  
					    });    
					    try {  
					     MimeMessage message = new MimeMessage(session);  
					     message.setFrom(new InternetAddress(user));  
					     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
					     message.setSubject("Twitter SignUp");  
					     message.setText("Thank you!,for joining us on Twitter,Your OTP is "+o+"\n Happy Tweeting!");   
					     Transport.send(message);  
					  
					     System.out.println("message sent successfully...");  
					   
					     } catch (MessagingException ek) {
					    	 ek.printStackTrace();
					    	 }
					    ot.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								val=Integer.parseInt(ot.getText());
								if(o==val)
				            	 {
								    	int i = 0;
										try {
											char[] usec=u.getText().toCharArray();
											char[] copy=Arrays.copyOf(usec,(u.getText().length())-4 );
											usid=new String(copy);
											userid=usid+rand2.nextInt(100000);
											i = stmt.executeUpdate("insert into users(user_name,user_id,mob_no,privacy,email_id,password) values("
													+"'"+u.getText()+"','"+userid+"',"
													+Long.parseLong(m.getText())+",'"+fla.getText()+"','"+ei.getText()+
													"','"+ pa.getText()+"')");
											stmt.execute("insert into following values('"+userid+"')");
											ui.setText(userid);
										} catch (NumberFormatException e1) { 
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}catch(SQLException e2) { 
											// TODO Auto-generated catch block
											displaySQLErrors(e2);
										} 
								    	ta.append("\n Inserted "+i+"rows successfully");
								    	try {
											con.close();
										} catch (SQLException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
				            	 }
								 else
								 {
								    ta.append("\n Sorry ,Please enter valid OTP");
								    try {
										con.close();
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								 }
								
							}
						}); 						
					    ta.setEditable(false);
					/*while(rs.next())  
						System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)); */     					  
					}catch(SQLException se) {
						displaySQLErrors(se);
					}
				catch(Exception x1){ 
						x1.printStackTrace();
						}
				
			}
		});
	}

}
