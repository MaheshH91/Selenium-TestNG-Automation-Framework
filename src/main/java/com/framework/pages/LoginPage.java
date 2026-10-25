package com.framework.pages;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.framework.core.BasePage;
import com.framework.utils.ConfigReader;

public class LoginPage extends BasePage {

    @FindBy(id = "userEmail")
    private WebElement emailInput;

    @FindBy(id = "userPassword")
    private WebElement passwordInput;

    @FindBy(id = "login")
    private WebElement submitLoginButton;

    @FindBy(css = "[class*='toast-message']")
    private WebElement alertToastMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public ProductCatalogPage login(String email, String password) {
        log.info("Executing user authentication for: {}", email);
        waitUtils.waitForVisibility(emailInput).clear();
        emailInput.sendKeys(email);

        waitUtils.waitForVisibility(passwordInput).clear();
        passwordInput.sendKeys(password);

        jsUtils.scrollIntoViewCenter(submitLoginButton);
        waitUtils.waitForClickability(submitLoginButton);

        try {
            submitLoginButton.click();
        } catch (ElementClickInterceptedException e) {
            log.warn("Normal click failed on login submit. Attempting JavaScript execution.");
            jsUtils.click(submitLoginButton);
        }

        return new ProductCatalogPage(driver);
    }

    public String getToastErrorMessage() {
        return waitUtils.waitForVisibility(alertToastMessage).getText().trim();
    }

    public void openApplication(String url) {
        log.info("Loading portal URL: {}", url);
        driver.get(url);
    }

    public void openApplication() {
        String baseUrl = ConfigReader.getProperty("url");
        openApplication(baseUrl);
    }
}