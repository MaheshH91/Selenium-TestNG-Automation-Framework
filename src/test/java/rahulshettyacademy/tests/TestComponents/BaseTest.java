package rahulshettyacademy.tests.TestComponents;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import rahulshettyacademy.pageObjects.LandingPage;
import rahulshettyacademy.utils.ConfigReader;

public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    
    // ThreadLocal storage for WebDriver and LandingPage
    private static final ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
    private static final ThreadLocal<LandingPage> tLandingPage = new ThreadLocal<>();
    public LandingPage landingPage;
    public static WebDriver getDriver() {
        return tdriver.get();
    }
    public LandingPage getLandingPage() {
        return tLandingPage.get();
    }

    /**
     * Browser resolution order:
     * 1. Maven CLI property: -Dbrowser=...
     * 2. TestNG XML parameter: <parameter name="browser" value="..." />
     * 3. ConfigReader file property (default fallback)
     */
    public WebDriver initializeDriver(String xmlBrowser) {
        String browserName = System.getProperty("browser") != null
                ? System.getProperty("browser")
                : (xmlBrowser != null ? xmlBrowser : ConfigReader.getProperty("browser", "chrome"));

        browserName = browserName.toLowerCase();
        log.info("Initializing browser: {}", browserName);

        WebDriver driver;
        boolean isHeadless = browserName.contains("headless");

        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (isHeadless) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
            }
            driver = new ChromeDriver(options);
            driver.manage().window().setSize(new Dimension(1920, 1080));
            if (!isHeadless) {
                driver.manage().window().maximize();
            }

        } else if (browserName.contains("firefox")) {
        	FirefoxOptions options = new FirefoxOptions();
            if (isHeadless) {
                options.addArguments("-headless");
                options.addArguments("--width=1920");
                options.addArguments("--height=1080");
            }
            driver = new FirefoxDriver(options);

        } else if (browserName.contains("edge")) {
            EdgeOptions options = new EdgeOptions();
            if (isHeadless) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            driver = new EdgeDriver(options);
            driver.manage().window().setSize(new Dimension(1920, 1080));
            if (!isHeadless) {
                driver.manage().window().maximize();
            }

        } else {
            log.error("Invalid browser specified: {}", browserName);
            throw new RuntimeException("Unsupported browser: " + browserName);
        }

        // Implicit wait kept to 0 or minimal since explicit waits are handled by WaitUtils
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        tdriver.set(driver);
        return driver;
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public LandingPage launchApplication(@Optional String browser) {
        WebDriver driver = initializeDriver(browser);
        landingPage = new LandingPage(driver);
        tLandingPage.set(landingPage);

        String url = ConfigReader.getProperty("url");
        log.info("Navigating to application URL: {}", url);
        landingPage.goTo(url);
        return landingPage;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            log.info("Closing WebDriver session.");
            try {
                getDriver().quit();
            } catch (Exception e) {
                log.warn("Error quitting driver: {}", e.getMessage());
            } finally {
                tdriver.remove();
                tLandingPage.remove();
            }
        }
    }
  
    public String getScreenshotBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        log.info("Loading test data from file: {}", filePath);
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
    }
}