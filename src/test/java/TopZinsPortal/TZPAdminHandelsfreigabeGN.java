package TopZinsPortal;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

public class TZPAdminHandelsfreigabeGN {

	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen

	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = null;
	public static String AnmeldungForsaAdmin = null;
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
	public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart)
			throws InterruptedException, IOException {

		// Ermittelt den Pfad des aktuellen Projekts
		projectpath = System.getProperty("user.dir");
		
		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter(
					"Fehlerreport TopZinsPortal Admin HandelsfreigabeGN - " + Ablaufart + ".html");
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

		if (AnmeldungForsaAdmin == null) {
			// Aufruf des Browser-Setups
			driver = TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);
		}
	}

	@DataProvider(name = "TZPAdminHandelsfreigabeGN")
	public static Object[][] getData() throws BiffException {
		// Ermittelt den Pfad des aktuellen Projekts
		projectpath = System.getProperty("user.dir");
		// Zugriff auf die zugehörigen Exceldaten

		TestdatenExceldatei = "\\Excel\\TopZinsPortalAdminHandelsfreigabeGN.xls";

		String excelPath = projectpath + TestdatenExceldatei;
		Object testData[][] = testData(excelPath, "Testdaten");
		return testData;
	}

	public static Object[][] testData(String excelPath, String sheetName) throws BiffException {
		// Ermittelt den Pfad des aktuellen Projekts
		String projectpath = System.getProperty("user.dir");
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
	@Test(dataProvider = "TZPAdminHandelsfreigabeGN")
	public void TZPAdminHandelsfreigabeGNTest(String Teststep, String Aktiv, String Menue, String ZeilenProSeite,
			String Unternehmensname) throws Exception {

		if (Aktiv.equals("Ja")) {

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPAdminHndelsfreigabeGN: " + Teststep + " - " + AblaufartGlobal,
					"Handelsfreigabe durch Admin");

			if (AnmeldungForsaAdmin == null) {

				driver.get(BaseUrl);
				// TZRegGN-Eingabemaske
				Thread.sleep(3 * Zeitspanne);
				test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

				// Login mit gültigen Daten

				String Emailadresse = Utils.TZPBeforeTest.AdminEmail();
				String Passwort = Utils.TZPBeforeTest.AdminPasswort();

				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse, test);
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort, test);

				// Button "Anmelden auswählen"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]",
						test);

				// Anmeldung ist erfolgt, fall null, wird alles wie früher durchlaufen
				AnmeldungForsaAdmin = "Ja";
			}

			// Button "Daten komplett" in menu clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//li[contains(@data-test, '" + Menue + "')]",
					test);
			
			// Weiterlauf nur nach implizierter Anzeige des Suchfeldes
			// Sobald die Kondition erfüllt wird, erfolgt der weitere Programmablauf.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='search']")));


     		// Firmenname in das Suchfeld eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
					"\\Admin Handelsfreigabe\\AmdinHandlefreigabe-GN-Auswahl", Teststep, test);
			Thread.sleep(3 * Zeitspanne);

			// Der Stift für das Laden der Daten hat keine eindeutige ID. Der Zugriff
			// erfolgt über den Eintrag im ersten Eingabefeld
			// Beachte, der Eintrag im ersten Eingabefeld ist abhängig vom Unternehmensnamen
			String xpathvalue = "//div[text() = '" + Unternehmensname
					+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root')]//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);

			// Direktsprung auf Dokumente
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@href='#/masterdata/documents']", test);

			// Haken setzen für Dokumentenkontrolle des Admins's für Geldnehmer
			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'registerExtractStatute']",
					test);
			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'agbBanks']", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
					"\\Admin Handelsfreigabe\\AmdinHandlefreigabe-GN-Haken", Teststep, test);

			// Button Freigeben auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
					"//span[text()='Freigeben']//ancestor::button[@tabindex='0']", test);
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);

			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);

			// Prüfen ob User in Register "Mit Handelsberechtigung" vorhanden ist?
			// Button "Daten komplett" in menu clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
					"//li[contains(@data-test, 'MIT HANDELSBERECHTIGUNG')]", test);

			// Die Anzeige auf 100 erhöhen
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//div[contains(@id,'mui')]",
					"//li[contains(text(),'", ZeilenProSeite, test);

			// Firmenname in das Suchfeld eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);
			// TSonderzeit zum Hochladen
			Thread.sleep(5 * Zeitspanne);

//          
//			driver.close();
//			// Für den Teardown
//			driver = null;
//			eyes = null;

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
		Utils.SeleniumUtils.BrowserBeenden(driver, Zeitspanne, extent, eyes);
	}

}
