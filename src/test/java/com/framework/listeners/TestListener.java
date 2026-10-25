package com.framework.listeners;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.framework.base.BaseTest;
import com.framework.reporting.ExtentManager;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);
    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> TEST_HOLDER = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            testName += " - " + Arrays.deepToString(parameters);
        }

        log.info("===> Starting execution: {}", testName);
        ExtentTest test = extent.createTest(testName);
        TEST_HOLDER.set(test);
        test.log(Status.INFO, "Execution initiated.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("===> Test Passed: {}", result.getMethod().getMethodName());
        ExtentTest test = TEST_HOLDER.get();
        if (test != null) {
            test.log(Status.PASS, "Test executed successfully.");
        }
        TEST_HOLDER.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("===> Test Failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        ExtentTest test = TEST_HOLDER.get();
        if (test == null) {
            test = extent.createTest(result.getMethod().getMethodName());
            TEST_HOLDER.set(test);
        }

        test.log(Status.FAIL, result.getThrowable());

        // Extract driver directly from BaseTest ThreadLocal safely
        WebDriver driver = BaseTest.getDriver();
        if (driver instanceof TakesScreenshot takesScreenshot) {
            try {
                String base64Image = takesScreenshot.getScreenshotAs(OutputType.BASE64);
                if (base64Image != null && !base64Image.isBlank()) {
                    test.addScreenCaptureFromBase64String(base64Image, "Failure Evidence");
                }
            } catch (Exception e) {
                test.log(Status.WARNING, "Failed to capture screenshot on failure: " + e.getMessage());
            }
        }
        TEST_HOLDER.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("===> Test Skipped: {}", result.getMethod().getMethodName());
        ExtentTest test = TEST_HOLDER.get();
        if (test == null) {
            test = extent.createTest(result.getMethod().getMethodName());
            TEST_HOLDER.set(test);
        }
        test.log(Status.SKIP, result.getThrowable());
        TEST_HOLDER.remove();
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("Beginning Test Suite run: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Completed Test Suite run: {}", context.getName());
        extent.flush();
    }
}