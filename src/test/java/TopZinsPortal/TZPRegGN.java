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

public class TZPRegGN {
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
			htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Reg. GG - " + Ablaufart + ".html");
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

		BaseUrl = TZPBeforeTest.Umgebung() + "/portal/registrierungGeldnehmer";
		
		SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden. "==" steht für die Überprüfung des Speicherorts

        // Aufruf des Browser-Setups 
		driver = TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);
	}

	@DataProvider(name = "TZPRegGN")
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
	@Test(dataProvider = "TZPRegGN")
	public void TZRegGGTest(String teststep, String Unternehmensname, String Anrede,
			String Vorname, String Nachname, String TelefonNummer, String Emailadresse, String Passwort,
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

	    driver.findElement(By.name("companyName")).click();
	    driver.findElement(By.name("companyName")).clear();
	    driver.findElement(By.name("companyName")).sendKeys(Unternehmensname);
		// Zeitspanne setzen
		Thread.sleep(2 * Zeitspanne);
	    
	    
		// Zuerst auf das übergeordnete div klicken
		driver.findElement(By.xpath("//div[contains(@class, 'MuiInputBase-root MuiOutlinedInput-root MuiInputBase-formControl')]")).click();
		if (Anrede.equals("Herr")) {
		 // Element aus der Liste auswählen
		 driver.findElement(By.xpath("//*[@id=\"menu-gender\"]/div[3]/ul/li[1]")).click();
		} 
		if (Anrede.equals("Frau")) {
		     // Auswahl Frau
			 driver.findElement(By.xpath("//*[@id=\"menu-gender\"]/div[3]/ul/li[2]")).click();
		} 
		//*[@id="menu-gender"]/div[3]/ul/li[1]
		
		//driver.findElement(By.name("mui-component-select-gender")).click();
	    // driver.findElement(By.name("gender")).click();
	    // }
		// Zeitspanne setzen
		Thread.sleep(2 * Zeitspanne);
	    
	    driver.findElement(By.name("firstName")).click();
	    driver.findElement(By.name("firstName")).clear();
	    driver.findElement(By.name("firstName")).sendKeys(Vorname);
	    
	    driver.findElement(By.name("firstName")).click();   
	    driver.findElement(By.name("lastName")).clear();
	    driver.findElement(By.name("lastName")).sendKeys(Nachname);
	    
	    driver.findElement(By.name("tel")).click();;
	    driver.findElement(By.name("tel")).clear();
	    driver.findElement(By.name("tel")).sendKeys(TelefonNummer);
	    
	    driver.findElement(By.name("email")).click();
	    driver.findElement(By.name("email")).clear();
	    driver.findElement(By.name("email")).sendKeys(Emailadresse);
	    
	    driver.findElement(By.name("emailConfirm")).click();
	    driver.findElement(By.name("emailConfirm")).clear();
	    driver.findElement(By.name("emailConfirm")).sendKeys(Emailadresse);
	    
	    driver.findElement(By.name("password")).click();
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys(Passwort);
	    
		// Zuerst auf das übergeordnete fieldset klicken
		driver.findElement(By.xpath("//fieldset[contains(@class, 'MuiFormControl-root MuiFormControl-marginDense')]")).click();
		// Zeitspanne setzen
		Thread.sleep(2 * Zeitspanne);
		
		// Datenschutz öffnet sich
		// Button Schliessen auswählen
		// Beachte, der Tag-Classname ist nicht eindeutig und sollte nicht verwendet werden!
		// Daher der zugriff über den Tag "data-test"
		driver.findElement(By.xpath("//button[contains(@data-test, 'accept-dsgvo-button')]")).click();
		// Zeitspanne setzen
		Thread.sleep(1 * Zeitspanne);
		
		
//      Bei Fehler not Clickable at point (X, Y) kann nachfolgender Code verwendet werden:
//		WebElement element = driver.findElement(By.xpath("//input[contains(@class, 'jss277')]"));
//		Actions actions = new Actions(driver);
//		actions.moveToElement(element).click().build().perform();
		
		// Der vollständie Name ändert sich mit einer willkürlichen Nummer nach dem jss
		// Ohne die jss-Kennzeichnung ist das Element nihct eindeutig. 
		 driver.findElement(By.xpath("//label[contains(@class, 'MuiFormControlLabel-root jss')]")).click();
 		// Zeitspanne setzen
		Thread.sleep(1 * Zeitspanne);			
		

		// Button Schliessen auswählen
		// Beachte, der Tag-Classname ist nicht eindeutig und sollte nicht verwendet werden!
		// Daher der zugriff über den Tag "data-test"
		driver.findElement(By.xpath("//button[contains(@data-test, 'accept-dsgvo-button')]")).click();
		// Zeitspanne setzen
		Thread.sleep(1 * Zeitspanne);
		
		// Button "Registrieren auswählen"
		driver.findElement(By.xpath("//button[contains(@type, 'submit')]")).click();
		// Zeitspanne setzen
		Thread.sleep(4 * Zeitspanne);
		
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
