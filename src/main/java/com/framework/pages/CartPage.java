package com.framework.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.framework.core.BasePage;

public class CartPage extends BasePage {

    @FindBy(css = ".cartSection h3")
    private List<WebElement> lineItemTitles;

    @FindBy(css = ".totalRow button")
    private WebElement proceedToCheckoutButton;

    private final By cartItemsLocator = By.cssSelector(".cartSection h3");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductInCart(String expectedProductName) {
        // Wait until at least one cart item or the cart container is visible
        try {
            waitUtils.waitForVisibility(cartItemsLocator);
        } catch (Exception e) {
            log.warn("Timed out waiting for cart line items to appear: {}", e.getMessage());
            return false;
        }

        return lineItemTitles.stream()
                .anyMatch(item -> item.getText().trim().equalsIgnoreCase(expectedProductName.trim()));
    }

    public CheckoutPage proceedToCheckout() {
        log.info("Proceeding to Checkout page");
        waitUtils.waitForClickability(proceedToCheckoutButton);
        jsUtils.click(proceedToCheckoutButton);
        return new CheckoutPage(driver);
    }
}