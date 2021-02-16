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

public class TZPTenderAngebotSendenGN {
	
	
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

			if (htmlReporter == null) {
				// start reporters
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Tender Angebot senden GN - " + Ablaufart + ".html");
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
		

		@DataProvider(name = "TZPTenderAngebotSendenGN")
		public static Object[][] getData() throws BiffException {
			// Ermittelt den Pfad des aktuellen Projekts
			projectpath = System.getProperty("user.dir");
			// Zugriff auf die zugehörigen Exceldaten
			
			TestdatenExceldatei = "\\Excel\\TopZinsPortalTenderGG-GN.xls";

			String excelPath = projectpath + TestdatenExceldatei;
			Object testData[][] = testData(excelPath, "Testdaten");
			return testData;
		}

		public static Object[][] testData(String excelPath, String sheetName) throws BiffException {
			// Aufruf des Constructors von ExcelUtils
			ExcelUtilsJXL excel = new ExcelUtilsJXL(excelPath, sheetName);

			int rowCount = ExcelUtilsJXL.getRowCount();
			int colCount = ExcelUtilsJXL.getColCount();
			
			System.out.println("Zeile=" + rowCount + "Spalte=" + colCount + "String Wert: ");

			// 2 Dimensionales Object-Array erzeugen
			Object data[][] = new Object[rowCount-1][colCount];

			// �ber alle Zeilen laufen (i=1, da i=0 die Headerzeile)
			for (int i = 1; i < rowCount; i++) {
				// �ber alle Spalten laufen
				for (int j = 0; j < colCount; j++) {

					String cellData = ExcelUtilsJXL.getExcelDataString(i, j);
					data[i - 1][j] = cellData;
					
					System.out.println("Pro Zeile=" + i + "Pro Spalte=" + j + "Pro String Wert: " + cellData);
					
					// Werte in einer Zeile anzeigen
					// System.out.print(cellData + " | ");
				}
			}
			return data;
		}


		// @Test
		@Test(dataProvider = "TZPTenderAngebotSendenGN")
		public void TZPTenderAngebotSendenGNTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG, String EndeDatum, 
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
					"Geldnehmer senden Angebote");

				driver.get(BaseUrl);
				// Loginseite oeffnen
				Thread.sleep(3 * Zeitspanne);
				test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
	
				
				
				
				// BORROWER 1-3 SENDET OFFER
				// 1. Login Borrower 1
				// Mehrmals durchlaufen mit gesetzten Variablen
				// Das array größer Dimensionieren als notwendig.
				String[] FrimaGN = new String[5];
				FrimaGN[1] = FirmaGN1;
				FrimaGN[2] = FirmaGN2;
				FrimaGN[3] = FirmaGN3;
				String[] EmailGN = new String[5];
				EmailGN[1] = EmailadresseGN1;
				EmailGN[2] = EmailadresseGN2;
				EmailGN[3] = EmailadresseGN3;
				String[] PassGN = new String[5];
				PassGN[1] = PasswortGN1;
				PassGN[2] = PasswortGN2;
				PassGN[3] = PasswortGN3;
				String[] ZinsGN = new String[5];
				ZinsGN[1] = ZinssatzGN1;
				ZinsGN[2] = ZinssatzGN2;
				ZinsGN[3] = ZinssatzGN3;
					
				
			    for (int Durchlauf = 1; Durchlauf < 4; Durchlauf++) {
			    	
			    if (FrimaGN[Durchlauf].equals("")) {
			    	// Leerer Firmeneintrag, bedeutet keine Aktion
			    }
			    else { 	
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailGN[Durchlauf], test);
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PassGN[Durchlauf], test);
				
				// 1.1 Button "Einloggen" klicken
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
				
				// 2. Klicken auf Register "Auschreibung"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@data-test='Ausschreibungen']", test);
				
				// Die Anzeige auf 100 erhöhen
				Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//div[contains(@id,'mui')]",
						"//li[contains(text(),'", "100", test);
				
				// 3. Tender auswählen
				Utils.SeleniumUtils.AuschreibungGNAuswahl(driver, Zeitspanne,  VolumenGG, test);
				
				// Nur Angebot abgeben, falls erforderleich
				if ((driver.findElement(By.xpath("//h5")).getText()).equals("Anfrage Ausschreibung")) {
			
			    System.out.println(driver.findElement(By.xpath("//h5")).getText());
					
				// 4. Feld "Zinssatz" ausfüllen
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//input[@inputmode='numeric']",ZinsGN[Durchlauf], test);
				
				// 5. Klicken auf Button "Angebot senden"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='senden']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
				
				// 6. Klicken auf Button "Ja"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Ja']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
				
				// 6.1 Klicken auf Button "OK"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
				
				} // Auschreibung durchgeführt?
				
				// 7. Borrower 1 ausloggen
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='logout-button']", test);
				
			    } // End if
				
			    } // Ende for 

			
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
