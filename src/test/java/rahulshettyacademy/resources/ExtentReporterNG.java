package rahulshettyacademy.resources;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReporterNG {

    private static String reportPath;
    
    public static synchronized ExtentReports getReportObject() {

        // Timestamp for unique report
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        // Ensure reports directory exists
        String reportDirectory = System.getProperty("user.dir") + File.separator + "reports";
        File dir = new File(reportDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Assign directly to the static class field (removed the local 'String' declaration)
        reportPath = reportDirectory + File.separator + "AutomationReport_" + timeStamp + ".html";

        // Spark Reporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        sparkReporter.config().setDocumentTitle("Automation Test Results");
        sparkReporter.config().setReportName("Web Automation Execution Report");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a ('zzz')");

        // Extent Reports Object
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // System Information
        extent.setSystemInfo("Project Name", "Rahul Shetty Academy API & Web Automation");
        extent.setSystemInfo("Tester", "Mahesh Holkar");
        extent.setSystemInfo("Organization", "SelfSpace Software");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Automation Tool", "Selenium WebDriver");
        extent.setSystemInfo("Framework", "TestNG");
        extent.setSystemInfo("Language", "Java");
        extent.setSystemInfo("Build Tool", "Maven");
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("OS Version", System.getProperty("os.version"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("Browser", "Chrome");

        return extent;
    }

    public static String getReportPath() {
        return reportPath;
    }
}