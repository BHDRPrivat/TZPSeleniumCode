package TopZinsPortal;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.applitools.eyes.selenium.Eyes;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import Utils.ExcelUtilsJXL;
import Utils.SeleniumUtils;
import jxl.read.biff.BiffException;

public class TZPStammGN {
	
	
	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen
	
	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public static String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = System.getProperty("user.dir");

	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	public String AblaufartGlobal;

	//public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;
		
		// Zu Testzwecken, direktsprung auf das Hochladen der PDF-dateien
		// Wenn alle Stammdaten eingegeben wurden, kann mit false direkt auf Dokumente zugegriffen werden
		// Boolean MissingData = true;
		Boolean MissingData = true;
		


		@Parameters({ "Ablaufart" })
		@BeforeTest
		public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart) throws InterruptedException, IOException {

			if (htmlReporter == null) {
				// start reporters
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Stammdaten GN - " + Ablaufart + ".html");
				// create ExtentReports and attach reporter(s)
				extent = new ExtentReports();
				extent.attachReporter(htmlReporter);
			}

			AblaufartGlobal = Ablaufart;
			StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
			Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();
			BaseUrl = Utils.TZPBeforeTest.Umgebung() + "/portal/login";

			// Aufruf des Browser-Setups
			driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);

		
		}
		
		private void TZPBeforeTest(String baseUrl2) {
			// TODO Auto-generated method stub
			
		}

		// @Test
		@Test(dataProvider = "TZPStammGN", dataProviderClass = Utils.DataSupplier.class)
		public void TZPStammGNTest(String Teststep, String Aktiv, String Emailadresse, String Passwort, String Unternehmensname, String LEI,
					String Land, String Webseite, String EmailGeschaefte, String Str, String HausNr, String PLZ, String Ort, String Adresszusatz,
					String Bank, String BIC, String IBAN, String Kontoinhaber,
					String Anrede, String Vorname, String TelefonNr, 
					String Titel, String Nachname,  String EmailLogin, 
					String TypELS, String HoeheELS, String MinVolumen, String MaxVolumen, String Agentur1, String Klasse1, 
					String Agentur2, String Klasse2, String Doc_1, String Datum_1) throws Exception {

			
			// BasicConfigurator.configure();
			
			
			if (Aktiv.equals("Ja")) {
			
			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPStammGN: " + Teststep + " - " + AblaufartGlobal,
					"Stammdateneingabe des Geldnehmers");

			driver.get(BaseUrl);
			// TZRegGG-Eingabemaske
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
		
			
			// Login mit gültigen Daten
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort, test);

     		// Button "Anmelden auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			Thread.sleep(3 * Zeitspanne);
			
					
			
     		// Button "Vollständige Registrierung auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@data-test, 'logout-dsgvo-button')]", test);
			
			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GN\\Login für die Stammdaten GN ", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
		
			if (MissingData) {
			
			// Register Unternehmen
			// Auswahl Unternehmensname
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "companyName", Unternehmensname, test);
			
			// Auswahl LEI
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lei", LEI, test);

			// Auswahl Land
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-country\"]", "//li[contains(text(),'", Land, test);
						
	        // Auswahl Webseite, Geschäfts-Email, Straße, Nr., PLZ, Ort, Adresszusatz
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "website", Webseite, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "emailForBusinessConfirmations", EmailGeschaefte, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "streetName", Str, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "houseNumber", HausNr, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "postCode", PLZ, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "location", Ort, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "addressAddition", Adresszusatz, test);
			
     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			// Auswahl Register Unternehmen -> wurden die Daten sauber gespeichert?
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Unternehmen')]", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GN\\Stammdaten GN 1-Unternehmen", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
			
     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);

			// Auswahl Register Bankverbindung -> Falls ein weiterer Reiter erzeugt wurde erfolgt ein Direktzugriff
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Bankverbindung')]", test);
						
			// Reiter Bankverbindung ausfüllen
			// Bank-Daten
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "bank", Bank, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "bic", BIC, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "iban", IBAN, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "recipientName", "iban", Kontoinhaber, test);
			

     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);

			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			// Auswahl Register Bankverbindung -> wurden die Daten sauber gespeichert?
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Bankverbindung')]", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GN\\Stammdaten GN 2-Bankverbindung", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
			
    		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);	
			
			// Wichtig: Nur mit Weiter Button prüfen
			// Auswahl Register Person-> direktsprung ist sicherer
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Person')]", test);
			
     		// Reiter Person ausfüllen
			// Anrede 
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-salutation\"]", "//li[contains(text(),'", Anrede, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "firstName", Vorname, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "phone", TelefonNr, test);
			// Titel 
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-title\"]", "//li[contains(text(),'", Titel, test);
			// Element wurde entfernt
			//Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "fax", FaxNr);			
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lastName", Nachname, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailLogin, test);			

     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			// Auswahl Register Person -> wurden die Daten sauber gespeichert?
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Person')]", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GN\\Stammdaten GN 3-Person", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
			
    		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);		
			
			// Wichtig, nur den Weiter Button verwenden
			// Auswahl Register -> Direktsprung ist sicherer
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Geldnehmer')]", test);

			
			// Reiter Geldnehmer ausfüllen
			//Typ ELS 
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-depositTypeId\"]", "//li[contains(text(),'", TypELS, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "id", "depositAmountInput", HoeheELS, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "minimumVolume", MinVolumen, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "maximumVolume", MaxVolumen, test);
			
			//Rating-Agentur 1
			// Beachte, da Moody's ein 'Symbol besitzt erfolgt der Test nicht einwandfrei
			// Korrekte Schreibweise in der Excel-Datei: Moody\'s
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-rating1AgencyId\"]", "//li[contains(text(),'", Agentur1, test);
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-rating1ValueId\"]", "//li[contains(text(),'", Klasse1, test);
			//Rating-Agentur 2
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-rating2AgencyId\"]", "//li[contains(text(),'", Agentur2, test);
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-rating2ValueId\"]", "//li[contains(text(),'", Klasse2, test);
		
     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			// Auswahl Register -> wurden die Daten sauber gespeichert?
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Geldnehmer')]", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GN\\Stammdaten GN 4-Geldnehmer", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
			
    		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);	
			
		
		
			} // IF-Ende für Missing Data
			else {
			 // Direktsprung auf Dokumente
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@href='#/masterdata/documents']", test);
			}
			
			// Wichtig, wieder deaktivieren, um nur den Weiter-Button zu prüfen
			// Auswahl Register -> wurden die Daten sauber gespeichert?
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Dokumente')]", test);
				
			
//			// Die Heftzwecke für das Hochladen des Dokuments hat keine eindeutige ID. Der Zugriff erfolgt über den Eintrag im ersten Eingabefeld
//			// Beachte, der Eintrag im ersten Eingabefeld ist abhängig vom Unternehmensnamen 
			// Alle anderen Zeilen können mit Standardwerten durchsucht werden. 
			// Das erste Element ist ein Listenelement mit eigener Zugriffslogik.
			Thread.sleep(3 * Zeitspanne);
		
			Utils.SeleniumUtils.PDFUploadListe(driver, Zeitspanne, "xpath", "", Unternehmensname, "", Datum_1, test);
			Thread.sleep(3 * Zeitspanne);
			// Durch das erste Listenelement wird der nächste Zugriff bereits die zweite Gruppe
			Utils.SeleniumUtils.PDFUpload(driver, Zeitspanne, "xpath", "", "Allgemeine Geschäftsbedingungen (AGBs)", "[2]", Datum_1, test);
    		Thread.sleep(3 * Zeitspanne);
	
    		// Hochladen auswählen		
    		Thread.sleep(3 * Zeitspanne);
    		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Hochladen']//ancestor::button[@tabindex='0']", test);
			//System.out.println("erster Zugriff hochladen: ");
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);
			
			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			// Kontrolle, ob kein Fehlertext angezeigt wird
			// Prüfen, ob die die Maske mit den Button Vollständige Registrierung angezeigt wird  
			// Programm läuft nicht weiter
			// Assert.assertFalse((driver.findElement(By.xpath("//p[text()='Bitte wählen Sie ein Dokument zum Hochladen aus']")).isDisplayed()));
			// Assert.assertEquals(driver.getPageSource().contains("Bitte wählen Sie ein Dokument zum Hochladen aus"), false);
			// Thread.sleep(3 * Zeitspanne);
			
		
			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			  //System.out.println("Aufnahme 1");
			 Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GN\\Stammdaten GN 5-Dokumente hochladen ", Teststep, test);
			 // System.out.println("Aufnahme 2");
			 Thread.sleep(3* Zeitspanne);
			
			// Hochladen auswählen	(Erneute für Ablauf mit GN -> gibt sonst keinen Ablauf 
			// System.out.println("vor if "); 
			
			 // IF führt zu einem Fehler
			 //if (driver.findElement(By.xpath("//span[text()='Hochladen']//ancestor::button[@tabindex='0']")).isDisplayed()) {
			//	System.out.println("drin if: ");	
			// Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Hochladen']//ancestor::button[@tabindex='0']", test);
			 //System.out.println("zweiter Zugriff hochladen: ");
			// }
									
			Thread.sleep(3 * Zeitspanne);
			// Handelsfreigabe beantragn	
			// System.out.println("vor Handelberechtigung ");
			
			
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Handelsberechtigung anfordern']//ancestor::button[@tabindex='0']", test);
			// TSonderzeit zum freigeben
			//System.out.println("erster Zugriff: " + driver.findElement(By.xpath("//span[text()='Handelsberechtigung anfordern']//ancestor::button[@tabindex='0']")).isEnabled());
			Thread.sleep(3 * Zeitspanne);	
			
			
			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			// Kontrolle, ob kein Fehlertext angezeigt wird
			// Prüfen, ob die die Maske mit den Button Vollständige Registrierung angezeigt wird  
			// Programm läuft nicht weiter
			// Assert.assertFalse((driver.findElement(By.xpath("//p[text()='Alle Pflichtfelder müsssenausgefüllt werden']")).isDisplayed()));
			// Assert.assertEquals(driver.getPageSource().contains("Alle Pflichtfelder müsssenausgefüllt werden"), false);
			// Thread.sleep(3 * Zeitspanne);
			
			// Screenshot aufnehmen 
			Thread.sleep(3 * Zeitspanne);
			 Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GN\\Stammdaten GN 6-Handelfreigabe angefordert ", Teststep, test);
			Thread.sleep(3 * Zeitspanne);

			// Handelsberechtigung auswählen	(Erneute für Ablauf mit GN -> gibt sonst keinen Ablauf 	
