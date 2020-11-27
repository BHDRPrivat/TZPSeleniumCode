package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesFile {

	static Properties proper = new Properties();
	static String ProjectPath = System.getProperty("user.dir");

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		getProperties("StandardBrowser");
		replaceProperties("StandardBrowser", "Firefox");
		getProperties("StandardBrowser");
	}

	public static String getProperties(String Schluessel) throws IOException {

		Properties proper = new Properties();
		String ProjectPath = System.getProperty("user.dir");
		String value = null;

		try {

			InputStream input = new FileInputStream(ProjectPath + "/src/test/java/config/config.properties");
			proper.load(input);
			value = proper.getProperty(Schluessel);
			System.out.println(value);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();

		}

		return value;
	}

	public static void replaceProperties(String Schluessel, String Wert) throws IOException {

		Properties proper = new Properties();
		String ProjectPath = System.getProperty("user.dir");

		try {

			OutputStream output = new FileOutputStream(ProjectPath + "/src/test/java/config/config.properties");
			// Löscht alle anderen und erzeugt nur ein Eintrag
			// proper.setProperty(Schluessel, Wert);
			// Daher nicht sinnvoll zu verwenden.
			// Es werden zwei Propertie-Dateien angelegt.

			proper.setProperty(Schluessel, Wert);
			proper.store(output, null);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}

	}

}
