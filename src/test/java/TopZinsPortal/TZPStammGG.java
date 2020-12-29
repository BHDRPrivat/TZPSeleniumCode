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
import Utils.SeleniumUtils;
import Utils.TZPBeforeTest;
import junit.framework.Assert;
import jxl.read.biff.BiffException;

public class TZPStammGG {
	
	
	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen
	
	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = null;

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
		public void Setup(@Optional("Ad Hoc Test") String Ablaufart) throws InterruptedException, IOException {

			if (htmlReporter == null) {
				// start reporters
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Stammdaten GG - " + Ablaufart + ".html");
				// create ExtentReports and attach reporter(s)
				extent = new ExtentReports();
				extent.attachReporter(htmlReporter);
			}
			AblaufartGlobal = Ablaufart;
			StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
			Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

			BaseUrl = TZPBeforeTest.Umgebung() + "/portal/login";

			SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
			// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
			// verglichen werden. "==" steht für die Überprüfung des Speicherorts

			// Aufruf des Browser-Setups
			driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);

		
		}
		
		private void TZPBeforeTest(String baseUrl2) {
			// TODO Auto-generated method stub
			
		}

		@DataProvider(name = "TZPStammGG")
		public static Object[][] getData() throws BiffException {
			// Ermittelt den Pfad des aktuellen Projekts
			projectpath = System.getProperty("user.dir");
			// Zugriff auf die zugehörigen Exceldaten
			
			TestdatenExceldatei = "\\Excel\\TopZinsPortalStammGG.xls";

			
			// Ablaufpräsentation
			// TestdatenExceldatei = "\\Excel\\AL-Risiko-Testdaten-V1-Fehler.xlsx";

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
		@Test(dataProvider = "TZPStammGG")
		public void TZPStammGGTest(String Teststep, String Aktiv, String Emailadresse, String Passwort, String Unternehmensname, String Kundengruppe, String LEI,
					String Land, String Webseite, String EmailGeschaefte, String Str, String HausNr, String PLZ, String Ort, String Adresszusatz,
					String Bank, String BIC, String IBAN, String Kontoinhaber,
					String Anrede, String Vorname, String TelefonNr, String FaxNr, String Titel, 
					String Nachname,  String EmailLogin, String Doc_1, String Datum_1) throws Exception {

			
			if (Aktiv.equals("Ja")) {
			
			// Mock
			// String teststep = "AL-R1";

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPStammGG: " + Teststep + " - " + AblaufartGlobal,
					"Stammdateneingabe des Geldgebers");

			driver.get(BaseUrl);
			// TZRegGG-Eingabemaske
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
			
			
			// Login mit gültigen Daten
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort, test);

     		// Button "Registrieren auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			Thread.sleep(3 * Zeitspanne);
			
     		// Button "Vollständige Registrierung auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@data-test, 'logout-dsgvo-button')]", test);
			
			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Login für die Stammdaten GG ", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
			
			if (MissingData) {
			
			// Reiter Unternehmen
			// Auswahl Unternehmensname
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "companyName", Unternehmensname, test);
			
			// Auswahl Kundengruppe
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-customerGroupId\"]", "//li[contains(text(),'", Kundengruppe, test);
			
			// Auswahl LEI
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lei", LEI, test);

			// Auswahl Land
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-country\"]", "//li[contains(text(),'", Land, test);
						
	        // Auswahl Webseite, Geschäfts-Email, Straße, Nr., PLZ, Ort, Adresszusatz
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "website", Webseite, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "emailForBusinessConfirmations", EmailGeschaefte, test);
			
			// in der Stage-Version
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "street", Str, test);
			
			// In der 3-Version so benannt
			// Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "streetName", Str, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "houseNumber", HausNr, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "postCode", PLZ, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "location", Ort, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "addressAddition", Adresszusatz, test);
			
     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
						
			// Auswahl Register Unternehmen -> wurden die Daten sauber gespeichert?
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Unternehmen')]", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Stammdaten GG 1-Unternehmen", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
			
     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
	
			// Auswahl Register Bankverbindung -> Falls ein weiterer Reiter erzeugt wurde erfolgt ein Direktzugriff
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Bankverbindung')]", test);
			
			
			// Reiter Bankverbindung ausfüllen
			// Bank-Daten
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "bank", Bank, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "bic", BIC, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "iban", IBAN, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "recipientName", "iban", Kontoinhaber, test);
			
			// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			// Auswahl Register Bankverbindung -> wurden die Daten sauber gespeichert?
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Bankverbindung')]", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Stammdaten GG 2-Bankverbindung", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
			
    		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);	

			// Auswahl Register Person -> Falls ein weiterer Reiter erzeugt wurde erfolgt ein Direktzugriff
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Person')]", test);			
			
			// Reiter Person ausfüllen
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "firstName", Vorname, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "phone", TelefonNr, test);
			// Element wurde entfernt
			//Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "fax", FaxNr);			
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lastName", Nachname, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailLogin, test);			
			
		
     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			// Auswahl Register Person -> wurden die Daten sauber gespeichert?
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[contains(@data-test, 'Person')]", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Stammdaten GG 3-Person", Teststep, test);
			Thread.sleep(3 * Zeitspanne);
			
    		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);	
			
		
			
			} // IF-Ende für Missing Data
			else {
			 // Direktsprung auf Dokumente
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@href='#/masterdata/documents']", test);
			}
				
				
			
