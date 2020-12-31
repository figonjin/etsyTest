package com.etsy.tests;

import com.etsy.pageObjects.ItemCart;
import com.etsy.pageObjects.ItemListing;
import com.etsy.pageObjects.PrivacyPolicy;
import com.etsy.pageObjects.SearchPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class EtsyCartTest {

    private WebDriver driver;

    private final SearchPage searchPagePO = new SearchPage();
    private final PrivacyPolicy gdprOverlayPO = new PrivacyPolicy();
    private final ItemCart itemCartPO = new ItemCart();
    private final ItemListing itemListingPO = new ItemListing();


    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                Paths.get("src/test/resources/chromedriver_win32/chromedriver.exe").toString());
        if(driver == null) {
            driver = new ChromeDriver();
        }

        PageFactory.initElements(driver, searchPagePO);
        PageFactory.initElements(driver, gdprOverlayPO);
        PageFactory.initElements(driver, itemListingPO);
        PageFactory.initElements(driver, itemCartPO);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @Given("User navigates to {string}")
    public void navigateToEtsy(String url) {
        driver.navigate().to(url);
        gdprOverlayPO.acceptGdpr(driver);
    }

    @When("User searches for {string}")
    public void searchForItem(String keyword) {
        searchPagePO.searchForItem(keyword);
    }


    /*
    Due to etsy's price sorting algorithm being unreliable and putting random lower/higher priced items in the middle of
    their own "sorted" list (and after removing Ad prices), the only reliable way of ensuring prices are sorted correctly is to compare the price
    of the first and last elements, on top of making sure the URL has the correct parameter
    */
    @And("User sorts by ascending prices")
    public void sortProductsByAscPrice() {
        searchPagePO.sortResultsPriceAsc(driver);
        List<WebElement> filteredList = searchPagePO.filterAdsFromItems();
        searchPagePO.validatePrices(filteredList, "ascending");
    }

    /*As etsy does not seem to offer proper pagination access client-side, and infinite scrolling was removed
    at some point in the past, the most reasonable way to grab the most expensive item is to sort by descending price
     */
    @And("User adds the most expensive {string} to the cart")
    public void addExpensiveItem(String keyword) {
        searchPagePO.sortResultsPriceDesc(driver);
        List<WebElement> filteredList = searchPagePO.filterAdsFromItems();
        searchPagePO.validatePrices(filteredList, "descending");

        driver.navigate().to(filteredList.get(0).findElement(By.cssSelector("a[data-listing-id]")).getAttribute("href"));

        itemCartPO.addItemToTestList(itemListingPO.returnListingTitle());
        itemListingPO.addItemToCart(driver);
    }

    @And("User adds any {string} to the cart")
    public void addAnyItem(String keyword) {
        List<WebElement> filteredList = searchPagePO.filterAdsFromItems();
        Random randomItem = new Random();

        driver.navigate().to(filteredList.get(randomItem.nextInt(filteredList.size())).findElement(By.cssSelector("a[data-listing-id]")).getAttribute("href"));

        itemCartPO.addItemToTestList(itemListingPO.returnListingTitle());
        itemListingPO.addItemToCart(driver);
    }

    @Then("the added items should be listed in the cart")
    public void validateItemsInCart() {
        itemCartPO.validateCartItems();
    }
}
