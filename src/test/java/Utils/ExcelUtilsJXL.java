package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelUtilsJXL {
	// Aufgrund eines Initialisierungsfehlers mit Apache Poi in der Forsa-Umungebung wurde auf eine 
	// Zugriffsmethode mittels JXL zugegriffen. 
	// Hierbei sind nur *.xls-Dateien möglich.

	static String projectPath;
	static Sheet sheet;
	static Workbook wb;

	// Constructor aufrufen, damit Variablen übergeben werden können
	public ExcelUtilsJXL(String excelPath, String sheetName) throws BiffException {
		try {
			FileInputStream filepath = new FileInputStream(excelPath); 
			wb = Workbook.getWorkbook(filepath);
			sheet = wb.getSheet(sheetName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// Prüfung der Funktionen
		getRowCount();
		getExcelDataString(0, 0);
		getExcelDataNumber(1, 1);
	}

	public static int getColCount() {
		int colCount = 0;
		try {
			// Anzahl der Spalten in Excel mittels JXL
			colCount = sheet.getColumns();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		System.out.println("Anzahl Spalten: " + colCount);
		return colCount;
	}

	public static int getRowCount() {
		int rowCount = 0;
		try {
			// Anzahl der Zeilen in Excel mittels JXL
			rowCount = sheet.getRows();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		System.out.println("Anzahl Zeilen: " + rowCount);
		return rowCount;
	}

	public static String getExcelDataString(int rowNum, int colNum) {
		String cellData = null;
		try {
			// Beachte die Z�hlung erfolgt mit "0" obwohl in Excel die erste Zeile mit "1"
			// beginnt.
			// Beachte die Umstellung der Zugriffsreihenfolge in JXL 
			// Zuerst Spalte dann Zeile
			Cell cell = sheet.getCell(colNum, rowNum);
			cellData = cell.getContents();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		//System.out.println("Zeile=" + rowNum + "Spalte=" + colNum + "String Wert: " + cellData);
		return cellData;
	}

	public static Double getExcelDataNumber(int rowNum, int colNum) {
		Double cellData1 = null;
		try {
			// Beachte die Zaehlung erfolgt mit "0" obwohl in Excel die erste Zeile mit "1"
			// beginnt.
			Cell cell = sheet.getCell(rowNum, colNum);
			cellData1 = Double.parseDouble(cell.getContents());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		//System.out.println("Zeile=" + rowNum + "Spalte=" + colNum + "Double Wert: " + cellData1);
		return cellData1;
	}
}
