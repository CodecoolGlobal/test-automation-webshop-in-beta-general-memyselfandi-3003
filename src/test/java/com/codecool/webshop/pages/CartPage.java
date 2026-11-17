package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {
    private final By cartList = By.cssSelector(".cart_list");
    private final By cartItems = By.cssSelector(".cart_item");
    private final By productName = By.cssSelector(".inventory_item_name");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/cart.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartList));
    }

    public int getProductCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement item : driver.findElements(cartItems)) {
            names.add(item.findElement(productName).getText());
        }
        return names;
    }

    public double getProductPrice(String name) {
        String priceText = getCartItem(name)
                .findElement(By.cssSelector(".inventory_item_price")).getText();
        return Double.parseDouble(priceText.replace("$", "").trim());
    }

    public int getProductQuantity(String name) {
        String quantityText = getCartItem(name)
                .findElement(By.cssSelector(".cart_quantity")).getText();
        return Integer.parseInt(quantityText);
    }

    public void removeProduct(String name) {
        WebElement item = getCartItem(name);
        WebElement removeButton = item.findElement(By.cssSelector("button[id^='remove-']"));
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        wait.until(ExpectedConditions.invisibilityOf(removeButton));
    }

    public void continueShopping() {
        click(continueShoppingButton);
    }

    private WebElement getCartItem(String name) {
        for (WebElement item : driver.findElements(cartItems)) {
            if (item.findElement(productName).getText().equals(name)) {
                return item;
            }
        }
        throw new NoSuchElementException("Product not found in the cart: " + name);
    }
}
