package TopZinsPortal;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.applitools.eyes.selenium.Eyes;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import Utils.ExcelUtilsJXL;
import Utils.SeleniumUtils;
import jxl.read.biff.BiffException;

public class TZPTransaktionBearbeitenGN {

	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen

	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = null;
	public SoftAssert softassert = new SoftAssert();

	// Variablen für die Unter-Test-Klassen
	public static Boolean TransaktionAkzeptierenGN = false;
	public static Boolean TransaktionAblehnenGN = false;
	public static Boolean TransaktionTelefonischGN = false;

	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	// Anzahl der Testfälle, wichtig für die einzelnen Zugriffe + 1
	// zählt danach herunter
	// Sollte eigentlich von der Anzahl der Zeilen in der Exceltabelle ermittelt
	// werden
	// Zugriff wurde nur ausgelagert.
//	AktuellTransaktionMaske = rowCount;
//		System.out.println("Zeile=" + rowCount + "Spalte=" + colCount + "String Wert: ");
	public static Integer AktuellTransaktionMaske = 3;

	public String AblaufartGlobal;

	// public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;

	// Zu Testzwecken, direktsprung auf das Hochladen der PDF-dateien
	// Wenn alle Stammdaten eingegeben wurden, kann mit false direkt auf Dokumente
	// zugegriffen werden
	// Boolean MissingData = true;
	Boolean MissingData = true;

