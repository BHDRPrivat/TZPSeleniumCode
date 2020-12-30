package TopZinsPortal;

import java.awt.Robot;
import java.awt.event.KeyEvent;
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
import Utils.TZPBeforeTest;
import junit.framework.Assert;
import jxl.read.biff.BiffException;

public class TZPRegGG {
// Der Registrierungsprozess eines Geldgebers wird Excel-Datengetrieben durchlaufen

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

	public static String AblaufartGlobal;

	// public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;

	@Parameters({ "Ablaufart" })
	@BeforeTest
	public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart) throws InterruptedException, IOException {

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter("Fehlerreport TopZinsPortal Registrierung GG - " + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}
		AblaufartGlobal = Ablaufart;
		StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
		Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

		BaseUrl = TZPBeforeTest.Umgebung() + "/portal/registrierungGeldgeber";

		SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden. "==" steht für die Überprüfung des Speicherorts

		// Aufruf des Browser-Setups
		driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);


		
	}

	@DataProvider(name = "TZPRegGG")
	public static Object[][] getData() throws BiffException {
		// Ermittelt den Pfad des aktuellen Projekts
		projectpath = System.getProperty("user.dir");
		// Zugriff auf die korrekten Exceldaten
		TestdatenExceldatei = "\\Excel\\TopZinsPortalRegGG.xls";

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
		Object data[][] = new Object[rowCount - 1][colCount];

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
	@Test(dataProvider = "TZPRegGG")
	public void TZPRegGGTest(String Teststep, String Aktiv, String Unternehmensname, String Anrede, String Titel, String Vorname,
			String Nachname, String TelefonNummer, String Emailadresse, String EmailConfirm, String Passwort,
			String Datenschutz, String BtnRegistrien, String BtnAbbrechen) throws Exception {

		if (Aktiv.equals("Ja")) {
		// Mock
		// String teststep = "AL-R1";

		// creates a toggle for the given test, adds all log events under it
		ExtentTest test = extent.createTest("TZPRegGG: " + Teststep + " - " + AblaufartGlobal,
				"Registrierung von Geldgebern (GG)");

		

		driver.get(BaseUrl);
		
		// TZRegGG-Eingabemaske
		Thread.sleep(3 * Zeitspanne);
		test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

		// Unternehmensname
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "companyName", Unternehmensname, test);

		// Auswahl Anrede
		Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-gender\"]",
				"//li[contains(text(),'", Anrede, test);

		// Auswahl Titel
		Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//*[@id=\"mui-component-select-title\"]",
				"//li[contains(text(),'", Titel, test);

		// Vorname
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "firstName", Vorname, test);

		// Nachname
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "lastName", Nachname, test);

		// Telefonnummer
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "tel", TelefonNummer, test);

		// E-Mail
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse, test);

		// E-Mail Bestätigung
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "emailConfirm", EmailConfirm, test);

		// Passwort
		Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort, test);

		// Screenshot aufnehmen
		Thread.sleep(3 * Zeitspanne);
		Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath, "\\Reg GG\\Eingabe-Reg-Werte-GG", Teststep, test);
		Thread.sleep(3 * Zeitspanne);

		// Zuerst auf das übergeordnete fieldset klicken
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
				"//fieldset[contains(@class, 'MuiFormControl-root MuiFormControl-marginDense')]", test);

		// Datenschutz öffnet sich
		// Button Schliessen auswählen
		// Beachte, der Tag-Classname ist nicht eindeutig und sollte nicht verwendet
		// werden!
		// Daher der zugriff über den Tag "data-test"
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
				"//button[contains(@data-test, 'accept-dsgvo-button')]", test);

//      Bei Fehler not Clickable at point (X, Y) kann nachfolgender Code verwendet werden:
//		WebElement element = driver.findElement(By.xpath("//input[contains(@class, 'jss277')]"));
//		Actions actions = new Actions(driver);
//		actions.moveToElement(element).click().build().perform();

		// Der vollständie Name ändert sich mit einer willkürlichen Nummer nach dem jss
		// Ohne die jss-Kennzeichnung ist das Element nihct eindeutig.
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
				"//label[contains(@class, 'MuiFormControlLabel-root jss')]", test);

		// Button Schliessen auswählen
		// Beachte, der Tag-Classname ist nicht eindeutig und sollte nicht verwendet
		// werden!
		// Daher der zugriff über den Tag "data-test"
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
				"//button[contains(@data-test, 'accept-dsgvo-button')]", test);


		// Button "Registrieren auswählen"
		Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);

	
		// Screenshot aufnehmen
		Thread.sleep(3 * Zeitspanne);
		Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,"\\Reg GG\\Nach-Registrierung-Button", Teststep, test );
		Thread.sleep(3 * Zeitspanne);

		// Kontrolle, ob Bestätigung angezeigt wird.
		// Prüfen, ob die die Maske mit den Button Vollständige Registrierung angezeigt wird  
		// Programm läuft nicht weiter
		Assert.assertTrue((driver.findElement(By.xpath("//span[text()='Vollständige Registrierung']")).isDisplayed()));
		Thread.sleep(3 * Zeitspanne);	
		
		
		driver.close();
		// Für den Teardown
		driver = null;
		eyes = null;

		// Neu Starten
		SetupSeleniumTestdaten(AblaufartGlobal);
		
		} // Nur wenn Aktiv "Ja" ist durchlaufen

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
