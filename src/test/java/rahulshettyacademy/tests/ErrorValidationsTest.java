package rahulshettyacademy.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import rahulshettyacademy.pageObjects.CartPage;
import rahulshettyacademy.pageObjects.ProductCataloguePage;
import rahulshettyacademy.tests.TestComponents.BaseTest;

public class ErrorValidationsTest extends BaseTest {

    @Test(groups = { "ErrorHandling", "Regression" })
    public void loginErrorValidation() throws IOException {
        landingPage.loginApplication("holkar@gmail.com", "Mahesh@1323");

        String actualErrorMessage = landingPage.getMeErrorMessage();
        String expectedErrorMessage = "Incorrect email or password.";

        Assert.assertEquals(actualErrorMessage, expectedErrorMessage,
                "Login error message mismatch. Expected: " + expectedErrorMessage + ", but got: " + actualErrorMessage);
    }

    @Test(groups = { "ErrorHandling" })
    public void productErrorValidation() throws IOException {
        String expectedProduct = "ZARA COAT 3";
        String incorrectProduct = "ZARA COAT 33";

        ProductCataloguePage productCatalogue = landingPage.loginApplication("mahesh1233@gmail.com", "Mahesh@123");
        productCatalogue.addProductToCart(expectedProduct);

        CartPage cartPage = productCatalogue.goToCartPage();
        boolean isIncorrectProductPresent = cartPage.verifyProductDisplay(incorrectProduct);

        Assert.assertFalse(isIncorrectProductPresent, "Unexpected product found in cart: " + incorrectProduct);
    }
}