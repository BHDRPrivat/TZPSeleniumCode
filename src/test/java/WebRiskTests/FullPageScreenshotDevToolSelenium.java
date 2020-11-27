package WebRiskTests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qeagle.devtools.webdriver.DevToolsService;

import com.qeagle.devtools.services.ChromeDevToolsService;
import com.qeagle.devtools.utils.FullScreenshot;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FullPageScreenshotDevToolSelenium {

	// Es erfolgt ein Screenshot mit falschem Inhalt.

	public static void main(String[] args) {

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		// driver.get("https://selenium.dev/");

		driver.get("http://automationtesting.in/");

		WebDriverWait wait = new WebDriverWait(driver, 30);
		// LastElementToLoad muss zugeordnet werde mittels By.XX()
		// wait.until(ExpectedConditions
		// .elementToBeClickable(By.xpath("//a[contains(text(),'Get Data from Excel
		// Using Column Name in C#')]")));

		ChromeDevToolsService devToolService = DevToolsService.getDevToolsService(driver);
		FullScreenshot.captureFullPageScreenshot(devToolService, "D:\\IR-WebRisk-Testdaten\\Fullscreenshot.png");

	}

}
