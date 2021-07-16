package TopZinsPortal;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.applitools.eyes.selenium.Eyes;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import Utils.ExcelUtilsJXL;
import Utils.SeleniumUtils;
import Utils.TZPBeforeTest;
import junit.framework.Assert;
import jxl.read.biff.BiffException;

public class TZPSeveralUserGNStamm {
	
	
	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen
	
	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = System.getProperty("user.dir");

	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	// Für E-mail einlesen
	public Object Mailweitergabe[][] = null;
	String RegistrierungslinkSU = null;
	
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
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Several User GG - " + Ablaufart + ".html");
				// create ExtentReports and attach reporter(s)
				extent = new ExtentReports();
				extent.attachReporter(htmlReporter);
			}
			AblaufartGlobal = Ablaufart;
			StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
			Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

			// Gesamtadresse: "http://3.127.85.31/portal/login"
			BaseUrl = Utils.TZPBeforeTest.Umgebung() + "/portal/login";
  
			
			SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
			// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
			// verglichen werden. "==" steht für die Überprüfung des Speicherorts

			// Aufruf des Browser-Setups
			driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);

		
		}
		
		@Test(dataProvider = "TZPSeveralUserGN", dataProviderClass = Utils.DataSupplier.class)
		public void TZPSeveralUserStammdatenGNTest(String Teststep, String Aktiv, String EmailadresseCompanyAdmin, String PasswortCompanyAdmin, String Anrede, 
				String FirstName, String LastName, String EmailadresseCompanyUser, String PasswortCompanyUser, 
				String Unternehmensname, String Vorname, String TelefonNr, String Titel, String Nachname, String DatumPDF) throws Exception {

			
			if (Aktiv.equals("Ja")) {
			
			// Mock
			// String teststep = "AL-R1";

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPServeralUserGG: " + Teststep + " - " + AblaufartGlobal,
					"Einladen eines Company Employeers eines Geldnehmers");
//
//			driver.get(BaseUrl);
//			// TZRegGG-Eingabemaske
//			Thread.sleep(3 * Zeitspanne);
//			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
//			
//			
//			// Login mit gültigen Daten
//			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseCompanyAdmin, test);
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortCompanyAdmin, test);
//
//     		// Button "Registrieren auswählen"
//			Utils.SeleniumUtils.ButtonKlick(driver, 
//			Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
//			Thread.sleep(3 * Zeitspanne);
//			
//						
//			// 3. Obern Profile icon clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='profile-button']", test);
//			
//			// 4. Register "Berechtigungen" auswaehlen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@data-test='Berechtigungen']", test);
//			
//			// 5. Anrede in dropdown auswaehlen
//			// Anrede 
//			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-salutation\"]", "//li[contains(text(),'", Anrede, test);
//			
//			
//			// 6. Vorname eingeben
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "firstName", FirstName, test);
//			
//			// 7. Nachname eingeben
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lastName", LastName, test);
//			
//			// 8. E-Mail eingeben
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseCompanyUser, test);
//			
//			// 9. Button "Einladen" clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Einladen']//ancestor::button", test);
//			
//			// Zeit für die Versendung der Mail geben.
// 			Thread.sleep(100 * Zeitspanne);
//
//			// Link für die Registration aus der E-Mail ermitteln
//			String EMailEingelesen = null;
//			Integer Anzahlmails = 0;
//			// Jeder Eintrag in der Exceldatei entspicht einer Kontrolle der E-Mail.
//			Anzahlmails = Utils.EMailUtils.AnzahlGMails(EmailadresseCompanyUser, PasswortCompanyUser, test);
//			if (Anzahlmails != 0) {
//				// Beachte, dass EMails-Objekt existiert nur im if-Bereich
//				// Die Variable existiertt nicht außerhalb.
//				Object EMails[][] = new Object[Anzahlmails][8];
//				EMails = Utils.EMailUtils.EmailLesen(Anzahlmails, EmailadresseCompanyUser, PasswortCompanyUser, test);
//				Mailweitergabe = EMails;
//			} // Keine Mails vorhanden
//			else {
//				System.out.println("Fehler:  Keine Mails vorhanden für " +EmailadresseCompanyUser);
//				test.log(Status.INFO, "Für die E-Mail-Adesse: " + EmailadresseCompanyUser + " sind keine Mails vorhanden.");
//			}
//			// in der Objektvariablen  Mailweitergabe befindet sich der Link zu der neuen Seite
//			// Beachte, die Klammer um (Mailweitergabe) führt zu einem Fehler
//			if (Mailweitergabe != null) {
//				RegistrierungslinkSU = Utils.EMailUtils.EMailSeveralUserRegistrationLink(Mailweitergabe, test);
//				System.out.println("Der Registierungslink lautet: " + RegistrierungslinkSU);				
//			}
//
//			// Es muss eine neue Webseite aufgebaut werden.
//     		driver.get(RegistrierungslinkSU);
//     		System.out.println("Der Registierungslink im driver: " + RegistrierungslinkSU);
//			
//			// warten bis die Seite aufgebaut wurde
//			// Sobald die Kondition erfüllt wird, erfolgt der weitere Programmablauf.
//			WebDriverWait wait = new WebDriverWait(driver, 10);
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
//			
//			
//			// Auswahl Titel
//			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-title\"]",
//					"//li[contains(text(),'", Titel, test);
//
//			// Telefonnummer
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "tel", TelefonNr, test);
//
//			// E-Mail Bestätigung
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "emailConfirm", EmailadresseCompanyUser, test);
//
//			// Passwort
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortCompanyUser, test);
//
//			// Screenshot aufnehmen
//			Thread.sleep(3 * Zeitspanne);
//			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Eingabe-Reg-Werte-GG", Teststep, test);
//			Thread.sleep(3 * Zeitspanne);
//
//			// Zuerst auf das übergeordnete fieldset klicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
//					"//fieldset[contains(@class, 'MuiFormControl-root MuiFormControl-marginDense')]", test);
//
//			// Datenschutz öffnet sich
//			// Button Schliessen auswählen
//			// Beachte, der Tag-Classname ist nicht eindeutig und sollte nicht verwendet
//			// werden!
//			// Daher der zugriff über den Tag "data-test"
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
//					"//button[contains(@data-test, 'accept-dsgvo-button')]", test);
//
//			// Der vollständie Name ändert sich mit einer willkürlichen Nummer nach dem jss
//			// Ohne die jss-Kennzeichnung ist das Element nihct eindeutig.
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
//					"//label[contains(@class, 'MuiFormControlLabel-root jss')]", test);
//
//			// Button Schliessen auswählen
//			// Beachte, der Tag-Classname ist nicht eindeutig und sollte nicht verwendet
//			// werden!
//			// Daher der zugriff über den Tag "data-test"
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
//					"//button[contains(@data-test, 'accept-dsgvo-button')]", test);
//
//
//			// Button "Registrieren auswählen"
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
//
//		
//			// Screenshot aufnehmen
//			Thread.sleep(8 * Zeitspanne);
//			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,"\\Reg GG\\Nach-Registrierung-Button", Teststep, test );
//			Thread.sleep(8 * Zeitspanne);
//
//			// Kontrolle, ob Bestätigung angezeigt wird.
//			// Prüfen, ob die die Maske mit den Button Vollständige Registrierung angezeigt wird  
//			// Programm läuft nicht weiter
//			Thread.sleep(3 * Zeitspanne);
//			//Assert.assertTrue((driver.findElement(By.xpath("//span[text()='Vollständige Registrierung']")).isDisplayed()));
//			// Weiterlauf ermöglichen 
//			SoftAssert softassert = new SoftAssert();
//			softassert.assertTrue((driver.findElement(By.xpath("//span[text()='Vollständige Registrierung']")).isDisplayed())); 
//			softassert.assertAll(); // Damit der Code weiter durchlaufen wird.
			
			// mit den neuen Daten einloggen und die beiden Pflichtdokumente ergänzen
			driver.get(BaseUrl);
			// TZRegGG-Eingabemaske
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
			
			
			// Login mit gültigen Daten
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseCompanyUser, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortCompanyUser, test);

     		// Button "Registrieren auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			Thread.sleep(5 * Zeitspanne);
			
     		// Button "Vollständige Registrierung auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@data-test, 'logout-dsgvo-button')]", test);
			
			// Auf Dokumente wechseln
			
			 // Direktsprung auf Dokumente
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@href='#/masterdata/documents']", test);
						
			
//			// Die Heftzwecke für das Hochladen des Dokuments hat keine eindeutige ID. Der Zugriff erfolgt über den Eintrag im ersten Eingabefeld
//			// Beachte, der Eintrag im ersten Eingabefeld ist abhängig vom Unternehmensnamen 
//			// Alle anderen Zeilen können mit Standardwerten durchsucht werden. 
//			
//			
//			Utils.SeleniumUtils.PDFUpload(driver, Zeitspanne, "xpath", "", "Personalausweis", "[2]", DatumPDF, test);
//			Thread.sleep(3 * Zeitspanne);
//			
//			Utils.SeleniumUtils.PDFUpload(driver, Zeitspanne, "xpath", "", "Handelsberechtigung", "[2]", DatumPDF, test);
//			Thread.sleep(3 * Zeitspanne);
//			
//			Thread.sleep(3 * Zeitspanne);	
//			// Hochladen auswählen		
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Hochladen']//ancestor::button[@tabindex='0']", test);
//			// TSonderzeit zum Hochladen
//			Thread.sleep(3 * Zeitspanne);
//			
//			// Button "OK" auswählen, wenn vorhanden
//			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
//			
//			// Kontrolle, ob kein Fehlertext angezeigt wird
//			// Prüfen, ob die die Maske mit den Button Vollständige Registrierung angezeigt wird  
//			// Programm läuft nicht weiter
//			// Suche nach einem Text. Nicht findelement verwenden, da dieser einen Fehler auswirft
//			// Assert.assertFalse((driver.findElement(By.xpath("//p[text()='Bitte wählen Sie ein Dokument zum Hochladen aus']")).isDisplayed()));
//			//Assert.assertEquals(driver.getPageSource().contains("Bitte wählen Sie ein Dokument zum Hochladen aus"), false);
//			// Weiterlauf ermöglichen 
//			SoftAssert softassert = new SoftAssert();
//			softassert.assertEquals(driver.getPageSource().contains("Bitte wählen Sie ein Dokument zum Hochladen aus"), false); 
//			softassert.assertAll(); // Damit der Code weiter durchlaufen wird.
			
			
			Thread.sleep(5 * Zeitspanne);
			
				
			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Stammdaten GG 4-Dokumente hochladen ", Teststep, test);
			Thread.sleep(5 * Zeitspanne);
			
			
			// Handelsfreigabe beantragn		
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Handelsberechtigung anfordern']//ancestor::button[@tabindex='0']", test);
			// TSonderzeit zum Hochladen
			Thread.sleep(5 * Zeitspanne);	
			
			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			// Kontrolle, ob kein Fehlertext angezeigt wird
			// Prüfen, ob die die Maske mit den Button Vollständige Registrierung angezeigt wird  
			// Programm läuft nicht weiter
			// Assert.assertFalse((driver.findElement(By.xpath("//p[text()='Alle Pflichtfelder müsssenausgefüllt werden']")).isDisplayed()));
			// Assert.assertEquals(driver.getPageSource().contains("Alle Pflichtfelder müsssenausgefüllt werden"), false);
			Thread.sleep(3 * Zeitspanne);
			
			
			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Stammdaten GG 5-Handelfreigabe angefordert ", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
