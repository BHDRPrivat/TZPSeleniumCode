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
	
	
	@DataProvider(name = "TZPTenderStartGG")
	public static Object[][] getDataTenderStartGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTenderGG-GN.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	
	
	@DataProvider(name = "TZPTenderAngebotSendenGN")
	public static Object[][] getDataTenderAngebotSendenGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTenderGG-GN.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	
	
	@DataProvider(name = "TenderAngebotAnnehmenGG")
	public static Object[][] getData() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTenderGG-GN.xls";
		return testData(excelPath, "Testdaten");
	}
	
	
	
	@DataProvider(name = "TZPCheckEMailsGG")
	public static Object[][] getDataCheckEMailsGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalCheckEMailGG.xls";
		return testData(excelPath, "Testdaten");
	}

	@DataProvider(name = "TZPCheckEMailsGN")
	public static Object[][] getDataCheckEMailsGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalCheckEMailGN.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPAdminHandelsfreigabeGG")
	public static Object[][] TZPAdminHandelsfreigabeGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalAdminHandelsfreigabeGG.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPAdminDeaktivierenGN")
	public static Object[][] TZPAdminDeaktivierenGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalAdminDeaktivierenGN.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPZinssatzVorgabeGN")
	public static Object[][] TZPZinssatzVorgabeGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalZinssatzVorgabeGN.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	@DataProvider(name = "TZPTransaktionStartGG")
	public static Object[][] TZPTransaktionStartGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTransaktionGG-GN.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPTransaktionBearbeitenGN")
	public static Object[][] TZPTransaktionBearbeitenGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTransaktionGG-GN.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	@DataProvider(name = "TZPTransaktionAkzeptierenGG")
	public static Object[][] TZPTransaktionAkzeptierenGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTransaktionGG-GN.xls";
		return testData(excelPath, "Testdaten");
	}		
	
	
	@DataProvider(name = "TZPAdminCourtageGN")
	public static Object[][] TZPAdminCourtageGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalAdminCourtage.xls";
		return testData(excelPath, "Testdaten");
	}	

	@DataProvider(name = "TZPSeveralUserGG")
	public static Object[][] TZPSeveralUserGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalSeveralUserGG.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	@DataProvider(name = "TZPSeveralUserGN")
	public static Object[][] TZPSeveralUserGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalSeveralUserGN.xls";
		return testData(excelPath, "Testdaten");
	}

	@DataProvider(name = "TZPCheckEMailsSeveralUserGG")
	public static Object[][] TZPCheckEMailSeveralUserGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalCheckEMailSeveralUserGG.xls";
		return testData(excelPath, "Testdaten");
	}		
	
	@DataProvider(name = "TZPCheckEMailsSeveralUserGN")
	public static Object[][] TZPCheckEMailSeveralUserGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalCheckEMailSeveralUserGN.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPAdminSeveralUserDeaktivierenLoeschenGG")
	public static Object[][] TZPAdminSeveralUserDeaktivierenGG() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalAdminSeveralUserDeaktivierenLoeschenGG.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPAdminSeveralUserDeaktivierenLoeschenGN")
	public static Object[][] TZPAdminSeveralUserDeaktivierenGN() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalAdminSeveralUserDeaktivierenLoeschenGN.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	@DataProvider(name = "TZPTransaktionStartGGSeveralUser")
	public static Object[][] TZPTransaktionStartGGSeveralUIser() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTransaktionGG-GN-SeveralUser.xls";
		return testData(excelPath, "Testdaten");
	}
	
	@DataProvider(name = "TZPTransaktionBearbeitenGNSeveralUser")
	public static Object[][] TZPTransaktionBearbeitenGNSeveralUser() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTransaktionGG-GN-SeveralUser.xls";
		return testData(excelPath, "Testdaten");
	}	

	@DataProvider(name = "TZPTransaktionAkzeptierenGGSeveralUser")
	public static Object[][] TZPTransaktionAkzeptierenGGSeveralUser() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTransaktionGG-GN-SeveralUser.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	@DataProvider(name = "TZPTenderStartGGSeveralUser")
	public static Object[][] getDataTenderStartGGSeveralUser() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTenderGG-GN-SeveralUser.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	
	
	@DataProvider(name = "TZPTenderAngebotSendenGNSeveralUser")
	public static Object[][] getDataTenderAngebotSendenGNSeveralUser() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTenderGG-GN-SeveralUser.xls";
		return testData(excelPath, "Testdaten");
	}	
	
	
	
	@DataProvider(name = "TenderAngebotAnnehmenGGSeveralUser")
	public static Object[][] getDataTenderAngebotAnnehmenGGSeveralUser() throws BiffException {
		// Zugriff auf die korrekten Exceldaten
		String excelPath = System.getProperty("user.dir") + "\\Excel\\TopZinsPortalTenderGG-GN-SeveralUser.xls";
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
