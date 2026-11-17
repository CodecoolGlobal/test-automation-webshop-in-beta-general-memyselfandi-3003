package com.codecool.webshop.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl;

    public BasePage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void fillField(By locator, String value) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(locator));
        input.clear();
        if (!value.isEmpty()) {
            input.sendKeys(value);
        }
    }

    protected void openPage(String path, By readyElement) {
        driver.get(baseUrl + path);
        wait.until(ExpectedConditions.visibilityOfElementLocated(readyElement));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
