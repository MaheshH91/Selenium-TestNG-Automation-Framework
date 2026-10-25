package com.framework.core;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.framework.pages.CartPage;
import com.framework.pages.OrderHistoryPage;
import com.framework.utils.JavaScriptUtils;
import com.framework.utils.WaitUtils;

public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WaitUtils waitUtils;
    protected final JavaScriptUtils jsUtils;

    @FindBy(css = "[routerlink*='cart']")
    private WebElement cartHeader;

    @FindBy(xpath = "//button[contains(@routerlink, 'myorders')]")
    private WebElement ordersHeader;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver, Duration.ofSeconds(10));
        this.jsUtils = new JavaScriptUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public CartPage navigateToCart() {
        log.info("Navigating to Cart page");
        waitUtils.waitForVisibility(cartHeader);
        jsUtils.scrollToTop();
        cartHeader.click();
        return new CartPage(driver);
    }

    public OrderHistoryPage navigateToOrderHistory() {
        log.info("Navigating to Order History page");
        waitUtils.waitForVisibility(ordersHeader);
        try {
            ordersHeader.click();
        } catch (Exception e) {
            log.warn("Standard navigation click intercepted. Falling back to JavaScript click.");
            jsUtils.click(ordersHeader);
        }
        return new OrderHistoryPage(driver);
    }

    public void waitForElementToDisappear(By locator) {
        waitUtils.waitForInvisibility(locator);
    }
}