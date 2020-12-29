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
import Utils.TZPBeforeTest;
import Utils.TZPSetupBrowser;
import junit.framework.Assert;
import jxl.read.biff.BiffException;

public class TZPAdminHandelsfreigabeGG {

	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen

	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = null;
	// Für AutoIT
	String workingDir;
	String autoitscriptpath;
	String filepath;

	// Wenn alle Stammdaten eingegeben wurden, kann mit true direkt auf Dokumente
	// zugegriffen werden
	Boolean MissingData = true;

	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	public static String AblaufartGlobal;

	// public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;

	@Parameters({ "Ablaufart" })
	@BeforeTest
	public void Setup(@Optional("Ad Hoc Test") String Ablaufart) throws InterruptedException, IOException {

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter(
					"Fehlerreport TopZinsPortal Admin HandelsfreigabeGG - " + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}


		System.out.println("Handelsfreigabe: " + Ablaufart);
		AblaufartGlobal = Ablaufart;
		StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
		Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

		// Hinweis: Für direkte Testläufe
		// Applitools und PDF-Druck dürfen nicht gleichzeitig ablaufen
		// Es kommt zu Fehlermeldungen

		workingDir = System.getProperty("user.dir");
		autoitscriptpath = workingDir + "\\AutoIT\\" + "File_upload_selenium_webdriver.au";
		filepath = workingDir + "\\DummyPDF\\PDF-Dummy.pdf";

		BaseUrl = TZPBeforeTest.Umgebung() + "/portal/login";

		SpeicherpfadTestdokumente = workingDir + "\\test-output\\PDFOutput\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden. "==" steht für die Überprüfung des Speicherorts

		// Aufruf des Browser-Setups
		driver = TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);

	}

	@DataProvider(name = "TZPAdminHandelsfreigabeGG")
	public static Object[][] getData() throws BiffException {
		// Ermittelt den Pfad des aktuellen Projekts
		projectpath = System.getProperty("user.dir");

		// Ermittelt den Pfad des aktuellen Projekts
		String projectpath = System.getProperty("user.dir");
		// Zugriff auf die zugehörigen Exceldaten

		TestdatenExceldatei = "\\Excel\\TopZinsPortalAdminHandelsfreigabeGG.xls";

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
		Object data[][] = new Object[rowCount - 1][colCount];

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
	@Test(dataProvider = "TZPAdminHandelsfreigabeGG")
	public void TZPAdminHandelsfreigabeGGTest(String Teststep, String Aktiv, String Emailadresse, String Passwort,
			String Menue, String ZeilenProSeite, String Unternehmensname) throws Exception {

		if (Aktiv.equals("Ja")) {

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPAdminHndelsfreigabeGG: " + Teststep + " - " + AblaufartGlobal,
					"Handelsfreigabe durch Admin");

			driver.get(BaseUrl);
			// TZRegGG-Eingabemaske
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

			// Login mit gültigen Daten
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort, test);

			// Button "Anmelden auswählen"
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);

			// Button "Daten komplett" in menu clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, '" + Menue + "')]",
					test);

			// Die Anzeige auf 100 erhöhen
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//div[contains(@id,'mui')]",
					"//li[contains(text(),'", ZeilenProSeite, test);

			// Firmenname in das Suchfeld eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
					"\\Admin Handelsfreigabe\\AmdinHandlefreigabe-GG-Auswahl", Teststep, test);
			Thread.sleep(3 * Zeitspanne);

			// Der Stift für das Laden der Daten hat keine eindeutige ID. Der Zugriff
			// erfolgt über den Eintrag im ersten Eingabefeld
			// Beachte, der Eintrag im ersten Eingabefeld ist abhängig vom Unternehmensnamen
			String xpathvalue = "//div[text() = '" + Unternehmensname
					+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root MuiTableRow-hover')]//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);

			// Direktsprung auf Dokumente	
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@href='#/masterdata/documents']", test);

			// Haken setzen für Dokumentenkontrolle des Admins's
			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'registerExtractStatute']",
					test);
			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'identityCard']", test);
			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'tradingLicense']", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
					"\\Admin Handelsfreigabe\\AmdinHandlefreigabe-GG-Haken", Teststep, test);

			// Button Freigeben auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
					"//span[text()='Freigeben']//ancestor::button[@tabindex='0']", test);
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);

			// Prüfen ob User in Register "Mit Handelsberechtigung" vorhanden ist?
			// Button "Daten komplett" in menu clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
					"//li[contains(@data-test, 'MIT HANDELSBERECHTIGUNG')]", test);

			// Die Anzeige auf 100 erhöhen
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//div[contains(@id,'mui')]",
					"//li[contains(text(),'", ZeilenProSeite, test);

			// Firmenname in das Suchfeld eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);
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

		// Offene Bereiche Schließen
		Utils.SeleniumUtils.BrowserBeenden(driver, Zeitspanne, extent, eyes);

	}

}
