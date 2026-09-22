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

    	    // Wait until Cart is present
    	    wait.until(ExpectedConditions.visibilityOf(cartHeader));

    	    // Scroll Cart into the center of the viewport
    	    ((JavascriptExecutor) driver).executeScript(
    	            "arguments[0].scrollIntoView({block:'center', inline:'center'});",
    	            cartHeader
    	    );

    	    // Wait until it becomes clickable
    	    wait.until(ExpectedConditions.elementToBeClickable(cartHeader));

    	    // Normal Selenium click
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