package com.framework.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.framework.core.BasePage;

public class ProductCatalogPage extends BasePage {

    @FindBy(css = ".mb-3")
    private List<WebElement> catalogCards;

    private final By catalogCardsBy = By.cssSelector(".mb-3");
    private final By productTitleBy = By.cssSelector("b");
    private final By addToCartButtonBy = By.cssSelector(".card-body button:last-of-type");
    private final By toastNotificationBy = By.cssSelector("#toast-container");
    private final By spinnerOverlayBy = By.cssSelector(".ng-animating");
    private final By loginSuccessToastBy = By.cssSelector(".toast-title[aria-label='Login Successfully']");

    public ProductCatalogPage(WebDriver driver) {
        super(driver);
    }

    public List<WebElement> getProductCatalog() {
        waitUtils.waitForAllVisible(catalogCardsBy);
        return catalogCards;
    }

    public WebElement locateProductCard(String productName) {
        log.info("Locating product card for item: '{}'", productName);
        return getProductCatalog().stream()
                .filter(card -> card.findElement(productTitleBy).getText().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);
    }

    public void addProductToCart(String productName) {
        WebElement productCard = locateProductCard(productName);
        if (productCard == null) {
            throw new IllegalStateException("Product not found in catalog: " + productName);
        }

        waitUtils.waitForInvisibility(loginSuccessToastBy);

        WebElement addBtn = productCard.findElement(addToCartButtonBy);
        jsUtils.scrollIntoViewCenter(addBtn);
        waitUtils.waitForClickability(addBtn).click();

        waitUtils.waitForVisibility(toastNotificationBy);
        waitUtils.waitForInvisibility(spinnerOverlayBy);
        log.info("Product successfully committed to cart: '{}'", productName);
    }
}