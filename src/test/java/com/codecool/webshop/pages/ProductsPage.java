package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class ProductsPage extends BasePage {
    private final By title = By.cssSelector(".title");
    private final By products = By.cssSelector(".inventory_item");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By sortDropdown = By.cssSelector(".product_sort_container");
    private final By productNames = By.cssSelector(".inventory_item_name");
    private final By productPrices = By.cssSelector(".inventory_item_price");

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

    public void sortProductsBy(String optionText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        Select sortSelect = new Select(dropdown);
        sortSelect.selectByVisibleText(optionText);
    }

    public String getSelectedSortOption() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortDropdown));
        return new Select(dropdown).getFirstSelectedOption().getText();
    }

    public List<String> getProductNames() {
        List<WebElement> elements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames));
        List<String> names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText());
        }
        return names;
    }

    public List<Double> getProductPrices() {
        List<WebElement> elements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(productPrices));
        List<Double> prices = new ArrayList<>();
        for (WebElement element : elements) {
            String priceText = element.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }
}
