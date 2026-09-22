package rahulshettyacademy.pageObjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class OrderPage extends AbstractComponents {

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "tr td:nth-child(3)")
    private List<WebElement> productNames;

    public boolean verifyOrderDisplay(String productName) {
        return productNames.stream()
                .anyMatch(product -> product.getText().trim().equalsIgnoreCase(productName.trim()));
    }
}