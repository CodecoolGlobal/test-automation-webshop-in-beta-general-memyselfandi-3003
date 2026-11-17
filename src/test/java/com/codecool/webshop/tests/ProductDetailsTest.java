package com.codecool.webshop.tests;

import com.codecool.webshop.pages.LoginPage;
import com.codecool.webshop.pages.ProductDetailsPage;
import com.codecool.webshop.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductDetailsTest extends BaseTest {
    private final String productName = "Sauce Labs Backpack";
    private ProductsPage productsPage;
    private ProductDetailsPage productDetailsPage;
    private double expectedPrice;
    private String expectedDescription;
    private String expectedImageSource;

    @BeforeEach
    public void setUpProductList() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        productsPage = new ProductsPage(driver, baseUrl);
        productDetailsPage = new ProductDetailsPage(driver, baseUrl);
        loginPage.open();
        loginPage.login(standardUsername, testPassword);
        productsPage.waitUntilLoaded();

        expectedPrice = productsPage.getProductPrice(productName);
        expectedDescription = productsPage.getProductDescription(productName);
        expectedImageSource = productsPage.getProductImageSource(productName);
        assertFalse(expectedDescription.isBlank(), "The list should contain a product description.");
        assertTrue(expectedImageSource != null && !expectedImageSource.isBlank(),
                "The list should contain a product image source.");
    }

    @Test
    @DisplayName("TC-PRODUCT-01: Open the corresponding product details via its image")
    public void openProductDetailsByImage() {
        productsPage.openProductByImage(productName);
        productDetailsPage.waitUntilLoaded();

        assertProductDetailsMatchList();
    }

    @Test
    @DisplayName("TC-PRODUCT-02: Open the corresponding product details via its name")
    public void openProductDetailsByName() {
        productsPage.openProductByName(productName);
        productDetailsPage.waitUntilLoaded();

        assertProductDetailsMatchList();
    }

    private void assertProductDetailsMatchList() {
        assertAll("Product details should match the selected list item",
                () -> assertTrue(productDetailsPage.getCurrentUrl().startsWith(baseUrl + "/inventory-item.html?id="),
                        "The selected product's dedicated details URL should open."),
                () -> assertEquals(productName, productDetailsPage.getProductName(),
                        "The details page should identify the selected product."),
                () -> assertEquals(expectedPrice, productDetailsPage.getProductPrice(),
                        "The price should match the product list."),
                () -> assertEquals(expectedDescription, productDetailsPage.getProductDescription(),
                        "The description should match the product list."),
                () -> assertEquals(expectedImageSource, productDetailsPage.getProductImageSource(),
                        "The same product image asset should be used on both pages."),
                () -> assertTrue(productDetailsPage.isProductImageDisplayed(),
                        "The product image should be visible.")
        );
    }
}
