package com.framework.reporting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public final class ExtentManager {

    private static ExtentReports extent;
    private static String reportFilePath;

    private ExtentManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportDir = System.getProperty("user.dir") + File.separator + "reports";
            File directory = new File(reportDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            reportFilePath = reportDir + File.separator + "ExecutionReport_" + timestamp + ".html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportFilePath);

            spark.config().setDocumentTitle("Test Execution Report");
            spark.config().setReportName("Enterprise Web UI Automation Suite");
            spark.config().setTheme(Theme.DARK);
            spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss z");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Automation Framework", "Selenium 4 / TestNG");
            extent.setSystemInfo("Java Runtime", System.getProperty("java.version"));
            extent.setSystemInfo("Host OS", System.getProperty("os.name"));
            extent.setSystemInfo("Environment", "QA");
        }
        return extent;
    }

    public static String getReportPath() {
        return reportFilePath;
    }
}