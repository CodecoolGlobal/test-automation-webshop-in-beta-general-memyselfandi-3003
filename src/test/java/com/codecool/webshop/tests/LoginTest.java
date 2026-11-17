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
        loginPage.login(standardUsername, testPassword);
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

    @Test
    @DisplayName("TC-LOGIN-02: Incorrect password prevents login")
    public void incorrectPasswordPreventLogin() {
        String loginUrl = loginPage.getCurrentUrl();

        loginPage.login(standardUsername, testPassword + "-incorrect");

        assertAll("Rejected login",
                () -> assertEquals(
                        "Epic sadface: Username and password do not match any user in this service",
                        loginPage.getLoginErrorText(),
                        "An incorrect password should display a credential error."
                ),
                () -> assertEquals(
                        loginUrl,
                        loginPage.getCurrentUrl(),
                        "The user should remain on the login page."
                ));
    }

    @Test
    @DisplayName("TC-LOGIN-03: Empty username prevents login")
    public void emptyUsernamePreventsLogin() {
        String loginUrl = loginPage.getCurrentUrl();

        loginPage.login("", testPassword);

        assertAll("Missing username",
                () -> assertEquals(
                        "Epic sadface: Username is required",
                        loginPage.getLoginErrorText(),
                        "An empty username should display a required-field error."
                ),
                () -> assertEquals(
                        loginUrl,
                        loginPage.getCurrentUrl(),
                        "The user should remain on the login page."
                )
        );
    }

    @Test
    @DisplayName("TC-LOGIN-04: Empty password prevents login")
    public void emptyPasswordPreventsLogin() {
        String loginUrl = loginPage.getCurrentUrl();

        loginPage.login(standardUsername, "");

        assertAll("Missing password",
                () -> assertEquals(
                        "Epic sadface: Password is required",
                        loginPage.getLoginErrorText(),
                        "An empty password should display a required-field error."
                ),
                () -> assertEquals(
                        loginUrl,
                        loginPage.getCurrentUrl(),
                        "The user should remain on the login page."
                )
        );
    }

    @Test
    @DisplayName("TC-LOGIN-05: Locked account cannot log in")
    public void lockedAccountCannotLogIn() {
        String loginUrl = loginPage.getCurrentUrl();

        loginPage.login(lockedUsername, testPassword);

        assertAll("Locked account",
                () -> assertEquals(
                        "Epic sadface: Sorry, this user has been locked out.",
                        loginPage.getLoginErrorText(),
                        "A locked account should display an account-restriction error."
                ),
                () -> assertEquals(
                        loginUrl,
                        loginPage.getCurrentUrl(),
                        "The locked user should remain on the login page."
                )
        );
    }
}
