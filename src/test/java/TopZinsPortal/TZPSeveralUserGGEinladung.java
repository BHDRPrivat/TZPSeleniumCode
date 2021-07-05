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

public class TZPSeveralUserGGEinladung {
	
	
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
		
		@Test(dataProvider = "TZPSeveralUserGG", dataProviderClass = Utils.DataSupplier.class)
		public void TZPSeveralUserEinladenGGTest(String Teststep, String Aktiv, String EmailadresseCompanyAdmin, String PasswortCompanyAdmin, String Anrede, 
				String FirstName, String LastName, String EmailadresseCompanyUser, String PasswortCompanyUser, 
				String Unternehmensname, String Vorname, String TelefonNr, String Titel, String Nachname, String DatumPDF) throws Exception {

			
			if (Aktiv.equals("Ja")) {
			
			// Mock
			// String teststep = "AL-R1";

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPServeralUserGG: " + Teststep + " - " + AblaufartGlobal,
					"Einladen eines Company Employeers eines Geldgebers");

			driver.get(BaseUrl);
			// TZRegGG-Eingabemaske
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

//			// Zeitspanne für Video			
//			Thread.sleep(100 * Zeitspanne);	
			
			
			// Login mit gültigen Daten
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseCompanyAdmin, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortCompanyAdmin, test);

     		// Button "Registrieren auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, 
			Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			Thread.sleep(3 * Zeitspanne);
			
		
			
			
			// 3. Obern Profile icon clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='profile-button']", test);
			
			// 4. Register "Berechtigungen" auswaehlen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@data-test='Berechtigungen']", test);
			
			// 5. Anrede in dropdown auswaehlen
			// Anrede 
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-salutation\"]", "//li[contains(text(),'", Anrede, test);
			
			
			// 6. Vorname eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "firstName", FirstName, test);
			
			// 7. Nachname eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lastName", LastName, test);
			
			// 8. E-Mail eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseCompanyUser, test);
			
			// 9. Button "Einladen" clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Einladen']//ancestor::button", test);
			Thread.sleep(3 * Zeitspanne);
     		
			
			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			
			// Zeit für die Versendung der Mail geben.
 			Thread.sleep(100 * Zeitspanne);
	
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
