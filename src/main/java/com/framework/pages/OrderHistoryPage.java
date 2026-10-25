package com.framework.pages;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.framework.core.BasePage;

public class OrderHistoryPage extends BasePage {

    @FindBy(css = "tr td:nth-child(3)")
    private List<WebElement> tableItemTitles;

    public OrderHistoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOrderPresent(String targetProductName) {
        return tableItemTitles.stream()
                .anyMatch(cell -> cell.getText().trim().equalsIgnoreCase(targetProductName.trim()));
    }
}