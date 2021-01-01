package com.etsy.pageObjects;

import org.hamcrest.MatcherAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class SearchPage {

    @FindBy(id = "global-enhancements-search-query")
    private WebElement searchBar;

    @FindBy(css = "button[data-id=\"gnav-search-submit-button\"")
    private WebElement submitSearch;

    @FindBy(xpath = "//*[@id=\"sortby\"]/button")
    private WebElement sortingOptions;

    @FindBy(xpath = "//*[@id=\"sortby\"]/div/a[4]")
    private WebElement sortByPriceAsc;

    @FindBy(xpath = "//*[@id=\"sortby\"]/div/a[3]")
    private WebElement sortByPriceDesc;

    @FindBy(xpath = "//*[@id=\"content\"]/div/div[1]/div/div/div[3]/div[2]/div[3]/div/div[1]/div/li[*]")
    private List<WebElement> itemList;


    public void searchForItem(String item) {
        searchBar.clear();
        searchBar.sendKeys(item);
        submitSearch.click();
    }

    public void sortResultsPriceAsc(WebDriver driver) {
        while(!driver.getCurrentUrl().contains("&order=price_asc")) {
            sortingOptions.click();
            sortByPriceAsc.click();
        }
    }

    /*As etsy does not seem to offer proper pagination access client-side, and infinite scrolling was removed
    at some point in the past, the cheapest way to grab the most expensive item is to sort by descending price
    */
    public void sortResultsPriceDesc(WebDriver driver) {
        while(!driver.getCurrentUrl().contains("&order=price_desc")) {
            sortingOptions.click();
            sortByPriceDesc.click();
        }
    }

    public ArrayList<WebElement> filterAdsFromItems() {
        Predicate<WebElement> filterStream = item -> (item.findElement(By.className("screen-reader-only")).getText().contains("Ad from shop") || item.findElement(By.className("screen-reader-only")).getText().isBlank());
        ArrayList<WebElement> filteredList = new ArrayList<>(itemList);
        filteredList.removeIf(filterStream);
        return filteredList;
    }

    /*
    Due to etsy's price sorting algorithm being unreliable and putting random lower/higher priced items in the middle of
    their own "sorted" list (and after removing Ad prices), the only reliable way of ensuring prices are sorted correctly is to compare the price
    of the first and last elements, on top of making sure the URL has the correct parameter
    */
    public void validatePrices(List<WebElement> productList, String priceOrder) {
        Float pageFirstProductPrice = Float.valueOf(productList.get(0).findElement(By.className("currency-value")).getText().replaceAll(",", ""));
        Float pageLastProductPrice = Float.valueOf(productList.get(productList.size() - 1).findElement(By.className("currency-value")).getText().replaceAll(",", ""));
        switch(priceOrder) {
            case "ascending":
                assertThat(pageFirstProductPrice, lessThanOrEqualTo(pageLastProductPrice));
                break;
            case "descending":
                assertThat(pageFirstProductPrice, greaterThanOrEqualTo(pageLastProductPrice));
                break;
            default:
                MatcherAssert.assertThat("Incorrect price order selected", false);

        }
    }

}
