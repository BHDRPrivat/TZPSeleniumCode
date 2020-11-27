package Utils;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilsApachePoi {

	static XSSFWorkbook workbook;
	static String projectPath;
	static XSSFSheet sheet;

	// Constructor aufrufen, damit Variablen übergeben werden können
	public ExcelUtilsApachePoi(String excelPath, String sheetName) {

		try {

			workbook = new XSSFWorkbook(excelPath);
			sheet = workbook.getSheet(sheetName);
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
			// Anzahl der Spalten in Excel
			colCount = sheet.getRow(0).getPhysicalNumberOfCells();
			System.out.println("Anzahl Spalten: " + colCount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		return colCount;
	}

	public static int getRowCount() {
		int rowCount = 0;
		try {
			// Anzahl der Zeilen in Excel
			rowCount = sheet.getPhysicalNumberOfRows();
			System.out.println("Anzahl Zeilen: " + rowCount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		return rowCount;
	}

	public static String getExcelDataString(int rowNum, int colNum) {
		String cellData = null;
		try {

			// Beachte die Z�hlung erfolgt mit "0" obwohl in Excel die erste Zeile mit "1"
			// beginnt.
			cellData = sheet.getRow(rowNum).getCell(colNum).getStringCellValue();

			System.out.println("String Wert: " + cellData);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		return cellData;
	}

	public static Double getExcelDataNumber(int rowNum, int colNum) {
		Double cellData1 = null;
		try {

			// Beachte die Z�hlung erfolgt mit "0" obwohl in Excel die erste Zeile mit "1"
			// beginnt.
			cellData1 = sheet.getRow(rowNum).getCell(colNum).getNumericCellValue();

			System.out.println("Nummerischer Wert: " + cellData1);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		return cellData1;
	}

}
