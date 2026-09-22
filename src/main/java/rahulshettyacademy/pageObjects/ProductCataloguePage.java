package rahulshettyacademy.pageObjects;

import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class ProductCataloguePage  extends AbstractComponents {


	public ProductCataloguePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(this.driver, driver);
	}

	@FindBy(css  = ".mb-3")
	List<WebElement> products;

	By productsBy = By.cssSelector(".mb-3");
	By addToCart = By.cssSelector(".card-body button:last-of-type");
	By toastMessage = By.cssSelector("#toast-container");
	
	@FindBy(css = ".ng-animating")
	WebElement spinner;
	
	
	public List<WebElement> getProductList(){
		waitForElementToAppear(productsBy);
		return products;
		
	}
	public WebElement getProductByName(String productName) {
		WebElement prod = products.stream()
		.filter(product -> product.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst()
		.orElse(null);
		return prod;
		
	}
	public void addProductToCart(String productName) {
		WebElement prod = getProductByName(productName);
		prod.findElement(addToCart).click();
		waitForElementToAppear(toastMessage);
		waitForElementToDisappear(spinner);
		
	}

	

}
