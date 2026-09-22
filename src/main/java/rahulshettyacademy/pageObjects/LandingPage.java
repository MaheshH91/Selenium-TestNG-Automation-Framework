package rahulshettyacademy.pageObjects;

import java.time.Duration;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(userEmail));
        wait.until(ExpectedConditions.visibilityOf(userPassword));

        userEmail.clear();
        userEmail.sendKeys(email);

        userPassword.clear();
        userPassword.sendKeys(password);

        // Scroll login button to the center of viewport
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                loginBtn);

        // Wait until button is visible and enabled
        wait.until(ExpectedConditions.visibilityOf(loginBtn));
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn));

        try {
            loginBtn.click();

        } catch (ElementClickInterceptedException e) {

            // Retry after scrolling again
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center', inline:'center'});",
                    loginBtn);

            wait.until(ExpectedConditions.elementToBeClickable(loginBtn));

            loginBtn.click();
        }

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