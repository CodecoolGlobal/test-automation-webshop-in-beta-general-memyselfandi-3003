package com.codecool.webshop.tests;

import com.codecool.webshop.pages.CartPage;
import com.codecool.webshop.pages.CheckoutCompletePage;
import com.codecool.webshop.pages.CheckoutInformationPage;
import com.codecool.webshop.pages.CheckoutOverviewPage;
import com.codecool.webshop.pages.LoginPage;
import com.codecool.webshop.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckoutTest extends BaseTest {
    private final String primaryProduct = "Sauce Labs Backpack";
    private final String secondaryProduct = "Sauce Labs Bike Light";
    private CartPage cartPage;
    private CheckoutInformationPage checkoutInformationPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;
    private double primaryPrice;
    private double secondaryPrice;
    private int primaryQuantity;
    private int secondaryQuantity;

    @BeforeEach
    public void setUpCheckout() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        ProductsPage productsPage = new ProductsPage(driver, baseUrl);
        cartPage = new CartPage(driver, baseUrl);
        checkoutInformationPage = new CheckoutInformationPage(driver, baseUrl);
        checkoutOverviewPage = new CheckoutOverviewPage(driver, baseUrl);
        checkoutCompletePage = new CheckoutCompletePage(driver, baseUrl);
        loginPage.open();
        loginPage.login(standardUsername, testPassword);
        productsPage.waitUntilLoaded();

        primaryPrice = productsPage.getProductPrice(primaryProduct);
        secondaryPrice = productsPage.getProductPrice(secondaryProduct);
        productsPage.addProductToCart(primaryProduct);
        productsPage.addProductToCart(secondaryProduct);
        productsPage.openCart();
        cartPage.waitUntilLoaded();
        primaryQuantity = cartPage.getProductQuantity(primaryProduct);
        secondaryQuantity = cartPage.getProductQuantity(secondaryProduct);
        assertAll("Checkout starts with one of each selected product",
                () -> assertEquals(1, primaryQuantity),
                () -> assertEquals(1, secondaryQuantity)
        );
        assertCartContents();

        cartPage.startCheckout();
        checkoutInformationPage.waitUntilLoaded();
    }

    @Test
    @DisplayName("TC-CHECKOUT-01: Complete an order with consistent products and totals")
    public void completeOrderWithValidCustomerInformation() {
        checkoutInformationPage.fillCustomerInformation("Kevin", "Tester", "12345");
        checkoutInformationPage.clickContinueButton();
        checkoutOverviewPage.waitUntilLoaded();
        double expectedSubtotal = primaryPrice * primaryQuantity + secondaryPrice * secondaryQuantity;

        assertAll("Order overview matches the selected cart",
                () -> assertEquals(2, checkoutOverviewPage.getProductCount()),
                () -> assertTrue(checkoutOverviewPage.getProductNames().containsAll(
                        List.of(primaryProduct, secondaryProduct))),
                () -> assertEquals(primaryPrice, checkoutOverviewPage.getProductPrice(primaryProduct)),
                () -> assertEquals(secondaryPrice, checkoutOverviewPage.getProductPrice(secondaryProduct)),
                () -> assertEquals(primaryQuantity, checkoutOverviewPage.getProductQuantity(primaryProduct)),
                () -> assertEquals(secondaryQuantity, checkoutOverviewPage.getProductQuantity(secondaryProduct)),
                () -> assertEquals(expectedSubtotal, checkoutOverviewPage.getSubtotal(), 0.001,
                        "Item subtotal should equal the sum of product prices multiplied by quantities."),
                () -> assertEquals(checkoutOverviewPage.getSubtotal() + checkoutOverviewPage.getTax(),
                        checkoutOverviewPage.getTotal(), 0.001,
                        "Total should equal the displayed subtotal plus the displayed tax.")
        );

        checkoutOverviewPage.finishOrder();
        checkoutCompletePage.waitUntilLoaded();
        assertAll("Order completion confirmation",
                () -> assertEquals(baseUrl + "/checkout-complete.html", checkoutCompletePage.getCurrentUrl()),
                () -> assertEquals("Thank you for your order!", checkoutCompletePage.getConfirmationHeader()),
                () -> assertTrue(checkoutCompletePage.getConfirmationText().contains("Your order has been dispatched"),
                        "The page should explicitly confirm the order.")
        );
    }

    @Test
    @DisplayName("TC-CHECKOUT-02: Missing First Name prevents checkout from advancing")
    public void missingFirstNamePreventsCheckout() {
        checkoutInformationPage.fillCustomerInformation("", "Tester", "12345");
        checkoutInformationPage.clickContinueButton();

        assertCheckoutRejected("Error: First Name is required");
    }

    @Test
    @DisplayName("TC-CHECKOUT-03: Missing Last Name prevents checkout from advancing")
    public void missingLastNamePreventsCheckout() {
        checkoutInformationPage.fillCustomerInformation("Kevin", "", "12345");
        checkoutInformationPage.clickContinueButton();

        assertCheckoutRejected("Error: Last Name is required");
    }

    @Test
    @DisplayName("TC-CHECKOUT-04: Missing Postal Code prevents checkout from advancing")
    public void missingPostalCodePreventsCheckout() {
        checkoutInformationPage.fillCustomerInformation("Kevin", "Tester", "");
        checkoutInformationPage.clickContinueButton();

        assertCheckoutRejected("Error: Postal Code is required");
    }

    @Test
    @DisplayName("TC-CHECKOUT-05: Cancel returns to the unchanged cart")
    public void cancelCheckoutPreservesCartContents() {
        checkoutInformationPage.cancelCheckout();
        cartPage.waitUntilLoaded();

        assertCartContents();
        assertFalse(checkoutCompletePage.isConfirmationDisplayed(),
                "Cancelling checkout should not display order completion.");
    }

    private void assertCheckoutRejected(String expectedError) {
        assertAll("Missing customer information is rejected",
                () -> assertEquals(expectedError, checkoutInformationPage.getErrorText()),
                () -> assertEquals(baseUrl + "/checkout-step-one.html", checkoutInformationPage.getCurrentUrl(),
                        "Checkout should remain on the customer-information page."),
                () -> assertTrue(checkoutInformationPage.isContinueButtonDisplayed()),
                () -> assertFalse(checkoutCompletePage.isConfirmationDisplayed())
        );
    }

    private void assertCartContents() {
        assertAll("Cart retains both selected products",
                () -> assertEquals(2, cartPage.getProductCount()),
                () -> assertTrue(cartPage.getProductNames().containsAll(List.of(primaryProduct, secondaryProduct))),
                () -> assertEquals(primaryPrice, cartPage.getProductPrice(primaryProduct)),
                () -> assertEquals(secondaryPrice, cartPage.getProductPrice(secondaryProduct)),
                () -> assertEquals(primaryQuantity, cartPage.getProductQuantity(primaryProduct)),
                () -> assertEquals(secondaryQuantity, cartPage.getProductQuantity(secondaryProduct)),
                () -> assertEquals(2, cartPage.getCartItemCount())
        );
    }
}
