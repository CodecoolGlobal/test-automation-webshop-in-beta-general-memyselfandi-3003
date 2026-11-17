package com.codecool.webshop.tests;

import com.codecool.webshop.pages.LoginPage;
import com.codecool.webshop.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeEach
    public void setUpLoginPage() {
        loginPage = new LoginPage(driver, baseUrl);
        productsPage = new ProductsPage(driver, baseUrl);
        loginPage.open();
    }

    @Test
    @DisplayName("TC-LOGIN-01: Successful login with valid credentials")
    public void successfulLoginWithValidCredentials() {
        loginPage.login(testUsername, testPassword);
        productsPage.waitUntilLoaded();

        assertAll("Product list after login",
                () -> assertEquals(baseUrl + "/inventory.html", productsPage.getCurrentUrl(),
                        "The product-list URL should open after login."),
                () -> assertEquals("Products", productsPage.getTitle(),
                        "The Products heading should be visible."),
                () -> assertTrue(productsPage.getProductCount() > 0,
                        "The product list should contain products."),
                () -> assertTrue(productsPage.isMenuButtonDisplayed(),
                        "The navigation menu should be visible.")
        );
    }
}
