package com.codecool.webshop.tests;

import com.codecool.webshop.pages.LoginPage;
import com.codecool.webshop.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductSortingTest extends BaseTest {
    private ProductsPage productsPage;

    @BeforeEach
    public void setUpProductList() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        productsPage = new ProductsPage(driver, baseUrl);
        loginPage.open();
        loginPage.login(standardUsername, testPassword);
        productsPage.waitUntilLoaded();
        assertTrue(productsPage.getProductCount() > 1,
                "Sorting requires at least two products.");
    }

    @Test
    @DisplayName("TC-SORT-01: Sort product names from A to Z")
    public void sortProductNamesAscending() {
        List<String> expectedNames = new ArrayList<>(productsPage.getProductNames());
        Collections.sort(expectedNames);

        productsPage.sortProductsBy("Name (Z to A)");

        productsPage.sortProductsBy("Name (A to Z)");

        assertAll("Names sorted from A to Z",
                () -> assertEquals("Name (A to Z)", productsPage.getSelectedSortOption()),
                () -> assertEquals(expectedNames, productsPage.getProductNames(),
                        "All original products should appear in ascending name order.")
        );
    }

    @Test
    @DisplayName("TC-SORT-02: Sort product names from Z to A")
    public void sortProductNamesDescending() {
        List<String> expectedNames = new ArrayList<>(productsPage.getProductNames());
        Collections.sort(expectedNames);
        Collections.reverse(expectedNames);

        productsPage.sortProductsBy("Name (Z to A)");

        assertAll("Names sorted from Z to A",
                () -> assertEquals("Name (Z to A)", productsPage.getSelectedSortOption()),
                () -> assertEquals(expectedNames, productsPage.getProductNames(),
                        "All original products should appear in descending name order.")
        );
    }

    @Test
    @DisplayName("TC-SORT-03: Sort standard-user prices from low to high")
    public void sortProductPricesAscending() {
        List<String> namesBeforeSorting = productsPage.getProductNames();
        List<Double> expectedPrices = new ArrayList<>(productsPage.getProductPrices());
        assertEquals(namesBeforeSorting.size(), expectedPrices.size(),
                "Every product should have a displayed price.");
        Collections.sort(expectedPrices);

        productsPage.sortProductsBy("Price (low to high)");

        assertAll("Prices sorted from low to high",
                () -> assertEquals("Price (low to high)", productsPage.getSelectedSortOption()),
                () -> assertEquals(expectedPrices, productsPage.getProductPrices(),
                        "All displayed prices should be in ascending numeric order."),
                () -> assertSameProducts(namesBeforeSorting, productsPage.getProductNames())
        );
    }

    @Test
    @DisplayName("TC-SORT-04: Sort product prices from high to low")
    public void sortProductPricesDescending() {
        List<String> namesBeforeSorting = productsPage.getProductNames();
        List<Double> expectedPrices = new ArrayList<>(productsPage.getProductPrices());
        assertEquals(namesBeforeSorting.size(), expectedPrices.size(),
                "Every product should have a displayed price.");
        Collections.sort(expectedPrices);
        Collections.reverse(expectedPrices);

        productsPage.sortProductsBy("Price (high to low)");

        assertAll("Prices sorted from high to low",
                () -> assertEquals("Price (high to low)", productsPage.getSelectedSortOption()),
                () -> assertEquals(expectedPrices, productsPage.getProductPrices(),
                        "All displayed prices should be in descending numeric order."),
                () -> assertSameProducts(namesBeforeSorting, productsPage.getProductNames())
        );
    }

    private void assertSameProducts(List<String> namesBefore, List<String> namesAfter) {
        List<String> expectedNames = new ArrayList<>(namesBefore);
        List<String> actualNames = new ArrayList<>(namesAfter);
        Collections.sort(expectedNames);
        Collections.sort(actualNames);
        assertEquals(expectedNames, actualNames,
                "Sorting should preserve every original product, including the product count.");
    }
}
