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
import jxl.read.biff.BiffException;

@Test
public class TZPTransaktionStartGGSeveralUser {
	
	
	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen
	
	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = null;
	public SoftAssert softassert = new SoftAssert();
	
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

			// Ermittelt den Pfad des aktuellen Projekts
			projectpath = System.getProperty("user.dir");
			
			if (htmlReporter == null) {
				// start reporters
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Transaktion Start GG - " + Ablaufart + ".html");
				// create ExtentReports and attach reporter(s)
				extent = new ExtentReports();
				extent.attachReporter(htmlReporter);
			}
			AblaufartGlobal = Ablaufart;
			StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
			Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

			BaseUrl = Utils.TZPBeforeTest.Umgebung() + "/portal/login";

			SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
			// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
			// verglichen werden. "==" steht für die Überprüfung des Speicherorts

			// Aufruf des Browser-Setups
			driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);

		
		}
		


		// @Test
		@Test(dataProvider = "TZPTransaktionStartGGSeveralUser", dataProviderClass = Utils.DataSupplier.class)
		public void TZPTransaktionStartGGTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG, String VolumenGG, 
				String ZinssatzGG, String Valuta, String Zinskonvention, String Zahlungsfrequenz, String SonstigesGG, String KommentarGG, String EndeAnfrageUhrzeitGG, 
				String BtnAnfrageSendenGG, String BtnAusloggenGG, String FirmaGN, String EmailadresseGN, String PasswortGN, String VolumenGN, 
				String ZinssatzGN, String EndeAngebotGN, String BtnAngebotSendenGN, String BtnAnfrageAblehnenGN, String BtnAngebotTelefonischWeiterleitenGN, 
				String BtnAngebotAnnehmenGG, String BtnAngebotAblehnenGG, String BtnAngebotTelefonischAnnehmenGG  ) throws Exception {

			
			if (Aktiv.equals("Ja")) {
			
			// Aufruf der bisherigen Transaktionsklasse mit den eigenen Daten	
				
				TopZinsPortal.TZPTransaktionStartGG.TZPTransaktionStartGGTest(Teststep, Aktiv,  EmailadresseGG, PasswortGG, VolumenGG, 
						 ZinssatzGG,  Valuta,  Zinskonvention,  Zahlungsfrequenz,  SonstigesGG,  KommentarGG,  EndeAnfrageUhrzeitGG, 
						 BtnAnfrageSendenGG,  BtnAusloggenGG,  FirmaGN,  EmailadresseGN,  PasswortGN,  VolumenGN, 
						 ZinssatzGN,  EndeAngebotGN,  BtnAngebotSendenGN,  BtnAnfrageAblehnenGN,  BtnAngebotTelefonischWeiterleitenGN, 
						 BtnAngebotAnnehmenGG,  BtnAngebotAblehnenGG, BtnAngebotTelefonischAnnehmenGG  );

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
