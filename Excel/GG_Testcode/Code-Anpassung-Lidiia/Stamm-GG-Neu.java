package TopZinsPortal;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.applitools.eyes.selenium.Eyes;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import Utils.ExcelUtilsJXL;
import Utils.TZPSetupBrowser;
import jxl.read.biff.BiffException;

public class TZPStammdatenGG {
	
	

	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen
	
		private WebDriver driver = null;
		private Integer Zeitspanne;
		private String BaseUrl;
		public String StandardBrowser;
		public String SpeicherpfadTestdokumente;
		public static String TestdatenExceldatei;

		// Klassenvariablen
		ExtentHtmlReporter htmlReporter = null;
		ExtentReports extent;

		public String Ablaufart;

		//public ChromeDevToolsService devToolsService = null;
		// Variable für Applitools
		public Eyes eyes = null;

		// @Parameters({ "Ablaufart" })
		
		@BeforeTest
		public void Setup() throws InterruptedException, IOException {

			if (htmlReporter == null) {
				// start reporters
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal " + Ablaufart + ".html");
				// create ExtentReports and attach reporter(s)
				extent = new ExtentReports();
				extent.attachReporter(htmlReporter);
			}
			
			// Hinweis: Für direkte Testläufe
			// Applitools und PDF-Druck dürfen nicht gleichzeitig ablaufen
			// Es kommt zu Fehlermeldungen

			this.Ablaufart = "PDF-Druck";
			System.out.println(Ablaufart);
			//StandardBrowser = "Chrome";
			StandardBrowser = "Firefox";
			Zeitspanne = 500;

			// Hinweis: Für direkte Testläufe
			// Applitools und PDF-Druck dürfen nicht gleichzeitig ablaufen
			// Es kommt zu Fehlermeldungen

			BaseUrl = "http://52.59.225.49/portal/login";
			SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
			// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
			// verglichen werden. "==" steht für die Überprüfung des Speicherorts

	        // Aufruf des Browser-Setups 
			driver = TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);
			
		}

		@DataProvider(name = "TZPStammGG")
		public static Object[][] getData() throws BiffException {
			// Ermittelt den Pfad des aktuellen Projekts
			String projectpath = System.getProperty("user.dir");
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

			int rowCount = excel.getRowCount();
			int colCount = excel.getColCount();
			
			System.out.println("Zeile=" + rowCount + "Spalte=" + colCount + "String Wert: ");

			// 2 Dimensionales Object-Array erzeugen
			Object data[][] = new Object[rowCount-1][colCount];

			// �ber alle Zeilen laufen (i=1, da i=0 die Headerzeile)
			for (int i = 1; i < rowCount; i++) {
				// �ber alle Spalten laufen
				for (int j = 0; j < colCount; j++) {

					String cellData = excel.getExcelDataString(i, j);
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
		public void TZP_Stamm_GG(String Teststep, String Emailadresse, String Passwort, String Unternehmensname, String Kundengruppe, String LEI,
					String Land, String Webseite, String EmailGeschaefte, String Str, String HausNr, String PLZ, String Ort, String Adresszusatz,
					String Bank, String BIC, String IBAN, String Kontoinhaber,
					String Anrede, String Vorname, String TelefonNr, String FaxNr, String Titel, 
					String Nachname,  String EmailLogin, String Doc_1, String Datum_1) throws Exception {

			// Mock
			// String teststep = "AL-R1";

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZRegGG: " + Teststep + " - " + Ablaufart,
					"Registrierung des Geldgebers");

			driver.get(BaseUrl);
			// TZRegGG-Eingabemaske
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

			// Login mit gültigen Daten
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort);

     		// Button "Registrieren auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]");
			
     		// Button "Vollständige Registrierung auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@data-test, 'logout-dsgvo-button')]");
			
         	// Auswahl Kundengruppe
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-customerGroupId\"]", "//li[contains(text(),'", Kundengruppe);
			
			// Auswahl LEI
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lei", LEI);

			// Auswahl Land
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-country\"]", "//li[contains(text(),'", Land);
						
	        // Auswahl Webseite, Geschäfts-Email, Straße, Nr., PLZ, Ort, Adresszusatz
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "website", Webseite);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "emailForBusinessConfirmations", EmailGeschaefte);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "street", Str);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "houseNumber", HausNr);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "postCode", PLZ);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "location", Ort);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "addressAddition", Adresszusatz);
			
     		// Button "Weiter" auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]");
			
			
			// driver.close();
			// Für den Teardown
			driver = null;
			eyes = null;

			// Neu Starten
			Setup();

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

//		public void ScreenshotAufnahme(String Ablaufart, ChromeDevToolsService devToolsService, String bildPath)
//				throws InterruptedException {
//			if (Ablaufart.equals("PDF-Druck")) {
//				// Screenshot erzeugen;
//				Thread.sleep(Zeitspanne);
	//
//				try {
//					// Take full screen
	//
//					FullScreenshot.captureFullPageScreenshot(devToolsService, bildPath);
//				} catch (AssertionError e) {
//					System.out.println(e);
//				}
	//
//				Thread.sleep(Zeitspanne);
//			}
//		}


		@AfterTest
		public void tearDown() throws InterruptedException {

			// calling flush writes everything to the log file
			extent.flush();

			Thread.sleep(3000);
			System.out.println("Test erfolgreich druchlaufen");
			if (driver != null) {
			//	driver.quit();
			}
			if (eyes != null) {
				eyes.close();
				eyes.abortIfNotClosed();
			}
		}

	}
