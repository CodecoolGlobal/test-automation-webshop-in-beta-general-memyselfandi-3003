# Webshop in Beta — SauceDemo Tests

Browser tests for [SauceDemo](https://www.saucedemo.com/) using **Java 17, Maven, Selenium WebDriver, and JUnit Jupiter**.

The project follows the Page Object Model with `BaseTest` and `BasePage`. Configuration is loaded using `dotenv-java`, and GitHub Actions runs the tests in headless Chrome.

## Test planning

- [Test cases](TEST_CASES.md): the initial positive and negative cases and selected test accounts.
- [Exploratory testing](exploratory%20testing.md): manual observations, failures, and open questions for all six accounts.

The baseline account is `standard_user`. Later cases will cover rejected login attempts and selected account-specific failures. Testing one account does not establish that all accounts work correctly.

## Current implementation

Only **TC-LOGIN-01 — Successful login with valid credentials** is implemented. It verifies the product-list URL, Products heading, a non-empty product list, and the navigation menu after signing in.

The initial GitHub Actions run on **17 November 2026** completed successfully: **1 test, 0 failures, 0 errors, and 0 skipped tests**. The Surefire results were uploaded and downloaded as the `test-results` artifact.

Other cases in the test specification are planned, not implemented. No release recommendation has been made.

## Local setup

Requirements: JDK 17 or newer and Google Chrome. Use IntelliJ IDEA's Maven integration or an installed Maven command-line distribution.

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

## Run the test locally

In IntelliJ, open `LoginTest` and run `successfulLoginWithValidCredentials` using the green run icon. The working directory must be the repository root so the `.env` file can be found.

Alternatively, run the Maven `test` lifecycle phase from IntelliJ's Maven tool window. If Maven is installed on your command line, run this from the repository root:

```bash
mvn test
```

Chrome opens visibly when the process environment variable `CI` is unset or is not `true`. Each test gets a fresh browser session, and `tearDown()` closes it afterwards. Results appear in IntelliJ's test runner and, for Maven runs, in `target/surefire-reports/` as XML/text files.

To check the headless mode locally, temporarily set `CI=true` in the test's IntelliJ run configuration under **Environment variables**. The test should run without displaying a Chrome window. Remove that variable afterwards to restore visible local execution. This flag is read using `System.getenv("CI")`, so setting it only in `.env` does not enable headless mode.

## GitHub Actions

The [Webshop UI tests workflow](.github/workflows/ui-tests.yml) runs on:

- **Push events**, including pushes to feature branches.
- **Pull request events** for pull requests targeting `main`, including opening a PR and adding commits to its source branch.

The `test` job runs on a GitHub-hosted Ubuntu runner and performs these steps in order:

| Step | Purpose |
| --- | --- |
| Checkout repository | Downloads the project revision associated with the workflow event. |
| Set up Java 17 | Prepares the Eclipse Temurin JDK used to compile and run the tests. |
| Prepare test configuration | Copies `.env.example` to `.env` in the runner's working directory. |
| Run tests | Executes `mvn --batch-mode test`, supplying `TEST_PASSWORD` from a repository secret. |
| Upload test results | Uploads generated Surefire reports as the `test-results` artifact, including when a test fails. |

GitHub Actions sets `CI=true` automatically. `BaseTest` detects this value and starts Chrome in headless mode with a configured window size of 1920 × 1080. The same test code runs locally and in CI.

### Repository secret

Before running the workflow in a repository, configure its password:

1. Open **Settings → Secrets and variables → Actions** on GitHub.
2. Select **New repository secret**.
3. Set the name to **`TEST_PASSWORD`** and the value to the SauceDemo login password.
4. Select **Add secret**.

The workflow supplies the secret as the `TEST_PASSWORD` environment variable for the **Run tests** step. `dotenv-java` reads the process environment value in preference to the empty password in the copied `.env` template. The application URL and baseline username come from `.env.example`.

### Read and download results

1. Open the repository's **Actions** tab and select a **Webshop UI tests** run.
2. Open the `test` job and expand **Run tests** to read the Maven/JUnit output, including the counts of executed, failed, errored, and skipped tests.
3. Return to the run's **Summary** page.
4. Under **Artifacts**, download **`test-results`** and extract the archive.

The artifact contains the files generated in `target/surefire-reports/`:

- **`.txt` files:** readable summaries for each test class.
- **`TEST-*.xml` files:** structured results containing test counts, individual test cases, execution times, and captured output.

The upload step uses `if: always()` so it attempts to preserve the reports even after a failed test step. It does not change a failing test run into a successful one. If execution stops before reports are generated, there may be no report artifact to download.

## Structure

```text
.github/workflows/ui-tests.yml  GitHub Actions workflow
src/test/java/com/codecool/webshop/
    pages/
        BasePage.java          Shared driver, waits, and field helpers
        LoginPage.java         Login form actions
        ProductsPage.java      Product-list observations
    tests/
        BaseTest.java          Configuration, browser setup, and teardown
        LoginTest.java         Login assertions
.env.example                   Configuration template without a password
pom.xml                        Dependencies and Maven plugins
```

Page objects contain browser interactions; tests contain JUnit assertions. Explicit waits use `WebDriverWait` and `ExpectedConditions`.

## Next steps

1. Add separate named account settings for the selected test users.
2. Add negative login cases, including the locked account.
3. Implement the other selected cases and create reproducible defect tickets.
4. Execute the expanded suite and write the release status report.
