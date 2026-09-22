package rahulshettyacademy.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    public static String captureBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public static String captureToFile(WebDriver driver, String testCaseName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String directory = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "screenshots";
        String filePath = directory + File.separator + testCaseName + "_" + timestamp + ".png";

        try {
            Path targetDir = Paths.get(directory);
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
        return filePath;
    }
}