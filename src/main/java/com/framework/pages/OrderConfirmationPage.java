package com.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.framework.core.BasePage;

public class OrderConfirmationPage extends BasePage {

    @FindBy(css = ".hero-primary")
    private WebElement confirmationHeader;

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public String getConfirmationHeadline() {
        return waitUtils.waitForVisibility(confirmationHeader).getText().trim();
    }
}