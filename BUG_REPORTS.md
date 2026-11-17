# SauceDemo defect reports

These reports are prepared locally for issue tracking. A local report ID does not mean a GitHub issue has been created.

## BUG-SORT-001 — Price low-to-high sorting fails for error_user

**Status:** confirmed locally on 17 November 2026; GitHub issue not created yet.

**Related test:** TC-SORT-03, `error_user` variant, implemented in `ErrorUserSortingTest.sortErrorUserPricesAscending`.

**Impact:** the affected account cannot display the catalog in ascending price order. A visible error interrupts the action. The same sorting case passes for `standard_user` in the same full-suite run.

### Environment

- Application: https://www.saucedemo.com/
- Account: `error_user`; use its valid password from local configuration or the application's login page.
- Browser: Chrome 157.0.8103.78, headless.
- OS: Windows; Java runtime 25.0.4.1, compiler release 17.
- Automation: Selenium 4.52.0, JUnit Jupiter 6.1.3, Maven 3.9.16.
- Reproduction evidence: one automated execution in the recorded full-suite run, consistent with the earlier exploratory observation.

### Preconditions

Start with a fresh browser session. Sign in successfully as `error_user` and wait for the Products page. The catalog contains six products in the initial Name (A to Z) order.

### Steps to reproduce

1. Open the application and sign in as `error_user` using valid credentials.
2. On Products, select **Price (low to high)** from the sorting dropdown.
3. Observe the alert and select OK to inspect the catalog afterwards.
4. Read the selected sorting option and the complete displayed price sequence.

### Expected result

No sorting-error alert appears. The selected option remains **Price (low to high)**. The same products remain present, and the prices are in ascending numeric order:

```text
7.99, 9.99, 15.99, 15.99, 29.99, 49.99
```

### Actual result

The application displays this alert:

```text
Sorting is broken! This error has been reported to Backtrace.
```

After accepting the alert, the selected option is **Name (A to Z)**. The prices remain in this unsorted sequence:

```text
29.99, 9.99, 15.99, 49.99, 7.99, 15.99
```

The original product names and their counts are preserved. The automation therefore reports three failed assertions inside one failed test: error alert, selected option, and price order. These are observations of the same sorting failure, not three separate test cases.

### Evidence and execution

```bash
mvn -Dtest=ErrorUserSortingTest test
```

The full local suite completed with **23 tests, 1 failure, 0 errors, and 0 skipped tests**; 22 executions passed. Maven returned a nonzero exit code because this functional requirement failed.

Generated evidence, excluded from Git:

- `target/surefire-reports/TEST-com.codecool.webshop.tests.ErrorUserSortingTest.xml`
- `target/surefire-reports/com.codecool.webshop.tests.ErrorUserSortingTest.txt`

The test accepts the alert only so that it can inspect the resulting catalog. It still requires correct sorting without an error alert. It is not skipped, disabled, or changed to accept the defect. No changes to the application under test are part of this report.
