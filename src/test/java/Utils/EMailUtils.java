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

	public static Integer AnzahlGMails(String userName, String password, ExtentTest test) {

		System.out.println("------------------- Start Anzahl der Mails ---------------");
		Integer AnzahlMails = 0;
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
			AnzahlMails = arrayMessages.length;

			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for pop3.");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		}
		test.log(Status.INFO, "Anzahl Mails " + AnzahlMails + " für " + userName);
		System.out.println("Anzahl der Mails :" + AnzahlMails + " für " + userName);
		System.out.println("------------------- Ende Anzahl der Mails ---------------");
		return AnzahlMails;
	}

	public static Object[][] EmailLesen(Integer AnzahlMails, String userName, String password, ExtentTest test) {

		// Container für die EMails
		System.out.println("------------------- Start Mails einlesen ---------------");
		Object EMails[][] = new Object[AnzahlMails][8];
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
			// System.out.println("messages.length---" + arrayMessages.length);

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
				} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
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

				// Pattern linkPattern = Pattern.compile(" <a\\b[^>]*href=\"[^>]*>(.*?)</a>",
				// Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
				Pattern linkPattern = Pattern.compile("href=\"([^\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher pageMatcher = linkPattern.matcher(messageContent);

				while (pageMatcher.find()) {
					System.out.println("Ein Link gefunden");
					links.add(pageMatcher.group());
				}

				for (String temp : links) {
					System.out.println("Link: " + temp);
					EMails[(i)][6] = EMails[(i)][6] + "Link: " + temp;
					if (temp.contains("Registrierung")) {
						System.out.println("Registrierung : " + temp);
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
		test.log(Status.INFO, "E-Mail für die E-Mail-Adesse: " + userName + " wurden eingelesen.");
		System.out.println("------------------- Ende Mails einlesen ---------------");
		return EMails;
	}

	public static boolean EmailPruefen(Object emails[][], String betreff, String anhang, String link1, String link2,
			String registrierung, ExtentTest test) {

		System.out.println("------------------- Start Mails prüfen ---------------");

		boolean Rueckgabe = true;
		String attachFiles = "";
		String links = "";
		String registeres = "";
		Integer PositionEMailmitBetreff = null;

		// Alle Mails durchsuchen, ob der Betreff vorhanden ist
		for (int i = 0; i < emails.length; i++) {
			String subject = emails[i][2].toString();
			if (subject.equals(betreff)) {
				// Betreff ist vorhanden
				PositionEMailmitBetreff = i;
				test.log(Status.INFO, "Betreff: " + subject + " ist vorhanden.");
				System.out.println("Betreff: " + subject + " ist vorhanden.");
			}
		}

		if (PositionEMailmitBetreff != null) {

			String from = emails[PositionEMailmitBetreff][1].toString();
			String subject = emails[PositionEMailmitBetreff][2].toString();
			String sentDate = emails[PositionEMailmitBetreff][3].toString();
			String contentType = emails[PositionEMailmitBetreff][4].toString();
			if (emails[PositionEMailmitBetreff][5] != null) {
				attachFiles = emails[PositionEMailmitBetreff][5].toString();
			}
			if (emails[PositionEMailmitBetreff][6] != null) {
				links = emails[PositionEMailmitBetreff][6].toString();
			}
			if (emails[PositionEMailmitBetreff][7] != null) {
				registeres = emails[PositionEMailmitBetreff][7].toString();
			}
			// ist der Anhang vorhanden?
			if (anhang != "") {
				// entspricht der Anhang dem der Mail?
				if (attachFiles.contains(anhang)) {
					test.log(Status.INFO, "Anhang: " + attachFiles + " stimmt überein: " + anhang);
					System.out.println("Anhang: " + attachFiles + " stimmt überein: " + anhang);
				} else {
					test.log(Status.ERROR, "Anhang: " + attachFiles + " stimmt nicht überein: " + anhang);
					System.out.println("Anhang: " + attachFiles + " stimmt nicht überein: " + anhang);
					Rueckgabe = false;
					return Rueckgabe;
				}
			} // Anhang-Vorgabe ist Leer, keine Kontrolle

			// ist der Link1 vorhanden?
			if (link1 != "") {
				if (links.contains(link1)) {
					test.log(Status.INFO, "Link1: " + link1 + " ist vorhanden");
					System.out.println("Link1: " + link1 + " ist vorhanden");
				} else {
					test.log(Status.ERROR, "Link1: " + link1 + " ist nicht vorhanden");
					System.out.println("Link1: " + link1 + " ist nicht vorhanden");
					Rueckgabe = false;
					return Rueckgabe;
				}
			} // Link1-Vorgabe ist Leer, keine Kontrolle

			// ist der Link2 vorhanden?
			if (link2 != "") {
				if (links.contains(link2)) {
					test.log(Status.INFO, "Link2: " + link2 + " ist vorhanden");
					System.out.println("Link2: " + link2 + " ist vorhanden");
				} else {
					test.log(Status.ERROR, "Link2: " + link2 + " ist nicht vorhanden");
					System.out.println("Link2: " + link2 + " ist nicht vorhanden");
					Rueckgabe = false;
					return Rueckgabe;
				}
			} // Link2-Vorgabe ist Leer, keine Kontrolle

			System.out.println("------------------- Ende Mails prüfen ---------------");
			return Rueckgabe;

		} // Betreff ist nicht vorhanden
		else {
			System.out.println("------------------- Betreff nicht vohanden ---------------");
			System.out.println("------------------- Ende Mails prüfen ---------------");
			Rueckgabe = false;
			return Rueckgabe;
		}
	}

	public static void check(String host, String storeType, String user, String password) {
		try {

			// create properties field
			Properties properties = new Properties();

			properties.put("mail.imap.host", host);
			properties.put("mail.imap.port", "993");
			properties.put("mail.imap.starttls.enable", "true");
			properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.setProperty("mail.imap.socketFactory.fallback", "false");
			properties.setProperty("mail.imap.socketFactory.port", String.valueOf(993));
			Session emailSession = Session.getDefaultInstance(properties);

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("imap");

			store.connect(host, user, password);

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);
			int n = messages.length;
			for (int i = 0; i < n; i++) {
				Message message = messages[i];
				ArrayList<String> links = new ArrayList<String>();
				if (message.getSubject().contains("Thank you for signing up for AppExe")) {
					String desc = message.getContent().toString();

					// System.out.println(desc);
					Pattern linkPattern = Pattern.compile(" <a\\b[^>]*href=\"[^>]*>(.*?)</a>",
							Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
					Matcher pageMatcher = linkPattern.matcher(desc);

					while (pageMatcher.find()) {
						links.add(pageMatcher.group());
					}
				} else {
					System.out.println("Email:" + i + " is not a wanted email");
				}
				for (String temp : links) {
					if (temp.contains("user-register")) {
						System.out.println(temp);
					}
				}

				/*
				 * System.out.println("---------------------------------");
				 * System.out.println("Email Number " + (i + 1)); System.out.println("Subject: "
				 * + message.getSubject()); System.out.println("From: " + message.getFrom()[0]);
				 * System.out.println("Text: " + message.getContent().toString());
				 */

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

	public static boolean IstEmailVorhanden(String EMailAdresse, String Passwort, String Betreff) {
		// Greift auf das G-Mail-Konto zu und überprüft, ob die Betreffzeile indentsich
		// ist mit dem Betreff String

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
			Session emailSession = Session.getInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(EMailAdresse, Passwort);
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
				Anzahl = 10;
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
				// All Settings / Forwarding and POP/IMAP / POP downlaod -> Option "Enable Pop
				// for all mail" auswählen / Save changes

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
					System.out.println("E-Mail ist " + EMailAdresse + "  mit Betreff: " + Betreff + " ist vorhanden");
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
