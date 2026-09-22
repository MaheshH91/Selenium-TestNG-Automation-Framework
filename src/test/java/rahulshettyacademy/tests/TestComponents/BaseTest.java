package rahulshettyacademy.tests.TestComponents;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import rahulshettyacademy.pageObjects.LandingPage;

public class BaseTest {

    private static final ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
    public LandingPage landingPage;
    public Properties prop;

    public static WebDriver getDriver() {
        return tdriver.get();
    }

    public WebDriver initializeDriver() throws IOException {
        prop = new Properties();
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (fis == null) {
                throw new RuntimeException("config.properties file not found on classpath!");
            }
            prop.load(fis);
        }
     // Inside initializeDriver() in BaseTest.java
        String browserName = System.getProperty("browser") != null 
                ? System.getProperty("browser") 
                : prop.getProperty("browser");

        WebDriver driver;
        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (browserName.contains("headless")) {
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
            }
            driver = new ChromeDriver(options);
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else if (browserName.contains("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (browserName.contains("headless")) {
                options.addArguments("-headless");
                options.addArguments("--width=1920");
                options.addArguments("--height=1080");
            }
            driver = new FirefoxDriver(options);
        } else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } else {
            throw new RuntimeException("Unsupported browser: " + browserName);
        }

        int waitTimeout = Integer.parseInt(prop.getProperty("timeout", "10"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTimeout));
        driver.manage().window().maximize();

        tdriver.set(driver);
        return getDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public LandingPage launchApplication() throws IOException {
        WebDriver driver = initializeDriver();
        landingPage = new LandingPage(driver);
        landingPage.goTo(prop.getProperty("url"));
        return landingPage;
    }

    public String getScreenshotBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            tdriver.remove();
        }
    }
}