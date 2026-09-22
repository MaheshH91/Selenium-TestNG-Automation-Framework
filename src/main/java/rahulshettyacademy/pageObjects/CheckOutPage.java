package rahulshettyacademy.pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class CheckOutPage extends AbstractComponents {

    public CheckOutPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".action__submit")
    private WebElement submitButton;

    @FindBy(css = "[placeholder='Select Country']")
    private WebElement countryTextbox;

    private By resultList = By.cssSelector(".ta-results");
    private By countryOptionsBy = By.xpath("//button[contains(@class,'ta-item')]");

    public void selectCountry(String countryName) {
        // 1. Enter the country name in the input box
        new Actions(driver).sendKeys(countryTextbox, countryName).build().perform();

        // 2. Wait until suggestion container is visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(resultList));

        // 3. Find matching option dynamically and click using JavaScript to prevent interception
        List<WebElement> options = driver.findElements(countryOptionsBy);
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(countryName)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
                break;
            }
        }
    }

    public ConfirmationPage submitOrder() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        return new ConfirmationPage(driver);
    }
}