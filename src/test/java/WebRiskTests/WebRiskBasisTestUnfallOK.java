
package WebRiskTests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.io.Files;

public class WebRiskBasisTestUnfallOK {
	private WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	private String StandardBrowser;

	@BeforeTest
	public void Setup() throws InterruptedException {

		StandardBrowser = "Chrome";
		// StandardBrowser = "Firefox";
		BaseUrl = "https://tarifrechner.interrisk.de/Basis.html";

		if ((driver == null) && (StandardBrowser == "Chrome")) {

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\chromedriver.exe");

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("plugins.always_open_pdf_externally", true);
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", "D:\\IR-WebRisk-Testdaten\\Unfall");

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
			fxProfile.setPreference("browser.download.dir", "D:\\IR-WebRisk-Testdaten\\Unfall");
			fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			driver = new FirefoxDriver();

		}

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// driver.manage().window().maximize();
		driver.manage().window().fullscreen();
		driver.get(BaseUrl);
	}

	@Test
	public void BasisUnfall() throws InterruptedException, IOException {

		Zeitspanne = 1000;

		// Basis-Anmeldeseite
		Thread.sleep(Zeitspanne);
		driver.findElement(By.linkText("Ich stimme zu")).click();

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("ir-login-username_w0")).clear();
		driver.findElement(By.id("ir-login-username_w0")).sendKeys("Interrisk");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("ir-login-password_w0")).clear();
		driver.findElement(By.id("ir-login-password_w0")).sendKeys("Interrisk");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("ir-login-button_w0")).click();
		Thread.sleep(3 * Zeitspanne);

		// Tarifauswahl
		Thread.sleep(3 * Zeitspanne);
		driver.findElement(By.linkText("Unfall")).click();

		// Unfall-Eingabemaske
		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u-vorname-1_w0")).sendKeys("Maxima");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u-nachname-1_w0")).sendKeys("Mahmut");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u-job-1_w0")).sendKeys("Dipl.-Physiker (Uni) (m/w/d)");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u_sex_1_w0_chosen")).click();
		driver.findElement(By.xpath("//li[contains(.,'weiblich')]")).click();

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u-gebdat-1_w0")).sendKeys("221268");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u-invbasic-1_w0")).sendKeys("100000");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u-uebergang-1_w0")).sendKeys("10000");

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u-khtg-1_w0")).sendKeys("100");

		// ScreenShot
		Thread.sleep(2 * Zeitspanne);
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile, new File("D:\\IR-WebRisk-Testdaten\\Unfall\\1-Unfall-Eingabemaske.png"));
		Thread.sleep(3 * Zeitspanne);

		// driver.findElement(By.id("pgs1_next")).click();
		driver.findElement(By.linkText("Weiter")).click();

		// Unfall-Ergebnisseite

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("u-gesfrage-1_n")).click();

		// Screenshot erzeugen;
		Thread.sleep(2 * Zeitspanne);
		File scrFile2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile2, new File("D:\\IR-WebRisk-Testdaten\\Unfall\\2-Unfall-Ergebnisseite.png"));
		Thread.sleep(3 * Zeitspanne);

		/*
		 * List<WebElement> ListePHV = driver.findElements(By.xpath(
		 * "//div[contains(@class, 'block right psel')]//following-sibling::div//following-sibling::div//div[contains(@class, 'vcenter')]//Label"
		 * )); // Kontrolle des XXL-Wertes
		 * Assert.assertEquals(ListePHV.get(0).getText(), "69,00 €");
		 * Thread.sleep(2 * Zeitspanne);
		 */
		driver.findElement(By.linkText("Weiter")).click();
		Thread.sleep(2 * Zeitspanne);

		// Warenkorb

		Thread.sleep(Zeitspanne);
		driver.findElement(By.id("wk_zahlw_w0_chosen")).click();
		driver.findElement(By.xpath("//li[contains(.,'jährlich')]")).click();

		// Screenshot erzeugen;
		Thread.sleep(1 * Zeitspanne);
		File scrFile3 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(scrFile3, new File("D:\\IR-WebRisk-Testdaten\\Unfall\\3-Unfall-Warenkorb.png"));
		Thread.sleep(3 * Zeitspanne);

		// Druckauswahl

		driver.findElement(By.linkText("Angebot / Antrag drucken")).click();
		Thread.sleep(2 * Zeitspanne);

		driver.findElement(By.id("antrag_E3_w0")).click();
		Thread.sleep(2 * Zeitspanne);

		driver.findElement(By.linkText("PDF erzeugen")).click();
		Thread.sleep(2 * Zeitspanne);

		driver.findElement(By.linkText("Angebots-Dokumente")).click();
		Thread.sleep(4 * Zeitspanne);

		driver.findElement(By.linkText("Antrags-Dokumente")).click();
		Thread.sleep(4 * Zeitspanne);

		// Automatische Speicherung der Dokumente unter Chrome

		// Screenshot erzeugen;
		Thread.sleep(2 * Zeitspanne);
		File scrFile4 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		// somewhere
		Files.copy(scrFile4, new File("D:\\IR-WebRisk-Testdaten\\Unfall\\4-Unfall-Druckauswahl.png"));
		Thread.sleep(3 * Zeitspanne);

		driver.findElement(By.linkText("Zurück")).click();
		Thread.sleep(2 * Zeitspanne);

	}

	@AfterSuite
	public void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		if (driver != null) {
			driver.quit();
		}
	}
}