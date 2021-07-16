package TopZinsPortal;

import java.io.IOException;

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
import Utils.TZPBeforeTest;
import Utils.TZPSetupBrowser;
import junit.framework.Assert;
import jxl.read.biff.BiffException;

public class TZPAdminApplitoolScreenshots {

	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen

	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = null;
	// Für AutoIT
	String workingDir;
	String autoitscriptpath;
	String filepath;

	// Wenn alle Stammdaten eingegeben wurden, kann mit true direkt auf Dokumente
	// zugegriffen werden
	Boolean MissingData = true;

	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	public static String AblaufartGlobal;

	// public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;

	@Parameters({ "Ablaufart" })
	@BeforeTest
	public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart) throws InterruptedException, IOException {

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter(
					"Fehlerreport TopZinsPortal Admin HandelsfreigabeGG - " + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}


		System.out.println("Handelsfreigabe: " + Ablaufart);
		AblaufartGlobal = Ablaufart;
		StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
		Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

		// Hinweis: Für direkte Testläufe
		// Applitools und PDF-Druck dürfen nicht gleichzeitig ablaufen
		// Es kommt zu Fehlermeldungen

		workingDir = System.getProperty("user.dir");
		autoitscriptpath = workingDir + "\\AutoIT\\" + "File_upload_selenium_webdriver.au";
		filepath = workingDir + "\\DummyPDF\\PDF-Dummy.pdf";

		BaseUrl = TZPBeforeTest.Umgebung() + "/portal/login";

		SpeicherpfadTestdokumente = workingDir + "\\test-output\\PDFOutput\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden. "==" steht für die Überprüfung des Speicherorts

		// Aufruf des Browser-Setups
		driver = TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);

	}

	
	@Test
	public void TZPApplitoolScreenshot() throws Exception {

		String Testversion = "Stage Sprint 43";
		String Aktiv = "Ja";
		String Teststep ="TZP-Aplitool-001";
		// Produktiv
		String Suchbegriff = "Volksbank";
//		// Testversion
//		String Suchbegriff = "BHDR";		
		String LogInVon = "01.05.2021";
		String LogInBis = "03.05.2021";
		String Scrollelement = null;
		
		
		if (Aktiv.equals("Ja")) {

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPAdminHndelsfreigabeGG: " + Teststep + " - " + AblaufartGlobal,
					"Handelsfreigabe durch Admin");

			driver.get(BaseUrl);
			// TZRegGG-Eingabemaske
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

			// Login mit gültigen Daten
			
			String Emailadresse = Utils.TZPBeforeTest.AdminEmail();
			String Passwort = Utils.TZPBeforeTest.AdminPasswort();
			
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort, test);

			// Button "Anmelden auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			
			
			
			// Ab hier Aufnahmen der Admin-Seiten
			// Button "Dashboard" in menu clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'DASHBOARD')]", test);			
			
			// Screenshot für Applitool aufnehmen
     		Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "DASHBOARD"+ "-" +Testversion, "", Teststep, test);
