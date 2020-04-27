package Twitter;

import java.util.Random;

public class RandomString {
	public  String getString(int n)
	{
		String rs = "abcdefghijklmnopqrstuvwxyz"+"0123456789"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(n);
		Random rd = new Random();
		int r;
		for(int i=0;i<n;i++)
		{
			r=rd.nextInt(rs.length());
			sb.append(rs.charAt(r));
		}
		return (new String(sb));
	}

}
