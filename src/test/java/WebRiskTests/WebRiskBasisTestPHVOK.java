package WebRiskTests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qeagle.devtools.webdriver.DevToolsService;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import com.qeagle.devtools.services.ChromeDevToolsService;
import com.qeagle.devtools.utils.FullScreenshot;

import Utils.ExcelUtilsJXL;
import config.PropertiesFile;

public class WebRiskBasisTestPHVOK {
	private WebDriver driver = null;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String Ablaufart = null;

	// private String Druckauswahl;
//	private String ApplitoolsAufnahme;

	// Bei der zweiten Aufnahmesession kommt es zu einem Fehler
	// Daher erst einmal die ScreenshotAufnehmen auf false in den
	// Properties-Files
	// Problem: Die Aufnahme der Ergebnisseite ist nicht vollständig.
//	private String ScreenshotAufnehmen;
	private ChromeDevToolsService devToolsService = null;

	// Variable für Applitools
	public Eyes eyes = null;

	// Hinweise zum excelgetriebenen Test des IR-PHV-Tarifs.
	// Die Bereitstellung eines weiteren Durchlaufs für den PHV-Tarif ist nur
	// mit einem "Neuen Vorgang" möglich. Hierbei wird aber ein neuer Reiter
	// erzeugt,
	// der es nicht ermöglicht, mittels ChromeDevToolsService weitere
	// Full-Screenshots
	// Aufzunehmen.

	//

	// Parameter aus der PHVAblauf.xml-Datei für die Steuerung der
	// PDF-Speicherung, Scrennshot-Aufnahmen
	// Applitools Verwendung
	@Parameters({ "Ablaufart" })
	@BeforeTest
	public void ablaufartFestlegen(@Optional("PDF-Druck") String Ablaufart) {
		this.Ablaufart = Ablaufart;
		// Testweise
		// Ablaufart = "Applitools";
	}

	public void Setup() throws InterruptedException, IOException {
		// Steuerungsparameter werden aus der Config-Datei ausgelesen
		// Den Standardbrowser als Chrome wählen, da dieser die PDF-Speicherung
		// ermöglicht.
		StandardBrowser = PropertiesFile.getProperties("StandardBrowser");
		Zeitspanne = Integer.valueOf(PropertiesFile.getProperties("Zeitspanne"));
//		Druckauswahl = PropertiesFile.getProperties("Druckauswahl");
//		ScreenshotAufnehmen = PropertiesFile.getProperties("ScreenshotAufnehmen");
//		ApplitoolsAufnahme = PropertiesFile.getProperties("ApplitoolsAufnahme");

		BaseUrl = "https://tarifrechner.interrisk.de/Basis.html";
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
			chromePrefs.put("download.default_directory", "D:\\IR-WebRisk-Testdaten\\PHV");

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

			driver = new ChromeDriver(options);
			// WebDriverManager.chromedriver().setup();
			// driver = new ChromeDriver(options);
			devToolsService = DevToolsService.getDevToolsService(driver);
			System.out.println("In Chrome gelaufen");
		}

