package TopZinsPortal;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class GetOTPFromGmail {

	public static WebDriver driver;
	
		public static void main(String[] args) throws InterruptedException, IOException {
			
			// Beispiel für Code, dass nach dem Untersuchen angezeigt wird aber 
			// Bein Aufruf zu einem Fehler "No such Element führt"
			

			String StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
			String SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";

			// Aufruf des Browser-Setups mit WebDriver
			driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);
				
		
			driver.get("https://onedrive.live.com/about/en-us/signin/"); 
			Thread.sleep(2000);
			
			// Es muss auf den Frame gewechselt werden, damit die Elemnete aufgerufen werden können
			WebElement frame = driver.findElement(By.xpath("//iframe[@class='SignIn']"));
			driver.switchTo().frame(frame);
			
			
			//Enter Email
			driver.findElement(By.xpath("//input[@type='email']"));
		    driver.findElement(By.xpath("//input[@type='email']")).clear();
		    driver.findElement(By.xpath("//input[@type='email']")).sendKeys("TZP.lendermail@gmail.com");
		    driver.findElement(By.xpath("//input[@value='Next']")).click();
			System.out.println("E Mail eingetragen");
			
			//Click on Next
			driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.TAB);
			driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.TAB);
			driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.ENTER); 
			
			
			Thread.sleep(2000);
			driver.findElement(By.id("idA_PWD_ForgotPassword")).click(); 
			driver.findElement(By.name("proofOption")).click(); 
		    driver.findElement(By.id("iSelectProofAction")).click();
			driver.findElement(By.id("iVerifyText")).sendKeys("123"); 
			driver.findElement(By.id("iVerifyldentityAction")).click();



	}

}
