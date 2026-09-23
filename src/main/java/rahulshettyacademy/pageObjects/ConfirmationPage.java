package rahulshettyacademy.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class ConfirmationPage extends AbstractComponents {

	public ConfirmationPage(WebDriver driver) {
		super(driver);
	}
	private final By confirmationBy = By.cssSelector(".hero-primary");
	@FindBy(css = ".hero-primary")
	private WebElement confirmationEle;

	public String getConfirmationMessage() {
		waitForElementToAppear(confirmationBy);
		return confirmationEle.getText().trim();
	}
}