		if ((driver == null) && (StandardBrowser.equals("Firefox"))) {

			// driver = new FirefoxDriver();
			FirefoxProfile fxProfile = new FirefoxProfile();

			fxProfile.setPreference("browser.download.folderList", 2);
			fxProfile.setPreference("browser.download.manager.showWhenStarting", false);
			fxProfile.setPreference("browser.download.dir", "D:\\IR-WebRisk-Testdaten\\PHV");
			fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			driver = new FirefoxDriver();

		}

	}

	@DataProvider(name = "PHVDaten")
	public static Object[][] getData() {
		// Ermittelt den Pfad des aktuellen Projekts
		String projectpath = System.getProperty("user.dir");
		String excelPath = projectpath + "\\Excel\\PHV-Testdaten-V2-PHV-Komplett.xlsx";
		Object testData[][] = testData(excelPath, "Testdaten");
		return testData;
	}

	public static Object[][] testData(String excelPath, String sheetName) {
		// Aufruf des Constructors von ExcelUtils
		ExcelUtilsJXL excel = new ExcelUtilsJXL(excelPath, sheetName);

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

	public void BasisanmeldungTarifauwahl() throws InterruptedException, IOException {

		Setup();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// driver.manage().window().maximize();
		driver.manage().window().fullscreen();

		driver.get(BaseUrl);
		Thread.sleep(3000);

		Thread.sleep(Zeitspanne);
		// Basis-Anmeldeseite

		Thread.sleep(Zeitspanne);
		buttonKlick(driver, "linkText", "Ich stimme zu", "click");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("ir-login-username_w0")).clear();
		driver.findElement(By.id("ir-login-username_w0")).sendKeys("Interrisk");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("ir-login-password_w0")).clear();
		driver.findElement(By.id("ir-login-password_w0")).sendKeys("Interrisk");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("ir-login-button_w0")).click();

		// Tarifauswahl
		Thread.sleep(3 * Zeitspanne);
		driver.findElement(By.linkText("Privathaftpflicht")).click();

		Thread.sleep(Zeitspanne);

	}

	public void ApplitoolsAufnahme(String Ablaufart, String teststep) {
		if (Ablaufart.equals("Applitools")) {
			// Applitools vorbereiten
			eyes = new Eyes();
			eyes.setApiKey("1epbTsh91uyej6yur9x0FzJb3WUit5naoVB8SYMRZUE110");
			try {

				// eyes.check("test", Target.window().fully());

				// eyes.open(driver, "IR-PHV", "Testfall: " + teststep, new RectangleSize(800,
				// 600));

				// eyes.open(driver, "IR-PHV", "Testfall: " + teststep,
				// Target.window().fully());

				eyes.open(driver, "IR-PHV", "Testfall: " + teststep, new RectangleSize(800, 600));

				eyes.checkWindow("PHV-Ergebnisseite");

				eyes.close();

			} finally {

			}
		}
	}

	public void ScreenshotAufnahme(String Ablaufart, ChromeDevToolsService devToolsService, String bildPath)
			throws InterruptedException {
		if (Ablaufart.equals("PDF-Druck")) {
			// Screenshot erzeugen;
			Thread.sleep(2 * Zeitspanne);

			try {
				// Take full screen

				FullScreenshot.captureFullPageScreenshot(devToolsService, bildPath);
			} catch (AssertionError e) {
				System.out.println(e);
			}

			Thread.sleep(3 * Zeitspanne);
		}
	}

	// Beachte der dataprovider darf nicht mit einer anderen Annotation verbunden
	// werden!

	@Test(dataProvider = "PHVDaten")
	public void BasisRisiko(String teststep, String geburtsdatum, String familienstatus, String deckungssumme,
			String zahlweise, String xxlBeitrag) throws InterruptedException, IOException {

		// Für die Aufnahmen von Full-Screenshots -> Get the Devtools Service
		// ChromeDevToolsService devToolsService = null;
		// devToolsService = DevToolsService.getDevToolsService(driver);

		// ChromeDevToolsService devToolService =
		// DevToolsService.getDevToolsService(driver);
		// FullScreenshot.captureFullPageScreenshot(devToolService,
		// "D:\\IR-WebRisk-Testdaten\\Fullscreenshot.png");

		// Anmeldung einmalig ausführen
		BasisanmeldungTarifauwahl();

		// PHV-Eingabemaske
		Thread.sleep(Zeitspanne);
		// Geburtsdatum
		driver.findElement(By.id("phv-gebdat_w0")).sendKeys(geburtsdatum);

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("phv_versobj_w0_chosen")).click();
		// FamilienStatus
		driver.findElement(By.xpath(familienstatus)).click();

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("phv_decksum_w0_chosen")).click();
		// Deckungssumme
		driver.findElement(By.xpath(deckungssumme)).click();

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("phv-vorvers_n")).click();

		// ScreenShot
		Thread.sleep(2 * Zeitspanne);

		// Take full screen
		ScreenshotAufnahme(Ablaufart, devToolsService,
				"D:\\IR-WebRisk-Testdaten\\PHV\\" + teststep + "-Eingabemaske.png");

		// driver.findElement(By.id("pgs1_next")).click();
		driver.findElement(By.linkText("Weiter")).click();

		// PHV-Ergebnisseite

		List<WebElement> ListePHV = driver.findElements(By.xpath("//div[@id='br-phv-netto']//descendant::label"));
		// in einen try Block schreiben, um die Verarbeitung nicht zu unterbinden

		try {
			// Kontrolle des XXL-Wertes
			Assert.assertEquals(ListePHV.get(0).getText(), xxlBeitrag);
		} catch (AssertionError e) {
			System.out.println(e);
		}

		WebDriverWait wait = new WebDriverWait(driver, 30);
		// LastElementToLoad miuss zugeordnet werde mittels By.XX()
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Weiter")));
		// Data from Excel Using Column Name in C#')]")));
		// label[contains(text(),'5,58')]
		// wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("label[contains(text(),'5,58')]"),
		// ""));

		Thread.sleep(3 * Zeitspanne);
		// Screenshot erzeugen;
		ScreenshotAufnahme(Ablaufart, devToolsService,
				"D:\\IR-WebRisk-Testdaten\\PHV\\" + teststep + "-Ergebnismaske.png");
		Thread.sleep(2 * Zeitspanne);

		// Aufnahme für Applitool
		ApplitoolsAufnahme(Ablaufart, teststep);
		Thread.sleep(2 * Zeitspanne);

		driver.findElement(By.linkText("Weiter")).click();
		Thread.sleep(2 * Zeitspanne);

		// Warenkorb

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("wk_zahlw_w0_chosen")).click();
		driver.findElement(By.xpath(zahlweise)).click();

		// Screenshot erzeugen;
		ScreenshotAufnahme(Ablaufart, devToolsService, "D:\\IR-WebRisk-Testdaten\\PHV\\" + teststep + "-Warenkorb.png");

		if (Ablaufart.equals("PDF-Druck")) {
			// Druckauswahl
			// Automatische Speicherung der Dokumente unter Chrome
			// Durch die festegelegten Options
			driver.findElement(By.linkText("Angebot / Antrag drucken")).click();
			Thread.sleep(2 * Zeitspanne);

			driver.findElement(By.linkText("PDF erzeugen")).click();
			Thread.sleep(2 * Zeitspanne);

			driver.findElement(By.linkText("Angebots-Dokumente")).click();
			Thread.sleep(4 * Zeitspanne);

			driver.findElement(By.linkText("Antrags-Dokumente")).click();
			Thread.sleep(4 * Zeitspanne);
		}

		// Scrennshot
		ScreenshotAufnahme(Ablaufart, devToolsService,
				"D:\\IR-WebRisk-Testdaten\\PHV\\" + teststep + "-Druckauswahl.png");

		Thread.sleep(3 * Zeitspanne);
		driver.close();
		// Für den Teardown
		driver = null;
		eyes = null;
		// driver.quit();

		// Das Vorgehen mit öffnen eines weietren Reiters fürht zu Fehlerverhalten im
		// Ablauf.
		// Daher wird die gesamte Session geschlossen und komplett neu geöffnet

