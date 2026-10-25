package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.pages.CartPage;
import com.framework.pages.ProductCatalogPage;

public class AuthenticationTest extends BaseTest {

    @Test(groups = {"ErrorHandling", "Regression"}, description = "Validates error banner on incorrect credentials")
    public void verifyInvalidCredentialsError() {
        getLoginPage().login("invalid_user@example.com", "InvalidKey#123");
        String actualError = getLoginPage().getToastErrorMessage();
        String expectedError = "Incorrect email or password.";

        Assert.assertEquals(actualError, expectedError, "Unexpected error banner payload received.");
    }

    @Test(groups = {"ErrorHandling"}, description = "Verifies product cross-contamination doesn't occur in Cart")
    public void verifyCartItemIsolation() {
        String validItem = "ZARA COAT 3";
        String invalidItem = "NON_EXISTENT_PRODUCT_XYZ";

        ProductCatalogPage catalogPage = getLoginPage().login("mahesh1233@gmail.com", "Mahesh@123");
        catalogPage.addProductToCart(validItem);

        CartPage cartPage = catalogPage.navigateToCart();
        Assert.assertFalse(cartPage.isProductInCart(invalidItem), 
                "Phantom product was identified inside the active cart: " + invalidItem);
    }
}