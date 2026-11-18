package com.codecool.webshop.tests;

import com.codecool.webshop.pages.LoginPage;
import com.codecool.webshop.pages.ProductsPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorUserSortingTest extends BaseTest {
    @Test
    @DisplayName("TC-SORT-03 [error_user]: Sort prices from low to high without an error alert")
    public void sortErrorUserPricesAscending() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        ProductsPage productsPage = new ProductsPage(driver, baseUrl);
        loginPage.open();
        loginPage.login(errorUsername, testPassword);
        productsPage.waitUntilLoaded();
        assertTrue(productsPage.getProductCount() > 1,
                "Sorting requires at least two products.");

        List<String> expectedNames = new ArrayList<>(productsPage.getProductNames());
        List<Double> expectedPrices = new ArrayList<>(productsPage.getProductPrices());
        assertEquals(expectedNames.size(), expectedPrices.size(),
                "Every product should have a displayed price.");
        Collections.sort(expectedNames);
        Collections.sort(expectedPrices);

        productsPage.sortProductsBy("Price (low to high)");
        String sortingAlert = productsPage.acceptSortingAlertIfPresent();
        List<Double> actualPrices = productsPage.getProductPrices();
        List<String> actualNames = new ArrayList<>(productsPage.getProductNames());
        Collections.sort(actualNames);

        assertAll("Correct price sorting for error_user; related defect BUG-SORT-001",
                () -> assertEquals("", sortingAlert,
                        "Sorting should complete without an error alert."),
                () -> assertEquals("Price (low to high)", productsPage.getSelectedSortOption(),
                        "The selected sorting option should reflect the requested order."),
                () -> assertEquals(expectedPrices, actualPrices,
                        "All prices should be in ascending numeric order."),
                () -> assertEquals(expectedNames, actualNames,
                        "Sorting should preserve every original product.")
        );
    }
}
