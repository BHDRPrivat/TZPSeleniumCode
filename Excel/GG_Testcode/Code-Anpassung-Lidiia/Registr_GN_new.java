package TopZinsPortal;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
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

public class TZPRegistrierungGN {
// Der Registrierungsprozess eines Geldgebers wird Excel-Datengetrieben durchlaufen
	
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

		this.Ablaufart = "Applitools";
		System.out.println(Ablaufart);
		StandardBrowser = "Chrome";
		Zeitspanne = 500;

		// Hinweis: Für direkte Testläufe
		// Applitools und PDF-Druck dürfen nicht gleichzeitig ablaufen
		// Es kommt zu Fehlermeldungen

		BaseUrl = "http://52.59.225.49/portal/registrierungGeldnehmer";
		SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden. "==" steht für die Überprüfung des Speicherorts

        // Aufruf des Browser-Setups 
		driver = TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);
	}

	@DataProvider(name = "TZRegGN")
	public static Object[][] getData() throws BiffException {
		// Ermittelt den Pfad des aktuellen Projekts
		String projectpath = System.getProperty("user.dir");
		// Zugriff auf die korrekten Exceldaten
		
		TestdatenExceldatei = "\\Excel\\TopZinsPortalRegGN.xls";

		
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
	@Test(dataProvider = "TZRegGN")
	public void TZRegGGTest(String teststep, String Unternehmensname, String Anrede,
			String Vorname, String Nachname, String TelefonNummer, String Emailadresse, String EmailConfirm, String Passwort,
			String Datenschutz, String BtnRegistrien, String BtnAbbrechen) throws Exception {

		// Mock
		// String teststep = "AL-R1";

		// creates a toggle for the given test, adds all log events under it
		ExtentTest test = extent.createTest("TZRegGG: " + teststep + " - " + Ablaufart,
				"Registrierung des Geldgebers");

		driver.get(BaseUrl);
		// TZRegGG-Eingabemaske
		Thread.sleep(3 * Zeitspanne);
		test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

	    // Unternehmensname
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "companyName", Unternehmensname);
		
	    // Auswahl Anrede
		Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-gender\"]", "//li[contains(text(),'", Anrede);
		
		// Auswahl Titel
		Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-title\"]", "//li[contains(text(),'", Titel);
			
		//*[@id="menu-gender"]/div[3]/ul/li[1]
		    
		// Vorname
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "firstName", Vorname);
		
		// Nachname
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lastName", Nachname);
	   
		// Telefonnummer
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "tel", TelefonNummer);
	    
		// E-Mail
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse);
	    
		// E-Mail Bestätigung
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "emailConfirm", EmailConfirm);
		
		// Passwort
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort);
	   	    
		// Zuerst auf das übergeordnete fieldset klicken
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//fieldset[contains(@class, 'MuiFormControl-root MuiFormControl-marginDense')]");
		
		// Datenschutz öffnet sich
		// Button Schliessen auswählen
		// Beachte, der Tag-Classname ist nicht eindeutig und sollte nicht verwendet werden!
		// Daher der zugriff über den Tag "data-test"
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@data-test, 'accept-dsgvo-button')]");
		
		
//      Bei Fehler not Clickable at point (X, Y) kann nachfolgender Code verwendet werden:
//		WebElement element = driver.findElement(By.xpath("//input[contains(@class, 'jss277')]"));
//		Actions actions = new Actions(driver);
//		actions.moveToElement(element).click().build().perform();
		
		// Der vollständie Name ändert sich mit einer willkürlichen Nummer nach dem jss
		// Ohne die jss-Kennzeichnung ist das Element nihct eindeutig. 
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//label[contains(@class, 'MuiFormControlLabel-root jss')]");
		

		// Button Schliessen auswählen
		// Beachte, der Tag-Classname ist nicht eindeutig und sollte nicht verwendet werden!
		// Daher der zugriff über den Tag "data-test"
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@data-test, 'accept-dsgvo-button')]");
		
		// Button "Registrieren auswählen"
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]");
		
		// Kontrolle, ob Bestätigung angezeigt wird.
		// Noch zu programmieren
		
		
//		driver.close();
//		// Für den Teardown
//		driver = null;
//		eyes = null;
//
//		// Neu Starten
//		Setup();

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

//	public void ScreenshotAufnahme(String Ablaufart, ChromeDevToolsService devToolsService, String bildPath)
//			throws InterruptedException {
//		if (Ablaufart.equals("PDF-Druck")) {
//			// Screenshot erzeugen;
//			Thread.sleep(Zeitspanne);
//
//			try {
//				// Take full screen
//
//				FullScreenshot.captureFullPageScreenshot(devToolsService, bildPath);
//			} catch (AssertionError e) {
//				System.out.println(e);
//			}
//
//			Thread.sleep(Zeitspanne);
//		}
//	}


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
