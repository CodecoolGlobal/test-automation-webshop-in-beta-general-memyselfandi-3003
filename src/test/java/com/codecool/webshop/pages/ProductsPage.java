package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductsPage extends BasePage {
    private final By title = By.cssSelector(".title");
    private final By products = By.cssSelector(".inventory_item");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/inventory.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(products));
        wait.until(ExpectedConditions.visibilityOfElementLocated(menuButton));
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText();
    }

    public int getProductCount() {
        return driver.findElements(products).size();
    }

    public boolean isMenuButtonDisplayed() {
        return driver.findElement(menuButton).isDisplayed();
    }

    public void logout() {
        click(menuButton);
        click(logoutLink);
    }
}
