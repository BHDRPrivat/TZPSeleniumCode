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
import Utils.SeleniumUtils;
import Utils.TZPBeforeTest;
import junit.framework.Assert;
import jxl.read.biff.BiffException;

public class TZPSeveralUserGNAdminHandelsfreigabe {

	// Die Stammdateneingabe eines Geldgebers wird Excel-Datengetrieben durchlaufen

	public static WebDriver driver;
	private Integer Zeitspanne;
	private String BaseUrl;
	public String StandardBrowser;
	public String SpeicherpfadTestdokumente;
	public static String TestdatenExceldatei;
	public static String projectpath = System.getProperty("user.dir");

	// Klassenvariablen
	ExtentHtmlReporter htmlReporter = null;
	ExtentReports extent;

	// Für E-mail einlesen
	public Object Mailweitergabe[][] = null;
	String RegistrierungslinkSU = null;

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

		if (htmlReporter == null) {
			// start reporters
			htmlReporter = new ExtentHtmlReporter(
					"Fehlerreport TopZinsPortal Several User GG - " + Ablaufart + ".html");
			// create ExtentReports and attach reporter(s)
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
		}
		AblaufartGlobal = Ablaufart;
		StandardBrowser = Utils.TZPBeforeTest.BrowserArt();
		Zeitspanne = Utils.TZPBeforeTest.Pausenzeit();

		// Gesamtadresse: "http://3.127.85.31/portal/login"
		BaseUrl = Utils.TZPBeforeTest.Umgebung() + "/portal/login";

		SpeicherpfadTestdokumente = "F:\\BHDR\\TopZinsPortalTest\\PDFDokumente\\";
		// Wichtiger Hinweis: In Java dürfen generische Strings nicht mit "=="
		// verglichen werden. "==" steht für die Überprüfung des Speicherorts

		// Aufruf des Browser-Setups
		driver = Utils.TZPSetupBrowser.BrowserSetup(driver, StandardBrowser, SpeicherpfadTestdokumente);

	}

	@Test(dataProvider = "TZPSeveralUserGN", dataProviderClass = Utils.DataSupplier.class)
	public void TZPServeralUserAdminHanbdelsfreigabeGNTest(String Teststep, String Aktiv,
			String EmailadresseCompanyAdmin, String PasswortCompanyAdmin, String Anrede, String FirstName,
			String LastName, String EmailadresseCompanyUser, String PasswortCompanyUser, String Unternehmensname,
			String Vorname, String TelefonNr, String Titel, String Nachname, String DatumPDF) throws Exception {

		if (Aktiv.equals("Ja")) {

			// Mock
			// String teststep = "AL-R1";

			// creates a toggle for the given test, adds all log events under it
			ExtentTest test = extent.createTest("TZPServeralUserGN: " + Teststep + " - " + AblaufartGlobal,
					"Einladen eines Company Employeers eines Geldnehmers");

			// Admin-Forsa Handelsfreigabe erteilen

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
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//button[contains(@type, 'submit')]", test);

			// Button "Daten komplett" in menu clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
					"//li[contains(@data-test, '" + "BEREIT FÜR HANDELSBERECHTIGUNG" + "')]", test);

			// Weiterlauf nur nach implizierter Anzeige des Suchfeldes
			// Sobald die Kondition erfüllt wird, erfolgt der weitere Programmablauf.
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='search']")));

			// Firmenname in das Suchfeld eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
					"\\Admin Handelsfreigabe\\AmdinHandlefreigabe-GG-Auswahl", Teststep, test);
			Thread.sleep(3 * Zeitspanne);

			// Der Stift für das Laden der Daten hat keine eindeutige ID. Der Zugriff
			// erfolgt über den Eintrag im ersten Eingabefeld
			// Beachte, der Eintrag im ersten Eingabefeld ist abhängig vom Unternehmensnamen
			// Bei den Several Usern muss eine Fallunterscheidung erfolgen

			// Sichtbarkeit des Buttons weiter verfeinern.

			String xpathvalue = "//div[text() = '" + Unternehmensname
					+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root')]//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";

			// Wenn Stift existiert, dirket auswählen
			if (Utils.SeleniumUtils.isElementPresent(driver, By.xpath(xpathvalue))) {
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);
			} else {

				// Ansonsten Button zum Auslösen wählen
				xpathvalue = "//div[text() = '" + Unternehmensname
						+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root')]//button[@type='button']";

				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);

				// den einzelnen User suchen
				xpathvalue = "//div[text() = '" + EmailadresseCompanyUser
						+ "']//ancestor::tr[contains(@class, 'MuiTableRow-root')]//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-colorPrimary']";
				Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", xpathvalue, test);

			}

			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);

			// Direktsprung auf Dokumente
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath", "//a[@href='#/masterdata/documents']", test);

//			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'identityCard']", test);
//			Utils.SeleniumUtils.HakenKlick(driver, Zeitspanne, "xpath", "//input[@value = 'tradingLicense']", test);

			// Screenshot aufnehmen
			Thread.sleep(3 * Zeitspanne);
			Utils.SeleniumUtils.FullPageScreenshotAShotSelenium(driver, Zeitspanne, projectpath,
					"\\Admin Handelsfreigabe\\AmdinHandlefreigabe-GG-Haken", Teststep, test);

			// Button Freigeben auswählen
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
					"//span[text()='Freigeben']//ancestor::button[@tabindex='0']", test);
			// TSonderzeit zum Hochladen
			Thread.sleep(3 * Zeitspanne);

			// Button "OK" auswählen, wenn vorhanden
			Utils.SeleniumUtils.OKButtonKlick(driver, Zeitspanne, test);

			// Prüfen ob User in Register "Mit Handelsberechtigung" vorhanden ist?
			// Button "Daten komplett" in menu clicken
			Utils.SeleniumUtils.ButtonKlick(driver, Zeitspanne, "xpath",
					"//li[contains(@data-test, 'MIT HANDELSBERECHTIGUNG')]", test);

			// Die Anzeige auf 100 erhöhen
			Utils.SeleniumUtils.ListenAuswahl(driver, Zeitspanne, "xpath", "//div[contains(@id,'mui')]",
					"//li[contains(text(),'", "100", test);

			// Firmenname in das Suchfeld eingeben
			Utils.SeleniumUtils.InputText(driver, Zeitspanne, "name", "search", Unternehmensname, test);
			Thread.sleep(5 * Zeitspanne);

			Thread.sleep(10 * Zeitspanne);

			driver.close();
			// Für den Teardown
			driver = null;
			eyes = null;

			// Neu Starten
			SetupSeleniumTestdaten(AblaufartGlobal);

		} // Nur wenn Aktic "Ja" ist durchlaufen

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
