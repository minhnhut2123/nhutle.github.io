package Selenium;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ApiGmail.CommonKeyword;

public class SeleniumUtils {
	private String filepath = "target/cucumber-reports/evidence/";
	private static final Logger log = Logger.getLogger(CommonKeyword.class);
	private int index = 0;
	
	
	public WebDriver createDriver() {
		System.setProperty("webdriver.chrome.driver", "src/test/sources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		return driver;
	}
	
	public void closeBrowser(WebDriver driver) {
		driver.quit();
	}
	
	public void goTo(WebDriver driver, String url) {
		driver.get(url);
	}
	
	public void waitUntilPageContainElement(WebDriver driver, String xpath, int time) {
		WebDriverWait wait = new WebDriverWait(driver,time);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
	}
	
	public void takeScreenshot(WebDriver driver){
		try {
        	String pattern = "ddMMyy_HHmmss";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	String date = simpleDateFormat.format(new Date());
			index ++;
			String screenshotName = "ss" + index + "_" + date + ".png";
//			filepath = filepath + screenshotName;
			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			File DestFile = new File(filepath + screenshotName);
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public void clickElement(WebElement element) {
		element.click();
	}
	
	public void inputText(WebElement element, String text) {
		element.sendKeys(text);
	}
	
	public void sortBy(WebDriver driver, String sortOption) {
		driver.findElement(By.xpath("//div[@id='FilterItemView_sortOrder_dropdown']//span[@data-action='a-dropdown-button']")).click();
		waitUntilPageContainElement(driver, "//div[@id='a-popover-1' and @aria-hidden='false']", 15);
		String sortItemValue = "//div[@id='a-popover-1' and @aria-hidden='false']//li//a[contains(text(),'" + sortOption +"')]";
		
		clickElement(driver.findElement(By.xpath(sortItemValue)));
	}
	
	public void addToCart(WebDriver driver, int quantity) {
		String xpath = "//select[@id='quantity']";
		String quantityXpath = "//select[@id='quantity']//option[@value='" + quantity + "']";
		
		waitUntilPageContainElement(driver, xpath, 10);
		clickElement(driver.findElement(By.xpath(xpath)));
		clickElement(driver.findElement(By.xpath(quantityXpath)));
		clickElement(driver.findElement(By.id("add-to-cart-button")));
	}
	
}
