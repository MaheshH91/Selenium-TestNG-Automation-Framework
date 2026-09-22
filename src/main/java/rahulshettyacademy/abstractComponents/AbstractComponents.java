package rahulshettyacademy.abstractComponents;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import rahulshettyacademy.pageObjects.CartPage;
import rahulshettyacademy.pageObjects.OrderPage;
import rahulshettyacademy.utils.JavaScriptUtils;
import rahulshettyacademy.utils.WaitUtils;

public class AbstractComponents {

    protected WebDriver driver;
    protected WaitUtils waitUtils;
    protected JavaScriptUtils jsUtils;

    public AbstractComponents(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver, Duration.ofSeconds(10));
        this.jsUtils = new JavaScriptUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[routerlink*='cart']")
    private WebElement cartHeader;

    @FindBy(css = "[routerlink*='myorders']")
    private WebElement orderHeader;

    public CartPage goToCartPage() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for Cart element
        wait.until(ExpectedConditions.visibilityOf(cartHeader));

        // Move page to the absolute top
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, 0);"
        );

        // Wait until Cart is displayed and enabled
        wait.until(driver -> {
            try {
                return cartHeader.isDisplayed()
                        && cartHeader.isEnabled();
            } catch (Exception e) {
                return false;
            }
        });

        // Verify Cart position is inside viewport
        wait.until(driver -> {
            try {
                Long y = (Long) ((JavascriptExecutor) driver)
                        .executeScript(
                                "return Math.round(arguments[0].getBoundingClientRect().top);",
                                cartHeader);

                return y >= 0;
            } catch (Exception e) {
                return false;
            }
        });
        Long y = (Long) ((JavascriptExecutor) driver)
                .executeScript(
                        "return Math.round(arguments[0].getBoundingClientRect().top);",
                        cartHeader);

        System.out.println("Cart Y position before click: " + y);
        cartHeader.click();

        return new CartPage(driver);
    }
    public OrderPage goToOrdersPage() {
        orderHeader.click();
        return new OrderPage(driver);
    }

    public void waitForElementToAppear(By locator) {
        waitUtils.waitForVisibility(locator);
    }

    public void waitForElementToAppear(WebElement element) {
        waitUtils.waitForVisibility(element);
    }

    public void waitForElementToDisappear(WebElement element) {
        waitUtils.waitForInvisibility(element);
    }
    public void waitForElementToDisappear(By locator) {
        waitUtils.waitForInvisibility(locator);
    }
    public void scrollDown() {
        jsUtils.scrollByPixels(0, 200);
    }
}