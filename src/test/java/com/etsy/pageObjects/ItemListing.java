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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

    public void addItemToCart(WebDriver driver) {
        try {
            Predicate<WebElement> variationFilter = filter -> (filter.getAttribute("id").equals("inventory-variation-select-quantity"));
            ArrayList<WebElement> variations = new ArrayList<>(variationSelections);
            variations.removeIf(variationFilter);
            WebDriverWait wait10 = new WebDriverWait(driver, 10);

            for (int i = 0; i < variations.size(); i++) {
                wait10.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//*[@id=\"inventory-variation-select-%d\"]", i))));
                Select test = new Select(driver.findElement(By.xpath(String.format("//*[@id=\"inventory-variation-select-%d\"]", i))));
                test.selectByIndex(1);
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }
        while(!driver.getCurrentUrl().contains("cart")) {
            try {
                addToCartButton.findElement(By.cssSelector("button")).submit();
            }
            catch (StaleElementReferenceException e) {

            }
        }
    }
}
