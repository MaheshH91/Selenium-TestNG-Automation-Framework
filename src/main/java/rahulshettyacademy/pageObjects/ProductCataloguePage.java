package rahulshettyacademy.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class ProductCataloguePage extends AbstractComponents {

    public ProductCataloguePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".mb-3")
    private List<WebElement> products;

    @FindBy(css = ".ng-animating")
    private WebElement spinner;

    private final By productsBy = By.cssSelector(".mb-3");
    private final By addToCart = By.cssSelector(".card-body button:last-of-type");
    private final By toastMessage = By.cssSelector("#toast-container");
    private final By spinnerBy = By.cssSelector(".ng-animating");
    public List<WebElement> getProductList() {
        waitForElementToAppear(productsBy);
        return products;
    }

    public WebElement getProductByName(String productName) {
        return getProductList().stream()
                .filter(product -> product.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);
    }

    public void addProductToCart(String productName) {
        WebElement prod = getProductByName(productName);
        if (prod != null) {
            prod.findElement(addToCart).click();
            waitForElementToAppear(toastMessage);
//            waitForElementToDisappear(spinner);
            waitForElementToDisappear(spinnerBy);
        } else {
            throw new RuntimeException("Product not found in catalogue: " + productName);
        }
    }
}