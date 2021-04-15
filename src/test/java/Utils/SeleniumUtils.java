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

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import junit.framework.Assert;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class SeleniumUtils {
	
	
	public static void ApplitoolsAufnahme(WebDriver driver, String appName, String testName, String optionalTag, ExtentTest test) {
		
		// public ChromeDevToolsService devToolsService = null;
		// Variable für Applitools
		Eyes eyes = null;
		
		    // Applitools vorbereiten
			eyes = new Eyes();
			eyes.setApiKey("1epbTsh91uyej6yur9x0FzJb3WUit5naoVB8SYMRZUE110");
			try {

				// eyes.check("AL_Risiko", Target.window().fully());

				// eyes.open(driver, "AL-Risiko", "Testfall: " + teststep, new
				// RectangleSize(800, 600));

				// eyes.open(driver, "IR-PHV", "Testfall: " + teststep,
				// Target.window().fully());

				// Kein Rectangle vorgeben um die Voreinstellung zu verwenden.
				eyes.open(driver, appName, testName);
				
				eyes.checkWindow(optionalTag);
				
				eyes.close();

			} finally {

			}
	}
	
	
	
	
	public static void DragDrop(WebDriver driver, Integer Zeitspanne, String Drag, String Drop, ExtentTest test) throws InterruptedException {
		// Routine zum Verschieben von Objekten
		
		// Zeitpsanne
		Thread.sleep(5 * Zeitspanne);	

		
		System.out.println("Drag = " + Drag );
		System.out.println("Drop = " + Drop );
		
		// Für die Auswahl der Eingabelemente sollte das Fenster erst verschoeben werden.
		// Create object of actions class
		Actions act=new Actions(driver);

		// Find element xpath which we need to drag
		WebElement dragelement =driver.findElement(By.xpath(Drag));
		
		// Find element xpath which we need to drop
		WebElement dropelement =driver.findElement(By.xpath(Drop));			
		
	
		// Click &amp; Hold drag Webelement
		act.clickAndHold(dragelement).build().perform();
		 
		// Move to drop Webelement
		act.moveToElement(dropelement).build().perform();
		 
		// Release drag webelement into drop webelement
		act.release(dropelement).build().perform();
		
		System.out.println("Nach Drag & Drop");
		
		// 10. zeit zum Aufbauen der Meldung 
		Thread.sleep(5 * Zeitspanne);	
	}
	
	
	
	
	
	
	public static void AuschreibungGNAuswahl(WebDriver driver, Integer Zeitspanne, String Suchstring, ExtentTest test) throws InterruptedException {
		// Anhand eines Suchstring, wird die Auswahl des Tabellenelements angewählt
		// Dei Eingabe in Excel muss mit "." erfolgen -> 200.000
		
		List<WebElement> allAusschreibungen = driver.findElements(By.xpath("(//ul//p[3])"));
		List<String> AuschreibungIDs = new ArrayList<String>();

			
		System.out.println("Suchstring: " + Suchstring );
		Thread.sleep(3 * Zeitspanne);
		// Ermittlung einer Position eines Eintrags
		int position = 0;

		for (WebElement AusschreibungID : allAusschreibungen) {
			AuschreibungIDs.add(AusschreibungID.getText());
		position++;
		// überprüfen, ob der Wert vorhanden ist, nicht ob er gleich ist, da Anzeige aus Teilelementen zusammengebaut wird.
		// Die Eingabe in der Excel-Tabelle muss als Text formatiert werden.
		if (AusschreibungID.getText().contains(Suchstring)) {
      	break;
		}
		}
		System.out.println("position : " + position);
		// Klick auf Button in der Zeile:
    	Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "(//ul//p[3])[" + position +"]", test);
		
    	Thread.sleep(3 * Zeitspanne);
		
	}
	
	
	
	public static void AusschreibungBankenAuswahl(WebDriver driver, Integer Zeitspanne, String Suchstring, ExtentTest test) throws InterruptedException {
		// Anhand eines Suchstring, wird die Auswahl des Tabellenelements angewählt
		
		List<WebElement> allBanks = driver.findElements(By.xpath("//tr//td[2]//p"));
		List<String> bankIDs = new ArrayList<String>();

		System.out.println("Suchstring: " + Suchstring );
		Thread.sleep(3 * Zeitspanne);
		if (Suchstring.equals("")) {
			// Bei leeren Eintrag passiert nichts
		}
		else {
		// Ermittlung einer Position eines Eintrags
		int position = 0;

		for (WebElement bankID : allBanks) {
		bankIDs.add(bankID.getText());
		position++;
		if (bankID.getText().equals(Suchstring)) {
		break;
		}
		}
		System.out.println("position : " + position);
		// Klick auf Button in der Zeile:
    	Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//tr[" + position +"]//input[contains(@class, 'jss')]", test);
		}
    	Thread.sleep(3 * Zeitspanne);
		
	}
	
	public static void InputDatum(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath1, String ObjektPath2, String Inputwert, ExtentTest test) throws InterruptedException {
		// Inputwert zu einem gültigen und aktuellen Datum wandeln 
		Calendar myCal = new GregorianCalendar(); 
		
		// Zeit setzen mit Date:
		myCal.setTime( new Date() );
		// Einzelne Felder extrahieren:
		int Jahr = myCal.get( Calendar.YEAR  );
		int Monat = myCal.get( Calendar.MONTH ) + 1;           // nicht vergessen die 1 zu addieren
		int Tag = myCal.get( Calendar.DATE  );
        // Variable für das Setzen einer führenden "0" bei einstelligen Angaben
		String Tag$ ="";
	    String Monat$ ="";
		
		System.out.println("Tag: " + Tag + " Monat: "+ Monat + " Jahr:" + Jahr);
		
		
		System.out.println("Eingang: " + Inputwert);
		if (Inputwert.equals("ActDatum")) {
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter df;
	        df = DateTimeFormatter.ofPattern("dd.MM.yyyy"); // Format: 31.01.2016
      		Inputwert = now.format(df);
    	}
		// Die Verschiebung um Tage wird analysiert
    	if ((Inputwert.substring(0, 10)).equals("ActDatum +")){
			System.out.println("Spezialdatum: " + Inputwert);
			// System.out.println("left 0 bis 10: " + Inputwert.substring(0, 10));
			int ZusatzTage = Integer.valueOf((Inputwert.substring(10)).trim());
			if (Tag+ZusatzTage > 28) {
				Tag = ZusatzTage;
				if (Monat < 11) {
					Monat = Monat +1 ;					
				}
				else {
					Monat = 1;
					Jahr = Jahr + 1;
				}
			}
			else {
				Tag =Tag + ZusatzTage;
			}
			if (Tag < 10) {
			 Tag$ = ("0" + Tag);
			}
			else {
				Tag$ = String.valueOf(Tag);
				}
			if (Monat < 10) {	
			   Monat$ = ("0" + Monat);	
			}
			else {
				Monat$ = String.valueOf(Monat);
			}
			Inputwert = (Tag$ + "." + Monat$ + "." + Jahr);
		}
		
    	String ObjektPath = ObjektPath1 + ObjektPath2;
   		
		System.out.println("Ausgang: " + Inputwert);
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		
			
		Thread.sleep(3 * Zeitspanne);
		try {	
			if 	(HTMLSelector.equals("name")) {
				driver.findElement(By.name(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.name(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.name(ObjektPath)).clear();
				driver.findElement(By.name(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			if 	(HTMLSelector.equals("xpath")) {
				driver.findElement(By.xpath(ObjektPath)).click();
				System.out.println(ObjektPath + "geklickt");
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.xpath(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.xpath(ObjektPath)).clear();
				driver.findElement(By.xpath(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			if 	(HTMLSelector.equals("id")) {
				driver.findElement(By.id(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.id(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.id(ObjektPath)).clear();
				driver.findElement(By.id(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			
			ObjektPath =ObjektPath1 + "//following::button";

			// Zur Wochenendkorrektur den Kalender-Icon 2 mal auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", ObjektPath, test);
			System.out.println("Kalender-Icon geklickt: " + ObjektPath);
			Thread.sleep(3 * Zeitspanne);
			// Tabelle schließen durch Klick auf den automatisch gewählten Kalendertag (dann kein Wochenende)
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@class, 'MuiPickersDay-daySelected')]", test);
			System.out.println("Tag geklickt: ");
	
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}
	
	
	public static void InputText(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Inputwert, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		
		System.out.println("Objekt: " + ObjektPath);
		System.out.println("Wert: " + Inputwert);
		
		
		
		Thread.sleep(3 * Zeitspanne);
		try {	
			if 	(HTMLSelector.equals("name")) {
				driver.findElement(By.name(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.name(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.name(ObjektPath)).clear();
				driver.findElement(By.name(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			if 	(HTMLSelector.equals("xpath")) {
				
				// Es erfolgt ein Scrollen zum Element 
				// Nur wenn es im sichtbaren Bereich liegt, kann Click ausgeführt werden
  				JavascriptExecutor js = (JavascriptExecutor)driver;
  				WebElement element = driver.findElement(By.xpath(ObjektPath));
  				// Das Element mittig im Bildschirm positionieren, um den garantierten Zugriff zu erhalten.
  				js.executeScript("arguments[0].scrollIntoView({block: \"center\", inline: \"center\"});", element);
				// Führt nicht immer zum erfolg. Wenn das Obljekt im sichtbaren Bereich liegt, aber überlagert wird.
  				// js.executeScript("arguments[0].scrollIntoViewIfNeeded({block: \"center\", inline: \"center\"});", element);
				
				
				
				driver.findElement(By.xpath(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.xpath(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.xpath(ObjektPath)).clear();
				driver.findElement(By.xpath(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			if 	(HTMLSelector.equals("id")) {
				driver.findElement(By.id(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.id(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.id(ObjektPath)).clear();
				driver.findElement(By.id(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}



		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}

	
	protected static boolean isElementPresent(WebDriver driver, By by){
        try{
            driver.findElement(by);
            return true;
        }
        catch(NoSuchElementException e){
            return false;
        }
    }

	
	public static void OKButtonKlick(WebDriver driver, Integer Zeitspanne, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		// Zeitspanne setzen
	
	    // Nur wenn der "OK"-Button angezeigt wird, erfolgt ein Klick auf dem Button bevor es normal weiter geht. 
		// Dann müssen die ganzen OK-klicks nicht seperat erfasst werden.

		String element = "//div[@role='dialog']//span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')]";
		
		if ( isElementPresent(driver, By.xpath(element))){
			driver.findElement(By.xpath(element)).click(); 
			Thread.sleep(4 * Zeitspanne);
		} // Wenn "OK"-Vorhanden dann geklickt
		
	
//		if ( isElementPresent(driver, By.xpath("//span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')]"))){
//			driver.findElement(By.xpath("//span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')]")).click(); 
//			Thread.sleep(2 * Zeitspanne);
//		} // Wenn "OK"-Vorhanden dann geklickt

	
	}	
	
	
	
	
	public static void ButtonKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		// Zeitspanne setzen
	
	
			Thread.sleep(3 * Zeitspanne);
		try {
			if 	(HTMLSelector.equals("xpath")) {
				
				JavascriptExecutor js = (JavascriptExecutor)driver;
     			WebElement element = driver.findElement(By.xpath(ObjektPath));
				
				// Es erfolgt ein scrollen zum Element 
				// Nur wenn es im sichtbaren Bereich liegt, kann Click ausgeführt werden
  				// Das Element mittig im Bildschirm positionieren, um den garantierten Zugriff zu erhalten.
  				js.executeScript("arguments[0].scrollIntoView({block: \"center\", inline: \"center\"});", element);
  				System.out.println("zur Mitte bewegt: "+ element);
  				
				Thread.sleep(2 * Zeitspanne);

				driver.findElement(By.xpath(ObjektPath)).click();
				System.out.println("geklickt: "+ ObjektPath);
				
				// Zeitspanne setzen
				Thread.sleep(4 * Zeitspanne);
			}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
		
	}	

	
	public static void TabelleButtonKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String FirmaGN, String ZinssatzGG,  ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		// Zeitspanne setzen
		Thread.sleep(3 * Zeitspanne);
		String ObjektPath ="Tabellen-Zugriff über " + FirmaGN + " und "+ ZinssatzGG;
		try {
			if 	(HTMLSelector.equals("xpath")) {
				// Aus den Banknamen und den Zinsatz den xpath-Zugriff generieren.
				// Vorlage  "//p[text()='FORSA Digital Bank']//ancestor::tr//div[contains(text(), '0,06')]//ancestor::td"
				ObjektPath = null;
				ObjektPath = "//p[text()='" + FirmaGN + "']//ancestor::tr//div[contains(text(), '" + ZinssatzGG + "')]//ancestor::td";
				System.out.println("Tabellenzugriff: " + ObjektPath);
				
				// Es erfolgt ein Scrollen zum Element 
				// Nur wenn es im sichtbaren Bereich liegt, kann Click ausgeführt werden
  				JavascriptExecutor js = (JavascriptExecutor)driver;
  				WebElement element = driver.findElement(By.xpath(ObjektPath));
  				// Das Element mittig im Bildschirm positionieren, um den garantierten Zugriff zu erhalten.
  				js.executeScript("arguments[0].scrollIntoView({block: \"center\", inline: \"center\"});", element);
				// Führt nicht immer zum erfolg. Wenn das Obljekt im sichtbaren Bereich liegt, aber überlagert wird.
  				// js.executeScript("arguments[0].scrollIntoViewIfNeeded({block: \"center\", inline: \"center\"});", element);
			
				
				Thread.sleep(2 * Zeitspanne);

				driver.findElement(By.xpath(ObjektPath)).click();
								
				Thread.sleep(2 * Zeitspanne);
	
			}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}

	public static void HakenKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {
			if ( !driver.findElement(By.xpath(ObjektPath)).isSelected()){
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", ObjektPath, test);	
				test.log(Status.INFO, "Auswahl des Objektes: " +ObjektPath);
			}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}


	public static void PDFUpload(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Suchbegriff, String Bereich, String DatumEintrag, ExtentTest test) throws InterruptedException, IOException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {
			// Für AutoIT
			String workingDir;
			String autoitscriptpath;
			String filepath;
			String xpathvalue;

			workingDir = System.getProperty("user.dir");
			autoitscriptpath = workingDir + "\\AutoIT\\" + "File_upload_selenium_webdriver.au";
			filepath = workingDir + "\\DummyPDF\\PDF-Dummy.pdf";

			xpathvalue="//input[contains(@value, '" + Suchbegriff +"')]//ancestor::div[contains(@class, 'jss')]" + Bereich + "//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";
			System.out.println("Zugriff = " + xpathvalue);
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
			test.log(Status.INFO, "Auswahl des Objektes: " +ObjektPath);
			// TSonderzeit zum Hochladen
			Thread.sleep(5 * Zeitspanne);


			// Auf das Leerzeichen vor " \" achten!
			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "PDF-Datei auswählen: " +" cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);


			// Datum ins Ausstellungsdatum, erst nachdem das Dokument hochgeladen wurde 
			// xpathvalue ="//input[contains(@value, '" + Suchbegriff + "')]//ancestor::div[contains(@class, 'jss')]" + Bereich +"//input[contains(@class, 'MuiOutlinedInput-inputAdornedEnd')]";

			xpathvalue ="//input[contains(@value, '" + Suchbegriff + "')]//ancestor::div[contains(@class, 'jss')]" + Bereich + "//label[text()='Ausstellungsdatum']//following::input[contains(@class, 'MuiOutlinedInput-inputAdornedEnd')]";


			System.out.println("Zugriff = " + xpathvalue);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", xpathvalue, DatumEintrag, test);	
			test.log(Status.INFO, "Datum eintragen: " +DatumEintrag + " in Objekt: " + xpathvalue);
			// TSonderzeit zum Hochladen
			Thread.sleep(3* Zeitspanne);
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}

	}


	public static void PDFUploadListe(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Suchbegriff, String Bereich, String DatumEintrag, ExtentTest test) throws InterruptedException, IOException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(5 * Zeitspanne);
		try {
			// Für AutoIT
			String workingDir;
			String autoitscriptpath;
			String filepath;
			String xpathvalue;

			workingDir = System.getProperty("user.dir");
			autoitscriptpath = workingDir + "\\AutoIT\\" + "File_upload_selenium_webdriver.au";
			filepath = workingDir + "\\DummyPDF\\PDF-Dummy.pdf";

			xpathvalue="//*[@id=\"mui-component-select-docType\"]//ancestor::div[contains(@class, 'jss')]" + Bereich + "//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";
			System.out.println("Zugriff = " + xpathvalue);
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
			test.log(Status.INFO, "Auswahl des Objektes: " +ObjektPath);
			// TSonderzeit zum Hochladen
			Thread.sleep(5 * Zeitspanne);


			// Auf das Leerzeichen vor " \" achten!
			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
			test.log(Status.INFO, "PDF-Datei auswählen: " +" cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);


			// Datum ins Ausstellungsdatum, erst nachdem das Dokument hochgeladen wurde 
			// xpathvalue ="//input[contains(@value, '" + Suchbegriff + "')]//ancestor::div[contains(@class, 'jss')]" + Bereich +"//input[contains(@class, 'MuiOutlinedInput-inputAdornedEnd')]";

			xpathvalue ="//*[@id=\"mui-component-select-docType\"]//ancestor::div[contains(@class, 'jss')]" + Bereich + "//label[text()='Ausstellungsdatum']//following::input[contains(@class, 'MuiOutlinedInput-inputAdornedEnd')]";


			System.out.println("Zugriff = " + xpathvalue);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", xpathvalue, DatumEintrag, test);	
			test.log(Status.INFO, "Datum eintragen: " +DatumEintrag + " in Objekt: " + xpathvalue);
			// TSonderzeit zum Hochladen
			Thread.sleep(5* Zeitspanne);
			
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}

	}




	public static void ListenAuswahl(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String ObjektPathListe, String Listenelement, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {
			if 	(HTMLSelector.equals("xpath")) { 
				// Zuerst muss auf das übergeordnete div geklickt werden
				driver.findElement(By.xpath(ObjektPath)).click();
				
				// Es erfolgt ein Scrollen zum Element 
				// Nur wenn es im sichtbaren Bereich liegt, kann Click ausgeführt werden
  				JavascriptExecutor js = (JavascriptExecutor)driver;
  				WebElement element = driver.findElement(By.xpath(ObjektPath));
  				// Das Element mittig im Bildschirm positionieren, um den garantierten Zugriff zu erhalten.
  				js.executeScript("arguments[0].scrollIntoView({block: \"center\", inline: \"center\"});", element);
  				Thread.sleep(2 * Zeitspanne);	
  				
				// Es muss noch einmal auf das entsprechende Listenelement geklickt werden
				driver.findElement(By.xpath(ObjektPathListe + Listenelement + "')]")).click();
				test.log(Status.INFO, "Im Listenelement: " + ObjektPathListe + " Ausgwählt: " + Listenelement);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);	
			}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}	

	
	
	
	public static void FullPageScreenshotAShotSelenium(WebDriver driver, Integer Zeitspanne, String projectpath,  String Kennzeichnung, String teststep, ExtentTest test) throws IOException, InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {
			// für die Aufnahme den Zoom verkleinern
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("document.body.style.zoom = '75%';");
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
					.takeScreenshot(driver);
			// Screenshot screenshot = new
			// AShot().shootingStrategy(ShootingStrategies.scaling(2)).takeScreenshot(driver);
			ImageIO.write(screenshot.getImage(), "PNG",
					new File(projectpath + "\\screenshots\\" + Kennzeichnung + " " + teststep + ".png"));
			test.log(Status.INFO, "Screenshot aufgenommen: " + projectpath + "\\screenshots\\" + Kennzeichnung + " " + teststep + ".png");
			// Für den weiteren Ablauf Zoom wieder auf 100% setzen
			executor.executeScript("document.body.style.zoom = '100%';");
			Thread.sleep(3 * Zeitspanne);

		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Versuch Screenshot: " +  projectpath + "\\screenshots\\" + Kennzeichnung + " " + teststep + ".png");
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}	


	public static void BrowserBeenden(WebDriver driver, Integer Zeitspanne,ExtentReports extent, Eyes eyes) throws InterruptedException {
		// calling flush writes everything to the log file
		Thread.sleep(3 * Zeitspanne);
		extent.flush();

		Thread.sleep(3000);
		System.out.println("Test erfolgreich druchlaufen");
		if (driver != null) {
			driver.quit();
		}
		if (eyes != null) {
			eyes.close();
			eyes.abortIfNotClosed();
		}
	}	

}



