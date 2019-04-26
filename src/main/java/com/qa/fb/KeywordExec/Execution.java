package com.qa.fb.KeywordExec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.fb.base.Base;
import com.sun.org.apache.xpath.internal.functions.Function;

public class Execution {
	protected WebDriver driver;
	Properties prop;
	Base base;
	WebElement element;

	public static XSSFWorkbook book;
	public static XSSFSheet sheet;
	public final String Scenario_Sheet = "F:/eclipse/workspace/KeywordFrame/src/main/java/com/qa/fb/testscenarios/Test_Scenario.xlsx";

	public void Test_scenario_Execution(String sheetname) throws IOException, InterruptedException {

		String locatorValue = null;
		String locatorID = null;
		File file = new File(Scenario_Sheet);
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			book = new XSSFWorkbook(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sheet = book.getSheet(sheetname);
		int k = 0;
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			locatorID = sheet.getRow(i + 1).getCell(k + 1).toString().trim();

			if (!locatorID.equalsIgnoreCase("NA")) {
				locatorValue = sheet.getRow(i + 1).getCell(k + 2).toString().trim();
			}

			String action = sheet.getRow(i + 1).getCell(k + 3).toString().trim();
			String value = sheet.getRow(i + 1).getCell(k + 4).toString().trim();

			switch (action) {
			case "open browser":
				base = new Base();
				prop = base.init_properties();
				if (value.isEmpty() || value.equals("NA")) {
					driver = base.init_driver(prop.getProperty("browser"));

				} else {
					driver = base.init_driver(value);
				}
				break;
			case "enter url":
				prop = base.init_properties();
				if (value.isEmpty() || value.equals("NA")) {
					driver.get(prop.getProperty("url"));

				} else {
					driver.get(value);
				}
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				break;
				
			case "SwitchToWindow":
				
				Set<String> handler = driver.getWindowHandles();
				Iterator<String> ite =handler.iterator();
				String parents = ite.next();
				while(ite.hasNext())
				{
					String child= ite.next();
					driver.switchTo().window(child).getTitle().equalsIgnoreCase(value);								
				}
				driver.manage().window().maximize();
			    break;
			    
			case "Alert":
				int timeout= 20;
				new WebDriverWait(driver,timeout).ignoring(NoAlertPresentException.class).until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();				
				if(value=="No")
				{
					alert.dismiss();
				}
				else
					alert.accept();
				
				
	/*		case "Fleunt Wait":
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
				.withTimeout(30, TimeUnit.SECONDS) 			
				.pollingEvery(5, TimeUnit.SECONDS) 			
				.ignoring(NoSuchElementException.class);
		WebElement clickseleniumlink = wait.until(new Function<WebDriver, WebElement>(){
		
			public WebElement apply(WebDriver driver ) {
				return driver.findElement(By.xpath("/html/body/div[1]/section/div[2]/div/div[1]/div/div[1]/div/div/div/div[2]/div[2]/div/div/div/div/div[1]/div/div/a/i"));
			}
		});
		*/
			case "quit":
				driver.close();

			default:
				break;

			}
			switch (locatorID) {
			case "id":
				element = driver.findElement(By.id(locatorValue));
				
				if (action.equals("sendkeys")){
					base.explicitwaitForClick(driver, element, 20);
					element.sendKeys(value);
				}
				else if (action.equals("click"))
					element.click();
				else if (action.equals("VisibleByText")) {
					Select select = new Select(element);
					select.selectByVisibleText(value);
				}
				else if (action.equals("VisibleByValue")) {
					Select select = new Select(element);
					select.selectByValue(value);
					Thread.sleep(5000);
				}
				else if (action.equals("SelectFrame")){
					int size = driver.findElements(By.tagName("iframe")).size();
					for(int j=0;j<size;j++)
			
					{
						driver.switchTo().frame(i);
						int total=driver.findElements(By.id(locatorValue)).size();
						System.out.println(total);
						if(total==1){
							driver.switchTo().frame(i);
							driver.findElement(By.id(locatorValue)).click();
						break;		
						}
					}
					
				}
				else if(action.equals("mousehover")){
					Actions action1 = new Actions(driver);
				action1.moveToElement(element).build().perform();
				}
				
				locatorID = null;
				break;
			case "name":
				element = driver.findElement(By.name(locatorValue));
				if (action.equals("sendkeys"))
					element.sendKeys(value);
				else if (action.equals("click"))
					element.click();
				else if (action.equals("VisibleByText")) {
					Select select = new Select(element);
					select.selectByVisibleText(value);
				}
				else if (action.equals("VisibleByValue")) {
					Select select = new Select(element);
					select.selectByValue(value);
					Thread.sleep(5000);
				}
					else if (action.equals("SelectFrame")){
						int size = driver.findElements(By.tagName("iframe")).size();
						for(int j=0;j<size;j++)
				
						{
							driver.switchTo().frame(i);
							int total=driver.findElements(By.name(locatorValue)).size();
							System.out.println(total);
							if(total==1){
								driver.switchTo().frame(i);
								driver.findElement(By.xpath(locatorValue)).click();
							break;		
							
						}
						}
						}
						else if(action.equals("mousehover")){
							Actions action1 = new Actions(driver);
						action1.moveToElement(element).build().perform();
						}
				
				locatorID = null;
				break;
			case "xpath":
				
				element = driver.findElement(By.xpath(locatorValue));
				Thread.sleep(2000);
				if (action.equals("sendkeys"))
					element.sendKeys(value);
				else if (action.equals("click"))
					element.click();
				else if (action.equals("VisibleByText")) {
					Select select = new Select(element);
					select.selectByValue(value);
					
				}
					else if (action.equals("VisibleByValue")) {
						Select select = new Select(element);
						select.selectByValue(locatorValue);
						Thread.sleep(5000);
				}
					else if (action.equals("SelectFrame")){
						int size = driver.findElements(By.tagName("iframe")).size();
						System.out.println(size);
						for(int j=0;j<size;j++)
				
						{
							driver.switchTo().frame(i);
							int total=driver.findElements(By.xpath(locatorValue)).size();
							System.out.println(total);
							if(total==1){
								driver.switchTo().frame(i);
								driver.findElement(By.xpath(locatorValue)).click();
							break;		
							}
						}
						
					}
					else if(action.equals("mousehover")){
						Actions action1 = new Actions(driver);
					action1.moveToElement(element).build().perform();
					}
					else if(action.equals("Date Picker")){
				JavascriptExecutor js = ((JavascriptExecutor)driver); 
				js.executeScript("arguments[0].setAttribute('value','"+value+"');", element);
				Thread.sleep(6000);
					}
				locatorID= null;
				break;
			case "LinkText":
              element = driver.findElement(By.linkText(locatorValue));
				if (action.equals("sendkeys"))
					element.sendKeys(value);
				else if (action.equals("click"))
					element.click();
				else if (action.equals("VisibleByText")) {
					Select select = new Select(element);
					select.selectByValue(value);
					
				}
					else if (action.equals("VisibleByValue")) {
						Select select = new Select(element);
						select.selectByValue(locatorValue);
						Thread.sleep(5000);
				}
					else if (action.equals("SelectFrame")){
						int size = driver.findElements(By.tagName("iframe")).size();
						System.out.println(size);
						for(int j=0;j<size;j++)
				
						{
							driver.switchTo().frame(j);
							int total=driver.findElements(By.linkText(locatorValue)).size();
							System.out.println(total);
							if(total==1){
								driver.findElement(By.linkText(locatorValue)).click();
							break;	
							
							
							}
							
						}
						driver.switchTo().defaultContent();
						
					}
					else if(action.equals("mousehover")){
						Actions action1 = new Actions(driver);
					action1.moveToElement(element).build().perform();
					}
						
				locatorID = null;
				break;

			}
		}
	}
}
