package com.framework.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptUtils {

    private final JavascriptExecutor js;

    public JavaScriptUtils(WebDriver driver) {
        this.js = (JavascriptExecutor) driver;
    }

    public void click(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    public void scrollIntoViewCenter(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", element);
    }

    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void scrollByPixels(int x, int y) {
        js.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public Long getBoundingClientRectTop(WebElement element) {
        return (Long) js.executeScript("return Math.round(arguments[0].getBoundingClientRect().top);", element);
    }
}