//			
//			
//			// Admin-Forsa Handelsfreigabe erteilen
//			
//
//				driver.get(BaseUrl);
//				// TZRegGG-Eingabemaske
//				Thread.sleep(3 * Zeitspanne);
//				test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
//
//				// Login mit gültigen Daten
//
//				String Emailadresse = Utils.TZPBeforeTest.AdminEmail();
//				String Passwort = Utils.TZPBeforeTest.AdminPasswort();
//
//				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse, test);
//				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort, test);
//
//				// Button "Anmelden auswählen"
//				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]",
//						test);
//
//
//			// Button "Daten komplett" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, '" + "BEREIT FÜR HANDELSBERECHTIGUNG" + "')]",	test);
//			
//			// Weiterlauf nur nach implizierter Anzeige des Suchfeldes
//			// Sobald die Kondition erfüllt wird, erfolgt der weitere Programmablauf.
//			wait = new WebDriverWait(driver, 10);
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='search']")));
//
//			// Firmenname in das Suchfeld eingeben
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);
//
//			// Screenshot aufnehmen
//			Thread.sleep(3 * Zeitspanne);
//			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
//					"\\Admin Handelsfreigabe\\AmdinHandlefreigabe-GG-Auswahl", Teststep, test);
//			Thread.sleep(3 * Zeitspanne);
//
//			// Der Stift für das Laden der Daten hat keine eindeutige ID. Der Zugriff
//			// erfolgt über den Eintrag im ersten Eingabefeld
//			// Beachte, der Eintrag im ersten Eingabefeld ist abhängig vom Unternehmensnamen
//			String xpathvalue = "//div[text() = '" + Unternehmensname
//					+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root')]//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
//			// TSonderzeit zum Hochladen
//			Thread.sleep(3 * Zeitspanne);
//
//			// Direktsprung auf Dokumente
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@href='#/masterdata/documents']", test);
//
//			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'identityCard']", test);
//			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'tradingLicense']", test);
//
//			// Screenshot aufnehmen
//			Thread.sleep(3 * Zeitspanne);
//			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
//					"\\Admin Handelsfreigabe\\AmdinHandlefreigabe-GG-Haken", Teststep, test);
//
//			// Button Freigeben auswählen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
//					"//span[text()='Freigeben']//ancestor::button[@tabindex='0']", test);
//			// TSonderzeit zum Hochladen
//			Thread.sleep(3 * Zeitspanne);
//
//			// Button "OK" auswählen, wenn vorhanden
//			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
//
//			// Prüfen ob User in Register "Mit Handelsberechtigung" vorhanden ist?
//			// Button "Daten komplett" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
//					"//li[contains(@data-test, 'MIT HANDELSBERECHTIGUNG')]", test);
//
//			// Die Anzeige auf 100 erhöhen
//			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//div[contains(@id,'mui')]",
//					"//li[contains(text(),'", "100", test);
//
//			// Firmenname in das Suchfeld eingeben
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);
//			Thread.sleep(5 * Zeitspanne);
//			
//			
			
			Thread.sleep(10 * Zeitspanne);
     		
	
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
