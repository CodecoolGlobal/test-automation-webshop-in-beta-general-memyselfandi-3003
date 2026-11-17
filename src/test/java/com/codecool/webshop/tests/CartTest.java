package com.codecool.webshop.tests;

import com.codecool.webshop.pages.CartPage;
import com.codecool.webshop.pages.LoginPage;
import com.codecool.webshop.pages.ProductDetailsPage;
import com.codecool.webshop.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartTest extends BaseTest {
    private final String primaryProduct = "Sauce Labs Backpack";
    private final String secondaryProduct = "Sauce Labs Bike Light";
    private ProductsPage productsPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;
    private double primaryPrice;

    @BeforeEach
    public void setUpEmptyCart() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        productsPage = new ProductsPage(driver, baseUrl);
        productDetailsPage = new ProductDetailsPage(driver, baseUrl);
        cartPage = new CartPage(driver, baseUrl);
        loginPage.open();
        loginPage.login(standardUsername, testPassword);
        productsPage.waitUntilLoaded();
        primaryPrice = productsPage.getProductPrice(primaryProduct);
        assertEquals(0, productsPage.getCartItemCount(),
                "Each independent session should start with an empty cart indicator.");
        assertEquals("Add to cart", productsPage.getCartButtonText(primaryProduct));
    }

    @Test
    @DisplayName("TC-CART-01: Add and remove a product from the product list")
    public void addAndRemoveProductFromList() {
        productsPage.addProductToCart(primaryProduct);
        assertAll("Product added from the list",
                () -> assertEquals("Remove", productsPage.getCartButtonText(primaryProduct)),
                () -> assertEquals(1, productsPage.getCartItemCount())
        );
        productsPage.openCart();
        cartPage.waitUntilLoaded();
        assertSingleProductInCart(primaryProduct, primaryPrice);

        cartPage.continueShopping();
        productsPage.waitUntilLoaded();
        productsPage.removeProductFromCart(primaryProduct);
        assertAll("Product removed from the list",
                () -> assertEquals("Add to cart", productsPage.getCartButtonText(primaryProduct)),
                () -> assertEquals(0, productsPage.getCartItemCount())
        );
        productsPage.openCart();
        cartPage.waitUntilLoaded();
        assertEmptyCart();
    }

    @Test
    @DisplayName("TC-CART-02: Add and remove a product from its details page")
    public void addAndRemoveProductFromDetails() {
        productsPage.openProductByName(primaryProduct);
        productDetailsPage.waitUntilLoaded();
        productDetailsPage.addProductToCart();
        assertAll("Product added from details",
                () -> assertEquals("Remove", productDetailsPage.getCartButtonText()),
                () -> assertEquals(1, productDetailsPage.getCartItemCount())
        );
        productDetailsPage.openCart();
        cartPage.waitUntilLoaded();
        assertSingleProductInCart(primaryProduct, primaryPrice);

        cartPage.continueShopping();
        productsPage.waitUntilLoaded();
        productsPage.openProductByName(primaryProduct);
        productDetailsPage.waitUntilLoaded();
        productDetailsPage.removeProductFromCart();
        assertAll("Product removed from details",
                () -> assertEquals("Add to cart", productDetailsPage.getCartButtonText()),
                () -> assertEquals(0, productDetailsPage.getCartItemCount())
        );
        productDetailsPage.openCart();
        cartPage.waitUntilLoaded();
        assertEmptyCart();
    }

    @Test
    @DisplayName("TC-CART-03: Removing a cart item updates its product-list button")
    public void removeProductFromCartPage() {
        productsPage.addProductToCart(primaryProduct);
        productsPage.openCart();
        cartPage.waitUntilLoaded();
        assertSingleProductInCart(primaryProduct, primaryPrice);

        cartPage.removeProduct(primaryProduct);
        assertEmptyCart();
        cartPage.continueShopping();
        productsPage.waitUntilLoaded();
        assertAll("Product list reflects cart removal",
                () -> assertEquals("Add to cart", productsPage.getCartButtonText(primaryProduct)),
                () -> assertEquals(0, productsPage.getCartItemCount())
        );
    }

    @Test
    @DisplayName("TC-CART-04: Removing one of two products preserves the other")
    public void manageTwoDistinctProducts() {
        double secondaryPrice = productsPage.getProductPrice(secondaryProduct);
        productsPage.addProductToCart(primaryProduct);
        productsPage.addProductToCart(secondaryProduct);
        productsPage.openCart();
        cartPage.waitUntilLoaded();

        assertAll("Cart contains both selected products",
                () -> assertEquals(2, cartPage.getProductCount()),
                () -> assertTrue(cartPage.getProductNames().containsAll(
                        List.of(primaryProduct, secondaryProduct)),
                        "Both selected product names should be present."),
                () -> assertEquals(primaryPrice, cartPage.getProductPrice(primaryProduct)),
                () -> assertEquals(secondaryPrice, cartPage.getProductPrice(secondaryProduct)),
                () -> assertEquals(1, cartPage.getProductQuantity(primaryProduct)),
                () -> assertEquals(1, cartPage.getProductQuantity(secondaryProduct)),
                () -> assertEquals(2, cartPage.getCartItemCount())
        );

        cartPage.removeProduct(primaryProduct);
        assertSingleProductInCart(secondaryProduct, secondaryPrice);
    }

    private void assertSingleProductInCart(String productName, double expectedPrice) {
        assertAll("Cart contains exactly the selected product",
                () -> assertEquals(1, cartPage.getProductCount()),
                () -> assertEquals(List.of(productName), cartPage.getProductNames()),
                () -> assertEquals(expectedPrice, cartPage.getProductPrice(productName)),
                () -> assertEquals(1, cartPage.getProductQuantity(productName)),
                () -> assertEquals(1, cartPage.getCartItemCount())
        );
    }

    private void assertEmptyCart() {
        assertAll("Cart is empty",
                () -> assertEquals(0, cartPage.getProductCount(),
                        "No product rows should remain in the cart."),
                () -> assertEquals(0, cartPage.getCartItemCount(),
                        "The header should show no positive cart count.")
        );
    }
}
