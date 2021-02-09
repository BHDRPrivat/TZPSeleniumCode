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

public class TZPTenderStartGG {
	
	// Beachte: Die Volumeneingabe des Geldgebers in der Exceldatei steuert den Ablauf.
	
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
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Ausschreibung Start GG - " + Ablaufart + ".html");
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
		

		@DataProvider(name = "TZPTenderStartGG")
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
		@Test(dataProvider = "TZPTenderStartGG")
		public void TZPTenderStartGGTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG, String EndeDatum, 
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
					"Start einen Tender durch den Geldgeber");

				driver.get(BaseUrl);
				// Loginseite oeffnen
				Thread.sleep(3 * Zeitspanne);
				test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
				
				// LENDER SENDET EINE TENDER ANFRAGE				
				// 1. Lender mit eine Handelsberechtigung einloggen
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseGG, test);
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortGG, test);
				
				// 1.1 Button "Einloggen" klicken
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
				
				// 2. Klicken auf Register "Auschreibung"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@data-test='Ausschreibungen']", test);

				
				// 3. Klicken auf Button "Neue Ausschreibung"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Neue Ausschreibung']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
				
				// 4. Alle Pflichtfelder ausfüllen
				// 4.1 Feld "Ende der Ausschreibung (Datum)" ausfüllen
				Utils.SeleniumUtils.InputDatum(driver, Zeitspanne, "xpath", "//Label[text() ='Ende der Ausschreibung (Datum)*']", "//following::input[contains(@class, 'MuiInput')][1]", EndeDatum, test);
				
				// 4.2 Feld "Ende der Ausschreibung (Zeit)" ausfüllen
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//Label[text() ='Ende der Ausschreibung (Uhrzeit)*']//following::input[contains(@class, 'MuiInput')][1]", EndeUhrzeit, test);

				// 4.3 Drei Banken auswählen 
				// Button klick zum öffnen der Maske
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//*[text()='Geldnehmer*']//following::button[contains(@class, 'MuiButtonBase-root')][1]", test);
				
				// 3 Banken auswählen und Maske wieder schließen
				Utils.SeleniumUtils.AusschreibungBankenAuswahl(driver, Zeitspanne, FirmaGN1,  test);
				Utils.SeleniumUtils.AusschreibungBankenAuswahl(driver, Zeitspanne, FirmaGN2,  test);
				Utils.SeleniumUtils.AusschreibungBankenAuswahl(driver, Zeitspanne, FirmaGN3,  test);
				
				// Auswahl annehmen
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='Auswahl bestätigen']//ancestor::button", test);
				
							
				// 4.4 Field "Volumen" ausfüllen
				// In der Zahl der Exceleingabe die "." entfernen, da bei der Eingabe in das Feld eine Fehlinterpretion erfolgt.
				VolumenGG = (VolumenGG.replace(".", ""));
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//Label[text() ='Volumen*']//following::input[contains(@class, 'MuiInput')]", VolumenGG, test);
				
				// 4.5 Field "Valuta" ausfüllen
				Utils.SeleniumUtils.InputDatum(driver, Zeitspanne, "xpath", "//Label[text() ='Valuta*']", "//following::input[contains(@class, 'MuiInput')]", Valuta, test);

				// 4.6 Field "Fälligkeit" ausfüllen
				Utils.SeleniumUtils.InputDatum(driver, Zeitspanne, "xpath", "//Label[text() ='Fälligkeit*']", "//following::input[contains(@class, 'MuiInput')]", Faelligkeit1, test);
				
				// 5. Klicken auf Button "Banken einladen"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
				
			
				// 7. Button "OK" klicken
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);

				// 6. Lender ausloggen
				// Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='logout-button']", test);
				
				Thread.sleep(10 * Zeitspanne);
				
				
				
			/*
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
			// Auf das Zellenelemnet mit Bankname und Zinssatz zugreifen
			Utils.SeleniumUtils.TabelleButtonKlick(driver, Zeitspanne, "xpath", FirmaGN, ZinssatzGG, test); 
			System.out.println("nach Buttonclick");
			
			// 5. Alle Pflichtfelder in Pop-up "Anfrage Termingeldanlage starten" ausfuellen
			// 5.1 Volumen 
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//input[@inputmode='numeric']", VolumenGN, test);
						
			
			// 5.2 Valuta
			Utils.SeleniumUtils.InputDatum(driver, Zeitspanne, "xpath", "//Label[text() ='Valuta*']//following::input[contains(@class, 'MuiInput')]", Valuta, test);
		
			// 6.0 Button "Anfrage senden" klicken 
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			
			// 6.1 OK Button der neuen Meldung
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')]", test);
			
			
			// 7. Geldgeber ausloggen
			// Beispiel, wie man ein Element mit einem bestimmten Attribut finden kann -> webDriver.findElements(By.xpath("//element[@attribute='value']"))
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='logout-button']", test);  // Klicken auf ein Button mit dem Attribut data-test='logout-button'
			
			Thread.sleep(3 * Zeitspanne);
						
			
			driver.close();
			// Für den Teardown
			driver = null;
			eyes = null;

			// Neu Starten
			SetupSeleniumTestdaten(AblaufartGlobal);*/

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
