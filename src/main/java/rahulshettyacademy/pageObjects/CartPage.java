package rahulshettyacademy.pageObjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class CartPage extends AbstractComponents {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".cartSection h3")
    private List<WebElement> productTitles;

    @FindBy(css = ".totalRow button")
    private WebElement checkOutEle;

    public boolean verifyProductDisplay(String productName) {
        return productTitles.stream()
                .anyMatch(cartProduct -> cartProduct.getText().equalsIgnoreCase(productName));
    }

    public CheckOutPage goToCheckout() {
        jsUtils.clickElementByJS(checkOutEle);
        return new CheckOutPage(driver);
    }
}