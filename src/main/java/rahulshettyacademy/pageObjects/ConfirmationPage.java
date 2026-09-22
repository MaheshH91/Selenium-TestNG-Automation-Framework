package rahulshettyacademy.pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class ConfirmationPage extends AbstractComponents {

	public ConfirmationPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".hero-primary")
	WebElement confirmationEle;

	public boolean verifyConfirmMessage() {
		String confirmationMessage = confirmationEle.getText();
		boolean match = confirmationMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER.");
		return match;
	}

	public String getConfirmationMessage() {
		return confirmationEle.getText();
	}

}
