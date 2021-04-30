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
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String projectpath = System.getProperty("user.dir");
	public SoftAssert softassert = new SoftAssert();

	// Objekt zum Speichern der Mails
	Object EMails[][] = new Object[10][6];
	
	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	public static String AblaufartGlobal;

	// public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;

	@Parameters({ "Ablaufart"})
	@BeforeTest
	public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart) throws InterruptedException, IOException {

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal E-Mail Versendung GG - " + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}
		AblaufartGlobal = Ablaufart;
		StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
		Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();
		BaseUrl = TZPBeforeTest.Umgebung() + "/portal/registrierungGeldgeber";
		SpeicherpfadTestdokumente = Utils.TZPBeforeTest.PDFPfad();
		
		
		
		// Aufruf des Browser-Setups
		// driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);
	}

	// Aufruf des Dataproviders über eine andere Klasse
	@Test(priority = 10, dataProvider = "TZPCheckEMailsGG", dataProviderClass = Utils.DataSupplier.class)
	public void TZPCheckEmailGGTest(String Teststep, String Aktiv, String Emailadresse, String Passwort, String Betreff1, String Anhang1, 
			String Betreff2, String Anhang2, String Betreff3, String Anhang3) throws Exception {

		if (Aktiv.equals("Ja")) {
		// Mock
		// String teststep = "AL-R1";

		// creates a toggle for the given test, adds all log events under it
		ExtentTest test = extent.createTest("TZPCheckEMailGG: " + Teststep + " - " + AblaufartGlobal,
				"Kontrolle von E-Mails an Geldgebern (GG)");

		
		// E-Mail vor der Kontrolle in den Speicher lesen
		EMails = Utils.EMailUtils.EmailLesen(Emailadresse,Passwort, test);		
		
		for (int i = 0; i < EMails.length; i++) {
		 
            // print out details of each message
            System.out.println("\t From: " + EMails[i][1]);
             System.out.println("\t Subject: " + EMails[i][2]);
             System.out.println("\t Sent Date: " + EMails[i][3]);
            // System.out.println("\t Message: " + messageContent);
            System.out.println("\t Attachments: " + EMails[i][5]);
            System.out.println("\t Links: " + EMails[i][6]);
            System.out.println("\t Registrierung: " + EMails[i][7]);
		
		}    
		
		SoftAssert softassert = new SoftAssert();
		softassert.assertTrue(Utils.EMailUtils.EmailPruefen(EMails, Betreff1, Anhang1, test));
		softassert.assertAll(); //Damit der Code weiter durchlaufen wird.
		
	    //driver.close();
		// Für den Teardown
		// driver = null;
		// eyes = null;

		// Neu Starten
		SetupSeleniumTestdaten(AblaufartGlobal);
		
		} // Nur wenn Aktiv "Ja" ist durchlaufen

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
