package WebRiskTests;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FullPageScreenshotAShotSelenium {

	// Es erfolgt ein Screenshot mit falschem Inhalt.

	
	public static void main(String[] args) throws IOException {

//		WebDriverManager.chromedriver().setup();
//		WebDriver driver = new ChromeDriver();

		WebDriverManager.firefoxdriver().setup();
		WebDriver driver = new FirefoxDriver();

		driver.get("http://automationtesting.in/");

		WebDriverWait wait = new WebDriverWait(driver, 3000);

//		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
//				.takeScreenshot(driver);
//		ImageIO.write(screenshot.getImage(), "PNG",
//				new File("D:\\IR-WebRisk-Testdaten\\Unfall\\1-Unfall-Eingabemaske.png"));

	}

}
