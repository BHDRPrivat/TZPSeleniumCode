package Utils;

import org.testng.annotations.DataProvider;

import jxl.read.biff.BiffException;

public class DataSupplier {
	
	@DataProvider(name = "TZPRegGG")
	public static Object[][] getDataRegGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalRegGG.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPRegGN")
	public static Object[][] getDataRegGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalRegGN.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPStammGG")
	public static Object[][] getDataStammGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalStammGG.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPStammGN")
	public static Object[][] getDataStammGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalStammGN.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TenderAngebotAnnehmenGG")
	public static Object[][] getData() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTenderGG-GN.xls";
		return testData(excelPath, "Testdaten");
	}
	
	
	// Auslesen der Exceldaten.
	public static Object[][] testData(String excelPath, String sheetName) throws BiffException {
		// Aufruf des Constructors von ExcelUtils
		ExcelUtilsJXL excel = new ExcelUtilsJXL(excelPath, sheetName);

		int rowCount = excel.getRowCount();
		int colCount = excel.getColCount();

		System.out.println("Zeile=" + rowCount + "Spalte=" + colCount + "String Wert: ");

		// 2 Dimensionales Object-Array erzeugen
		Object data[][] = new Object[rowCount - 1][colCount];

		// �ber alle Zeilen laufen (i=1, da i=0 die Headerzeile)
		for (int i = 1; i < rowCount; i++) {
			// �ber alle Spalten laufen
			for (int j = 0; j < colCount; j++) {

				String cellData = excel.getExcelDataString(i, j);
				data[i - 1][j] = cellData;

				System.out.println("Pro Zeile=" + i + "Pro Spalte=" + j + "Pro String Wert: " + cellData);

				// Werte in einer Zeile anzeigen
				// System.out.print(cellData + " | ");
			}
		}
		return data;
	}

}
