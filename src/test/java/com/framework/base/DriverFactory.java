package com.framework.base;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.framework.utils.ConfigReader;

public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {}

    public static WebDriver createInstance(String browserParam) {
        String browser = (System.getProperty("browser") != null)
                ? System.getProperty("browser")
                : (browserParam != null ? browserParam : ConfigReader.getProperty("browser", "chrome"));

        browser = browser.trim().toLowerCase();
        boolean isHeadless = browser.contains("headless");
        log.info("Instantiating WebDriver session for browser: [{}], headless: [{}]", browser, isHeadless);

        WebDriver driver;

        if (browser.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("profile.password_manager_leak_detection", false);
            options.setExperimentalOption("prefs", prefs);
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            options.addArguments("--disable-notifications", "--remote-allow-origins=*");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            if (isHeadless) {
                options.addArguments(
                    "--headless=new",
                    "--window-size=1920,1080",
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--disable-gpu"
                );
            }
            driver = new ChromeDriver(options);

        } else if (browser.contains("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            if (isHeadless) {
                options.addArguments("-headless", "--width=1920", "--height=1080");
            }
            driver = new FirefoxDriver(options);

        } else if (browser.contains("edge")) {
            EdgeOptions options = new EdgeOptions();
            Map<String, Object> edgePrefs = new HashMap<>();
            edgePrefs.put("credentials_enable_service", false);
            edgePrefs.put("profile.password_manager_enabled", false);
            edgePrefs.put("profile.password_manager_leak_detection", false);
            options.setExperimentalOption("prefs", edgePrefs);
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            options.addArguments("--disable-notifications");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            if (isHeadless) {
                options.addArguments(
                    "--headless=new",
                    "--window-size=1920,1080",
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--disable-gpu"
                );
            }
            driver = new EdgeDriver(options);

        } else {
            throw new IllegalArgumentException("Unsupported browser type supplied: " + browser);
        }

        // Apply viewport resolution on the driver instance
        if (isHeadless) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else {
            driver.manage().window().maximize();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        return driver;
    }
}