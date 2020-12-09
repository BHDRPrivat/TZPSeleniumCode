package Utils;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class SeleniumUtils {

	public static void InputText(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Inputwert, ExtentTest test) throws InterruptedException {
    // Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
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
			
	} catch (NoSuchElementException e) {
		// Fehlerbehandlung einfügen
		test.log(Status.FAIL, "Eintrag in " +ObjektPath  + " mit Wert: " + Inputwert);
		test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
		System.out.println("Gemeldeter Fehler: " + e);
	}
	}
	

	public static void ButtonKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
		try {
		if 	(HTMLSelector.equals("xpath")) {
			driver.findElement(By.xpath(ObjektPath)).click();
			test.log(Status.INFO, "Auswahl des Objektes: " +ObjektPath);
			// Zeitspanne setzen
			Thread.sleep(4 * Zeitspanne);
		}
		} catch (NoSuchElementException e) {
			// Fehlerbehandlung einfügen
			test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
			test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
			System.out.println("Gemeldeter Fehler: " + e);
		}
	}


	public static void HakenKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
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
		}
	}
	
	
	public static void PDFUpload(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Suchbegriff, String Bereich, String DatumEintrag, ExtentTest test) throws InterruptedException, IOException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
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
		Thread.sleep(3 * Zeitspanne);
		

		// Auf das Leerzeichen vor " \" achten!
		Runtime.getRuntime().exec("cmd.exe /c Start AutoIt3.exe " + autoitscriptpath + " \""	+ filepath + "\"");
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
		Thread.sleep(1* Zeitspanne);
	    } catch (NoSuchElementException e) {
		// Fehlerbehandlung einfügen
		test.log(Status.FAIL, "Auswahl des Objektes: " + ObjektPath);
		test.log(Status.FAIL, "Gemeldeter Fehler: " + e);
		System.out.println("Gemeldeter Fehler: " + e);
	}

	}

	
	
	public static void ListenAuswahl(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String ObjektPathListe, String Listenelement, ExtentTest test) throws InterruptedException {
		// Mit Try, Catch den Weiterlauf nach einem Fehler ermöglichen  
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
	}
	}	
}