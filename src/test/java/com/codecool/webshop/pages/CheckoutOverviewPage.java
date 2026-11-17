package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class CheckoutOverviewPage extends BasePage {
    private final By overviewItems = By.cssSelector(".cart_item");
    private final By productName = By.cssSelector(".inventory_item_name");
    private final By subtotal = By.cssSelector(".summary_subtotal_label");
    private final By tax = By.cssSelector(".summary_tax_label");
    private final By total = By.cssSelector(".summary_total_label");
    private final By finishButton = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/checkout-step-two.html"));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(overviewItems));
        wait.until(ExpectedConditions.visibilityOfElementLocated(subtotal));
        wait.until(ExpectedConditions.visibilityOfElementLocated(tax));
        wait.until(ExpectedConditions.visibilityOfElementLocated(total));
        wait.until(ExpectedConditions.elementToBeClickable(finishButton));
    }

    public int getProductCount() {
        return driver.findElements(overviewItems).size();
    }

    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement item : driver.findElements(overviewItems)) {
            names.add(item.findElement(productName).getText());
        }
        return names;
    }

    public double getProductPrice(String name) {
        return getAmount(getOverviewItem(name).findElement(By.cssSelector(".inventory_item_price")));
    }

    public int getProductQuantity(String name) {
        return Integer.parseInt(getOverviewItem(name)
                .findElement(By.cssSelector(".cart_quantity")).getText());
    }

    public double getSubtotal() {
        return getAmount(driver.findElement(subtotal));
    }

    public double getTax() {
        return getAmount(driver.findElement(tax));
    }

    public double getTotal() {
        return getAmount(driver.findElement(total));
    }

    public void finishOrder() {
        click(finishButton);
    }

    private double getAmount(WebElement element) {
        String text = element.getText();
        String amount = text.substring(text.indexOf("$") + 1).trim();
        return Double.parseDouble(amount);
    }

    private WebElement getOverviewItem(String name) {
        for (WebElement item : driver.findElements(overviewItems)) {
            if (item.findElement(productName).getText().equals(name)) {
                return item;
            }
        }
        throw new NoSuchElementException("Product not found in checkout overview: " + name);
    }
}
