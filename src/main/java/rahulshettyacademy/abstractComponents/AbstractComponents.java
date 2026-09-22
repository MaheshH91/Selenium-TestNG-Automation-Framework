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

public class AbstractComponents {
    protected WebDriver driver;

    public AbstractComponents(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[routerlink*='cart']")
    private WebElement cartHeader;

    @FindBy(css = "[routerlink*='myorders']")
    private WebElement orderHeader;

    public CartPage goToCartPage() {
        cartHeader.click();
        return new CartPage(driver);
    }

    public OrderPage goToOrdersPage() {
        orderHeader.click();
        return new OrderPage(driver);
    }

    public void waitForElementToAppear(By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToAppear(WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToDisappear(WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.invisibilityOf(element));
    }

    public void scrollDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,200)");
    }
}