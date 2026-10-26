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

		// Wait until the Cart header link is fully clickable
		waitUtils.waitForClickability(cartHeader);
		jsUtils.scrollToTop();

		try {
			cartHeader.click();
		} catch (Exception e) {
			log.warn("Cart icon click intercepted, falling back to JS click");
			jsUtils.click(cartHeader);
		}

		// Ensure browser navigated to the cart route
		new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
				.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/dashboard/cart"));

		return new CartPage(driver);
	}

	/*
	 * public OrderHistoryPage navigateToOrderHistory() {
	 * log.info("Navigating to Order History page");
	 * waitUtils.waitForVisibility(ordersHeader); try { ordersHeader.click(); }
	 * catch (Exception e) { log.
	 * warn("Standard navigation click intercepted. Falling back to JavaScript click."
	 * ); jsUtils.click(ordersHeader); } return new OrderHistoryPage(driver); }
	 */

	public void waitForElementToDisappear(By locator) {
		waitUtils.waitForInvisibility(locator);
	}
	public OrderHistoryPage navigateToOrderHistory() {
        log.info("Navigating to Order History page");
        waitUtils.waitForClickability(ordersHeader);
        jsUtils.scrollToTop();
        
        try {
            ordersHeader.click();
        } catch (Exception e) {
            log.warn("Standard navigation click intercepted. Falling back to JavaScript click: {}", e.getMessage());
            jsUtils.click(ordersHeader);
        }

        // Wait until route transition finishes
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/dashboard/myorders"));

        return new OrderHistoryPage(driver);
    }
}