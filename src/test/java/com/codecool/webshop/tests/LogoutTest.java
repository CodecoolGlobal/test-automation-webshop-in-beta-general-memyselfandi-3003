package com.codecool.webshop.tests;

import com.codecool.webshop.pages.LoginPage;
import com.codecool.webshop.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogoutTest extends BaseTest {
  private LoginPage loginPage;
  private ProductsPage productsPage;
  private String loginUrl;

  @BeforeEach
  public void setUpLoggedInUser() {
    loginPage = new LoginPage(driver, baseUrl);
    productsPage = new ProductsPage(driver, baseUrl);

    loginPage.open();
    loginUrl = loginPage.getCurrentUrl();

    loginPage.login(standardUsername, testPassword);
    productsPage.waitUntilLoaded();
  }

  @Test
  @DisplayName("TC-LOGOUT-01: Logout returns the user to the login page")
  public void logoutReturnsUserToLoginPage() {
    productsPage.logout();
    loginPage.waitUntilLoaded();

    assertAll("Login page after logout",
            () -> assertEquals(
                    loginUrl,
                    loginPage.getCurrentUrl(),
                    "Logout should return the user to the login page."
            ),
            () -> assertTrue(
                    loginPage.isLoginButtonDisplayed(),
                    "The Login button should be visible after logout."
            )
    );
  }

  @Test
  @DisplayName("TC-LOGOUT-02: Protected page requires login after logout")
  public void protectedPageRequiresLoginAfterLogout() {
    String inventoryUrl = productsPage.getCurrentUrl();

    productsPage.logout();
    loginPage.waitUntilLoaded();

    driver.navigate().back();
    loginPage.waitUntilFormVisible();

    assertAll("Access through browser Back",
            () -> assertTrue(
                    loginPage.isLoginButtonDisplayed(),
                    "Browser Back should require login again."
            ),
            () -> assertEquals(
                    0,
                    productsPage.getProductCount(),
                    "Products should not be displayed after logout."
            )
    );

    driver.get(inventoryUrl);
    loginPage.waitUntilFormVisible();

    assertAll("Access through direct URL",
            () -> assertTrue(
                    loginPage.isLoginButtonDisplayed(),
                    "Direct navigation should require login again."
            ),
            () -> assertEquals(
                    0,
                    productsPage.getProductCount(),
                    "Direct navigation should not expose products."
            )
    );
  }
}
