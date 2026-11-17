# Webshop in Beta — SauceDemo Tests

Browser tests for [SauceDemo](https://www.saucedemo.com/) using **Java 17, Maven, Selenium WebDriver, and JUnit Jupiter**.

The project follows the Page Object Model with `BaseTest` and `BasePage`, matching the structure used in the earlier Riddles project. Local configuration is loaded using `dotenv-java`.

## Test planning

- [Test cases](TEST_CASES.md): the initial positive and negative cases and selected test accounts.
- [Exploratory testing](exploratory%20testing.md): manual observations, failures, and open questions for all six accounts.

The baseline account is `standard_user`. Later cases will cover rejected login attempts and selected account-specific failures. Testing one account does not establish that all accounts work correctly.

## Current implementation

Only **TC-LOGIN-01 — Successful login with valid credentials** is implemented. It verifies the product-list URL, Products heading, a non-empty product list, and the navigation menu after signing in.

Other cases in the test specification are planned, not implemented. No release recommendation has been made.

## Local setup

1. Open `pom.xml` as a Maven project in IntelliJ IDEA.
2. Select JDK 17 or newer and reload the Maven dependencies.
3. Copy `.env.example` to `.env` in the repository root, unless a local `.env` already exists.
4. Fill in `TEST_PASSWORD` using the password shown on the SauceDemo login page. Keep `TEST_USERNAME=standard_user` for the first test.

Configuration keys:

```dotenv
BASE_URL=https://www.saucedemo.com
TEST_USERNAME=standard_user
TEST_PASSWORD=
```

The example intentionally leaves the password empty. Fill it in only in your local `.env`. Git ignores `.env`; `.env.example` contains no password and can be committed.

## Run the test

In IntelliJ, open `LoginTest` and run `successfulLoginWithValidCredentials` using the green run icon. The working directory must be the repository root so the `.env` file can be found.

Alternatively, run the Maven `test` lifecycle phase from IntelliJ's Maven tool window. If Maven is installed on your command line, run this from the repository root:

```bash
mvn test
```

Chrome opens visibly. Each test gets a fresh browser session, and `tearDown()` closes it afterwards. Results appear in IntelliJ's test runner and, for Maven runs, in `target/surefire-reports/` as XML/text files.

## Structure

```text
src/test/java/com/codecool/webshop/
    pages/
        BasePage.java       Shared driver, waits, and field helpers
        LoginPage.java      Login form actions
        ProductsPage.java   Product-list observations
    tests/
        BaseTest.java       Configuration, browser setup, and teardown
        LoginTest.java      Login assertions
.env.example                Configuration template without a password
pom.xml                     Dependencies and Maven plugins
```

Page objects contain browser interactions; tests contain JUnit assertions. Explicit waits use `WebDriverWait` and `ExpectedConditions`.

## CI and next steps

GitHub Actions has not been configured. We will build it as a guided exercise after reviewing the local test. Selenium Grid, Cucumber, Maven Wrapper, automatic screenshot capture, and a separate HTML-report plugin are not part of the current setup.

Next steps:

1. Run and review the successful-login test locally.
2. Add negative login cases, including the locked account.
3. Build the GitHub Actions workflow step by step.
4. Implement the other selected cases, file reproducible defect tickets, and write the release status report.
