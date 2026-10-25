package com.framework.listeners;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.framework.reporting.ExtentManager;

public class SuiteListener implements ISuiteListener {

    private static final Logger log = LogManager.getLogger(SuiteListener.class);
    private static final AtomicBoolean REPORT_OPENED = new AtomicBoolean(false);

    @Override
    public void onStart(ISuite suite) {
        log.info("Suite Execution Started: {}", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("Suite Execution Finalized: {}", suite.getName());
        openExecutionReport();
    }

    private void openExecutionReport() {
        if (GraphicsEnvironment.isHeadless() || !Desktop.isDesktopSupported()) {
            log.info("Headless or non-desktop environment detected. Skipping automatic report launch.");
            return;
        }

        if (REPORT_OPENED.compareAndSet(false, true)) {
            String reportPath = ExtentManager.getReportPath();
            if (reportPath != null) {
                File file = new File(reportPath);
                if (file.exists()) {
                    try {
                        Desktop.getDesktop().browse(file.toURI());
                    } catch (Exception e) {
                        log.warn("Unable to trigger local browser for report: {}", e.getMessage());
                    }
                }
            }
        }
    }
}