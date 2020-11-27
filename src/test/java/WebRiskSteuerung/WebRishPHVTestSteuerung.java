package WebRiskSteuerung;

import java.io.IOException;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import config.PropertiesFile;

public class WebRishPHVTestSteuerung {
	// Erst mal nicht verwenden, da die Propertie-Datei zerschossen wird.

	public static void main(String[] args) throws IOException {
		// Intrinsische Auswirkungen durch den Testablauf
		// Aufgrund von Fehlern, bei der gleichzeitigen Verwendung von PDF-Speicherung
		// und Applitools, wird der Druchlauf mit modifizierter configdatei 2 mal
		// Durchlaufen

	}

	@Parameters({ "Ablaufart" })
	@Test
	public void PropertiesSetzen(@Optional("Default") String Ablaufart) throws IOException {
		if (Ablaufart.equals("PDF-Druck")) {
			// PropertiesFile.replaceProperties("StandardBrowser", "Chrome");
			// PropertiesFile.replaceProperties("Zeitspanne", "1000");
			PropertiesFile.replaceProperties("Druckauswahl", "true");
			PropertiesFile.replaceProperties("ScreenshotAufnehmen", "true");
			PropertiesFile.replaceProperties("ApplitoolsAufnahme", "false");
		}
		if (Ablaufart.equals("Applitools")) {
			// PropertiesFile.replaceProperties("StandardBrowser", "Chrome");
			// PropertiesFile.replaceProperties("Zeitspanne", "1000");
			PropertiesFile.replaceProperties("Druckauswahl", "false");
			PropertiesFile.replaceProperties("ScreenshotAufnehmen", "false");
			PropertiesFile.replaceProperties("ApplitoolsAufnahme", "true");
		}

	}
}
