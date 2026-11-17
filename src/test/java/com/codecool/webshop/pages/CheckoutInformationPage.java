package com.codecool.webshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutInformationPage extends BasePage {
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public CheckoutInformationPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/checkout-step-one.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(postalCodeInput));
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
    }

    public void fillCustomerInformation(String firstName, String lastName, String postalCode) {
        fillField(firstNameInput, firstName);
        fillField(lastNameInput, lastName);
        fillField(postalCodeInput, postalCode);
    }

    public void clickContinueButton() {
        click(continueButton);
    }

    public void cancelCheckout() {
        click(cancelButton);
    }

    public String getErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public boolean isContinueButtonDisplayed() {
        return driver.findElement(continueButton).isDisplayed();
    }
}
