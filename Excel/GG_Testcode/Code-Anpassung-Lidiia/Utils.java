package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;

public class SeleniumUtils {

	public static void InputText(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String Inputwert) throws InterruptedException {

		if 	(HTMLSelector.equals("name")) {
			driver.findElement(By.name(ObjektPath)).click();
			// Löscht vorhande Einträge sicherer als clear()  
			driver.findElement(By.name(ObjektPath)).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        	driver.findElement(By.name(ObjektPath)).sendKeys(Inputwert);
			// Zeitspanne setzen
			Thread.sleep(2 * Zeitspanne);
		}
	}

	public static void ButtonKlick(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath) throws InterruptedException {

		if 	(HTMLSelector.equals("xpath")) {
			driver.findElement(By.xpath(ObjektPath)).click();
			// Zeitspanne setzen
			Thread.sleep(4 * Zeitspanne);
		}

	}

	public static void ListenAuswahl(WebDriver driver, Integer Zeitspanne, String HTMLSelector, String ObjektPath, String ObjektPathListe, String Listenelement) throws InterruptedException {

		if 	(HTMLSelector.equals("xpath")) { 
			// Zuerst muss auf das übergeordnete div geklickt werden
			driver.findElement(By.xpath(ObjektPath)).click();
			// Es muss noch einmal auf das entsprechende Listenelement geklickt werden
			driver.findElement(By.xpath(ObjektPathListe + Listenelement + "')]")).click();
			// Zeitspanne setzen
			Thread.sleep(2 * Zeitspanne);	
		}
	}

}