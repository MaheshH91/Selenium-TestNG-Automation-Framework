package rahulshettyacademy.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import rahulshettyacademy.abstractComponents.AbstractComponents;

public class ConfirmationPage extends AbstractComponents {

    public ConfirmationPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".hero-primary")
    private WebElement confirmationEle;

    public String getConfirmationMessage() {
        return confirmationEle.getText().trim();
    }
}