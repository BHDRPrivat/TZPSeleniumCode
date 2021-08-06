package TopZinsPortal;

import java.io.IOException;

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
import Utils.SeleniumUtils;
import jxl.read.biff.BiffException;

public class TZPTenderAngebotAnnehmenGGSeveralUser {
	
	
	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen
	
	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = System.getProperty("user.dir");
	public SoftAssert softassert = new SoftAssert();
	
	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	public String AblaufartGlobal;

	//public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;
		
		// Zu Testzwecken, Direktsprung auf das Hochladen der PDF-Dateien
		// Wenn alle Stammdaten eingegeben wurden, kann mit false direkt auf Dokumente zugegriffen werden
		// Boolean MissingData = true;
		Boolean MissingData = true;
		


		@Parameters({ "Ablaufart" })
		@BeforeTest
		public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart) throws InterruptedException, IOException {

			if (htmlReporter == null) {
				// start reporters
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Tender Annahme GG - " + Ablaufart + ".html");
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
		


		@Test(dataProvider = "TenderAngebotAnnehmenGGSeveralUser", dataProviderClass = Utils.DataSupplier.class)
		public void TZPTenderAngebotAnnehmenGGSeveralUserTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG, String EndeDatum, 
		String	EndeUhrzeit, String Zinskonvention, String Zahlungsfrequenz, 		
		String VolumenGG, String Valuta, String Faelligkeit1, String Faelligkeit2, String Faelligkeit3, String KommentarGG,
		String FirmaGN1, String EmailadresseGN1,	String PasswortGN1, String	ZinssatzGN1,
		String FirmaGN2, String EmailadresseGN2,	String PasswortGN2, String	ZinssatzGN2,
		String FirmaGN3, String EmailadresseGN3,	String PasswortGN3, String	ZinssatzGN3,
		String BtnAuswahlBestaetigenGG, String BtnBankenEinladenGG, String BtnAusloggenGG, 
		String BtnAngebotSendenGN1, String	BtnAngebotAblehnenGN1, String BtnAngebotTelefonischGN1,
		String BtnAngebotSendenGN2, String	BtnAngebotAblehnenGN2, String BtnAngebotTelefonischGN2,
		String BtnAngebotSendenGN3, String	BtnAngebotAblehnenGN3, String BtnAngebotTelefonischGN3,
	    String BtnAngebotAnnehmenGG, String	BtnAngebotAblehnenGG, String BtnAngebotTelefonischGG

				) throws Exception {
			
			if (Aktiv.equals("Ja")) {
				
				// creates a toggle for the given test, adds all log events under it
				ExtentTest test = extent.createTest("TZP_Tender: " + Teststep + " - " + AblaufartGlobal,
					"Geldgeber schließt die Ausschreibung");

				driver.get(BaseUrl);
				// Loginseite oeffnen
				Thread.sleep(3 * Zeitspanne);
				test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
				
				// LENDER SCHLIESST TENDER
				// 1. Lender einloggen
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseGG, test);
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortGG, test);
				
				// 1.1 Button "Einloggen" klicken
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
				
				// 2. Klicken auf Register "Ausschreibung"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@data-test='Ausschreibungen']", test);
			
				// Die Anzeige auf 100 erhöhen
				Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//div[contains(@id,'mui')]",
						"//li[contains(text(),'", "100", test);
								
				// 3. Tender auswählen
				Utils.SeleniumUtils.AuschreibungGNAuswahl(driver, Zeitspanne,  VolumenGG, test);
				
				// 1 Zinssatz auswählen 
				// Später den Zinssatz einer Bank auswählen
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//tbody//tr//td[2]", test);
				
				// 5.6 Eintrag "Kommentar" 
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//textarea[@name='comment']", (KommentarGG + " Abschluss Geldgeber GG"), test);	
					
				// 4. Klicken auf Button "Angebot annehmen"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='annehmen']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
				
				// 5. Klicken auf Button "Ja"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Ja']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
				
				// 5.1 Klicken auf Button "OK"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
		
			driver.close();
			// Für den Teardown
			driver = null;
			eyes = null;

			// Neu Starten
			SetupSeleniumTestdaten(AblaufartGlobal);

		} // Nur wenn Aktic "Ja" ist durchlaufen

		} 
		
		
		@AfterTest
		public void BrowserTearDown() throws InterruptedException {

	        // Offene Bereiche Schließen
			Utils.SeleniumUtils.BrowserBeenden(driver, Zeitspanne, extent,  eyes);
		}

	}
