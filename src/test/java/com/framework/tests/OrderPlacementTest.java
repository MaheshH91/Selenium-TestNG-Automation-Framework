package com.framework.tests;

import java.util.HashMap;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.framework.base.BaseTest;
import com.framework.data.PurchaseDataProvider;
import com.framework.pages.CartPage;
import com.framework.pages.CheckoutPage;
import com.framework.pages.OrderConfirmationPage;
import com.framework.pages.OrderHistoryPage;
import com.framework.pages.ProductCatalogPage;

public class OrderPlacementTest extends BaseTest {

    private static final String EXPECTED_CONFIRMATION = "THANKYOU FOR THE ORDER.";

    @Test(
        dataProvider = "purchaseOrders",
        dataProviderClass = PurchaseDataProvider.class,
        groups = {"Purchase", "Regression"},
        priority = 1,
        description = "Verifies end-to-end checkout workflow for catalog merchandise"
    )
    public void submitOrderE2ETest(HashMap<String, String> data) {
        ProductCatalogPage catalogPage = getLoginPage().login(data.get("email"), data.get("password"));
        catalogPage.addProductToCart(data.get("product"));

        CartPage cartPage = catalogPage.navigateToCart();
        Assert.assertTrue(cartPage.isProductInCart(data.get("product")), 
                "Product missing in Cart: " + data.get("product"));

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.selectDestinationCountry("India");

        OrderConfirmationPage confirmationPage = checkoutPage.submitOrder();
        String message = confirmationPage.getConfirmationHeadline();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(message.equalsIgnoreCase(EXPECTED_CONFIRMATION), 
                "Order headline confirmation mismatch. Found: " + message);
        softAssert.assertAll();
    }

    @Test(
        dataProvider = "purchaseOrders",
        dataProviderClass = PurchaseDataProvider.class,
        dependsOnMethods = {"submitOrderE2ETest"},
        groups = {"Purchase"},
        priority = 2,
        description = "Validates the historical record of the placed order in user order logs"
    )
    public void verifyOrderInHistoryTest(HashMap<String, String> data) {
        ProductCatalogPage catalogPage = getLoginPage().login(data.get("email"), data.get("password"));
        OrderHistoryPage historyPage = catalogPage.navigateToOrderHistory();

        Assert.assertTrue(historyPage.isOrderPresent(data.get("product")), 
                "Order missing from historical records: " + data.get("product"));
    }
}