	@Parameters({ "Ablaufart" })
	@BeforeTest
	public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart)
			throws InterruptedException, IOException {

		// Ermittelt den Pfad des aktuellen Projekts
		projectpath = System.getProperty("user.dir");

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter(
					"Fehlerreport TopZinsPortal Transaktion Bearbeiten GN - " + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}
		AblaufartGlobal = Ablaufart;
		StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
		Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

		BaseUrl = Utils.TZPBeforeTest.Umgebung() + "/portal/login";

		SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden. "==" steht für die Überprüfung des Speicherorts

		// Aufruf des Browser-Setups
		driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);

	}

	// @Test
	@Test(priority = 0, dataProvider = "TZPTransaktionBearbeitenGN", dataProviderClass = Utils.DataSupplier.class)
	public void TZPTransaktionBearbeitenGNTest(String Teststep, String Aktiv, String EmailadresseGG, String PasswortGG,
			String VolumenGG, String ZinssatzGG, String Valuta, String Zinskonvention, String Zahlungsfrequenz,
			String SonstigesGG, String KommentarGG, String EndeAnfrageUhrzeitGG, String BtnAnfrageSendenGG,
			String BtnAusloggenGG, String FirmaGN, String EmailadresseGN, String PasswortGN, String VolumenGN,
			String ZinssatzGN, String EndeAngebotGN, String BtnAngebotSendenGN, String BtnAnfrageAblehnenGN,
			String BtnAngebotTelefonischWeiterleitenGN, String BtnAngebotAnnehmenGG, String BtnAngebotAblehnenGG,
			String BtnAngebotTelefonischAnnehmenGG) throws Exception {

		if (Aktiv.equals("Ja")) {

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZP_Transaktion: " + Teststep + " - " + AblaufartGlobal,
					"Bearbeitung einer Transaktion durch den Geldnehmer");

			driver.get(BaseUrl);
			// 1. Loginseite oeffnen
			Thread.sleep(3 * Zeitspanne);
			test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

			// Zeit zum Akzeptieren geben.
			Thread.sleep(5 * Zeitspanne);

			// 9. Login entsprechenden Geldgeber mit gueltigen Daten
			SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", EmailadresseGN, test);
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", PasswortGN, test);

			// 9.1 Button "Login" auswaehlen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);

			// 10. zeit zum Aufbauen der Meldung
			Thread.sleep(8 * Zeitspanne);

			Utils.SeleniumUtils.DragDrop(driver, Zeitspanne,
					"(//div[contains(@class, 'MuiDialogTitle-root')])[" + AktuellTransaktionMaske + "]",
					"//div[@data-test='sentinelStart']", test);

			// 5.1 Volumen
			// für jeden Testfall wird die erste der Vorhanden Transaktionsmasken ausgewählt
			System.out.println("Vor Imput Text");
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
					"(//input[@name='volume'])[" + AktuellTransaktionMaske + "]", VolumenGN, test);
			System.out.println("Volumen GN = " + VolumenGN);

			// Zinssatz ändern
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
					"(//input[@name='interestRate'])[" + AktuellTransaktionMaske + "]", ZinssatzGN, test);
			System.out.println("Zinssatz GN = " + ZinssatzGN);

			// 5.7 Feld "Ende der Angebots (Zeit)" ausfüllen
			// Bei allen Fällen identisch, daher vor den anderen Feldern possitioniert
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
					"(//Label[text() ='Ende des Angebots (Uhrzeit)*']//following::input[contains(@class, 'MuiInput')][1])["
							+ AktuellTransaktionMaske + "]",
					EndeAnfrageUhrzeitGG, test);

			// 11. Button "Angebot senden" klicken
			if (BtnAngebotSendenGN.equals("Ja")) {
				// Angebot wird durch den GN abgesendet
				System.out.println("Angebot senden GN");

				// 5.5 Eintrag "Sonstiges"
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
						"(//textarea[@name='other'])[" + AktuellTransaktionMaske + "]",
						(SonstigesGG + " Geldnehmer GN sendet Angebot"), test);

				// 5.6 Eintrag "Kommentar"
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
						"(//textarea[@name='comment'])[" + AktuellTransaktionMaske + "]",
						(KommentarGG + " Geldnehmer GN sendet Angebot "), test);

				// Angebot wird durch GN angenommen
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
						"(//span[text()='senden']//ancestor::button[contains(@class, 'MuiButtonBase')])["
								+ AktuellTransaktionMaske + "]",
						test);

				
				Thread.sleep(3 * Zeitspanne);
				// Sichtbarkeit des Buttons als Bedingung für den erolgreichen Durchlauf
				TransaktionAkzeptierenGN = (driver.findElement(By.xpath("//*[@class='MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary']")).isDisplayed());
				
				
				// 12. Button "Ja" in Pop-up klicken (class="MuiButtonBase-root MuiButton-root
				// MuiButton-contained MuiButton-containedPrimary"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
						"//*[@class='MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary']",
						test);
	
      			// warten bis OK-Maske erscheint.
				// Button "OK" auswählen, wenn vorhanden
				Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);

			} else if (BtnAnfrageAblehnenGN.equals("Ja")) {
				// Angebot wird durch den GN abgelehnt

				System.out.println("Angebot Ablehnen GN");

				// 5.5 Eintrag "Sonstiges"
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
						"(//textarea[@name='other'])[" + AktuellTransaktionMaske + "]",
						(SonstigesGG + " Geldnehmer GN lehnt Angebot ab"), test);

				// 5.6 Eintrag "Kommentar"
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
						"(//textarea[@name='comment'])[" + AktuellTransaktionMaske + "]",
						(KommentarGG + " Geldnehmer GN lehnt Angebot ab"), test);

				
				
				// Sichtbarkeit des Buttons als Bedingung für den erolgreichen Durchlauf
				TransaktionAblehnenGN = (driver.findElement(By.xpath("(//span[text()='ablehnen']//ancestor::button[contains(@class, 'MuiButtonBase')])["
						+ AktuellTransaktionMaske + "]")).isDisplayed());
				
				
				// Angebot wird durch GN abgelehnt
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
						"(//span[text()='ablehnen']//ancestor::button[contains(@class, 'MuiButtonBase')])["
								+ AktuellTransaktionMaske + "]",
						test);

		
				
				// Button "OK" auswählen, wenn vorhanden
				Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);

			} else if (BtnAngebotTelefonischWeiterleitenGN.equals("Ja")) {
				// Angebot wird telefonsich weitergeleitet
				System.out.println("Angebot Telefonisch GN");

				// 5.5 Eintrag "Sonstiges"
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
						"(//textarea[@name='other'])[" + AktuellTransaktionMaske + "]",
						(SonstigesGG + " Geldnehmer GN Telefonisch"), test);

				// 5.6 Eintrag "Kommentar"
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "xpath",
						"(//textarea[@name='comment'])[" + AktuellTransaktionMaske + "]",
						(KommentarGG + " Geldnehmer GN Telefonisch "), test);

				
				// Sichtbarkeit des Buttons als Bedingung für den erolgreichen Durchlauf
				TransaktionTelefonischGN = (driver.findElement(By.xpath("(//span[text()='telefonisch weiterleiten']//ancestor::button[contains(@class, 'MuiButtonBase')])["
						+ AktuellTransaktionMaske + "]")).isDisplayed());
				
				
				// Angebot wird durch GN an Forsa weitergeleitet
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
						"(//span[text()='telefonisch weiterleiten']//ancestor::button[contains(@class, 'MuiButtonBase')])["
								+ AktuellTransaktionMaske + "]",
						test);

			
				
				// Button "OK" auswählen, wenn vorhanden
				Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);



			}

			// Für jeden Testfall wird die entsperechende Transaktionsmasken ausgewählt
			// Beachet die Reihenfolge ist bei den GN in umgekehrter Reihenfolge

			AktuellTransaktionMaske = AktuellTransaktionMaske - 1;

			Thread.sleep(3 * Zeitspanne);

			// 13. Geldnehmer ausloggen
