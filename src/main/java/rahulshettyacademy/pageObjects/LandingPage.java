package rahulshettyacademy.pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class LandingPage extends AbstractComponents{


	public LandingPage(WebDriver driver) {
		super(driver);
	}

	
	@FindBy(id="userEmail")
	WebElement userEmail;
	
	@FindBy(id="userPassword")
	WebElement userPassword;
	
	@FindBy(id="login")
	WebElement loginBtn;
	
	@FindBy(css ="[class*='toast-message']")
	WebElement errorMessage;
	
	public ProductCataloguePage loginApplication(String email, String password) {
		userEmail.sendKeys(email);
		userPassword.sendKeys(password);
		loginBtn.click();
		ProductCataloguePage productCatalogue = new ProductCataloguePage(driver);
		return productCatalogue;
	}

	public String getMeErrorMessage() {
		waitForElementToAppear(errorMessage);
		return errorMessage.getText();
	
	}
	public void goTo(String url) {
	    driver.get(url);
	}
}
