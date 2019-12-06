package StepDefinitions;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Selenium.SeleniumUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SeleniumTesting {

	private static final Logger log = Logger.getLogger(SeleniumTesting.class);
	private WebDriver driver = null;
	private SeleniumUtils seleniumUtils = new SeleniumUtils();
	String homePageUrl = "";
	String firstItemPrice = "";
	String secondItemPrice = "";



//	@After
//	private void close() {
//		driver.quit();
//	}

	@Given("User goes to Amazon with url {string}")
	public void user_goes_to_Amazon_with_url(String url) throws Exception {
		homePageUrl = url;
		driver = seleniumUtils.createDriver();
		seleniumUtils.goTo(driver, url);
	   seleniumUtils.takeScreenshot(driver);

	}

	@When("Select {string} menu")
	public void select_menu(String dealMenu) {
		driver.findElement(By.linkText(dealMenu)).click();
		driver.findElement(By.className("gbh1-bold")).isEnabled();
		assertEquals(driver.findElement(By.className("gbh1-bold")).getText(), dealMenu);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Sorts the items by {string}")
	public void sorts_the_items_by(String sortItem) {
		seleniumUtils.sortBy(driver,sortItem);
		String sortItemAfterSelected = "//div[@id='FilterItemView_sortOrder_dropdown']//span[@class='a-dropdown-prompt' and normalize-space(text()='" + sortItem + "')]";
		seleniumUtils.waitUntilPageContainElement(driver, sortItemAfterSelected, 15);
		assertEquals(sortItem,driver.findElement(By.xpath(sortItemAfterSelected)).getText());
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Click on button {string}")
	public void click_on_button(String buttonName) {
		String xpath = "(//span[@data-action='gbdeal-actionrecord']//a[normalize-space(text()='" + buttonName + "')])[1]";
		seleniumUtils.waitUntilPageContainElement(driver, xpath, 10);
		seleniumUtils.clickElement(driver.findElement(By.xpath(xpath)));
		seleniumUtils.waitUntilPageContainElement(driver, "//h1[contains(text(),'Discount applied in prices displayed')]", 10);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Click on first deal item")
	public void click_on_item_first_deal() {
		String xpath = "(//div[@id='octopus-dlp-asin-stream']//li//a)[1]";
		String buttonAddToCart = "//span[@id='submit.add-to-cart']//span[@id='submit.add-to-cart-announce']";
		seleniumUtils.waitUntilPageContainElement(driver, xpath, 10);
		seleniumUtils.clickElement(driver.findElement(By.xpath(xpath)));
		seleniumUtils.waitUntilPageContainElement(driver, buttonAddToCart, 10);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Add {int} items into cart")
	public void add_items_into_cart(int quantity) {
		firstItemPrice = driver.findElement(By.id("price_inside_buybox")).getText().trim();
		seleniumUtils.addToCart(driver,quantity);
	}

	@Then("Verify {int} items added into cart")
	public void verify_items_added_into_cart(int quantity) {
		String addedStatus = "//div[@id='huc-v2-order-row-confirm-text']//h1[normalize-space(text()='Added to Cart')]";
		String addedItem = "//div[@id='hlb-subcart']//span[contains(text(),'" + quantity + " items')]";
		seleniumUtils.waitUntilPageContainElement(driver, addedStatus, 10);
		seleniumUtils.waitUntilPageContainElement(driver, addedItem, 10);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Go back to main page")
	public void go_back_to_main_page() {
		seleniumUtils.goTo(driver, homePageUrl);
		seleniumUtils.waitUntilPageContainElement(driver, "//div[@class='a-carousel-viewport']", 10);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Search for {string}")
	public void search_for(String stringSearch) {
		String xpathTextSearch = "//span[@data-component-type='s-result-info-bar']//span[contains(text(),'" + stringSearch + "')]";
		WebElement textbox = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
		WebElement searchBtn = driver.findElement(By.xpath("//form[@name='site-search']//input[@type='submit']"));
	
		seleniumUtils.inputText(textbox, stringSearch);
		seleniumUtils.clickElement(searchBtn);
		seleniumUtils.waitUntilPageContainElement(driver, xpathTextSearch, 10);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Sort the items by {string}")
	public void sort_the_items_by(String sortOption) {
		driver.findElement(By.xpath("//span[@aria-label='Sort by:']//span[@class='a-dropdown-prompt']")).click();
		seleniumUtils.waitUntilPageContainElement(driver, "//div[@id='a-popover-2' and @aria-hidden='false']", 15);
		String sortItemValue = "//div[@id='a-popover-2' and @aria-hidden='false']//li//a[contains(text(),'" + sortOption
				+ "')]";

		seleniumUtils.clickElement(driver.findElement(By.xpath(sortItemValue)));
		String sortItemAfterSelected = "//span[@class='a-dropdown-prompt']";
		seleniumUtils.waitUntilPageContainElement(driver, sortItemAfterSelected, 15);
		assertEquals(sortOption, driver.findElement(By.xpath(sortItemAfterSelected)).getText());
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@When("Click on the {string} product")
	public void click_on_the_first_product(String status) {
		WebElement firstProduct = driver.findElement(By.xpath("//span[text()='"+status+"']/ancestor::div[@class='sg-row']/following-sibling::div[@class='sg-row']/div//h2//a"));
		String productName = firstProduct.getText();
		seleniumUtils.clickElement(firstProduct);
		
		seleniumUtils.waitUntilPageContainElement(driver, "//span[@id='productTitle']", 15);
		String titleProduct = driver.findElement(By.xpath("//span[@id='productTitle']")).getText().trim();
		assertEquals(productName, titleProduct);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@When("Add {int} items into the cart")
	public void add_items_into_the_cart(int quantity) {
		WebElement radioOnTimeBuy = driver.findElement(By.id("oneTimeBuyBox"));
		secondItemPrice = driver.findElement(By.xpath("//div[@id='oneTimeBuyBox']//span[@class='a-color-price']")).getText().trim();
		seleniumUtils.clickElement(radioOnTimeBuy);
		seleniumUtils.addToCart(driver,quantity);
	}


	@When("Go to Cart")
	public void go_to_Cart() {
		seleniumUtils.clickElement(driver.findElement(By.id("nav-cart")));
	}

	@Then("Verify the individual price of each item")
	public void verify_the_individual_price_of_each_item() {
		int count = driver.findElements(By.xpath("//form[@id='activeCartViewForm']//div[@data-asin]")).size();
			
		for(int i=1; i<=count; i++) {
			WebElement item = driver.findElement(By.xpath("(//form[@id='activeCartViewForm']//div[@data-asin])["+i+"]//span[contains(@class,'sc-product-price')]"));
			if(i==1) {
				assertEquals(secondItemPrice, item.getText().trim());
			} else
				assertEquals(firstItemPrice, item.getText().trim());
		}
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("Verify the subtotal price of all item")
	public void verify_the_subtotal_price_of_all_item() throws InterruptedException {
		int subTotal = 0;
		int count = driver.findElements(By.xpath("//form[@id='activeCartViewForm']//div[@data-asin and not(contains(@data-removed,'true'))]")).size();
		
		for(int i=1; i<=count; i++) {
			String price = driver.findElement(By.xpath("(//form[@id='activeCartViewForm']//div[@data-asin and not(contains(@data-removed,'true'))])["+i+"]")).getAttribute("data-price");
			String quantity = driver.findElement(By.xpath("(//form[@id='activeCartViewForm']//div[@data-asin and not(contains(@data-removed,'true'))])["+i+"]")).getAttribute("data-quantity");
			subTotal = subTotal + (Integer.parseInt(price) * Integer.parseInt(quantity));
		}
		
		Thread.sleep(1000);
		seleniumUtils.waitUntilPageContainElement(driver, "//span[@id='sc-subtotal-amount-activecart']//span", 15);
		String subTotalUI = driver.findElement(By.xpath("//span[@id='sc-subtotal-amount-activecart']//span")).getText().trim();
		subTotalUI = subTotalUI.replace(".00", "");
		subTotalUI = subTotalUI.replace(",", "");
		
    	String subTotalFortmat = Integer.toString(subTotal);
    	subTotalFortmat = "$" + subTotalFortmat;
    	
    	assertEquals(subTotalFortmat, subTotalUI);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Edit the first item quantity to {int}")
	public void edit_the_first_item_quantity_to(int quantity) throws InterruptedException {
		seleniumUtils.waitUntilPageContainElement(driver, "//span[@id='a-autoid-0']", 15);
		seleniumUtils.clickElement(driver.findElement(By.id("a-autoid-0-announce")));
		seleniumUtils.waitUntilPageContainElement(driver, "//div[@aria-hidden='false']", 15);
		String quantityXpath1 = "//div[@aria-hidden='false']//li//a[contains(text(),'" + quantity + "')]";
		seleniumUtils.clickElement(driver.findElement(By.xpath(quantityXpath1)));

		String quantityItemAfterSelected = "//span[@class='a-dropdown-prompt']";
		seleniumUtils.waitUntilPageContainElement(driver, quantityItemAfterSelected, 15);
		assertEquals(quantity, Integer.parseInt(driver.findElement(By.xpath(quantityItemAfterSelected)).getText().trim()));
		Thread.sleep(1000);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@When("Edit the second item quantity to {int}")
	public void edit_the_second_item_quantity_to(int quantity) throws InterruptedException {
		seleniumUtils.waitUntilPageContainElement(driver, "//span[@id='a-autoid-2']", 15);
		seleniumUtils.clickElement(driver.findElement(By.id("a-autoid-2-announce")));
		seleniumUtils.waitUntilPageContainElement(driver, "//div[@aria-hidden='false']", 15);
		String quantityXpath2 = "//div[@aria-hidden='false']//li//a[contains(text(),'" + quantity + "')]";
		seleniumUtils.clickElement(driver.findElement(By.xpath(quantityXpath2)));

		String quantityItemAfterSelected = "(//form[@id='activeCartViewForm']//div[@data-asin])[2]//span[@class='a-dropdown-prompt']";
		seleniumUtils.waitUntilPageContainElement(driver, quantityItemAfterSelected, 15);
		assertEquals(quantity, Integer.parseInt(driver.findElement(By.xpath(quantityItemAfterSelected)).getText().trim()));
		Thread.sleep(1000);
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@When("delete the {string} item")
	public void delete_the_item(String string) throws InterruptedException {
		WebElement deteleBtn = driver.findElement(By.xpath("(//form[@id='activeCartViewForm']//div[@data-asin])[1]//span[contains(@class,'sc-action-delete')]//input"));
		seleniumUtils.clickElement(deteleBtn);
		Thread.sleep(1000);
	}


	@When("Click Proceed to Checkout button")
	public void click_Proceed_to_Checkout_button() {
		seleniumUtils.waitUntilPageContainElement(driver, "//input[@name='proceedToCheckout']", 15);
	    seleniumUtils.clickElement(driver.findElement(By.xpath("//input[@name='proceedToCheckout']")));
	}

	@Then("Verify {string} form displays")
	public void verify_form_displays(String title) {
		seleniumUtils.waitUntilPageContainElement(driver, "//form[@name='signIn']//h1", 15);
		assertEquals(title, driver.findElement(By.xpath("//form[@name='signIn']//h1")).getText().trim());
		try {
			seleniumUtils.takeScreenshot(driver);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		seleniumUtils.closeBrowser(driver);
	}

}