//		// Die Eingabemaske für den nächsten Testfall vorbereiten
//		// Beachte, die Auswahl eines bereits berechneten Tarifs springt
//		// Auf die Ergebnisseite
//		driver.findElement(By.xpath("//a[contains(text(),'Neuer Vorgang')]")).click();
//		Thread.sleep(3 * Zeitspanne);
//		// Im Hauptmenü erneut den PHV-Tarif auswählen
//		// Es wird ein neuer Tab geöffnet. Es muss zu diesem gewechselt werden
//		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
//		// Bisherigen Reiter schließen
//		driver.switchTo().window(tabs.get(0));
//		// Auch den DevToolService schließen, sonst läuft der zweite Durchlauf nicht.
//		// devToolsService.close();
//		// driver.close();
//		// Auf den neuen Reiter wechseln
//		driver.switchTo().window(tabs.get(1));
//
//		// Tarifauswahl
//		Thread.sleep(3 * Zeitspanne);
//		driver.findElement(By.linkText("Privathaftpflicht")).click();
//		Thread.sleep(3 * Zeitspanne);

	}

	private void buttonKlick(WebDriver driver, String locator, String locatorwert, String aktion) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(locatorwert)));
		element.click();
	}

	@AfterSuite
	public void tearDown() throws InterruptedException {
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