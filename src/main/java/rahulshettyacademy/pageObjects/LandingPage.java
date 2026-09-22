package rahulshettyacademy.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class LandingPage extends AbstractComponents {

    public LandingPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "userEmail")
    private WebElement userEmail;

    @FindBy(id = "userPassword")
    private WebElement userPassword;

    @FindBy(id = "login")
    private WebElement loginBtn;

    @FindBy(css = "[class*='toast-message']")
    private WebElement errorMessage;

    public ProductCataloguePage loginApplication(String email, String password) {
        userEmail.sendKeys(email);
        userPassword.sendKeys(password);
        loginBtn.click();
        return new ProductCataloguePage(driver);
    }

    public String getMeErrorMessage() {
        waitForElementToAppear(errorMessage);
        return errorMessage.getText();
    }

    public void goTo(String url) {
        driver.get(url);
    }
}