//			// Die Heftzwecke für das Hochladen des Dokuments hat keine eindeutige ID. Der Zugriff erfolgt über den Eintrag im ersten Eingabefeld
//			// Beachte, der Eintrag im ersten Eingabefeld ist abhängig vom Unternehmensnamen 
			// Alle anderen Zeilen können mit Standardwerten durchsucht werden. 
			
			Utils.SeleniumUtils.PDFUpload(driver, Zeitspanne, "xpath", "", Unternehmensname, "", Datum_1, test);
			
			Utils.SeleniumUtils.PDFUpload(driver, Zeitspanne, "xpath", "", "Personalausweis", "[2]", Datum_1, test);
			
			Utils.SeleniumUtils.PDFUpload(driver, Zeitspanne, "xpath", "", "Handelsberechtigung", "[2]", Datum_1, test);
			
			Thread.sleep(10 * Zeitspanne);	
			// Hochladen auswählen		
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Hochladen']//ancestor::button[@tabindex='0']", test);
			// TSonderzeit zum Hochladen
			Thread.sleep(10 * Zeitspanne);
			
			
			// Kontrolle, ob kein Fehlertext angezeigt wird
			// Prüfen, ob die die Maske mit den Button Vollständige Registrierung angezeigt wird  
			// Programm läuft nicht weiter
			// Suche nach einem Text. Nicht findelement verwenden, da dieser einen Fehler auswirft
			// Assert.assertFalse((driver.findElement(By.xpath("//p[text()='Bitte wählen Sie ein Dokument zum Hochladen aus']")).isDisplayed()));
			Assert.assertEquals(driver.getPageSource().contains("Bitte wählen Sie ein Dokument zum Hochladen aus"), false);
			Thread.sleep(5 * Zeitspanne);
			
				
			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Stammdaten GG 4-Dokumente hochladen ", Teststep, test);
			Thread.sleep(10 * Zeitspanne);
			
			
			// Handelsfreigabe beantragn		
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Handelsberechtigung anfordern']//ancestor::button[@tabindex='0']", test);
			// TSonderzeit zum Hochladen
			Thread.sleep(10 * Zeitspanne);			
			
			// Kontrolle, ob kein Fehlertext angezeigt wird
			// Prüfen, ob die die Maske mit den Button Vollständige Registrierung angezeigt wird  
			// Programm läuft nicht weiter
			// Assert.assertFalse((driver.findElement(By.xpath("//p[text()='Alle Pflichtfelder müsssenausgefüllt werden']")).isDisplayed()));
			Assert.assertEquals(driver.getPageSource().contains("Alle Pflichtfelder müsssenausgefüllt werden"), false);
			Thread.sleep(5 * Zeitspanne);
			
			
			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Stammdaten GG 5-Handelfreigabe angefordert ", Teststep, test);
			Thread.sleep(5 * Zeitspanne);
	
			driver.close();
			// Für den Teardown
			driver = null;
			eyes = null;

			// Neu Starten
			Setup(AblaufartGlobal);

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
		public void tearDown() throws InterruptedException {

	        // Offene Bereiche Schließen
			Utils.SeleniumUtils.BrowserBeenden(driver, Zeitspanne, extent,  eyes);
		}

	}
