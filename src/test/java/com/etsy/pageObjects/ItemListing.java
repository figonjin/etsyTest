package com.etsy.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ItemListing {

    @FindBy(css = "h1[data-buy-box-listing-title]")
    private WebElement listingTitle;

    @FindBy(css = "[id*=\"inventory-variation-select\"]")
    private List<WebElement> variationSelections;

    @FindBy(className = "add-to-cart-form")
    private WebElement addToCartButton;

    public String returnListingTitle() {
        return listingTitle.getText();
    }

    /* Upon selecting a drop-down options, Etsy momentarily blocks the other drop-down lists.
    Therefore the only way to avoid stale elements when the DOM reloads is to directly refer to their path upon intended use, as opposed
    to pre-defining them as seen with other PageObject elements.
    However, since not every product has a drop-down list that requires selection (quantity is ignored,due to
    having a pre-defined value from the start), the code has to be stuck into a try/catch clause to avoid issues
     */
    public void addItemToCart(WebDriver driver) {
        try {
            Predicate<WebElement> variationFilter = filter -> (filter.getAttribute("id").equals("inventory-variation-select-quantity"));
            ArrayList<WebElement> variations = new ArrayList<>(variationSelections);
            variations.removeIf(variationFilter);
            WebDriverWait wait5 = new WebDriverWait(driver, 5);

            for (int i = 0; i < variations.size(); i++) {
                wait5.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//*[@id=\"inventory-variation-select-%d\"]", i))));
                Select test = new Select(driver.findElement(By.xpath(String.format("//*[@id=\"inventory-variation-select-%d\"]", i))));
                test.selectByIndex(1);
            }

        }
        catch (Exception e) {
        }

        /* While the DOM doesn't seem to change the button's path upon selecting drop-down variations
        the button nonetheless gets momentarily blocked when the page reloads with new selections.
        This appeared to be the cheapest way to get the button to press without creating un-necessary issues in the process
         */
        while(!driver.getCurrentUrl().contains("cart")) {
            try {
                addToCartButton.findElement(By.cssSelector("button")).submit();
            }
            catch (StaleElementReferenceException e) {

            }
        }
    }
}
