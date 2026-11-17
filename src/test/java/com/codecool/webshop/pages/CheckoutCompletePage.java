package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CheckoutCompletePage extends BasePage {
    private final By confirmationHeader = By.cssSelector(".complete-header");
    private final By confirmationText = By.cssSelector(".complete-text");

    public CheckoutCompletePage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/checkout-complete.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationHeader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationText));
    }

    public String getConfirmationHeader() {
        return driver.findElement(confirmationHeader).getText();
    }

    public String getConfirmationText() {
        return driver.findElement(confirmationText).getText();
    }

    public boolean isConfirmationDisplayed() {
        List<WebElement> headers = driver.findElements(confirmationHeader);
        return !headers.isEmpty() && headers.get(0).isDisplayed();
    }
}
