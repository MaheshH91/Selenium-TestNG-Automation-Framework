package rahulshettyacademy.tests.TestComponents;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import rahulshettyacademy.resources.ExtentReporterNG;

public class Listeners implements ITestListener {

    private static final ExtentReports extent = ExtentReporterNG.getReportObject();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        // If the test receives DataProvider parameters, append them to distinguish tests in report
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            testName += " - " + Arrays.deepToString(params);
        }

        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
        extentTest.get().log(Status.INFO, "Test execution started.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test executed successfully.");
        extentTest.remove(); // Prevent ThreadLocal memory leaks
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
        extentTest.get().fail(result.getThrowable());

        WebDriver driver = BaseTest.getDriver();
        Object instance = result.getInstance();

        if (driver != null && instance instanceof BaseTest) {
            try {
                String base64Screenshot = ((BaseTest) instance).getScreenshotBase64(driver);
                extentTest.get().addScreenCaptureFromBase64String(base64Screenshot, "Failure Snapshot");
            } catch (Exception e) {
                extentTest.get().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            }
        }
        extentTest.remove(); // Prevent ThreadLocal memory leaks
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // If a test skipped before onTestStart (e.g. @BeforeMethod failure), initialize it
        if (extentTest.get() == null) {
            ExtentTest test = extent.createTest(result.getMethod().getMethodName());
            extentTest.set(test);
        }

        Throwable skipReason = result.getThrowable();
        extentTest.get().log(Status.SKIP, "Test skipped: " +
                (skipReason != null ? skipReason.getMessage() : "No exception provided"));

        extentTest.remove(); // Prevent ThreadLocal memory leaks
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        extentTest.get().log(Status.WARNING, "Test failed but within success percentage.");
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println(">>> Test suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        displayExtentReport();
        System.out.println(">>> Test suite finished: " + context.getName());
    }

    public void displayExtentReport() {
        try {
            // Check that we are not running on a headless CI/CD environment
            if (!GraphicsEnvironment.isHeadless() && Desktop.isDesktopSupported()) {
                String reportPath = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "index.html";
                File reportFile = new File(reportPath);
                if (reportFile.exists()) {
                    Desktop.getDesktop().browse(reportFile.toURI());
                    System.out.println("Extent Report opened: " + reportPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Could not open Extent Report automatically: " + e.getMessage());
        }
    }
}