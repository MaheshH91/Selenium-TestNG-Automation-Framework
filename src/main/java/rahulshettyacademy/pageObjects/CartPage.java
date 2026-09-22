package rahulshettyacademy.pageObjects;

import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class CartPage extends AbstractComponents {


	public CartPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(this.driver, driver);
	}

	@FindBy(css = ".cartSection h3")
	private List<WebElement> productTitles;

	@FindBy(css = ".totalRow button")
	WebElement checkOutEle;

	public boolean verifyProductDisplay(String productName) {
		Boolean match = productTitles.stream()
				.anyMatch(cartProduct -> cartProduct.getText().equalsIgnoreCase(productName));
		return match;

	}

	public CheckOutPage goToCheckout() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", checkOutEle);
		return new CheckOutPage(driver);

	}

}
