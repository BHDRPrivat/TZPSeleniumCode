package AlteLeipziger;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.qeagle.devtools.webdriver.DevToolsService;
import org.testng.Assert;
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
import com.qeagle.devtools.services.ChromeDevToolsService;
import com.qeagle.devtools.utils.FullScreenshot;

import Utils.ExcelUtilsApachePoi;

public class AlteLeipzigerRisikoLebensversicherung {
// Hinwweis:
// Ablauf führt zu einem Fehler der Art: [Utils] [ERROR] [Error] java.lang.ExceptionInInitializerError	
	
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

	public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;

	// Parameter aus der PHVAblauf.xml-Datei für die Steuerung der
	// PDF-Speicherung, Scrennshot-Aufnahmen
	// Applitools Verwendung

	// Sep. 2020 Das Programm DevTools funktioniert nicht mehr und muss
	// auskommentiert werden.
	// dadurch sind keine Screenshots mehr möglich. Alle Codestellen zum Aufrufen
	// der Scrennshots sind nicht mehr möglich
	// Die Stellen mit ScreenshotAufnahme(Ablaufart, devToolsService,
	// SpeicherpfadTestdokumente + teststep + "-Risiko-Eingabemaske.png");
	// sind auszukommentieren
	// Beachte: Deine Programmierung muss für spezielle Codezeilen eine separate
	// Codeausführung beinhalten.
	@Parameters({ "Ablaufart" })
	@BeforeTest
	public void Setup(@Optional("PDF-Druck") String Ablaufart) throws InterruptedException, IOException {

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter("Fehlerreport Alte Leipziger Risiko-" + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}

		// Steuerungsparameter werden aus der Config-Datei ausgelesen
		// Den Standardbrowser als Chrome wählen, da dieser die PDF-Speicherung
		// ermöglicht.
		// StandardBrowser = PropertiesFile.getProperties("StandardBrowser");
		// Zeitspanne = Integer.valueOf(PropertiesFile.getProperties("Zeitspanne"));
		this.Ablaufart = Ablaufart;
		System.out.println(Ablaufart);
		StandardBrowser = "Chrome";
		Zeitspanne = 500;

		// für direkte Testläufe
		// Applitools und PDF-Druck dürfen nicht gleichzeitig ablaufen
		// Es kommt zu Fehlermeldungen
		// public String Ablaufart = "Applitool";
		// oder
		// public String Ablaufart = "PDF-Druck";

		// Druckauswahl = PropertiesFile.getProperties("Druckauswahl");
		// ScreenshotAufnehmen = PropertiesFile.getProperties("ScreenshotAufnehmen");
		// ApplitoolsAufnahme = PropertiesFile.getProperties("ApplitoolsAufnahme");

		BaseUrl = "https://www.alte-leipziger-rechner.de/risikoleben";
		SpeicherpfadTestdokumente = "D:\\AL-Testdaten\\Risiko\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden.
		// "==" steht für die Überprüfung des Speicherorts

		if ((driver == null) && (StandardBrowser.equals("Chrome"))) {

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\chromedriver.exe");

			// Den Aktuellen Chromedriver in nachfolgendes Verzeichnis kopieren
			// D:\BHDR-Daten\workspace\InterRiskMavenProject\src\chromedriver.exe

			// Chromedriver-Version ermitteln:
			// Google Chrome ist auf dem neuesten Stand.
			// Version 75.0.3770.100 (Offizieller Build) (64-Bit)
			// Entsprechenden Driver downloaden und in das Verzeichnis kopieren.
			// System.out.println(System.getProperty("user.dir") +
			// "\\src\\chromedriver.exe");

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("plugins.always_open_pdf_externally", true);
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", SpeicherpfadTestdokumente);

			// Keine Auswirkungen auf den Zoom-Level.
			// chromePrefs.put("profile.default_zoom_level", 40);
			// chromePrefs.put("document.body.style.zoom", 40);
			// chromePrefs.put("default_zoom_level", -2.2293886449264093);
			// chromePrefs.put("preferences.default_zoom_level", -2.2293886449264093);

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);

			/* For tomcat 9.0.zip. */
			// cDriver.get("http://mirror.nexcess.net/apache/tomcat/tomcat-9/v9.0.0.M22/bin/apache-tomcat-9.0.0.M22.zip");

			/*
			 * For pdf. First check not show pdf in Chrome browser.
			 */
			// this.setChromeOptions(driver);

			// driver = new ChromeDriver(options);
			// WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// driver.manage().window().maximize();
			// driver.manage().window().fullscreen();

			devToolsService = DevToolsService.getDevToolsService(driver);
			System.out.println("In Chrome gelaufen");
		}

