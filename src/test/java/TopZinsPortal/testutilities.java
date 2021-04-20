package TopZinsPortal;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Store;

import com.testing.framework.EmailUtils;

public class testutilities {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		
		// Die EmailUtils sind in einer eigenen Jar-Datei und nicht Teil des Util-Verzeichnisses  
		EmailUtils emailUtils = new EmailUtils();
		Properties prop= new Properties();
	    prop.load(new FileInputStream("C:\\Users\\b.dikmen\\git\\TZPSeleniumCode\\TopZinsPortalTestablauf\\resources\\TZP-Lender.properties"));
	    Store connection=emailUtils.connectToGmail(prop); 
	    emailUtils.getUnreadMessages(connection, "Inbox");
	    
	    @SuppressWarnings("unchecked")
		List<String> emailtext=emailUtils.getMessageByFromEmail(connection, "Inbox", "account-security-noreply@accountprotection.microsoft.com", "Zurücksetzung des Kennworts für das Microsoft-Konto");
		  if (emailtext.size()<1)
			throw new Exception("No Email recieved");
			else
			{
			String regex= "[^\\d]+";
			String[] OTP=emailtext.get(0).split(regex);
			System.out.println("OTP is : "+ OTP[1]);
			}

	}

}
