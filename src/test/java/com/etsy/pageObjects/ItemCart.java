package com.etsy.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ItemCart {

    private ArrayList<String> boughtItems = new ArrayList<>();

    @FindBy(css = "div[data-checkout-header]")
    private WebElement checkoutHeader;

    @FindBy(css = "a[data-title]")
    private List<WebElement> itemNames;


    public void addItemToTestList(String item) {
        boughtItems.add(0, item);
    }

    public void validateCartItems() {
        int itemsInCart = Integer.parseInt(checkoutHeader.getText().split(" ")[0]);
        assertThat(itemsInCart, equalTo(boughtItems.size()));
        for (int i = 0; i < boughtItems.size(); i++) {
            assertThat(itemNames.get(i).getText(), equalTo(boughtItems.get(i)));
        }
    }
}
