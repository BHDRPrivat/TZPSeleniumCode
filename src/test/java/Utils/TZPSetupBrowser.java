package Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;



public class TZPSetupBrowser {
	
	public static WebDriver BrowserSetup(WebDriver driver, String StandardBrowser, String SpeicherpfadTestdokumente) throws InterruptedException, IOException {

		if ((driver == null) && (StandardBrowser.equals("Chrome"))) {

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\chromedriver.exe");
			
		    // Die Verwendung des WebDrivermanager
		    WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			
			// Normaler driver, falls WebdriverMamnager zu einer Fehlermeldung führt.
			// driver = new ChromeDriver();
						
			

			// Den Aktuellen Chromedriver in nachfolgendes Verzeichnis kopieren
			// D:\BHDR-Daten\workspace\InterRiskMavenProject\src\chromedriver.exe
			// Chromedriver-Version ermitteln:
			// Google Chrome ist auf dem neuesten Stand. 
			// Version 75.0.3770.100 (Offizieller Build) (64-Bit)
			// Entsprechenden Driver downloaden und in das Verzeichnis kopieren.
			// System.out.println(System.getProperty("user.dir") +
			// "\\src\\chromedriver.exe");

			// Setzen von Chrome-Preferenzen zum automatischen Speichern von PDF-Dateien
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("plugins.always_open_pdf_externally", true);
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", SpeicherpfadTestdokumente);

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);

			driver = new ChromeDriver(options);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// driver.manage().window().maximize();
			// driver.manage().window().fullscreen();

			// devToolsService = DevToolsService.getDevToolsService(driver);
			System.out.println("In Chrome gelaufen");
		}

		if ((driver == null) && (StandardBrowser.equals("Firefox"))) {

			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			
			
			// driver = new FirefoxDriver();
			FirefoxProfile fxProfile = new FirefoxProfile();

			fxProfile.setPreference("browser.download.folderList", 2);
			fxProfile.setPreference("browser.download.manager.showWhenStarting", false);
			fxProfile.setPreference("browser.download.dir", SpeicherpfadTestdokumente);
			fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			driver = new FirefoxDriver();

		}
		return driver;
	}
	
}
