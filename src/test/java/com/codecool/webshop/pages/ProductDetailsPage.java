package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductDetailsPage extends BasePage {
    private final By productName = By.cssSelector(".inventory_details_name");
    private final By productPrice = By.cssSelector(".inventory_details_price");
    private final By productDescription = By.cssSelector(".inventory_details_desc");
    private final By productImage = By.cssSelector(".inventory_details_img");

    public ProductDetailsPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/inventory-item.html?id="));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productName));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productPrice));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productDescription));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productImage));
    }

    public String getProductName() {
        return driver.findElement(productName).getText();
    }

    public double getProductPrice() {
        String priceText = driver.findElement(productPrice).getText();
        return Double.parseDouble(priceText.replace("$", "").trim());
    }

    public String getProductDescription() {
        return driver.findElement(productDescription).getText();
    }

    public String getProductImageSource() {
        return driver.findElement(productImage).getDomProperty("src");
    }

    public boolean isProductImageDisplayed() {
        return driver.findElement(productImage).isDisplayed();
    }
}