//
//			// Button "INAKTIV" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'INAKTIV')]", test);			
//			
//			// Screenshot für Applitool aufnehmen
//     		Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "INAKTIV"+ "-" +Testversion, "", Teststep, test);
//     		
//			// Button "Eingeladen" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'EINGELADEN')]", test);
//			
//			// Screenshot für Applitool aufnehmen
//     		Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "EINGELADEN"+ "-" +Testversion, "", Teststep, test);
//			
//			// Button "Aktuelle Benutzer" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'AKTUELLE BENUTZER')]", test);
//
//			// Screenshot für Applitool aufnehmen
//     		Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "AKTUELLE BENUTZER"+ "-" +Testversion, "", Teststep, test);
//			
//			
//			// Suchbegriff eintragen
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search",  Suchbegriff, test);
//			Thread.sleep(3 * Zeitspanne);
//			
//			// Screenshot für Applitool aufnehmen
//			// Scrollelement
//			Scrollelement = "//table[@id='CurrentUsers-table']//ancestor::div[contains(@class, 'jss')]";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "AKTUELLE BENUTZER " + Suchbegriff + "-"+ Testversion, Scrollelement, Teststep, test);			
//
//		
//			// Button "OHNE HANDELSBERECHTIGUNG" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'OHNE HANDELSBERECHTIGUNG')]", test);	
//			
//			// Screenshot für Applitool aufnehmen
//     		Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "OHNE HANDELSBERECHTIGUNG"+ "-" +Testversion, "", Teststep, test);
//
//			// Suchbegriff eintragen
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search",  Suchbegriff, test);
//			Thread.sleep(3 * Zeitspanne);
//			
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "OHNE HANDELSBERECHTIGUNG "+ Suchbegriff +  "-" + Testversion, Scrollelement, Teststep, test);		
//
//			// Button "BEREIT FÜR HANDELSBERECHTIGUNG" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'BEREIT FÜR HANDELSBERECHTIGUNG')]", test);
//			
//			// Screenshot für Applitool aufnehmen
//     		Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "BEREIT FÜR HANDELSBERECHTIGUNG"+ "-" +Testversion, "", Teststep, test);
//			
//			
//			// Suchbegriff eintragen
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search",  Suchbegriff, test);
//			Thread.sleep(3 * Zeitspanne);			
//
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "BEREIT FÜR HANDELSBERECHTIGUNG "+ Suchbegriff + "-" + Testversion, Scrollelement, Teststep, test);	
//			
//			// Button "MIT HANDELSBERECHTIGUNG" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'MIT HANDELSBERECHTIGUNG')]", test);	
//			
//			// Screenshot für Applitool aufnehmen
//     		Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "MIT HANDELSBERECHTIGUNG"+ "-" +Testversion, "", Teststep, test);
//
//			// Suchbegriff eintragen
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search",  Suchbegriff, test);
//			Thread.sleep(3 * Zeitspanne);			
//			
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "MIT HANDELSBERECHTIGUNG "+ Suchbegriff + "-" + Testversion, Scrollelement, Teststep, test);	
//			
//			// Button "TRANSAKTION" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'TRANSAKTION')]", test);	
//
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "TRANSAKTION Ohne Optionen"+ "-" + Testversion, Scrollelement, Teststep, test);	
//			
//			// Option auswählen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()= 'Abgelehnte Transaktionen']", test);	
//
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "TRANSAKTION Abgelehnte"+ "-" + Testversion, Scrollelement, Teststep, test);	
//			
//			// Option abwählen
//			// Abwahl nur notwendig, wenn Durchlauf erfolgt.
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()= 'Abgelehnte Transaktionen']", test);				
//			
//			
//			// Option auswählen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()= 'Abgeschlossene Transaktionen']", test);				
//			
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "TRANSAKTION Abgeschlossen"+ "-" + Testversion, Scrollelement, Teststep, test);	
//			
//			// Option abwählen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()= 'Abgeschlossene Transaktionen']", test);	
//			
//			// Option auswählen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()= 'Nicht bearbeitet']", test);		
//			
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "TRANSAKTION Nicht bearbeitet"+ "-" + Testversion, Scrollelement, Teststep, test);	
//			
//			
//			// Button "AUSSCHREIBUNG" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'AUSSCHREIBUNG')]", test);	
//			
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "AUSSCHREIBUNG Ohne Optionen"+ "-" + Testversion, Scrollelement, Teststep, test);
//			
//			// Option auswählen			
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text() ='FORSA kontaktieren']", test);			
//		
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "AUSSCHREIBUNG Forsa kontaktieren"+ "-" + Testversion, Scrollelement, Teststep, test);
//			
//			// Option abwählen			
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text() ='FORSA kontaktieren']", test);	
//			
//			
//			// Option auswählen			
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text() ='Ausschreibung abgeschlossen']", test);
//			
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "AUSSCHREIBUNG Abgeschlossene"+ "-" + Testversion, Scrollelement, Teststep, test);
//
//			
//			// Button "Fälligkeiten" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'FÄLLIGKEITENLISTE')]", test);	
//			
//			
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "FÄLLIGKEITENLISTE"+ "-" + Testversion, Scrollelement, Teststep, test);
//
//	
//			// Suchbegriff eintragen
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search",  Suchbegriff, test);
//			Thread.sleep(2 * Zeitspanne);
//			
//			// Lupe zur Ausführung auswählen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//*[contains(@d, 'M15.5')]//ancestor::button", test);	
//			Thread.sleep(2 * Zeitspanne);
//			
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "FÄLLIGKEITENLISTE " + Suchbegriff +  "-" + Testversion, Scrollelement, Teststep, test);
//			
//						
//			// Button "ÜBERSICHT" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'ÜBERSICHT')]", test);	
//
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "ÜBERSICHT"+ "-" + Testversion, Scrollelement, Teststep, test);
//			
//			// Button "FAVORITEN" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'FAVORITEN')]", test);	
//
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "FAVORITEN ohne Optionen"+ "-" + Testversion, Scrollelement, Teststep, test);
//
//			// Favoriten sollten die bestehenden Optionen verwenden, daher eine Veränderung nicht durchführen.
//			
//			// Führt zu einem Fehler
//			// Listenelement "Alle" aus Liste "Sparkassen Sicherungssystem" wählen (kein klassisches Listenelement)
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//label[contains(text(), 'Sicherungssystem')]//ancestor::div[contains(@class, 'MuiFormControl-root jss')]", test);
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[@data-value='all']", test);
//			// Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//label[contains(text(), 'Sicherungssystem')]//ancestor::div[contains(@class, 'MuiFormControl-root jss')]", test);
//		
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "FAVORITEN Option gewählt"+ "-" + Testversion, Scrollelement, Teststep, test);
//			
//			// Button "LOG-IN-HISTORIE" in menu clicken
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, 'LOG-IN-HISTORIE')]", test);
//			
//			// Suchbegriff eintragen
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//input[@placeholder ='von']",  LogInVon, test);
//			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//input[@placeholder ='bis']",  LogInBis, test);
//			
//            // Button "suchen" auswählen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text() = 'Suchen']", test);
//		
//			
//			// Neue Aufnahme
//			Scrollelement = "";
//			Utils.SeleniumUtils.ApplitoolsAufnahme(driver, "Admin-Forsa", "LOG-IN-HISTORIE"+ "-" + Testversion, Scrollelement, Teststep, test);
//						
			
			// kurze Pause vor dem schließen
			Thread.sleep(5 * Zeitspanne);
			
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
		Utils.SeleniumUtils.BrowserBeenden(driver, Zeitspanne, extent, eyes);

	}

}