//			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[@data-test='logout-button']", test);  // Klicken auf ein Button mit dem Attribut data-test='logout-button'
//			System.out.println("GN logout");

			Thread.sleep(3 * Zeitspanne);

			driver.close();
			// Für den Teardown
			driver = null;
			eyes = null;

			// Neu Starten
			SetupSeleniumTestdaten(AblaufartGlobal);

		} // Nur wenn Aktiv "Ja" ist, durchlaufen

	}

	
	@Test(priority=10)
	public void TZPTransaktionAkzeptierenGNTest() {
	// Softassert-Kontrolle durchfühern
	SoftAssert softassert = new SoftAssert();
	// Variable Login gibt an, welches Ergebnis vorliegt.
	softassert.assertTrue(TransaktionAkzeptierenGN);
	softassert.assertAll(); // Damit der Code weiter durchlaufen wird.
	}
	
	@Test(priority=20)
	public void TZPTransaktionAblehnenGNTest() {
	// Softassert-Kontrolle durchfühern
	SoftAssert softassert = new SoftAssert();
	// Variable Login gibt an, welches Ergebnis vorliegt.
	softassert.assertTrue(TransaktionAblehnenGN);
	softassert.assertAll(); // Damit der Code weiter durchlaufen wird.
	}	
	
	@Test(priority=30)
	public void TZPTransaktionTelefonischWeiterleitenGNTest() {
	// Softassert-Kontrolle durchfühern
	SoftAssert softassert = new SoftAssert();
	// Variable Login gibt an, welches Ergebnis vorliegt.
	softassert.assertTrue(TransaktionTelefonischGN);
	softassert.assertAll(); // Damit der Code weiter durchlaufen wird.
	}	
	
	
	
	public void ApplitoolsAufnahme(String Ablaufart, String teststep) {
		if (Ablaufart.equals("Applitool")) {
			// Applitools vorbereiten
			eyes = new Eyes();
			eyes.setApiKey("1epbTsh91uyej6yur9x0FzJb3WUit5naoVB8SYMRZUE110");
			try {

				// eyes.check("AL_Risiko", Target.window().fully());

				// eyes.open(driver, "AL-Risiko", "Testfall: " + teststep, new
				// RectangleSize(800, 600));

				// eyes.open(driver, "IR-PHV", "Testfall: " + teststep,
				// Target.window().fully());

				// eyes.open(driver, "IR-PHV", "Testfall: " + teststep, new
				// RectangleSize(900,750));

				// Kein Rectangle vorgeben um die Voreinstellung zu verwenden.
				eyes.open(driver, "IR-PHV", "Testfall: " + teststep);

				eyes.checkWindow("AL-Risiko-Ergebnisseite");

				eyes.close();

			} finally {

			}
		}
	}

	@AfterTest
	public void BrowserTearDown() throws InterruptedException {

		// Offene Bereiche Schließen
		Utils.SeleniumUtils.BrowserBeenden(driver, Zeitspanne, extent, eyes);
	}

}