		if ((driver == null) && (StandardBrowser.equals("Firefox"))) {

			// driver = new FirefoxDriver();
			FirefoxProfile fxProfile = new FirefoxProfile();

			fxProfile.setPreference("browser.download.folderList", 2);
			fxProfile.setPreference("browser.download.manager.showWhenStarting", false);
			fxProfile.setPreference("browser.download.dir", SpeicherpfadTestdokumente);
			fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			driver = new FirefoxDriver();

		}

	}

	@DataProvider(name = "ALRisikoDaten")
	public static Object[][] getData() {
		// Ermittelt den Pfad des aktuellen Projekts
		String projectpath = System.getProperty("user.dir");
		// Zugriff auf die korrekten Exceldaten
		TestdatenExceldatei = "\\Excel\\AL-Risiko-Testdaten-V1.xlsx";

		// Ablaufpräsentation
		// TestdatenExceldatei = "\\Excel\\AL-Risiko-Testdaten-V1-Fehler.xlsx";

		String excelPath = projectpath + TestdatenExceldatei;
		Object testData[][] = testData(excelPath, "Testdaten");
		return testData;
	}

	public static Object[][] testData(String excelPath, String sheetName) {
		// Aufruf des Constructors von ExcelUtils
		ExcelUtilsApachePoi excel = new ExcelUtilsApachePoi(excelPath, sheetName);

		int rowCount = excel.getRowCount();
		int colCount = excel.getColCount();

		// 2 Dimensionales Object-Array erzeugen
		Object data[][] = new Object[rowCount - 1][colCount];

		// �ber alle Zeilen laufen (i=1, da i=0 die Headerzeile)
		for (int i = 1; i < rowCount; i++) {
			// �ber alle Spalten laufen
			for (int j = 0; j < colCount; j++) {

				String cellData = excel.getExcelDataString(i, j);
				data[i - 1][j] = cellData;
				// Werte in einer Zeile anzeigen
				// System.out.print(cellData + " | ");
			}
		}
		return data;
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

	public void ScreenshotAufnahme(String Ablaufart, ChromeDevToolsService devToolsService, String bildPath)
			throws InterruptedException {
		if (Ablaufart.equals("PDF-Druck")) {
			// Screenshot erzeugen;
			Thread.sleep(Zeitspanne);

			try {
				// Take full screen

				FullScreenshot.captureFullPageScreenshot(devToolsService, bildPath);
			} catch (AssertionError e) {
				System.out.println(e);
			}

			Thread.sleep(Zeitspanne);
		}
	}

	// @Test
	@Test(dataProvider = "ALRisikoDaten")
	public void testAlteLeipzigerRisikoLebensversicherung(String teststep, String FamilienStatus, String Geburtsdatum,
			String Einkommen, String Art, String Raucher, String Akademiker, String AndereTodesfallsumme,
			String Todesfallsumme, String MonatlicherBeitrag, String Zahlbeitrag) throws Exception {

		// Mock
		// String teststep = "AL-R1";

		// creates a toggle for the given test, adds all log events under it
		ExtentTest test = extent.createTest("Alte Leipziger Risiko: " + teststep + " - " + Ablaufart,
				"Endanwender Risiko-Berechnungstool");

		driver.get(BaseUrl);
		// PHV-Eingabemaske
		Thread.sleep(3 * Zeitspanne);
		test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

		// Cookie-Abfrage
		driver.findElement(By.id("uc-btn-accept-banner")).click();
		test.log(Status.INFO, "Cookies akzeptieren");
		Thread.sleep(3 * Zeitspanne);

		driver.findElement(By.linkText(FamilienStatus)).click();
		// log(Status, details)
		test.log(Status.INFO, "Link: " + FamilienStatus + " ausgewaehlt");

		// PHV-Eingabemaske
		Thread.sleep(Zeitspanne);

		driver.findElement(By.xpath("//form[@id='eingabeFormular']/div[2]/fieldset/div")).click();
		Thread.sleep(Zeitspanne);

		driver.findElement(By.id("geburtsdatum")).clear();
		driver.findElement(By.id("geburtsdatum")).sendKeys(Geburtsdatum);
		Thread.sleep(Zeitspanne);

		// log(Status, details)
		test.log(Status.INFO, "Geburtstag: " + Geburtsdatum);

		driver.findElement(By.id("gehalt")).click();
		Thread.sleep(Zeitspanne);
		// Clear funktioniert nicht
		// driver.findElement(By.id("gehalt")).clear();
		// Mit Strg+a und Delete wird der Inhalt gelöscht
		driver.findElement(By.id("gehalt")).sendKeys(Keys.CONTROL + "a");
		driver.findElement(By.id("gehalt")).sendKeys(Keys.DELETE);

		Thread.sleep(Zeitspanne);

		driver.findElement(By.id("gehalt")).sendKeys(Einkommen);
		Thread.sleep(Zeitspanne);

		// log(Status, details)
		test.log(Status.INFO, "Einkommen: " + Einkommen);

		if (!(FamilienStatus.equals("Immobilie"))) {
			driver.findElement(By.id(Art)).click();
			Thread.sleep(Zeitspanne);
		}
		driver.findElement(By.id(Raucher)).click();
		Thread.sleep(Zeitspanne);
		test.log(Status.INFO, "Raucher: " + Raucher);

		driver.findElement(By.id(Akademiker)).click();
		test.log(Status.INFO, "Akademiker: " + Akademiker);

		// Wenn andere Todesfallsumme gewählt, die entsprechende
		// Eingabemaske öffnen

		if ((AndereTodesfallsumme.equals("ja"))) {
			if (!(driver.findElement(By.id("andereTodesfallsumme")).isSelected())) {
				driver.findElement(By.id("andereTodesfallsumme")).click();
			}
			Thread.sleep(Zeitspanne);
			driver.findElement(By.id("gewuenschteTodesfallsumme")).sendKeys(Keys.CONTROL + "a");
			driver.findElement(By.id("gewuenschteTodesfallsumme")).sendKeys(Keys.DELETE);
			Thread.sleep(Zeitspanne);
			driver.findElement(By.id("gewuenschteTodesfallsumme")).sendKeys(Todesfallsumme);
			test.log(Status.INFO, "Andere Todesfallsumme: " + Todesfallsumme);
		}
		if ((AndereTodesfallsumme.equals("nein")) && (driver.findElement(By.id("andereTodesfallsumme")).isSelected())) {
			// Abwählen
			driver.findElement(By.id("andereTodesfallsumme")).click();
			Thread.sleep(Zeitspanne);
		}

		// Take full screen
		ScreenshotAufnahme(Ablaufart, devToolsService,
				SpeicherpfadTestdokumente + teststep + "-Risiko-Eingabemaske.png");

		driver.findElement(By.xpath("//input[@value='Berechnen']")).click();

		Thread.sleep(Zeitspanne);

		try {
			// Kontrolle des monatlichen Beitrags
			Assert.assertEquals(driver.findElement(By.xpath("//table[@class='bordertable']//td[1]")).getText(),
					MonatlicherBeitrag);
			test.pass("Monatlicher Beitrag ist OK");
		} catch (AssertionError e) {
			test.fail("Ueberpruefung des monatlichen Zahlbeitrags erforderlich");
			System.out.println(e);
		}
		Thread.sleep(Zeitspanne);

		try {
			// Kontrolle des monatlichen Beitrags
			Assert.assertEquals(driver.findElement(By.xpath("//table[@class='bordertable']//td[2]")).getText(),
					Zahlbeitrag);
			test.pass("Monatlicher Zahlbeitrag ist OK");
		} catch (AssertionError e) {
			test.fail("Ueberpruefung des monatlichen Zahlbeitrags erforderlich");
			System.out.println(e);
		}
		Thread.sleep(Zeitspanne);

		// PDF-Speicher über Button drucken
		if (Ablaufart.equals("PDF-Druck")) {
			driver.findElement(By.linkText("Drucken")).click();

			// Take full screen
			ScreenshotAufnahme(Ablaufart, devToolsService,
					SpeicherpfadTestdokumente + teststep + "-Risiko-Ergebnismaske.png");
		}

		// Applitool zum globalen UX-Vergleich
		// Aufnahme für Applitool
		ApplitoolsAufnahme(Ablaufart, teststep);
		Thread.sleep(2 * Zeitspanne);

		// Ausgangszustand für den nächsten Testfall bereitstellen
		// Verworfen, da intrinische Efekte zu groß werden.
		// driver.findElement(By.xpath("//a[contains(text(),'Zurück')]")).click();
		// Thread.sleep(2 * Zeitspanne);
		// driver.findElement(By.xpath("//a[contains(text(),'Zurück')]")).click();
		// Thread.sleep(2 * Zeitspanne);

		driver.close();
		// Für den Teardown
		driver = null;
		eyes = null;

		// Neu Starten
		Setup(Ablaufart);

	}

	@AfterTest
	public void tearDown() throws InterruptedException {

		// calling flush writes everything to the log file
		extent.flush();

		Thread.sleep(3000);
		System.out.println("Test erfolgreich druchlaufen");
		if (driver != null) {
			driver.quit();
		}
		if (eyes != null) {
			eyes.close();
			eyes.abortIfNotClosed();
		}
	}

}
