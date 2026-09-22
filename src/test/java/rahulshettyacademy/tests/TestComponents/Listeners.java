package rahulshettyacademy.tests.TestComponents;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private static final AtomicBoolean reportOpened = new AtomicBoolean(false);

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();

		// Distinguish parameterized data provider tests in the report
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
		if (extentTest.get() == null) {
			ExtentTest test = extent.createTest(result.getMethod().getMethodName());
			extentTest.set(test);
		}

		Throwable skipReason = result.getThrowable();
		extentTest.get().log(Status.SKIP,
				"Test skipped: " + (skipReason != null ? skipReason.getMessage() : "No exception provided"));

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
			// Do not open report on headless CI/CD environments
			if (GraphicsEnvironment.isHeadless()) {
				return;
			}

			if (!Desktop.isDesktopSupported()) {
				return;
			}

			// Atomic check BEFORE opening: ensures only the first finished thread triggers the browser
			if (reportOpened.getAndSet(true)) {
				return;
			}

			// Get the exact timestamp-based report path
			String reportPath = ExtentReporterNG.getReportPath();

			if (reportPath == null || reportPath.isEmpty()) {
				System.err.println("Extent Report path is not available.");
				return;
			}

			File reportFile = new File(reportPath);

			if (reportFile.exists()) {
				Desktop.getDesktop().browse(reportFile.toURI());
				System.out.println("Extent Report opened: " + reportFile.getAbsolutePath());
			} else {
				System.err.println("Extent Report not found: " + reportFile.getAbsolutePath());
			}

		} catch (Exception e) {
			System.err.println("Could not open Extent Report automatically: " + e.getMessage());
		}
	}
}