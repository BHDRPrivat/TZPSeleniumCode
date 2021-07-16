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
import org.testng.asserts.SoftAssert;

import com.applitools.eyes.selenium.Eyes;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import Utils.ExcelUtilsJXL;
import Utils.SeleniumUtils;
import jxl.read.biff.BiffException;

public class TZPTransaktionAkzeptierenGG {
	
	
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
	
	// Anzahl der Testfälle wichtig für die einzelnen Zugriffe 
    // Später die exakte Zahl auslesen
	// hier wird Sie im Code festgelegt:
	public static Integer AktuellTransaktionMaske = 5; 



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
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Transaktion Akzeptieren GG - " + Ablaufart + ".html");
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
		@Test(dataProvider = "TZPTransaktionAkzeptierenGG", dataProviderClass = Utils.DataSupplier.class)
		public void TZPTransaktionAkzeptierenGGTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG, String VolumenGG, 
				String ZinssatzGG, String Valuta, String Zinskonvention, String Zahlungsfrequenz, String SonstigesGG, String KommentarGG, String EndeAnfrageUhrzeitGG, 
				String BtnAnfrageSendenGG, String BtnAusloggenGG, String FirmaGN, String EmailadresseGN, String PasswortGN, String VolumenGN, 
				String ZinssatzGN, String EndeAngebotGN, String BtnAngebotSendenGN, String BtnAnfrageAblehnenGN, String BtnAngebotTelefonischWeiterleitenGN, 
				String BtnAngebotAnnehmenGG, String BtnAngebotAblehnenGG, String BtnAngebotTelefonischAnnehmenGG  ) throws Exception {
			
			if (Aktiv.equals("Ja")) {
             
			// Von hinten nach vorne abarbeiten	
			AktuellTransaktionMaske = AktuellTransaktionMaske - 1; 

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZP_Transaktion: " + Teststep + " - " + AblaufartGlobal,
					"Akzeptieren einer Transaktion durch den Geldgeber");
				
			driver.get(BaseUrl);
			// 1. Loginseite oeffnen
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

     		// 15. Login Geldgeber
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseGG, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortGG, test);
			
    		// 15.1 Button "Login" auswaehlen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
             String drag = "(//div[contains(@class, 'MuiDialogTitle-root')])[" + AktuellTransaktionMaske +"]";
			
			// Verschiebung der Maske
			Utils.SeleniumUtils.DragDrop(driver, Zeitspanne, drag, "//div[@data-test='sentinelStart']", test);

			
			// Fallunterscheidung zwischen Annehmen und mit OK-Bestätigen
			
		    // 11. Button "Angebot senden" klicken 
    		if ((BtnAngebotSendenGN.equals("Ja")) || (BtnAngebotTelefonischWeiterleitenGN.equals("Ja"))) {
    			// Angebot wurde durch den GN abgesendet	
    			System.out.println("Angebot gesendet GN");
		    
    			// Beachte, durch die ablehnten Transaktionen werden einige Einegbaefelde und Button nihct erzeugt. Daher ist  
    			// die Zählung dieser Objekte unterschiedlich von den Masken. 
    			
    			// 5.5 Eintrag "Sonstiges" 
    			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", drag + "//following::textarea[@name='other']", (SonstigesGG + " Geldgeber GG 2"), test);			

    			// 5.6 Eintrag "Kommentar" 
    			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", drag + "//following::textarea[@name='comment']", (KommentarGG + " Geldgeber GG 2"), test);	
    			
    			// 16. Button "Angebot annehmen" klicken
    			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", drag + "//following::span[text()='annehmen']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
    			
    			// 17. Button "Ja" in Pop-up klicken
    			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//*[@class='MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary']" , test);
    		
    			Thread.sleep(5 * Zeitspanne);
    			
    			// Prüfung ob die Bestätigung geöffnet wird?
    			SoftAssert softassert = new SoftAssert();
    			softassert.assertEquals(driver.getPageSource().contains("Die Transaktion wurde erfolgreich abgeschlossen"), false); 
    			softassert.assertAll(); // Damit der Code weiter durchlaufen wird.
    			
    			
    		}
			else if (BtnAnfrageAblehnenGN.equals("Ja")) {
			// Anfrage wurde durch den GN abgelehnt	
			// OK Button der Ablehnung bestätigen	
				
    			System.out.println("Anfrage Abgelehnt GN");
		    
   			   // Button "OK" auswählen von der Drag-Maske
    			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", drag + "//following::span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
				
			}

				
			
			Thread.sleep(3 * Zeitspanne);
			
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
