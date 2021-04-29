package TopZinsPortal;


	import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.IntStream;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

	public class TZPCheckingMails {

		
		
		   public static void main(String[] args) {

//			      String host = "pop.gmail.com";// change accordingly
//			      String mailStoreType = "pop3s";
//			      // String username = "TZPBorrowermail@gmail.com";// change accordingly
//			      String username = "TZPseveralusergn1@gmail.com";// change accordingly
//			      String password = "Test12345!";// change accordingly
//
//			      check(host, mailStoreType, username, password);
			   

			      String EMailAdresse = "tzpseveralusergn1@gmail.com";// change accordingly
			      String Passwort = "Test12345!";// change accordingly
			      String Betreff = "Herzlichen Dank für Ihre Registrierung auf dem FORSA TopZinsPortal";
	              String Anhang = "Vollständige-Registrierung-TopZinsPortal.pdf"; 
			      //Utils.SeleniumUtils.IstEmailVorhanden(EMailAdresse, Passwort, Betreff);
			      Utils.SeleniumUtils.EmailAttachments(EMailAdresse, Passwort, Betreff, Anhang);
		   }	
		
		
		
	   public static void check(String host, String storeType, String user,
	      String password) 
	   {
	      try {

	      // create properties field
	      Properties properties = new Properties();

	      properties.put("mail.pop3s.host", host);
	      properties.put("mail.pop3s.port", "995");
	      properties.put("mail.pop3s.starttls.enable", "true");

	      // Setup authentication, get session
	      Session emailSession = Session.getInstance(properties,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(
	                  user, password);
	            }
	         });
	      // emailSession.setDebug(true);

	      // create the POP3 store object and connect with the pop server
	      Store store = emailSession.getStore(storeType);

	      store.connect();

	      // create the folder object and open it
	      Folder emailFolder = store.getFolder("INBOX");
	      emailFolder.open(Folder.READ_ONLY);

	      // retrieve the messages from the folder in an array and print it
	      Message[] messages = emailFolder.getMessages();
	      System.out.println("messages.length---" + messages.length);
	      Integer Anzahl = messages.length;
	      // Anzahl der Mails auf 10 beschränken
	      if (messages.length > 10) {
        	  Anzahl =10; 
          }
         
      
	      for (int i = 0, n = Anzahl; i < n; i++) {
	    	 Message message = messages[i];
	    	 Part messagePart = message;
	    	 Object content = message.getContent();
	         System.out.println("---------------------------------");
	         System.out.println("Email Number " + (i + 1));
	         System.out.println("Subject: " + message.getSubject());
	         System.out.println("From: " + message.getFrom()[0]);
	         System.out.println("Text: " + message.getContent().toString());  
       
	         
	         // Beachte, Nachfolgender Code verändert die Mails in der Inbox derart,
	         // Dass die Mails als "gelesen" markiert und danach nicht mehr angezeigt werden.
	         // Die Einstellungen von Pop3 müssen jedesmal angepasst werden
	         // All Settings / Forwarding and POP/IMAP / POP downlaod -> Option "Enable Pop for all mail" auswählen / Save changes
	         
	            // -- or its first body part if it is a multipart message --
	            // Durch den Zugriff auf den Body, wird die Nachricht als gelesen markiert
	            if (content instanceof Multipart) {
	                messagePart = ((Multipart) content).getBodyPart(0);
	                System.out.println("[ Multipart Message ]");
	            }
	         
	            // -- Get the content type --
	            String contentType = messagePart.getContentType();
	            // -- If the content is plain text, we can print it --
	            System.out.println("CONTENT:" + contentType);   
	            String meld= "\n-------------------------------------------------------";
	            if (contentType.startsWith("text/plain") || contentType.startsWith("text/html")) {
	                InputStream is = messagePart.getInputStream();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	                String thisLine;
	               
	                while (( thisLine= reader.readLine()) != null) {
	                    System.out.println("thisLine: "+thisLine);
	                    meld =  meld +"\n"+ thisLine;
	                }
	             }
	            System.out.println("-----------------------------");
	          
	            // System.out.println("E-Mail-Inhalt: "+meld);
	         
	      }

	      
	      // close the store and folder objects
	      emailFolder.close(false);
	      store.close();

	      } catch (NoSuchProviderException e) {
	         e.printStackTrace();
	      } catch (MessagingException e) {
	         e.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }



	}
	
	
	
