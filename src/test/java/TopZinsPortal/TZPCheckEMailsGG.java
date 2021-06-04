package TopZinsPortal;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
import Utils.TZPBeforeTest;
import junit.framework.Assert;
import jxl.read.biff.BiffException;

public class TZPCheckEMailsGG {
// Die Versendung von E-Mails wird überprüft

	public static WebDriver driver;
	private Integer Zeitspanne;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String projectpath = System.getProperty("user.dir");
	public SoftAssert softassert = new SoftAssert();
	public String EMailEingelesen = null;
	Object Mailweitergabe[][] = null;
	
	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;
	Integer Anzahlmails = 0;

	public static String AblaufartGlobal;

	// public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;

	@Parameters({ "Ablaufart" })
	@BeforeTest
	public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart)
			throws InterruptedException, IOException {

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter(
					"Fehlerreport TopZinsPortal E-Mail Versendung GG - " + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}
		AblaufartGlobal = Ablaufart;
		StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
		Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();
		SpeicherpfadTestdokumente = Utils.TZPBeforeTest.PDFPfad();

		// Aufruf des Browser-Setups
		// driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser,
		// SpeicherpfadTestdokumente);
	}

	// Aufruf des Dataproviders über eine andere Klasse
	@Test(priority = 10, dataProvider = "TZPCheckEMailsGG", dataProviderClass = Utils.DataSupplier.class)
	public void TZPCheckEMailGGTest(String Teststep, String Aktiv, String Emailadresse, String Passwort, String Betreff,
			String Anhang, String Link1, String Link2, String Registrierung) throws Exception {

		// Beachte, Nach dem Einlesen der Mails werden diese als "Gelesen" markiert und beim nächsten Durchlauf nicht mehr eingelesen
		
		// Zeitspanne bis zur Zusendung der Mails lassen
		Thread.sleep(30 * Zeitspanne);
		
		
		System.out.println("\n" + "Teststep: " + Teststep);
		
		
		
		
		if (Aktiv.equals("Ja")) {
			// Mock
			// String teststep = "AL-R1";

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPCheckEMailGG: " + Teststep + " - " + AblaufartGlobal,
					"Kontrolle von E-Mails an Geldgebern (GG)");

			// Die Anzahl der E-Mails ermitteln, um die Dimension des Arrays festelegen zu können
			// Objekt zum Speichern der Mails
			if (EMailEingelesen == null || EMailEingelesen != Emailadresse) {
				// Nach dem ersten Lesen, oder E-Mailänderung die Werte anpassen
				EMailEingelesen = "Ja";
				EMailEingelesen = Emailadresse;
				// Wenn die E-Mail-Adresse zum ersten Mal eingelesen wird, oder sich ändert,
				// wird die E-Mail-Adresse komplett ausgelesen
				// E-Mail vor der Kontrolle in den Speicher lesen
				// Jeder Eintrag in der Exceldatei entspicht einer Kontrolle der E-Mail.
				Anzahlmails = Utils.EMailUtils.AnzahlGMails(Emailadresse, Passwort, test);
				if (Anzahlmails != 0) {
					Object EMails[][] = new Object[Anzahlmails][8];
					EMails = Utils.EMailUtils.EmailLesen(Anzahlmails, Emailadresse, Passwort, test);
					Mailweitergabe = EMails;
				} // Keine Mails vorhanden
				else {
					System.out.println("Fehler:  Keine Mails vorhanden für " + Emailadresse);
					test.log(Status.INFO, "Für die E-Mail-Adesse: " + Emailadresse + " sind keine Mails vorhanden.");

					// SoftAssertion (Braucht ein Objekt)
					SoftAssert softassert = new SoftAssert();
					softassert.assertTrue(false);
					softassert.assertAll(); // Damit der Code weiter durchlaufen wird.
				}
			} // Mit den bisherigen E-Mail-Daten den Test weiterführen 
			
			SoftAssert softassert = new SoftAssert();
			softassert.assertTrue(Utils.EMailUtils.EmailPruefen(Mailweitergabe, Betreff, Anhang, Link1, Link2, Registrierung, test));
			softassert.assertAll(); // Damit der Code weiter durchlaufen wird.
			

		} // Nur wenn Aktiv "Ja" ist durchlaufen

		// Neu Starten
		SetupSeleniumTestdaten(AblaufartGlobal);
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
