package AlteLeipziger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class TEstmitJXLExcelzugriff {

 WebDriver driver;

 @Test(priority = 1)
 public void login() throws InterruptedException {


  System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\chromedriver.exe"); 
  driver = new ChromeDriver();
  // driver.manage().window().maximize();
  driver.get("http://www.wikishown.com");
  System.out.println("Browser is opened and wikishown url in entered.");

  driver.manage().window().maximize();
  Thread.sleep(2000);

  driver.findElement(By.id("userId")).sendKeys("xxxxxxxxx");
  driver.findElement(By.id("passId")).sendKeys("xxxxxxxx");
  driver.findElement(By.id("submit")).click();

  System.out.println("Wikishown My Account page is loaded");
 }

 //Calling Excel file input column names using String

 @Test(priority = 2, dataProvider = "Cust_Reg")
 public void customer_regis(String Mobileno, String Firstname, String Lastname) throws Exception,
  BiffException, IOException {

   driver.findElement(By.name("searchMobileNo")).clear();
   driver.findElement(By.name("searchMobileNo")).sendKeys(Mobileno);

   Thread.sleep(3000);
   driver.findElement(By.id("searchBtn")).click();

   System.out.println("search button is clicked");
   Thread.sleep(2000);

   driver.findElement(By.id("fname")).sendKeys(Firstname);
   driver.findElement(By.id("lname")).sendKeys(Lastname);

   driver.findElement(By.id("validateBtn")).click();
   System.out.println("Validate Button is clicked");
  }

 //Identification of Excel file
 @DataProvider(name = "Cust_Reg")
 public Object[][] CustRegistration() throws Exception {
 // FileInputStream filepath = new FileInputStream("F:\\BHDR\\Zwischen\\Testdata.xlsx");
 // FileInputStream filepath = new FileInputStream("C:/Zwischen/Testdata.xlsx");
  
  FileInputStream filepath = new FileInputStream("C:\\Zwischen\\Testdata.xls"); 
	 
  Workbook wb = Workbook.getWorkbook(filepath);
  Sheet sheet = wb.getSheet("Tabelle1");

  int row = sheet.getRows();
  System.out.println("number of rows" + row);
  int column = sheet.getColumns();
  System.out.println("number of columns" + column);
  String Testdata[][] = new String[row - 1][column];
  int count = 0;

  for (int i = 1; i < row; i++) {
   for (int j = 0; j < column; j++) {
    Cell cell = sheet.getCell(j, i);
    Testdata[count][j] = cell.getContents();
   }
   count++;
  }
  filepath.close();
  return Testdata;
 }
}
