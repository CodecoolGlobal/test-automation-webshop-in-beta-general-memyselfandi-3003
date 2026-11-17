# Webshop in Beta — SauceDemo Tests

Browser tests for [SauceDemo](https://www.saucedemo.com/) using **Java 17, Maven, Selenium WebDriver, and JUnit Jupiter**.

The project follows the Page Object Model with `BaseTest` and `BasePage`. Configuration is loaded using `dotenv-java`, and GitHub Actions runs the tests in headless Chrome.

## Test planning

- [Test cases](TEST_CASES.md): the initial positive and negative cases and selected test accounts.
- [Exploratory testing](exploratory%20testing.md): manual observations, failures, and open questions for all six accounts.

The login suite uses `standard_user` for successful login and invalid/missing-credential cases, and `locked_out_user` for account-restriction coverage. Other selected account-specific cases remain planned. Testing these login scenarios does not establish that every feature works for every account.

## Current implementation

**Five login cases are implemented:** one positive case and four negative cases.

| Case | Scenario |
| --- | --- |
| TC-LOGIN-01 | Valid credentials open the product list; the inventory URL, Products heading, non-empty list, and navigation menu are verified. |
| TC-LOGIN-02 | An incorrect password produces the expected credential error and leaves the user on the login page. |
| TC-LOGIN-03 | An empty username produces the expected required-field error and leaves the login page open. |
| TC-LOGIN-04 | An empty password produces the expected required-field error and leaves the login page open. |
| TC-LOGIN-05 | Valid credentials for the locked account produce the expected restriction error and leave the login page open. |

The initial GitHub Actions run on **17 November 2026** completed successfully: **1 test, 0 failures, 0 errors, and 0 skipped tests**. The Surefire results were uploaded and downloaded as the `test-results` artifact.

The tester reported that all five login cases passed locally on **17 November 2026**. The workflow for the expanded login suite then completed successfully in **GitHub Actions run #5**, using commit [`16d512f`](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/commit/16d512f2b9bf5e2b8a547742181df6147eb72775/checks). The test and report-upload steps both succeeded. The remaining **17 logical cases** are planned, not implemented. No release recommendation has been made.

## Local setup

Requirements: JDK 17 or newer and Google Chrome. Use IntelliJ IDEA's Maven integration or an installed Maven command-line distribution.

1. Open `pom.xml` as a Maven project in IntelliJ IDEA.
2. Select JDK 17 or newer and reload the Maven dependencies.
3. Copy `.env.example` to `.env` in the repository root, unless a local `.env` already exists.
4. Fill in `TEST_PASSWORD` using the password shown on the SauceDemo login page. Set `STANDARD_USERNAME=standard_user` and `LOCKED_USERNAME=locked_out_user`; both accounts use the same demo password.

Configuration keys:

```dotenv
BASE_URL=https://www.saucedemo.com
STANDARD_USERNAME=standard_user
LOCKED_USERNAME=locked_out_user
TEST_PASSWORD=
```

The example intentionally leaves the password empty. Fill it in only in your local `.env`. Git ignores `.env`; `.env.example` contains no password and can be committed.

## Run the test locally

In IntelliJ, open `LoginTest` and run the class using its green run icon to execute all five login cases. The working directory must be the repository root so the `.env` file can be found.

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

The workflow supplies the secret as the `TEST_PASSWORD` environment variable for the **Run tests** step. `dotenv-java` reads the process environment value in preference to the empty password in the copied `.env` template. The application URL and both account names come from `.env.example`. The secret name remains `TEST_PASSWORD` for the shared demo password.

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

1. Review and merge the login-test pull request after its checks pass.
2. Implement logout and post-logout access checks.
3. Implement the other selected cases and create reproducible defect tickets.
4. Execute the expanded suite and write the release status report.
