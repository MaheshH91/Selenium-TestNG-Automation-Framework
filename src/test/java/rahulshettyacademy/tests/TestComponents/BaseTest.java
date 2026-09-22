package rahulshettyacademy.tests.TestComponents;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
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

    private static final ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
    public LandingPage landingPage;

    public static WebDriver getDriver() {
        return tdriver.get();
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
        WebDriver driver;

        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (browserName.contains("headless")) {
                options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
            }
            driver = new ChromeDriver(options);
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else if (browserName.contains("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (browserName.contains("headless")) {
                options.addArguments("-headless", "--width=1920", "--height=1080");
            }
            driver = new FirefoxDriver(options);
        } else if (browserName.contains("edge")) {
            driver = new EdgeDriver();
        } else {
            throw new RuntimeException("Unsupported browser: " + browserName);
        }

        int waitTimeout = ConfigReader.getIntProperty("timeout", 10);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTimeout));
        driver.manage().window().maximize();

        tdriver.set(driver);
        return getDriver();
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public LandingPage launchApplication(@Optional String browser) {
        WebDriver driver = initializeDriver(browser);
        landingPage = new LandingPage(driver);
        landingPage.goTo(ConfigReader.getProperty("url"));
        return landingPage;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            tdriver.remove();
        }
    }

    public String getScreenshotBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Utility method for TestNG @DataProvider methods to convert JSON data files
     * into a List of HashMaps.
     */
    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
    }
}