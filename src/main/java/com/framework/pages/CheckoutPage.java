package com.framework.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.framework.core.BasePage;

public class CheckoutPage extends BasePage {

    @FindBy(css = "[placeholder='Select Country']")
    private WebElement countryInputField;

    @FindBy(css = ".action__submit")
    private WebElement submitOrderButton;

    private final By autosuggestResultsBy = By.cssSelector(".ta-results");
    private final By autosuggestItemsBy = By.xpath("//button[contains(@class,'ta-item')]");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void selectDestinationCountry(String countryName) {
        log.info("Selecting destination country: {}", countryName);
        new Actions(driver).sendKeys(countryInputField, countryName).perform();
        waitUtils.waitForVisibility(autosuggestResultsBy);

        List<WebElement> options = driver.findElements(autosuggestItemsBy);
        WebElement matchingOption = options.stream()
                .filter(opt -> opt.getText().trim().equalsIgnoreCase(countryName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Target country option not found: " + countryName));

        jsUtils.click(matchingOption);
    }

    public OrderConfirmationPage submitOrder() {
        log.info("Submitting order transaction");
        waitUtils.waitForClickability(submitOrderButton);
        jsUtils.click(submitOrderButton);
        return new OrderConfirmationPage(driver);
    }
}