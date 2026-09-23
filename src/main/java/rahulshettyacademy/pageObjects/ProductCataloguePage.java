package rahulshettyacademy.pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rahulshettyacademy.abstractComponents.AbstractComponents;

public class ProductCataloguePage extends AbstractComponents {
	private static final Logger log = LogManager.getLogger(ProductCataloguePage.class);

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
		log.info("Searching for product: '{}'", productName);
		return getProductList().stream()
				.filter(product -> product.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(productName))
				.findFirst().orElse(null);
	}

//    public void addProductToCart(String productName) {
//        WebElement prod = getProductByName(productName);
//        if (prod != null) {
//            prod.findElement(addToCart).click();
//            waitForElementToAppear(toastMessage);
////            waitForElementToDisappear(spinner);
//            waitForElementToDisappear(spinnerBy);
//        } else {
//            throw new RuntimeException("Product not found in catalogue: " + productName);
//        }
//    }
	public void addProductToCart(String productName) {

		WebElement prod = getProductByName(productName);

		if (prod != null) {

			// Wait for Login Successfully toast to disappear
			By loginSuccessToast = By.cssSelector(".toast-title[aria-label='Login Successfully']");

			waitForElementToDisappear(loginSuccessToast);

			WebElement addToCartButton = prod.findElement(addToCart);

			// Scroll button into the center of the viewport
			((JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", addToCartButton);

			// Wait until button is clickable
			waitForElementToBeClickable(addToCartButton);

			addToCartButton.click();
			log.info("Clicked Add to Cart for '{}'", productName);
			// Wait for Add to Cart confirmation toast
			waitForElementToAppear(toastMessage);

			// Wait for animation/spinner to disappear
			waitForElementToDisappear(spinnerBy);
			log.info("'{}' added to cart successfully", productName);
		} else {
			log.error("Product '{}' not found in catalogue", productName);
			throw new RuntimeException("Product not found in catalogue: " + productName);
		}
	}

	private void waitForElementToBeClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(element));

	}
}