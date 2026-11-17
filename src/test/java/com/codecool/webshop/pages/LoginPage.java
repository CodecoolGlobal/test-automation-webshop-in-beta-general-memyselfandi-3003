package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");

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
}
