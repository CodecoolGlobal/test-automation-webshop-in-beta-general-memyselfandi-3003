package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By loginErrorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void open() {
        openPage("/", usernameInput);
    }

    public void enterUsername(String username) {
        fillField(usernameInput, username);
    }

    public void enterPassword(String password) {
        fillField(passwordInput, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public String getLoginErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessage)).getText();
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));
        waitUntilFormVisible();
    }

    public void waitUntilFormVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
    }

    public boolean isLoginButtonDisplayed() {
        return driver.findElement(loginButton).isDisplayed();
    }
}
