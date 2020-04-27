package Twitter;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Followers extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	Connection con;
	Statement stmt;
	ResultSet rs;
	JScrollPane sp;
	JPanel jp;
	int count;
	ArrayList<JPanel> aljp;
	ArrayList<String> als;
	String uid;
	public Followers() {
		
	}
	public Followers(String usid) {
		uid=usid;
		aljp=new ArrayList<JPanel>();
		als=new ArrayList<String>();
		jp=new JPanel(new GridLayout(10, 1));
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");     
			con=DriverManager.getConnection(  
			"jdbc:oracle:thin:@localhost:1521:ORCL","Project","Project");    
			stmt=con.createStatement();
			rs=stmt.executeQuery("select uf.user_id,u.user_name from users_following uf,users u "
					+ "where uf.f_user_id='"+uid+"' and uf.user_id=u.user_id order by uf.since DESC");
			while(rs.next()) {
				count++;
				JPanel jp1=new JPanel(new GridBagLayout());
				GridBagConstraints gbc=new GridBagConstraints();
				JLabel tun=new JLabel(rs.getString(1));
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=0;
				gbc.gridy=0;
				jp1.add(tun,gbc);
				JLabel usn=new JLabel("@"+rs.getString(2)+"\t");
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=0;
				gbc.gridy=1;
				jp1.add(usn,gbc);
				JButton buto=new JButton("Remove");
				gbc.fill=GridBagConstraints.HORIZONTAL;
				gbc.gridx=2;
				gbc.gridy=1;
				buto.addActionListener(this);
				jp1.add(buto,gbc);
				jp.add(jp1);
				aljp.add(jp1);
				als.add(rs.getString(1));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		setLayout(new FlowLayout());
		sp=new JScrollPane(jp);
		setVisible(true);
		setSize(400, 600);
		add(sp);
	}
	public void actionPerformed(ActionEvent e) {
		JButton but=(JButton) e.getSource();
		for(int i=0;i<aljp.size();i++) {
			Component comp1[]=aljp.get(i).getComponents();
			for(Component comp:comp1) {
				if(comp instanceof JButton) {
					if((JButton)comp==but) {
						try {
							stmt.execute("delete from users_following where f_user_id='"+uid+"' and user_id='"+als.get(i)+"'");
							but.setText("Removed");
							invalidate();
							validate();
							repaint();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
}

