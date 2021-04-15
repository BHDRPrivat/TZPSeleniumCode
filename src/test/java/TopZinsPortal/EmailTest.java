package TopZinsPortal;

import javax.mail.Message;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Utils.EmailUtils;

public class EmailTest {
	
	  private static EmailUtils emailUtils;

	  @BeforeClass
	  public static void connectToEmail() {
	    try {
	    // "smtp.gmail.com"	
	      emailUtils = new EmailUtils("b.dikmen.wi@gmail.com", "Patara318", "imap.gmail.com", Utils.EmailUtils.EmailFolder.INBOX);
	    } catch (Exception e) {
	      e.printStackTrace();
	      Assert.fail(e.getMessage());
	    }
	  }

	  @Test
	  public void testVerificationCode() {
	    try {
	      //TODO: Execute actions to send verification code to email

	      String verificationCode = emailUtils.getAuthorizationCode();

	      //TODO: Enter verification code on screen and submit

	      //TODO: add assertions

	    } catch (Exception e) {
	      e.printStackTrace();
	      Assert.fail(e.getMessage());
	    }
	  }

	  @Test
	  public void testTextContained() {
	    try{
	    Message email = emailUtils.getMessagesBySubject("rechnung", true, 5)[0];
	    Assert.assertTrue(emailUtils.isTextInMessage(email, "Mobilfunkrechnung"), "Approval message is not in email");
	    } catch (Exception e) {
	      e.printStackTrace();
	      Assert.fail(e.getMessage());
	    }
	  }
	  
}
