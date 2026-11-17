package com.codecool.webshop.tests;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

public abstract class BaseTest {
    protected WebDriver driver;
    protected String baseUrl;
    protected String standardUsername;
    protected String lockedUsername;
    protected String testPassword;

    @BeforeEach
    public void setUp() {
        Dotenv dotenv = Dotenv.load();

        baseUrl = dotenv.get("BASE_URL");
        standardUsername = dotenv.get("STANDARD_USERNAME");
        lockedUsername = dotenv.get("LOCKED_USERNAME");
        testPassword = dotenv.get("TEST_PASSWORD");

        if (baseUrl == null || baseUrl.isBlank()
                || standardUsername == null || standardUsername.isBlank()
                || lockedUsername == null || lockedUsername.isBlank()
                || testPassword == null || testPassword.isBlank()) {
            throw new IllegalStateException(
                    "Set BASE_URL, STANDARD_USERNAME, LOCKED_USERNAME "
                            + "and TEST_PASSWORD in the project-root .env file.");
        }

        baseUrl = baseUrl.replaceAll("/+$", "");

        boolean isCi = "true".equals(System.getenv("CI"));

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_leak_detection", false
        ));

        if (isCi) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);

        if (!isCi) {
            driver.manage().window().maximize();
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
