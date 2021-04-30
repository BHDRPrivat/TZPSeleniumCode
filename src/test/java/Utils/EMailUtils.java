package Utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import junit.framework.Assert;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class EMailUtils {
	
	
	
	 public static Object[][] EmailLesen(String userName, String password, ExtentTest test) {
	        
		      // Container für die EMails
		      Object EMails[][] = new Object[10][10];   
		      Properties properties = new Properties();
	
	        
		      String host = "pop.gmail.com";// change accordingly
		      String mailStoreType = "pop3";
		      String port = "995";
	        
	        // server setting
	        properties.put("mail.pop3.host", host);
	        properties.put("mail.pop3.port", port);
	 
	        // SSL setting
	        properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
	        properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));
	 
	        Session session = Session.getDefaultInstance(properties);
	 
	        try {
	            // connects to the message store
	            Store store = session.getStore(mailStoreType);
	            store.connect(userName, password);
	 
	            // opens the inbox folder
	            Folder folderInbox = store.getFolder("INBOX");
	            folderInbox.open(Folder.READ_ONLY);
	 
	            // fetches new messages from server
	            Message[] arrayMessages = folderInbox.getMessages();
	            System.out.println("messages.length---" + arrayMessages.length);
	            
	            String messageContent = "";
	            
	            for (int i = 0; i < arrayMessages.length; i++) {
	                Message message = arrayMessages[i];
	                Address[] fromAddress = message.getFrom();
	                String from = fromAddress[0].toString();
	                String subject = message.getSubject();
	                String sentDate = message.getSentDate().toString();
	                String contentType = message.getContentType();
	                
	 
	                // store attachment file name, separated by comma
	                String attachFiles = "";
	 
	                if (contentType.contains("multipart")) {
	                    // content may contain attachments
	                    Multipart multiPart = (Multipart) message.getContent();
	                    int numberOfParts = multiPart.getCount();
	                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
	                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
	                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
	                            // this part is attachment
	                            String fileName = part.getFileName();
	                            attachFiles += fileName + ", ";
	                            part.saveFile("F:\\BHDR\\Zwischen\\" + File.separator + fileName);
	                        } else {
	                            // this part may be the message content
	                            messageContent = part.getContent().toString();
	                        }
	                    }
	 
	                    if (attachFiles.length() > 1) {
	                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
	                    }
	                } else if (contentType.contains("text/plain")
	                        || contentType.contains("text/html")) {
	                    Object content = message.getContent();
	                    if (content != null) {
	                       messageContent = content.toString();
	                                
	                    }
	                }
	 
	                
	                
	                // print out details of each message
	                System.out.println("Message #" + (i + 1) + ":");
	                System.out.println("\t From: " + from);
	                EMails[(i)][1] = from;
	                System.out.println("\t Subject: " + subject);
	                EMails[(i)][2] = subject;
	                System.out.println("\t Sent Date: " + sentDate);
	                EMails[(i)][3] = sentDate;
	                // System.out.println("\t Message: " + messageContent);
	                EMails[(i)][4] = messageContent;
	                System.out.println("\t Attachments: " + attachFiles);
	                EMails[(i)][5] = attachFiles;
	 
	             // Link ermitteln:
	             ArrayList<String> links = new ArrayList<String>();

	             //Pattern linkPattern = Pattern.compile(" <a\\b[^>]*href=\"[^>]*>(.*?)</a>",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	             Pattern linkPattern = Pattern.compile("href=\"([^\"]*)",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	             Matcher pageMatcher = linkPattern.matcher(messageContent);

	             while(pageMatcher.find()){
	            	 System.out.println("Ein Link gefunden");
	                 links.add(pageMatcher.group());
	             } 

	              for(String temp:links){
	            	  System.out.println(temp); 
	            	  EMails[(i)][6] = EMails[(i)][6] + "," + temp;
	            	 if(temp.contains("Registrierung")){
	                  System.out.println(temp);
	                  EMails[(i)][7] = temp;
	              }
	              }
	               
	            }
	            
	            // disconnect
	            folderInbox.close(false);
	            store.close();
	        } catch (NoSuchProviderException ex) {
	            System.out.println("No provider for pop3.");
	            ex.printStackTrace();
	        } catch (MessagingException ex) {
	            System.out.println("Could not connect to the message store");
	            ex.printStackTrace();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        return EMails;   
	 }
	
	
	
	
	
    public static boolean EmailPruefen(Object Emails[][], String betreff, String anhang, ExtentTest test) {
                  boolean Rueckgabe = false;
    	
    	//        Properties properties = new Properties();
// 
//	      String host = "pop.gmail.com";// change accordingly
//	      String mailStoreType = "pop3";
//	      String port = "995";
//        
//        // server setting
//        properties.put("mail.pop3.host", host);
//        properties.put("mail.pop3.port", port);
// 
//        // SSL setting
//        properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
//        properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));
// 
//        Session session = Session.getDefaultInstance(properties);
// 
//        try {
            // connects to the message store
//            Store store = session.getStore(mailStoreType);
//            store.connect(userName, password);
// 
//            // opens the inbox folder
//            Folder folderInbox = store.getFolder("INBOX");
//            folderInbox.open(Folder.READ_ONLY);
// 
//            // fetches new messages from server
//            Message[] arrayMessages = folderInbox.getMessages();
//            System.out.println("messages.length---" + arrayMessages.length);
//            
//            String messageContent = "";
            
            for (int i = 0; i < Emails.length; i++) {
               if (Emails[i][1] != null) {
                String from = Emails[i][1].toString();
                String subject = Emails[i][2].toString();
                String sentDate =Emails[i][3].toString();
                String contentType = Emails[i][4].toString();
                // Überprügung des Subjects
                System.out.println(subject + " ---" + betreff);
                if (subject.equals(betreff)) {
                	Rueckgabe = true;	
                }
            
               }
            }
            return Rueckgabe;
            
//                // store attachment file name, separated by comma
//                String attachFiles = "";
// 
//                if (contentType.contains("multipart")) {
//                    // content may contain attachments
//                    Multipart multiPart = (Multipart) message.getContent();
//                    int numberOfParts = multiPart.getCount();
//                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
//                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
//                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
//                            // this part is attachment
//                            String fileName = part.getFileName();
//                            attachFiles += fileName + ", ";
//                            part.saveFile("F:\\BHDR\\Zwischen\\" + File.separator + fileName);
//                        } else {
//                            // this part may be the message content
//                            messageContent = part.getContent().toString();
//                        }
//                    }
// 
//                    if (attachFiles.length() > 1) {
//                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
//                    }
//                } else if (contentType.contains("text/plain")
//                        || contentType.contains("text/html")) {
//                    Object content = message.getContent();
//                    if (content != null) {
//                       messageContent = content.toString();
//                                
//                    }
//                }
// 
//                
//                
//                // print out details of each message
//                System.out.println("Message #" + (i + 1) + ":");
//                System.out.println("\t From: " + from);
//                System.out.println("\t Subject: " + subject);
//                System.out.println("\t Sent Date: " + sentDate);
//                // System.out.println("\t Message: " + messageContent);
//                System.out.println("\t Attachments: " + attachFiles);
//            
// 
//             // Link ermitteln:
//             ArrayList<String> links = new ArrayList<String>();
//
//             //Pattern linkPattern = Pattern.compile(" <a\\b[^>]*href=\"[^>]*>(.*?)</a>",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
//             Pattern linkPattern = Pattern.compile("href=\"([^\"]*)",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
//             Matcher pageMatcher = linkPattern.matcher(messageContent);
//
//             while(pageMatcher.find()){
//            	 System.out.println("Ein Link gefunden");
//                 links.add(pageMatcher.group());
//             } 
//
//              for(String temp:links){
//            	  System.out.println(temp); 
//            	 if(temp.contains("Registrierung")){
//                  System.out.println(temp);
//              }
//              }
//            
//            }
//            
//            // disconnect
//            folderInbox.close(false);
//            store.close();
//        } catch (NoSuchProviderException ex) {
//            System.out.println("No provider for pop3.");
//            ex.printStackTrace();
//        } catch (MessagingException ex) {
//            System.out.println("Could not connect to the message store");
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
	
    
    public static void check(String host, String storeType, String user, String password) 
    {
       try {

       //create properties field
       Properties properties = new Properties();

       properties.put("mail.imap.host",host);
       properties.put("mail.imap.port", "993");
       properties.put("mail.imap.starttls.enable", "true");
       properties.setProperty("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory");
         properties.setProperty("mail.imap.socketFactory.fallback", "false");
         properties.setProperty("mail.imap.socketFactory.port",String.valueOf(993));
       Session emailSession = Session.getDefaultInstance(properties);

       //create the POP3 store object and connect with the pop server
       Store store = emailSession.getStore("imap");

       store.connect(host, user, password);

       //create the folder object and open it
       Folder emailFolder = store.getFolder("INBOX");
       emailFolder.open(Folder.READ_ONLY);

       // retrieve the messages from the folder in an array and print it
       Message[] messages = emailFolder.getMessages();
       System.out.println("messages.length---" + messages.length);
          int n=messages.length;
          for (int i = 0; i<n; i++) {
          Message message = messages[i];
          ArrayList<String> links = new ArrayList<String>();
          if(message.getSubject().contains("Thank you for signing up for AppExe")){
          String desc=message.getContent().toString();

        // System.out.println(desc);
        Pattern linkPattern = Pattern.compile(" <a\\b[^>]*href=\"[^>]*>(.*?)</a>",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
         Matcher pageMatcher = linkPattern.matcher(desc);

         while(pageMatcher.find()){
             links.add(pageMatcher.group());
         } 
          }else{
          System.out.println("Email:"+ i + " is not a wanted email");
          }
          for(String temp:links){
          if(temp.contains("user-register")){
              System.out.println(temp);
          }
          }

          /*System.out.println("---------------------------------");
          System.out.println("Email Number " + (i + 1));
          System.out.println("Subject: " + message.getSubject());
          System.out.println("From: " + message.getFrom()[0]);
          System.out.println("Text: " + message.getContent().toString());*/

       }
       //close the store and folder objects
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
    
	
    public static boolean IstEmailVorhanden(String EMailAdresse, String Passwort, String Betreff) {
    	// Greift auf das G-Mail-Konto zu und überprüft, ob die Betreffzeile indentsich ist mit dem Betreff String
    	
	      String host = "pop.gmail.com";// change accordingly
	      String mailStoreType = "pop3s";
	      
	      Boolean Rueckgabe = false;
	      
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
	            		   EMailAdresse, Passwort);
	            }
	         });
	      // emailSession.setDebug(true);

	      // create the POP3 store object and connect with the pop server
	      Store store = emailSession.getStore(mailStoreType);

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
	         // System.out.println("Name Anhang: " + message.ATTACHMENT.chars().toString()); 
	         
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
	         
	            
	            
//	            // -- Get the content type --
//	            String contentType = messagePart.getContentType();
//	            // -- If the content is plain text, we can print it --
//	            System.out.println("CONTENT:" + contentType);   
//	            String meld= "\n-------------------------------------------------------";
//	            if (contentType.startsWith("text/plain") || contentType.startsWith("text/html")) {
//	                InputStream is = messagePart.getInputStream();
//	                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//	                String thisLine;
//	               
//	                while (( thisLine= reader.readLine()) != null) {
//	                    System.out.println("thisLine: "+thisLine);
//	                    meld =  meld +"\n"+ thisLine;
//	                }
//	             }
//	            System.out.println("-----------------------------");
//	          
//	            // System.out.println("E-Mail-Inhalt: "+meld);

	  	      if ((message.getSubject()).equals(Betreff)) {
	  	    	System.out.println("E-Mail ist " + EMailAdresse + "  mit Betreff: " +  Betreff + " ist vorhanden");
	  	    	Rueckgabe = true;
		      } 
		   	        
	            
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
	      
	      return Rueckgabe;
    }
        
  	


}



