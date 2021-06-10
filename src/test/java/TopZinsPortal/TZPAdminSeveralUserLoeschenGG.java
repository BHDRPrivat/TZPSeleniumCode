package TopZinsPortal;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import Utils.TZPBeforeTest;
import Utils.TZPSetupBrowser;
import junit.framework.Assert;
import jxl.read.biff.BiffException;

public class TZPAdminSeveralUserLoeschenGG {

	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen

	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = null;
	public static String AnmeldungForsaAdmin = null;
	// Für AutoIT
	String workingDir;
	String autoitscriptpath;
	String filepath;
	String xpathvalue;

	// Wenn alle Stammdaten eingegeben wurden, kann mit true direkt auf Dokumente
	// zugegriffen werden
	Boolean Handel = true;

	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	public static String AblaufartGlobal;

	// public ChromeDevToolsService devToolsService = null;
	// Variable für Applitools
	public Eyes eyes = null;

	@Parameters({ "Ablaufart" })
	@BeforeTest
	public void SetupSeleniumTestdaten(@Optional("Ad Hoc Test") String Ablaufart)
			throws InterruptedException, IOException {

		// Ermittelt den Pfad des aktuellen Projekts
		projectpath = System.getProperty("user.dir");

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter(
					"Fehlerreport TopZinsPortal Admin User Dektivieren - " + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}

		System.out.println("Admin löschen: " + Ablaufart);
		AblaufartGlobal = Ablaufart;
		StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
		Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

		workingDir = System.getProperty("user.dir");
		autoitscriptpath = workingDir + "\\AutoIT\\" + "File_upload_selenium_webdriver.au";
		filepath = workingDir + "\\DummyPDF\\PDF-Dummy.pdf";

		BaseUrl = TZPBeforeTest.Umgebung() + "/portal/login";

		SpeicherpfadTestdokumente = workingDir + "\\test-output\\PDFOutput\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden. "==" steht für die Überprüfung des Speicherorts

		if (AnmeldungForsaAdmin == null) {
			// Aufruf des Browser-Setups
			driver = TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);
		}
	}

	// @Test
	@Test(dataProvider = "TZPAdminSeveralUserDeaktivierenLoeschenGN", dataProviderClass = Utils.DataSupplier.class)
	public void TZPAdminSeveralUserDeaktivierenGNTest(String Teststep, String Aktiv, String Menue,
			String ZeilenProSeite, String Unternehmensname, String EmailadresseCompanyUser, String Menue2)
			throws Exception {

		if (Aktiv.equals("Ja")) {

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPAdmin" + "deaktivieren: " + Teststep + " - " + AblaufartGlobal,
					"Löschen eines Anwenders");

			if (AnmeldungForsaAdmin == null) {

				driver.get(BaseUrl);
				// TZRegGG-Eingabemaske
				Thread.sleep(3 * Zeitspanne);
				test.log(Status.INFO, "Web-Applikation im Browser geoeffnet: " + BaseUrl);

				// Login mit gültigen Daten

				String Emailadresse = Utils.TZPBeforeTest.AdminEmail();
				String Passwort = Utils.TZPBeforeTest.AdminPasswort();

				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "email", Emailadresse, test);
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "password", Passwort, test);

				// Button "Anmelden auswählen"
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]",
						test);

				// Anmeldung ist erfolgt
				// AnmeldungForsaAdmin = "JA";
				// wird zurückgehalten, da der Aufbau der Tabellen-Liste zu viel Zeit in
				// anspruch nimmt
				// und der Neustart des Browsers die Zeit zur Verfügung stellt.
				AnmeldungForsaAdmin = "Ja";
			}

			if (Handel) {

				// Button in menu clicken
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
						"//li[contains(@data-test, '" + Menue2 + "')]", test);

				// Weiterlauf nur nach implizierter Anzeige des Suchfeldes
				// Sobald die Kondition erfüllt wird, erfolgt der weitere Programmablauf.
				WebDriverWait wait = new WebDriverWait(driver, 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='search']")));

				if (AnmeldungForsaAdmin == null) {
					// Die Anzeige auf 100 ändern, einmalig nach dem ersten Lauf zur Kontrolle
					Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//div[contains(@id,'mui')]",
							"//li[contains(text(),'", ZeilenProSeite, test);
					Thread.sleep(3 * Zeitspanne);
				}

				// Firmenname in das Suchfeld eingeben
				Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);

				// Fallunterscheidung
				// Sichtbarkeit des Lösch-Icons weiter verfeinern.

				
				xpathvalue = "//div[text() = '" + EmailadresseCompanyUser
						+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root')]//button[contains(@class, 'MuiIconButton-colorPrimary')]";
				
				// Wenn Lösch-Icon existiert, direkt auswählen
				if (Utils.SeleniumUtils.isElementPresent(driver, By.xpath(xpathvalue))) {
					Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
				} else {

					// Ansonsten Button zum Auslösen wählen
					xpathvalue = "//div[text() = '" + Unternehmensname
							+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root')]//button[@type='button']";

					Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);

					// den einzelnen User suchen
					xpathvalue = "//div[text() = '" + EmailadresseCompanyUser
							+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root')]//button[contains(@class, 'MuiIconButton-colorPrimary')]";

					Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);

				}


				// Button auswählen
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
						"//span[text()='Bestätigen']//ancestor::button[@tabindex='0']", test);
				// TSonderzeit zum löschen
				Thread.sleep(3 * Zeitspanne);

				// Button "OK" auswählen, wenn vorhanden
				Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);

				// Screenshot aufnehmen
				Thread.sleep(3 * Zeitspanne);
				Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
						"\\Admin UserDeaktivieren\\Nach-Bestätigen-Button", Teststep, test);
				Thread.sleep(3 * Zeitspanne);

			}


			// Neu Starten
			SetupSeleniumTestdaten(AblaufartGlobal);
		} // Nur wenn Aktic "Ja" ist durchlaufen

	}


	@AfterTest
	public void BrowserTearDown() throws InterruptedException {

		// Offene Bereiche Schließen
		Utils.SeleniumUtils.BrowserBeenden(driver, Zeitspanne, extent, eyes);
	}

}
