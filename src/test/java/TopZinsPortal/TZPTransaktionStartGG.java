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
import Utils.TZPBeforeTest;
import junit.framework.Assert;
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
				htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Tranaktion Start GG - " + Ablaufart + ".html");
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
		public void TZPTransaktionStartGGTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG, String VolumenGG, String Valuta, String Zinskonvention, 
		String Zahlungsfrequenz, String EndeAnfrageGG1, 
		String BtnAnfrageSendenGG1, String BtnAusloggenGG1, String EmailadresseGN, String PasswortGN, String VolumenGN, String Zinssatz, String EndeAnfrageGN1, String BtnAngebotSendenGN1, 
		String BtnJaGN1, String BtnAusloggenGN1, String BtnAngebotAnnehmenGG2, String BtnJaGG2, String BtnAusloggenGG2  ) throws Exception {

			
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
			// Beispiel, wie man auf ein table cell clicken kann -> driver.findElement(By.xpath("//table/tbody/tr[2]/td[2]")).getText(); 
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//table/tbody/tr[6]/td[7]", test);  // Auswaehlen ein Zinssatz von der Spalte "1 M" (7 Spalte) von Bank in der 6 Zeile
			
			// 5. Alle Pflichtfelder in Pop-up "Anfrage Termingeldanlage starten" ausfuellen
			// 5.1 Volumen 
			// ????
			// Übergeordnetes Label
			//label[text()="Volumen*"]
			
			
			// 5.2 Valuta
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", "//*[@class='MuiInputBase-input-14181 MuiOutlinedInput-input-14168 MuiInputBase-inputAdornedEnd-14186 MuiOutlinedInput-inputAdornedEnd-14172 MuiInputBase-inputMarginDense-14182 MuiOutlinedInput-inputMarginDense-14169']", VolumenGN, test);
		
			// 6. Button "Anfrage senden" klicken 
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);
			
			// 7. Geldgeber ausloggen
			// Beispiel, wie man ein Element mit einem bestimmten Attribut finden kann -> webDriver.findElements(By.xpath("//element[@attribute='value']"))
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='logout-button']", test);  // Klicken auf ein Button mit dem Attribut data-test='logout-button'
			
			// 8. Loginseite oeffnen
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
			
			// 9. Login entsprechenden Geldgeber mit gueltigen Daten
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseGN, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortGN, test);
			
			// 10. Alle Pflichtfelder bearbeiten 
			// Geht auch ohne bearbeiten			
			
			// 11. Button "Angebot senden" klicken 
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//*[@class='MuiButtonBase-root MuiButton-root jss4198 MuiButton-text']", test);
			
			// 12. Button "Ja" in Pop-up klicken (class="MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//*[@class='MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary']", test);
			
			// 13. Geldnehmer ausloggen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='logout-button']", test);  // Klicken auf ein Button mit dem Attribut data-test='logout-button'
			
			// 14. Loginseite oeffnen
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);
			
			// 15. Login Geldgeber
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseGG, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortGG, test);
			
			// 16. Button "Angebot annehmen" klicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//*[@class='MuiButtonBase-root MuiButton-root jss16090 MuiButton-text']", test);
			
			// 17. Button "Ja" in Pop-up klicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//*[@class='MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary']", test);
						
			
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
