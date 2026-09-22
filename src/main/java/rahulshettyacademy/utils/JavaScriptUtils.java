package rahulshettyacademy.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptUtils {

    private final JavascriptExecutor js;

    public JavaScriptUtils(WebDriver driver) {
        this.js = (JavascriptExecutor) driver;
    }

    public void clickElementByJS(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollByPixels(int x, int y) {
        js.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public void flashElement(WebElement element) {
        String originalColor = element.getCssValue("backgroundColor");
        for (int i = 0; i < 2; i++) {
            changeColor("rgb(255, 230, 0)", element);
            changeColor(originalColor, element);
        }
    }

    private void changeColor(String color, WebElement element) {
        js.executeScript("arguments[0].style.backgroundColor = '" + color + "'", element);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}
    }
}