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

public class TZPTransaktionStartGG {
	
	
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
		

		@DataProvider(name = "TZPTransaktionStartGG")
		public static Object[][] getData() throws BiffException {
			// Ermittelt den Pfad des aktuellen Projekts
			projectpath = System.getProperty("user.dir");
			// Zugriff auf die zugehörigen Exceldaten
			
			TestdatenExceldatei = "\\Excel\\TopZinsPortalTransaktionGG-GN.xls";

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
		@Test(dataProvider = "TZPTransaktionStartGG")
		public void TZPTransaktionStartGGTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG, String VolumenGG, 
				String ZinssatzGG, String Valuta, String Zinskonvention, String Zahlungsfrequenz, String SonstigesGG, String KommentarGG, String EndeAnfrageUhrzeitGG, 
				String BtnAnfrageSendenGG, String BtnAusloggenGG, String FirmaGN, String EmailadresseGN, String PasswortGN, String VolumenGN, 
				String ZinssatzGN, String EndeAngebotGN, String BtnAngebotSendenGN, String BtnAnfrageAblehnenGN, String BtnAngebotTelefonischWeiterleitenGN, 
				String BtnAngebotAnnehmenGG, String BtnAngebotAblehnenGG, String BtnAngebotTelefonischAnnehmenGG  ) throws Exception {

			
			if (Aktiv.equals("Ja")) {
				

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZP_Transaktion: " + Teststep + " - " + AblaufartGlobal,
					"Start einer Transaktion durch den Geldgeber");

			driver.get(BaseUrl);
			// 1. Loginseite oeffnen
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

			// 2. Login Geldgeber, der eine handelsberechtigung hat, mit gueltigen Daten
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseGG, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortGG, test);

     		// 3. Button "Login" auswaehlen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			// 4. Ein Zinssatz in view "Alle Banken" auswaehlen
			// Die Ansicht Alle Banken aufrufen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='Übersicht']", test);
			// Unterpunkt Alle Banken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@href='/portal/Uebersicht/alleBanken']", test);
				
			
			// Auf das Zellenelemnet mit Bankname und Zinssatz zugreifen
			Utils.SeleniumUtils.TabelleButtonKlick(driver, Zeitspanne, "xpath", FirmaGN, ZinssatzGG, test); 

			
			// 5. Alle Pflichtfelder in Pop-up "Anfrage Termingeldanlage starten" ausfuellen
			// 5.1 Volumen 
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//input[@inputmode='numeric']", VolumenGG, test);
						
			
			// 5.2 Valuta
			Utils.SeleniumUtils.InputDatum(driver, Zeitspanne, "xpath", "//Label[text() ='Valuta*']","//following::input[contains(@class, 'MuiInput')]", Valuta, test);
		
			//5.3 Zinskonvention
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id='mui-component-select-interestRateConversionId']", "//li[contains(text(),'", Zinskonvention, test);
			
			//5.4 Zahlungsfrequenz
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id='mui-component-select-paymentFrequencyId']", "//li[contains(text(),'", Zahlungsfrequenz, test);
		
			// 5.5 Eintrag "Sonstiges" 
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//textarea[@name='other']", (SonstigesGG + " Geldgeber GB"), test);			

			// 5.6 Eintrag "Kommentar" 
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//textarea[@name='comment']", (KommentarGG + " Geldgeber GG"), test);		
			
			// 5.7 Feld "Ende der Anfrage (Zeit)" ausfüllen
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//Label[text() ='Ende der Anfrage (Uhrzeit)*']//following::input[contains(@class, 'MuiInput')][1]", EndeAnfrageUhrzeitGG, test);
			
			// 6.0 Button "Anfrage senden" klicken 
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			
			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);
			
			
			// 7. Geldgeber ausloggen
			// Beispiel, wie man ein Element mit einem bestimmten Attribut finden kann -> webDriver.findElements(By.xpath("//element[@attribute='value']"))
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='logout-button']", test);  // Klicken auf ein Button mit dem Attribut data-test='logout-button'
			
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
