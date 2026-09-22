package rahulshettyacademy.pageObjects;

import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class OrderPage extends AbstractComponents {
	public OrderPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(this.driver, driver);
	}
	@FindBy(css =  "tr td:nth-child(3)")
	private List<WebElement> productNames;
	
	@FindBy(css = ".totalRow button")
	WebElement checkoutEle;
	
	public boolean verifyOrderDisplay(String productName) {
		Boolean match = productNames.stream()
				.anyMatch(product -> product.getText().equalsIgnoreCase(productName));
		return match;

	}
	

}
