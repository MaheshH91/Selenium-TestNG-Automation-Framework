package rahulshettyacademy.tests;

import java.io.IOException;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import rahulshettyacademy.data.TestDataProvider;
import rahulshettyacademy.pageObjects.CartPage;
import rahulshettyacademy.pageObjects.CheckOutPage;
import rahulshettyacademy.pageObjects.ConfirmationPage;
import rahulshettyacademy.pageObjects.OrderPage;
import rahulshettyacademy.pageObjects.ProductCataloguePage;
import rahulshettyacademy.tests.TestComponents.BaseTest;

public class SubmitOrderTest extends BaseTest {

    private static final String SUCCESS_MESSAGE = "THANKYOU FOR THE ORDER.";

    @Test(
        dataProvider = "purchaseOrderData",
        dataProviderClass = TestDataProvider.class,
        groups = { "Purchase", "Regression" },
        priority = 1,
        description = "Places an end-to-end order and verifies confirmation text"
    )
    public void submitOrder(HashMap<String, String> input) throws IOException, InterruptedException {
        ProductCataloguePage productCatalogue = landingPage.loginApplication(
            input.get("email"),
            input.get("password")
        );

        productCatalogue.getProductList();
        productCatalogue.addProductToCart(input.get("product"));

        CartPage cartPage = productCatalogue.goToCartPage();
        boolean isProductInCart = cartPage.verifyProductDisplay(input.get("product"));
        Assert.assertTrue(isProductInCart, "Product not found in cart: " + input.get("product"));

        CheckOutPage checkOutPage = cartPage.goToCheckout();
        checkOutPage.selectCountry("India");

        ConfirmationPage confirmationPage = checkOutPage.submitOrder();
        String confirmationMessage = confirmationPage.getConfirmationMessage();

        // Using SoftAssert if you check multiple UI elements on the confirmation screen
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(
            confirmationMessage.equalsIgnoreCase(SUCCESS_MESSAGE),
            "Confirmation message mismatch. Received: " + confirmationMessage
        );
        softAssert.assertAll();
    }

    @Test(
        dataProvider = "purchaseOrderData",
        dataProviderClass = TestDataProvider.class,
        dependsOnMethods = { "submitOrder" },
        groups = { "Purchase" },
        priority = 2,
        description = "Verifies placed product exists in Orders History page"
    )
    public void orderHistoryTest(HashMap<String, String> input) {
        ProductCataloguePage productCatalogue = landingPage.loginApplication(
            input.get("email"),
            input.get("password")
        );

        OrderPage orderPage = productCatalogue.goToOrdersPage();
        boolean isOrderPresent = orderPage.verifyOrderDisplay(input.get("product"));

        Assert.assertTrue(isOrderPresent, "Order for product '" + input.get("product") + "' was not found in history.");
    }
}