package TopZinsPortal;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class TZPTransaktionAbschlussGNSeveralUser {
	// Beachte die Reihenfolge der Masken ist bei GN in umgekehrter Reihenfolge ! 
	
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
	public static Integer AktuellTransaktionMaske = 0; 


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
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Transaktion Abschluss GN - " + Ablaufart + ".html");
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
		
        // Nicht umstellen, da die Anzahl der Zeilen benötigt wird.
		@DataProvider(name = "TZPTransaktionAbschlussGNSeveralUser")
		public static Object[][] getData() throws BiffException {
			// Ermittelt den Pfad des aktuellen Projekts
			projectpath = System.getProperty("user.dir");
			// Zugriff auf die zugehörigen Exceldaten
			
			TestdatenExceldatei = "\\Excel\\TopZinsPortalTransaktionGG-GN-SeveralUser.xls";

			String excelPath = projectpath + TestdatenExceldatei;
			Object testData[][] = testData(excelPath, "Testdaten");
			return testData;
		}

		public static Object[][] testData(String excelPath, String sheetName) throws BiffException {
			// Aufruf des Constructors von ExcelUtils
			ExcelUtilsJXL excel = new ExcelUtilsJXL(excelPath, sheetName);

			int rowCount = ExcelUtilsJXL.getRowCount();
			int colCount = ExcelUtilsJXL.getColCount();
			
			AktuellTransaktionMaske = rowCount ;
			
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
		@Test(dataProvider = "TZPTransaktionAbschlussGNSeveralUser")
		public void TZPTransaktionAbschlussGNSeveralUserTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG, String VolumenGG, 
				String ZinssatzGG, String Valuta, String Zinskonvention, String Zahlungsfrequenz, String SonstigesGG, String KommentarGG, String EndeAnfrageUhrzeitGG, 
				String BtnAnfrageSendenGG, String BtnAusloggenGG, String FirmaGN, String EmailadresseGN, String PasswortGN, String VolumenGN, 
				String ZinssatzGN, String EndeAngebotGN, String BtnAngebotSendenGN, String BtnAnfrageAblehnenGN, String BtnAngebotTelefonischWeiterleitenGN, 
				String BtnAngebotAnnehmenGG, String BtnAngebotAblehnenGG, String BtnAngebotTelefonischAnnehmenGG  ) throws Exception {

			
			if (Aktiv.equals("Ja")) {
				
				// Für jeden Testfall wird die entsperechende Transaktionsmasken ausgewählt 
				// Beachte die Reihenfolge ist bei den GN in umgekehrter Reihenfolge	
				// Die Anzahl der verschiebraen Elemente finden 	
				
			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZP_Transaktion: " + Teststep + " - " + AblaufartGlobal,
					"Akzeptieren einer Transaktion durch den Geldnehmer");

			driver.get(BaseUrl);
			// 18. Loginseite oeffnen
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

					
			// 19. Login entsprechenden Geldnehmer mit gueltigen Daten
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseGN, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortGN, test);
			
     		// 19.1 Button "Login" auswaehlen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
		

			// Nur die real vorhandenen Masken mit OK schließen	
			AktuellTransaktionMaske = driver.findElements(By.xpath("//div[contains(@class, 'MuiDialogTitle-root')]")).size();		
			System.out.println("Anzahl Masken = " + AktuellTransaktionMaske);
			
			if (BtnAnfrageAblehnenGN.equals("Ja")) {
				// Nichts passiert, da entprechendes Fenster nihct existiert
				
			} else {
		
            // Drag Drop 			
			Utils.SeleniumUtils.DragDrop(driver, Zeitspanne, "(//div[contains(@class, 'MuiDialogTitle-root')])[" + AktuellTransaktionMaske +"]", "//div[@data-test='sentinelStart']", test);
		
			// 20. Button "Angebot annehmen" klicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "(//span[text()='OK']//ancestor::button[contains(@class, 'MuiButtonBase')])[" + AktuellTransaktionMaske + "]", test);
			
			Thread.sleep(8 * Zeitspanne);
			}	
			
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
