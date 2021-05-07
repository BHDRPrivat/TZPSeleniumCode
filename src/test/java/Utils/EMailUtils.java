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
				//Pattern linkPattern = Pattern.compile("http=\"([^\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
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
		String subject ="";
		Integer PositionEMailmitBetreff = null;

		// Keine Mails vorhanden, die Kontrolle abbrechen
        if (emails==null) {
			Rueckgabe = false;
			System.out.println("------- Keine Mails zur Prüfung übergeben -----------");
			System.out.println("------------------- Ende Mails prüfen ---------------");
			return Rueckgabe;
        }
		
		
		// Alle Mails durchsuchen, ob der Betreff vorhanden ist
		for (int i = 0; i < emails.length; i++) {
			subject = emails[i][2].toString();
			if (subject.equals(betreff)) {
				// Betreff ist vorhanden
				PositionEMailmitBetreff = i;
				test.log(Status.INFO, "Betreff: " + subject + " ist vorhanden.");
				System.out.println("Betreff: " + subject + " ist vorhanden.");
			}
		}
		// Die Mail mit dem Betreff gefunden, weitere Prüfungen durchführen
		if (PositionEMailmitBetreff != null) {

			String from = emails[PositionEMailmitBetreff][1].toString();
			subject = emails[PositionEMailmitBetreff][2].toString();
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
					System.out.println("------------------- Ende Mails prüfen ---------------");
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
					System.out.println("------------------- Ende Mails prüfen ---------------");
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
					System.out.println("------------------- Ende Mails prüfen ---------------");
					Rueckgabe = false;
					return Rueckgabe;
				}
			} // Link2-Vorgabe ist Leer, keine Kontrolle

			System.out.println("------------------- Ende Mails prüfen ---------------");
			return Rueckgabe;

		} // Betreff ist nicht vorhanden
		else {
			test.log(Status.INFO, "Betreff: " + subject + " ist nicht vorhanden.");
			System.out.println("------------------- Betreff nicht vohanden ---------------");
			System.out.println("------------------- Ende Mails prüfen ---------------");
			Rueckgabe = false;
			return Rueckgabe;
		}
		
		
	}

}
