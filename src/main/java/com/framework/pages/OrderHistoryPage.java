package com.framework.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.framework.core.BasePage;

public class OrderHistoryPage extends BasePage {

    @FindBy(css = "tr td:nth-child(3)")
    private List<WebElement> tableItemTitles;

    private final By orderTableRowsLocator = By.cssSelector("tr td:nth-child(3)");

    public OrderHistoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOrderPresent(String targetProductName) {
        log.info("Verifying existence of product in orders history: '{}'", targetProductName);
        
        try {
            // Wait until table rows are visible and populated by the backend API
            waitUtils.waitForVisibility(orderTableRowsLocator);
        } catch (Exception e) {
            log.warn("Timed out waiting for order rows to populate in history table: {}", e.getMessage());
            return false;
        }

        return tableItemTitles.stream()
                .anyMatch(cell -> cell.getText().trim().equalsIgnoreCase(targetProductName.trim()));
    }
}