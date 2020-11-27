package WebRiskTests;

import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import config.PropertiesFile;

public class RecordIRRisiko {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private String BaseUrl;
	private String StandardBrowser;

	@BeforeTest
	public void Setup() throws InterruptedException, IOException {

		// Den Standardbrowser als Chrome wählen, da dieser die PDF-Speicherung
		// ermöglicht.
		StandardBrowser = PropertiesFile.getProperties("StandardBrowser");
		System.out.println(StandardBrowser);

		BaseUrl = "https://tarifrechner.interrisk.de/Basis.html";

		if ((driver == null) && (StandardBrowser == "Chrome")) {

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
			chromePrefs.put("download.default_directory", "D:\\IR-WebRisk-Testdaten\\Risiko");

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
			fxProfile.setPreference("browser.download.dir", "D:\\IR-WebRisk-Testdaten\\Risiko");
			fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			driver = new FirefoxDriver();

		}

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		// driver.manage().window().fullscreen();
		driver.get(BaseUrl);
	}

	@Test
	public void testUntitledTestCase() throws Exception {
		driver.get("https://tarifrechner.interrisk.de/Basis.html");
		driver.findElement(By.linkText("Ich stimme zu")).click();
		// driver.findElement(By.xpath("//div[@id='ir-innerwrap']/div/div/div[2]/div/div")).click();
		driver.findElement(By.id("ir-login-username_w0")).click();
		driver.findElement(By.id("ir-login-username_w0")).clear();
		driver.findElement(By.id("ir-login-username_w0")).sendKeys("interrisk");
		driver.findElement(By.id("ir-login-password_w0")).click();
		driver.findElement(By.id("ir-login-password_w0")).clear();
		driver.findElement(By.id("ir-login-password_w0")).sendKeys("interrisk");
		driver.findElement(By.id("ir-login-button_w0")).click();
		driver.findElement(By.xpath("(//a[contains(text(),'Risiko-Leben')])[2]")).click();
		driver.findElement(By.id("rlv_versbeg_w0")).clear();
		driver.findElement(By.id("rlv_versbeg_w0")).sendKeys("01.05.2020");
		driver.findElement(By.xpath("//div[@id='rlv_versbeg']/span/i")).click();
		driver.findElement(By.id("rlv_versbeg_w0")).clear();
		driver.findElement(By.id("rlv_versbeg_w0")).sendKeys("01.06.2020");
		driver.findElement(By.id("rlv_vp_gebdat_w0")).click();
		driver.findElement(By.id("rlv_vp_gebdat_w0")).clear();
		driver.findElement(By.id("rlv_vp_gebdat_w0")).sendKeys("23.08.1968");
		driver.findElement(By.id("rlv_vp_gebdat_w0")).clear();
		driver.findElement(By.id("rlv_vp_gebdat_w0")).sendKeys("23.08.1968");
		driver.findElement(By.id("rlv_job_w0")).clear();
		driver.findElement(By.id("rlv_job_w0")).sendKeys("Mathematiker");
		driver.findElement(By.id("rlv_groesse_w0")).click();
		driver.findElement(By.id("rlv_gewicht_w0")).click();
		driver.findElement(By.id("rlv_vorgabe_lst_w0")).click();
		driver.findElement(By.id("rlv_vorgabe_lst_w0")).sendKeys("100000");
		driver.findElement(By.id("rlv_versdauer_w0")).click();
		driver.findElement(By.id("rlv_versdauer_w0")).sendKeys("20");
		driver.findElement(By.id("pgs1_next")).click();
		driver.findElement(By.xpath("//div[@id='br-rlv_brutto']/div[3]/div/div/label")).click();
		driver.findElement(By.id("rlv_btn_next")).click();
		driver.findElement(By.linkText("Angebot / Antrag drucken")).click();
		driver.findElement(By.id("at-partnernummer_w0")).click();
		driver.findElement(By.id("at-partnernummer_w0")).clear();
		driver.findElement(By.id("at-partnernummer_w0")).sendKeys("123456");
		driver.findElement(By.id("doc-ausw-create")).click();
		driver.findElement(By.linkText("Angebots-Dokumente")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [selectWindow | win_ser_1
		// | ]]
		// ERROR: Caught exception [ERROR: Unsupported command [selectWindow |
		// win_ser_local | ]]
		driver.findElement(By.linkText("Antrags-Dokumente")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [selectWindow | win_ser_2
		// | ]]
		// ERROR: Caught exception [ERROR: Unsupported command [selectWindow |
		// win_ser_local | ]]
		driver.findElement(By.linkText("Beenden")).click();
		driver.findElement(By.linkText("Speichern")).click();
		driver.findElement(By.id("txt-prmpt")).clear();
		driver.findElement(By.id("txt-prmpt")).sendKeys("Risiko-Test");
		driver.findElement(By.linkText("Speichern und beenden")).click();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