//			if (driver.findElement(By.xpath("//span[text()='Handelsberechtigung anfordern']//ancestor::button[@tabindex='0']")).isEnabled()) {
//				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Handelsberechtigung anfordern']//ancestor::button[@tabindex='0']", test);
//				System.out.println("zweiter Zugriff: " + driver.findElement(By.xpath("//span[text()='Handelsberechtigung anfordern']//ancestor::button[@tabindex='0']")).isEnabled());
//			}
			
			
			
			driver.close();
			// Für den Teardown
			driver = null;
			eyes = null;

			// Neu Starten
			SetupSeleniumTestdaten(AblaufartGlobal);

		} // Nur wenn Aktic "Ja" ist durchlaufen

		} 
		
		public void ApplitoolsAufnahme(String Ablaufart, String teststep) {
			if (Ablaufart.equals("Applitool")) {
				// Applitools vorbereiten
				eyes = new Eyes();
				eyes.setApiKey("1epbTsh91uyej6yur9x0FzJb3WUit5naoVB8SYMRZUE110");
				try {

					// eyes.check("AL_Risiko", Target.window().fully());

					// eyes.open(driver, "AL-Risiko", "Testfall: " + teststep, new
					// RectangleSize(800, 600));

					// eyes.open(driver, "IR-PHV", "Testfall: " + teststep,
					// Target.window().fully());

					// eyes.open(driver, "IR-PHV", "Testfall: " + teststep, new
					// RectangleSize(900,750));

					// Kein Rectangle vorgeben um die Voreinstellung zu verwenden.
					eyes.open(driver, "IR-PHV", "Testfall: " + teststep);

					eyes.checkWindow("AL-Risiko-Ergebnisseite");

					eyes.close();

				} finally {

				}
			}
		}




		@AfterTest
		public void BrowserTearDown() throws InterruptedException {

	        // Offene Bereiche Schließen
			Utils.SeleniumUtils.BrowserBeenden(driver, Zeitspanne, extent,  eyes);
		}

	}
