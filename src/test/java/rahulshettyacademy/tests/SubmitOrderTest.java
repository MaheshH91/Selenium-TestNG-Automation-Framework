package rahulshettyacademy.tests;

import java.io.IOException;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import rahulshettyacademy.data.TestDataProvider;
import rahulshettyacademy.pageObjects.CartPage;
import rahulshettyacademy.pageObjects.CheckOutPage;
import rahulshettyacademy.pageObjects.ConfirmationPage;
import rahulshettyacademy.pageObjects.OrderPage;
import rahulshettyacademy.pageObjects.ProductCataloguePage;
import rahulshettyacademy.tests.TestComponents.BaseTest;

public class SubmitOrderTest extends BaseTest {

    @Test(dataProvider = "purchaseOrderData", dataProviderClass = TestDataProvider.class, groups = { "Purchase", "Regression" })
    public void submitOrder(HashMap<String, String> input) throws IOException, InterruptedException {
        ProductCataloguePage productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));
        productCatalogue.getProductList();
        productCatalogue.addProductToCart(input.get("product"));

        CartPage cartPage = productCatalogue.goToCartPage();
        boolean isProductInCart = cartPage.verifyProductDisplay(input.get("product"));
        Assert.assertTrue(isProductInCart, "Product not found in cart: " + input.get("product"));

        CheckOutPage checkOutPage = cartPage.goToCheckout();
        checkOutPage.selectCountry("India");

        ConfirmationPage confirmationPage = checkOutPage.submitOrder();
        String confirmationMessage = confirmationPage.getConfirmationMessage();

        Assert.assertTrue(confirmationMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."),
                "Confirmation message mismatch. Received: " + confirmationMessage);
    }

    @Test(dataProvider = "purchaseOrderData", dataProviderClass = TestDataProvider.class, dependsOnMethods = { "submitOrder" }, groups = { "Purchase" })
    public void orderHistoryTest(HashMap<String, String> input) {
        ProductCataloguePage productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));
        OrderPage orderPage = productCatalogue.goToOrdersPage();
        boolean isOrderPresent = orderPage.verifyOrderDisplay(input.get("product"));
        Assert.assertTrue(isOrderPresent, "Order not found in history: " + input.get("product"));
    }
}