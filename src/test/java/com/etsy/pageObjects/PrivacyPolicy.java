package com.etsy.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class PrivacyPolicy {

    @FindBy(xpath = "//*[@id=\"gdpr-single-choice-overlay\"]/div/div[2]/div[2]/button")
    private WebElement gdprAcceptButton;

    /*As there was occasional failure on accepting the privacy policy using the webdriver's click method, I have opted
    to use Actions instead, as it appears to be circumventing the issue
    */
    public void acceptGdpr(WebDriver driver) {
        Actions click = new Actions(driver);
        click.moveToElement(gdprAcceptButton).click();
        click.perform();
    }
}
