package WebRiskTests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.io.Files;

public class WebRiskEndkundeTestPHVOK {
	private WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	private String StandardBrowser;
	private String Speicherstamm;

	public static String getzufallstring(int size) {

		StringBuffer zufall = new StringBuffer();
		for (int i = 0; i < size; i++) {
			zufall.append((char) ('a' + 26 * Math.random()));
			// 'a' + (0..25) = ('a' .. 'z')
		}
		return zufall.toString();
	}

	@BeforeTest
	public void Setup() throws InterruptedException {

		// Achtung, Antrag wird real versendet.
		StandardBrowser = "Chrome";
		// StandardBrowser = "Firefox";
		BaseUrl = "https://tarifrechner.interrisk.de/Endkunde.html";
		Speicherstamm = "D:\\IR-WebRisk-Testdaten\\PHV-Endkunde";

		if ((driver == null) && (StandardBrowser == "Chrome")) {

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\chromedriver.exe");

			// Vorgaben für den Chrome-Browser zum Speichern von PDF-Dokumneten

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("plugins.always_open_pdf_externally", true);
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", Speicherstamm);

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

		}

		if ((driver == null) && (StandardBrowser == "Firefox")) {

			// driver = new FirefoxDriver();
			FirefoxProfile fxProfile = new FirefoxProfile();

			fxProfile.setPreference("browser.download.folderList", 2);
			fxProfile.setPreference("browser.download.manager.showWhenStarting", false);
			fxProfile.setPreference("browser.download.dir", Speicherstamm);
			fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			driver = new FirefoxDriver();

		}

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		// driver.manage().window().fullscreen();
		driver.get(BaseUrl);
	}

	@Test
	public void BasisPHV() throws InterruptedException, IOException {

		Zeitspanne = 1500;

		// Basis-Anmeldeseite
		Thread.sleep(Zeitspanne);
		driver.findElement(By.linkText("Ich stimme zu")).click();

		// Tarifauswahl
		Thread.sleep(3 * Zeitspanne);
		driver.findElement(By.linkText("Privathaftpflicht")).click();

		// PHV-Eingabemaske
		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("phv-gebdat_w0")).sendKeys("121169");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("phv_versobj_w0_chosen")).click();
		driver.findElement(By.xpath("//li[contains(.,'Single mit/ohne Kind')]")).click();

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("phv_decksum_w0_chosen")).click();
		driver.findElement(By.xpath("//li[contains(.,'25 Mio.')]")).click();

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("phv-vorvers_n")).click();

		// ScreenShot
		Thread.sleep(2 * Zeitspanne);
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile, new File(Speicherstamm + "\\1-PHV-Eingabemaske.png"));
		Thread.sleep(3 * Zeitspanne);

		// driver.findElement(By.id("pgs1_next")).click();
		driver.findElement(By.linkText("Weiter")).click();
		Thread.sleep(3 * Zeitspanne);

		// PHV-Ergebnisseite

		Thread.sleep(3 * Zeitspanne);
		driver.findElement(By.id("phv-beratungs-wunsch-2_n")).click();

		// Screenshot erzeugen;
		Thread.sleep(2 * Zeitspanne);
		File scrFile2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile2, new File(Speicherstamm + "\\2-PHV-Ergebnisseite.png"));
		Thread.sleep(3 * Zeitspanne);

		List<WebElement> ListePHV = driver.findElements(By.xpath(
				"//div[contains(@class, 'block right psel')]//following-sibling::div//following-sibling::div//div[contains(@class, 'vcenter')]//Label"));
		// Kontrolle des XXL-Wertes
		Assert.assertEquals(ListePHV.get(0).getText(), "54,00 €");
		Thread.sleep(2 * Zeitspanne);

		driver.findElement(By.linkText("Weiter")).click();
		Thread.sleep(2 * Zeitspanne);

		// Warenkorb

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("wk_zahlw_w0_chosen")).click();
		driver.findElement(By.xpath("//li[contains(.,'vierteljährlich')]")).click();

		// Screenshot erzeugen;
		Thread.sleep(1 * Zeitspanne);
		File scrFile3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile3, new File(Speicherstamm + "\\3-PHV-Warenkorb.png"));
		Thread.sleep(3 * Zeitspanne);

		// Druckauswahl
		driver.findElement(By.linkText("jetzt beantragen")).click();
		Thread.sleep(2 * Zeitspanne);

		// Antragsdaten
		// Name mit Zufallsgenerator

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("at-vorname_w0")).sendKeys(getzufallstring(10) + "-PHV");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("at-nachname_w0")).sendKeys(getzufallstring(10) + "-PHV");

		// Adressblock -- Wichtig: Tab mitgeben, damit die Einträge akzeptiert
		// werden
		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("at-plz_w0")).sendKeys("65203" + Keys.TAB);
		Thread.sleep(2 * Zeitspanne);
		driver.findElement(By.id("at-ort_w0")).clear();
		Thread.sleep(2 * Zeitspanne);
		driver.findElement(By.id("at-ort_w0")).sendKeys("Wiesbaden" + Keys.TAB);
		Thread.sleep(2 * Zeitspanne);
		driver.findElement(By.id("at-str_w0")).sendKeys("Carl-Bosch-Str." + Keys.TAB);
		Thread.sleep(2 * Zeitspanne);
		driver.findElement(By.id("at-hnr_w0")).sendKeys("3" + Keys.TAB);
		Thread.sleep(2 * Zeitspanne);

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("at-telefon_w0")).sendKeys("0611-8767654");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("at-email_w0")).sendKeys("alfi@t-online.de");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("at-iban-endben_w0")).sendKeys("DE89370400440532013000");

		// Screenshot erzeugen;
		Thread.sleep(1 * Zeitspanne);
		File scrFile4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile4, new File(Speicherstamm + "\\4-PHV-Antragsdaten.png"));
		Thread.sleep(3 * Zeitspanne);

		driver.findElement(By.linkText("Weiter")).click();
		Thread.sleep(2 * Zeitspanne);

		// Druckübersicht

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("antrag_E1_w0")).click();
		Thread.sleep(Zeitspanne);

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("antrag_E2_w0")).click();
		Thread.sleep(2 * Zeitspanne);

		// Druckauswahl
		driver.findElement(By.id("btn-create-docs")).click();
		Thread.sleep(2 * Zeitspanne);

		driver.findElement(By.linkText("Angebots-Dokumente")).click();
		Thread.sleep(4 * Zeitspanne);

		driver.findElement(By.linkText("Antrags-Dokumente")).click();
		Thread.sleep(4 * Zeitspanne);

		// Automatische Speicherung der Dokumente unter Chrome

		// Screenshot erzeugen;
		Thread.sleep(1 * Zeitspanne);
		File scrFile5 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile5, new File(Speicherstamm + "\\5-PHV-Druckauswahl.png"));
		Thread.sleep(5 * Zeitspanne);

		// Versendung wird unterbunden.
		// driver.findElement(By.linkText("Zahlungspflichtig
		// beantragen")).click();
		Thread.sleep(5 * Zeitspanne);

		// Screenshot erzeugen;
		Thread.sleep(1 * Zeitspanne);
		File scrFile6 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile6, new File(Speicherstamm + "\\6-PHV-Versendet.png"));
		Thread.sleep(5 * Zeitspanne);

		driver.findElement(By.linkText("Zur Produktübersicht")).click();
		Thread.sleep(3 * Zeitspanne);

	}

	@AfterSuite
	public void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		if (driver != null) {
			driver.quit();
		}
	}
}