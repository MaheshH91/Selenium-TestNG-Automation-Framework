package rahulshettyacademy.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class CheckOutPage extends AbstractComponents {

    public CheckOutPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".action__submit")
    private WebElement submitButton;

    @FindBy(css = "[placeholder='Select Country']")
    private WebElement countryTextbox;

    private final By resultList = By.cssSelector(".ta-results");
    private final By countryOptionsBy = By.xpath("//button[contains(@class,'ta-item')]");

    public void selectCountry(String countryName) {
        new Actions(driver).sendKeys(countryTextbox, countryName).build().perform();
        waitForElementToAppear(resultList);

        List<WebElement> options = driver.findElements(countryOptionsBy);
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(countryName)) {
                jsUtils.clickElementByJS(option);
                break;
            }
        }
    }

    public ConfirmationPage submitOrder() {
        jsUtils.clickElementByJS(submitButton);
        return new ConfirmationPage(driver);
    }
}