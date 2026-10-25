package com.framework.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.framework.pages.LoginPage;

public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    private static final ThreadLocal<WebDriver> DRIVER_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<LoginPage> LOGIN_PAGE_HOLDER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return DRIVER_HOLDER.get();
    }

    public LoginPage getLoginPage() {
        return LOGIN_PAGE_HOLDER.get();
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional String browser) {
        WebDriver driver = DriverFactory.createInstance(browser);
        DRIVER_HOLDER.set(driver);

        LoginPage loginPage = new LoginPage(driver);
        LOGIN_PAGE_HOLDER.set(loginPage);

        loginPage.openApplication();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            WebDriver driver = DRIVER_HOLDER.get();
            if (driver != null) {
                log.info("Terminating active WebDriver session");
                driver.quit();
            }
        } catch (Exception e) {
            log.warn("Exception during driver termination: {}", e.getMessage());
        } finally {
            // Guarantee removal of ThreadLocals even if setup or driver.quit() threw an exception
            DRIVER_HOLDER.remove();
            LOGIN_PAGE_HOLDER.remove();
        }
    }

    public String captureScreenshotAsBase64() {
        WebDriver driver = getDriver();
        if (driver instanceof TakesScreenshot takesScreenshot) {
            try {
                return takesScreenshot.getScreenshotAs(OutputType.BASE64);
            } catch (Exception e) {
                log.warn("Unable to capture base64 screenshot: {}", e.getMessage());
            }
        }
        return null;
    }
}