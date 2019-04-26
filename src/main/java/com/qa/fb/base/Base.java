package com.qa.fb.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Base {

	public static WebDriver driver;
	public static Properties prop;

	public static WebDriver init_driver(String browserName) {
		if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "E:/WebAutomation/Scenarios/chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browserName.equalsIgnoreCase("FireFox")) {
			driver = new FirefoxDriver();
		}

		return driver;
	}

	public Properties init_properties() {
		prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream(
					"F:/eclipse/workspace/KeywordFrame/src/main/java/com/qa/fb/config/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;

	}

	public void takeScreenshot(String testMethodName) {
		DateFormat DF = new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss");
		Date D = new Date();
		String time = DF.format(D);
		String ErrSS = prop.getProperty("user.dir") + testMethodName + time + ".jpg";
		File srcfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(srcfile, new File(ErrSS));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void explicitwaitForClick(WebDriver driver, WebElement locator, int timeout)
	{
		new WebDriverWait(driver,timeout).ignoring(ElementNotVisibleException.class).until(ExpectedConditions.elementToBeClickable(locator));
				
	}
	public void explicitwaitForVisibile(WebDriver driver, WebElement locator, int timeout)
	{
		new WebDriverWait(driver,timeout).ignoring(ElementNotVisibleException.class).until(ExpectedConditions.visibilityOf(locator));
				
	}
	
}
