package Utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.applitools.eyes.selenium.Eyes;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import junit.framework.Assert;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class SeleniumUtils {
	
	public static void InputDatum(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Inputwert, ExtentTest test) throws InterruptedException {
		// Inputwert zu einem gültigen und aktuellen Datum wandeln 
		
		System.out.println("Eingang: " + Inputwert);
		if (Inputwert.equals("ActDatum")) {
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter df;
	        df = DateTimeFormatter.ofPattern("dd.MM.yyyy");     // 31.01.2016
    		Inputwert = now.format(df);
		}
		System.out.println("Ausgang: " + Inputwert);
		
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {	
			if 	(HTMLSelector.equals("name")) {
				driver.findElement(By.name(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.name(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.name(ObjektPath)).clear();
				driver.findElement(By.name(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			if 	(HTMLSelector.equals("xpath")) {
				driver.findElement(By.xpath(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.xpath(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.xpath(ObjektPath)).clear();
				driver.findElement(By.xpath(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			if 	(HTMLSelector.equals("id")) {
				driver.findElement(By.id(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.id(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.id(ObjektPath)).clear();
				driver.findElement(By.id(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}



		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}
	
	
	public static void InputText(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Inputwert, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {	
			if 	(HTMLSelector.equals("name")) {
				driver.findElement(By.name(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.name(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.name(ObjektPath)).clear();
				driver.findElement(By.name(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			if 	(HTMLSelector.equals("xpath")) {
				driver.findElement(By.xpath(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.xpath(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.xpath(ObjektPath)).clear();
				driver.findElement(By.xpath(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}
			if 	(HTMLSelector.equals("id")) {
				driver.findElement(By.id(ObjektPath)).click();
				//Löscht vorhande Einträge sicherer als clear()  
				driver.findElement(By.id(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				// Für Firefox
				driver.findElement(By.id(ObjektPath)).clear();
				driver.findElement(By.id(ObjektPath)).sendKeys(Inputwert);
				test.log(Status.INFO, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);

			}



		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}


	public static void ButtonKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  

		try {
			if 	(HTMLSelector.equals("xpath")) {
				
				JavascriptExecutor js = (JavascriptExecutor)driver;
     			WebElement element = driver.findElement(By.xpath(ObjektPath));
				
				// Es erfolgt ein scrollen zum Element 
				// Nur wenn es im sichtbaren Bereich liegt, kann Click ausgeführt werden
				js.executeScript("arguments[0].scrollIntoViewIfNeeded(true);", element);
     			// true -> nach unten scrollen
				// false -> nach oben scrollen
				Thread.sleep(2 * Zeitspanne);

				driver.findElement(By.xpath(ObjektPath)).click();
				
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);
			}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}

	
	public static void TabelleButtonKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String FirmaGN, String ZinssatzGG,  ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		String ObjektPath ="Tabellen-Zugriff über " + FirmaGN + " und "+ ZinssatzGG;
		try {
			if 	(HTMLSelector.equals("xpath")) {
				// Aus den Banknamen und den Zinsatz den xpath-Zugriff generieren.
				// Vorlage  "//p[text()='FORSA Digital Bank']//ancestor::tr//div[contains(text(), '0,06')]//ancestor::td"
				ObjektPath = null;
				ObjektPath = "//p[text()='" + FirmaGN + "']//ancestor::tr//div[contains(text(), '" + ZinssatzGG + "')]//ancestor::td";
				System.out.println("Tabellenzugriff: " + ObjektPath);
				
				// Es erfolt ein scrollen zum Element 
				// Nur wenn es im sichtbaren Bereich liegt, kann Click ausgeführt werden
  				JavascriptExecutor js = (JavascriptExecutor)driver;
				WebElement element = driver.findElement(By.xpath(ObjektPath));
				js.executeScript("arguments[0].scrollIntoViewIfNeeded(true);", element);
				
				Thread.sleep(2 * Zeitspanne);

				driver.findElement(By.xpath(ObjektPath)).click();
				
				Thread.sleep(2 * Zeitspanne);
	
			}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}

	public static void HakenKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {
			if ( !driver.findElement(By.xpath(ObjektPath)).isSelected()){
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", ObjektPath, test);	
				test.log(Status.INFO, "Auswahl des Objektes: " +ObjektPath);
			}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}


	public static void PDFUpload(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Suchbegriff, String Bereich, String DatumEintrag, ExtentTest test) throws InterruptedException, IOException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {
			// Für AutoIT
			String workingDir;
			String autoitscriptpath;
			String filepath;
			String xpathvalue;

			workingDir = System.getProperty("user.dir");
			autoitscriptpath = workingDir + "\\AutoIT\\" + "File_upload_selenium_webdriver.au";
			filepath = workingDir + "\\DummyPDF\\PDF-Dummy.pdf";

			xpathvalue="//input[contains(@value, '" + Suchbegriff +"')]//ancestor::div[contains(@class, 'jss')]" + Bereich + "//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";
			System.out.println("Zugriff = " + xpathvalue);
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
			test.log(Status.INFO, "Auswahl des Objektes: " +ObjektPath);
			// TSonderzeit zum Hochladen
			Thread.sleep(5 * Zeitspanne);


			// Auf das Leerzeichen vor " \" achten!
			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "PDF-Datei auswählen: " +" cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);


			// Datum ins Ausstellungsdatum, erst nachdem das Dokument hochgeladen wurde 
			// xpathvalue ="//input[contains(@value, '" + Suchbegriff + "')]//ancestor::div[contains(@class, 'jss')]" + Bereich +"//input[contains(@class, 'MuiOutlinedInput-inputAdornedEnd')]";

			xpathvalue ="//input[contains(@value, '" + Suchbegriff + "')]//ancestor::div[contains(@class, 'jss')]" + Bereich + "//label[text()='Ausstellungsdatum']//following::input[contains(@class, 'MuiOutlinedInput-inputAdornedEnd')]";


			System.out.println("Zugriff = " + xpathvalue);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", xpathvalue, DatumEintrag, test);	
			test.log(Status.INFO, "Datum eintragen: " +DatumEintrag + " in Objekt: " + xpathvalue);
			// TSonderzeit zum Hochladen
			Thread.sleep(3* Zeitspanne);
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}

	}


	public static void PDFUploadListe(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Suchbegriff, String Bereich, String DatumEintrag, ExtentTest test) throws InterruptedException, IOException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(5 * Zeitspanne);
		try {
			// Für AutoIT
			String workingDir;
			String autoitscriptpath;
			String filepath;
			String xpathvalue;

			workingDir = System.getProperty("user.dir");
			autoitscriptpath = workingDir + "\\AutoIT\\" + "File_upload_selenium_webdriver.au";
			filepath = workingDir + "\\DummyPDF\\PDF-Dummy.pdf";

			xpathvalue="//*[@id=\"mui-component-select-docType\"]//ancestor::div[contains(@class, 'jss')]" + Bereich + "//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";
			System.out.println("Zugriff = " + xpathvalue);
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
			test.log(Status.INFO, "Auswahl des Objektes: " +ObjektPath);
			// TSonderzeit zum Hochladen
			Thread.sleep(5 * Zeitspanne);


			// Auf das Leerzeichen vor " \" achten!
			Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
			test.log(Status.INFO, "PDF-Datei auswählen: " +" cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);


			// Datum ins Ausstellungsdatum, erst nachdem das Dokument hochgeladen wurde 
			// xpathvalue ="//input[contains(@value, '" + Suchbegriff + "')]//ancestor::div[contains(@class, 'jss')]" + Bereich +"//input[contains(@class, 'MuiOutlinedInput-inputAdornedEnd')]";

			xpathvalue ="//*[@id=\"mui-component-select-docType\"]//ancestor::div[contains(@class, 'jss')]" + Bereich + "//label[text()='Ausstellungsdatum']//following::input[contains(@class, 'MuiOutlinedInput-inputAdornedEnd')]";


			System.out.println("Zugriff = " + xpathvalue);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath", xpathvalue, DatumEintrag, test);	
			test.log(Status.INFO, "Datum eintragen: " +DatumEintrag + " in Objekt: " + xpathvalue);
			// TSonderzeit zum Hochladen
			Thread.sleep(5* Zeitspanne);
			
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}

	}




	public static void ListenAuswahl(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String ObjektPathListe, String Listenelement, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {
			if 	(HTMLSelector.equals("xpath")) { 
				// Zuerst muss auf das übergeordnete div geklickt werden
				driver.findElement(By.xpath(ObjektPath)).click();
				// Es muss noch einmal auf das entsprechende Listenelement geklickt werden
				driver.findElement(By.xpath(ObjektPathListe + Listenelement + "')]")).click();
				test.log(Status.INFO, "Im Listenelement: " + ObjektPathListe + " Ausgwählt: " + Listenelement);
				// Zeitspanne setzen
				Thread.sleep(2 * Zeitspanne);	
			}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}	

	
	
	
	public static void FullPageScreenshotAShotSelenium(WebDriver driver, Integer Zeitspanne, String projectpath,  String Kennzeichnung, String teststep, ExtentTest test) throws IOException, InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		Thread.sleep(3 * Zeitspanne);
		try {
			// für die Aufnahme den Zoom verkleinern
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("document.body.style.zoom = '75%';");
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
					.takeScreenshot(driver);
			// Screenshot screenshot = new
			// AShot().shootingStrategy(ShootingStrategies.scaling(2)).takeScreenshot(driver);
			ImageIO.write(screenshot.getImage(), "PNG",
					new File(projectpath + "\\screenshots\\" + Kennzeichnung + " " + teststep + ".png"));
			test.log(Status.INFO, "Screenshot aufgenommen: " + projectpath + "\\screenshots\\" + Kennzeichnung + " " + teststep + ".png");
			// Für den weiteren Ablauf Zoom wieder auf 100% setzen
			executor.executeScript("document.body.style.zoom = '100%';");
			Thread.sleep(3 * Zeitspanne);

		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Versuch Screenshot: " +  projectpath + "\\screenshots\\" + Kennzeichnung + " " + teststep + ".png");
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
			Assert.assertTrue("Gemeldeter Fehler: " + e, false);
		}
	}	


	public static void BrowserBeenden(WebDriver driver, Integer Zeitspanne,ExtentReports extent, Eyes eyes) throws InterruptedException {
		// calling flush writes everything to the log file
		Thread.sleep(3 * Zeitspanne);
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



