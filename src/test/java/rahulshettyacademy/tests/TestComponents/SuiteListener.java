package rahulshettyacademy.tests.TestComponents;


import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import rahulshettyacademy.resources.ExtentReporterNG;

public class SuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        System.out.println("========== Starting Suite: " + suite.getName() + " ==========");
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("========== Completed Suite: " + suite.getName() + " ==========");
        openReport();
    }

    private void openReport() {
        try {
            if (GraphicsEnvironment.isHeadless() || !Desktop.isDesktopSupported()) {
                return;
            }
            String reportPath = ExtentReporterNG.getReportPath();
            if (reportPath != null && !reportPath.isEmpty()) {
                File reportFile = new File(reportPath);
                if (reportFile.exists()) {
                    Desktop.getDesktop().browse(reportFile.toURI());
                }
            }
        } catch (Exception e) {
            System.err.println("Could not auto-open report: " + e.getMessage());
        }
    }
}