# SauceDemo — Test Cases

This is the initial test specification for [SauceDemo](https://www.saucedemo.com/), based on manual exploration. It contains **22 logical test cases** across the six required user stories, with **1–5 cases per story**. **TC-LOGIN-01 through TC-LOGIN-05, TC-LOGOUT-01 through TC-LOGOUT-02, the four standard-user sorting cases, TC-PRODUCT-01 through TC-PRODUCT-02, TC-CART-01 through TC-CART-04, and TC-CHECKOUT-01 through TC-CHECKOUT-05 are implemented. A combined local headless Maven run on 17 November 2026 passed all twenty-two baseline executions with no failures, errors, or skipped tests.** All 22 logical cases have a baseline implementation. The additional `error_user` execution of TC-SORT-03 is planned and not implemented. The earlier five-login-test workflow succeeded in **GitHub Actions run #5** for commit [`16d512f`](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/commit/16d512f2b9bf5e2b8a547742181df6147eb72775/checks). The earlier seven-test workflow succeeded in [GitHub Actions run #9](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/actions/runs/44131621619) for commit `ede1771`. The push workflow for the eleven-test version succeeded for commit [`60e6af9`](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/commit/60e6af935c73a21b4cc60d0aba6c2fa64983411d/checks); its `test` job took 56 seconds, as shown in the user-provided workflow summary. Initial exploratory observations are kept separate from automated execution results.

## Scope and accounts

| Account | Scope in this initial suite |
| --- | --- |
| `standard_user` | Main account for all six user stories, including applicable negative cases. |
| `locked_out_user` | One negative login case. Post-login cases are not applicable. |
| `error_user` | An additional execution of the ascending-price sorting case to cover an observed account-specific failure. |
| `problem_user` | Exploration and defect documentation; automated coverage deferred in this initial suite. |
| `performance_glitch_user` | Exploration and defect documentation; timing checks deferred until measurement conditions and thresholds are defined. |
| `visual_user` | Exploration and defect documentation; visual and sorting automation deferred in this initial suite. |

TC-SORT-03 has two account variants, so the plan contains **23 account-specific executions**. Confirm the assignment's interpretation of parameterized cases if execution rows are counted separately; even then, sorting has five executions. The other user stories remain within the same limit.

This selection provides a manageable starting scope. It does not establish that the deferred accounts are safe to release. Confirmed defects found during exploration still require tickets, whether or not they are included in automation.

## Execution rules

- Use an independent browser session for each case. Start with an empty cart and no authenticated session unless the case specifies otherwise.
- Use the same browser and record its version, operating system, application URL, and execution date with the results.
- Supply valid passwords through environment variables or CI secrets. Do not store passwords in the repository or test evidence.
- Use `standard_user` unless a case names a different account. For protected-page cases, sign in before performing the listed steps.
- Use ordinary valid checkout data, for example First Name `Alex`, Last Name `Tester`, and Postal Code `12345`. These are sample inputs, not a specification of accepted formats.
- In missing-field checkout cases, leave only the named field empty and fill the other two.
- Use a stable product fixture for repeatable execution. A suggested primary product is **Sauce Labs Backpack**, with **Sauce Labs Bike Light** as a second distinct product. Confirm these products are available when implementing the suite.
- Verify state using meaningful page content, product identity, and cart contents. A URL change or badge alone is not sufficient evidence for the whole flow.
- Do not use Reset App State as the sole cleanup mechanism: exploration found that it can leave stale product buttons.
- Each case is an independent result. A failure that prevents a later assertion in the same case should be recorded with the step where execution stopped.

## US-01 — Log in

**Goal:** users can identify themselves, while invalid or restricted attempts are handled correctly.

| ID | Case / type | Steps | Expected result |
| --- | --- | --- | --- |
| TC-LOGIN-01 | Successful login — Positive | Open the login page. Enter valid `standard_user` credentials. Select Login. | The product list appears and authenticated navigation is available. No login error is displayed. |
| TC-LOGIN-02 | Incorrect password — Negative | Enter `standard_user` and a non-empty incorrect password. Select Login. | Authentication is rejected. A visible credential error appears and the product list is not accessible through the attempted login. |
| TC-LOGIN-03 | Missing username — Negative | Leave Username empty. Enter a valid password. Select Login. | Authentication is rejected and a visible message identifies the missing username. |
| TC-LOGIN-04 | Missing password — Negative | Enter `standard_user`. Leave Password empty. Select Login. | Authentication is rejected and a visible message identifies the missing password. |
| TC-LOGIN-05 | Locked account — Negative | Enter `locked_out_user` and its valid password. Select Login. | Authentication is rejected and a visible message identifies the account restriction. The product list does not appear. |

**Automation status:** all five cases are implemented in `LoginTest`. Negative cases wait for the visible login error, assert its exact text, and check that the login URL is unchanged. TC-LOGIN-05 expects: `Epic sadface: Sorry, this user has been locked out.` The tester reported that all five cases passed locally. The expanded login-suite workflow also succeeded in GitHub Actions run #5, including its test and report-upload steps.

## US-02 — Log out

**Goal:** a signed-in user can end the session and protected content is no longer accessible.

| ID | Case / type | Steps | Expected result |
| --- | --- | --- | --- |
| TC-LOGOUT-01 | End the session — Positive | Sign in. Open the navigation menu. Select Logout. | The login page appears and authenticated controls are no longer available. |
| TC-LOGOUT-02 | Protected-page access after logout — Negative | Sign in and record the product-list URL. Log out. Use browser Back and inspect the page. Then navigate directly to the recorded URL. | Neither route restores usable authenticated product content. The application requires authentication again. |

**Exploration note:** manual logout was observed to work for all accounts that could sign in. **Automation status:** both cases are implemented in `LogoutTest` using `standard_user`, and passed in the combined local headless Maven run. TC-LOGOUT-02 checks browser Back first and direct URL navigation second; if the first attempt fails, the second is not reached in that execution. A visible login form and zero matching product cards are required after each attempt. The seven-test workflow also succeeded in GitHub Actions run #9, including test execution and report upload.

## US-03 — Browse an ordered product list

**Goal:** every product is present in the selected order. These controls are sorting options rather than filters that remove products.

For each sorting case, capture the product identities before sorting and verify that the same products remain afterwards. Check the complete sequence, including adjacent values, rather than only the first and last items. Parse prices as numbers.

| ID | Case / type | Steps | Expected result |
| --- | --- | --- | --- |
| TC-SORT-01 | Name A–Z — Positive | Sign in and select Name A–Z. Read all displayed product names. | Names are in ascending alphabetical order and the product set is unchanged. |
| TC-SORT-02 | Name Z–A — Positive | Sign in and select Name Z–A. Read all displayed product names. | Names are in descending alphabetical order and the product set is unchanged. |
| TC-SORT-03 | Price low–high — Positive | Sign in and select Price low–high. Read all displayed prices. Run independently for `standard_user` and `error_user`. | Prices are in nondecreasing numeric order, the product set is unchanged, and no sorting-error popup appears. |
| TC-SORT-04 | Price high–low — Positive | Sign in and select Price high–low. Read all displayed prices. | Prices are in nonincreasing numeric order and the product set is unchanged. |

**Automation status:** `ProductSortingTest` implements all four cases for `standard_user`. Each test starts in a fresh authenticated browser session and requires at least two products. Selenium `Select` chooses the visible option text; assertions compare the entire displayed name or numeric price sequence with a sorted copy of the original data and verify the selected option. Name-list equality preserves product names and their counts; price cases additionally compare the product names independently of order. TC-SORT-01 first selects Z–A to avoid passing solely because A–Z is the initial order. The four sorting tests passed in the combined eleven-test local run. The additional `error_user` variant of TC-SORT-03 is not implemented yet and must not be reported as passing.

**Known observation:** sorting with `error_user` displayed “Sorting is broken! This error has been reported to Backtrace.” Its variant of TC-SORT-03 is expected to expose that failure if it is still present. The expected result remains correct sorting; the test must not pass merely because the popup appears. This is a positive functional case, not a negative-input test.

## US-04 — View a product

**Goal:** product-list links open the corresponding product detail page.

| ID | Case / type | Steps | Expected result |
| --- | --- | --- | --- |
| TC-PRODUCT-01 | Open via product image — Positive | Sign in. Record the primary product's name, price, description, and image from the list. Select its image. | A dedicated detail page opens for that product. Name, numeric price, description, and image correspond to the selected list item. |
| TC-PRODUCT-02 | Open via product name — Positive | Start from a fresh session and sign in. Record the primary product's details. Select its name. | The corresponding detail page opens and displays consistent product information. |

**Automation status:** both cases are implemented in `ProductDetailsTest` for `standard_user`, using Sauce Labs Backpack. Each independent session captures the list price, description, and image source before clicking. Assertions require a dedicated product-detail URL, the selected product name, matching numeric price and description, the same image asset URL, and a visible image element. Image dimensions and pixel appearance are not compared; the current application uses the same asset on both pages. The tests compare cross-page consistency, not the correctness of the catalog data against an external specification. Both cases passed in the combined thirteen-test local headless run. The list links use a placeholder `href`; tests check the actual navigation after clicking rather than requiring a destination URL in the link markup.

## US-05 — Manage the cart

**Goal:** adding and removing products keeps controls, cart contents, and the item-count indicator consistent.

| ID | Case / type | Steps | Expected result |
| --- | --- | --- | --- |
| TC-CART-01 | Add and remove from the list — Positive | Sign in with an empty cart. Add the primary product from the list. Open the cart and verify its contents. Return to the list and remove that product. Reopen the cart. | After adding, the cart contains exactly the selected product, its list button displays Remove, and the count is 1. After removing, the cart is empty, the button displays Add to cart, and no positive cart count remains. |
| TC-CART-02 | Add and remove from product details — Positive | Sign in and open the primary product's detail page. Add it and inspect the cart. Reopen the same detail page and remove it. Inspect the cart again. | The detail-page controls perform both actions. Cart contents and count match each action; the button returns to Add to cart after removal. |
| TC-CART-03 | Remove from the cart page — Positive | Sign in. Add the primary product. Open the cart and select its Remove button. Return to the product list. | The product disappears from the cart, no positive cart count remains, and the list displays Add to cart for that product. |
| TC-CART-04 | Several distinct products — Positive | Sign in. Add the primary and secondary products. Open the cart. Remove only the primary product. | Initially the cart contains exactly the two selected products with the expected names and prices, and the count is 2. Removing one leaves the other unchanged and the count becomes 1. |

**Automation status:** all four cases are implemented in `CartTest` for `standard_user` and passed in the combined seventeen-test local headless run. Each case starts in a fresh authenticated browser session. Assertions inspect actual cart rows, exact product names, numeric prices captured from the list, quantities, badge counts, and relevant Add to cart / Remove buttons. The two-product case checks both original products and then confirms that removing Backpack leaves only Bike Light with its original price and quantity. The cart header helpers are shared in `BasePage`; list, detail, and cart-specific actions remain in their page objects. Reset App State is not used for cleanup.

## US-06 — Check out

**Goal:** users can complete an accurate order, while incomplete required customer information prevents progress.

**Shared setup:** sign in, add the primary and secondary products, open the cart, and start checkout. Retain their names, prices, and displayed quantities for verification.

| ID | Case / type | Steps | Expected result |
| --- | --- | --- | --- |
| TC-CHECKOUT-01 | Complete an order — Positive | Fill all three customer-information fields with valid data. Select Continue. Check the overview, then select Finish. | The overview contains exactly the selected products with consistent quantities and prices. Item subtotal equals the sum of line totals; total equals subtotal plus displayed tax. Finish displays an explicit order-completion confirmation. |
| TC-CHECKOUT-02 | Missing First Name — Negative | Leave First Name empty; fill Last Name and Postal Code. Select Continue. | A visible message identifies the required First Name. Checkout does not advance to the overview and no order is completed. |
| TC-CHECKOUT-03 | Missing Last Name — Negative | Fill First Name and Postal Code; leave Last Name empty. Select Continue. | A visible message identifies the required Last Name. Checkout does not advance to the overview and no order is completed. |
| TC-CHECKOUT-04 | Missing Postal Code — Negative | Fill First Name and Last Name; leave Postal Code empty. Select Continue. | A visible message identifies the required Postal Code. Checkout does not advance to the overview and no order is completed. |
| TC-CHECKOUT-05 | Cancel before completion — Alternative flow | From the customer-information step, select Cancel. Inspect the returned cart. | The cart page appears with both selected products and consistent prices and count. No order-completion confirmation appears. |

**Automation status:** all five cases are implemented in `CheckoutTest` for `standard_user`. Each fresh session adds Backpack and Bike Light, retains list prices and cart quantities, verifies the starting cart, and opens customer information. TC-CHECKOUT-01 checks exact product membership, prices, quantities, the sum of line totals, and subtotal plus displayed tax before selecting Finish. It then verifies the completion URL and explicit confirmation text. Monetary sums use a tolerance of 0.001 USD to allow `double` rounding while detecting a one-cent discrepancy. Missing-field cases assert the exact required-field message, the unchanged customer-information URL, a visible Continue button, and no visible completion confirmation. Cancel returns to the same cart with both product prices, quantities, and badge count preserved. All five cases passed in the combined twenty-two-test local headless run. These are UI checks; no backend order records are inspected.

**Assertion limit:** checking arithmetic with the displayed tax does not verify the correctness of the tax policy. A tax-rate expectation requires a separate business rule. No special postal-code format is assumed by this plan.

## Planned coverage summary

| User story | Logical cases | Account-specific executions |
| --- | ---: | ---: |
| Log in | 5 | 5 |
| Log out | 2 | 2 |
| Ordered product list | 4 | 5 |
| Product details | 2 | 2 |
| Cart | 4 | 4 |
| Checkout | 5 | 5 |
| **Total** | **22** | **23** |

The plan includes **8 negative cases**: four login cases, one post-logout access case, and three required-field checkout cases. Sorting and cart failures found in other accounts are functional defects; they do not turn those checks into negative tests.

## Deferred and additional coverage

The following observations remain relevant but are outside this initial automated suite:

- Reset App State and stale Remove buttons across accounts.
- `problem_user` product images, product-specific cart failures, checkout input corruption, and About navigation failure.
- `error_user` product-specific cart failures, checkout validation ambiguity, and ineffective Finish action.
- `visual_user` displaced elements and price-sorting failures.
- Measured delays and repeated Dynamic Catalog entries for `performance_glitch_user`.
- Order PDF contents and post-order Back Home.
- Empty-cart checkout and other input boundaries once expected behavior is confirmed.

Keep these findings in defect tracking or exploratory notes. Expand automation according to risk, confirmed requirements, and available time. Do not change assertions to accept known faulty behavior.

## Execution results and issue links

**Local headless Maven run, 17 November 2026:** the complete suite passed with **22 tests, 0 failures, 0 errors, and 0 skipped tests**. This result was verified from the generated Surefire reports, rather than inferred from exploratory notes.

| Case ID | Account / input | Local result | Evidence | Issue |
| --- | --- | --- | --- | --- |
| TC-LOGIN-01 | `standard_user`, valid credentials | Pass | Local Maven / Surefire | — |
| TC-LOGIN-02 | `standard_user`, incorrect password | Pass | Local Maven / Surefire | — |
| TC-LOGIN-03 | Empty username, valid password | Pass | Local Maven / Surefire | — |
| TC-LOGIN-04 | `standard_user`, empty password | Pass | Local Maven / Surefire | — |
| TC-LOGIN-05 | `locked_out_user`, valid password | Pass | Local Maven / Surefire | — |
| TC-LOGOUT-01 | `standard_user`, logout | Pass | Local Maven / Surefire | — |
| TC-LOGOUT-02 | `standard_user`, post-logout Back and direct URL | Pass | Local Maven / Surefire | — |
| TC-SORT-01 | `standard_user`, Name A–Z | Pass | Local Maven / Surefire | — |
| TC-SORT-02 | `standard_user`, Name Z–A | Pass | Local Maven / Surefire | — |
| TC-SORT-03 | `standard_user`, Price low–high | Pass | Local Maven / Surefire | — |
| TC-SORT-03 | `error_user`, Price low–high | Not Run | Not implemented; exploratory observation only | Ticket pending |
| TC-SORT-04 | `standard_user`, Price high–low | Pass | Local Maven / Surefire | — |
| TC-PRODUCT-01 | `standard_user`, Backpack image | Pass | Local Maven / Surefire | — |
| TC-PRODUCT-02 | `standard_user`, Backpack name | Pass | Local Maven / Surefire | — |
| TC-CART-01 | `standard_user`, add/remove from list | Pass | Local Maven / Surefire | — |
| TC-CART-02 | `standard_user`, add/remove from details | Pass | Local Maven / Surefire | — |
| TC-CART-03 | `standard_user`, remove from cart | Pass | Local Maven / Surefire | — |
| TC-CART-04 | `standard_user`, two distinct products | Pass | Local Maven / Surefire | — |
| TC-CHECKOUT-01 | `standard_user`, complete order | Pass | Local Maven / Surefire | — |
| TC-CHECKOUT-02 | `standard_user`, missing First Name | Pass | Local Maven / Surefire | — |
| TC-CHECKOUT-03 | `standard_user`, missing Last Name | Pass | Local Maven / Surefire | — |
| TC-CHECKOUT-04 | `standard_user`, missing Postal Code | Pass | Local Maven / Surefire | — |
| TC-CHECKOUT-05 | `standard_user`, cancel checkout | Pass | Local Maven / Surefire | — |

Local verification details:

- Execution: Maven 3.9.16 with `CI=true`; headless Chrome.
- Java runtime: 25.0.4.1; compiler release: 17.
- Operating system: Windows.
- Installed Chrome: 157.0.8103.78.
- Reports: `target/surefire-reports/TEST-com.codecool.webshop.tests.LoginTest.xml`, `target/surefire-reports/TEST-com.codecool.webshop.tests.LogoutTest.xml`, `target/surefire-reports/TEST-com.codecool.webshop.tests.ProductSortingTest.xml`, `target/surefire-reports/TEST-com.codecool.webshop.tests.ProductDetailsTest.xml`, `target/surefire-reports/TEST-com.codecool.webshop.tests.CartTest.xml`, and `target/surefire-reports/TEST-com.codecool.webshop.tests.CheckoutTest.xml`, with their corresponding `.txt` summaries.

**Earlier CI evidence:** GitHub Actions run **#5**, for commit [`16d512f`](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/commit/16d512f2b9bf5e2b8a547742181df6147eb72775/checks), completed successfully with the five-login-test version. Its test and report-upload steps succeeded.

**Earlier seven-test CI verification, 17 November 2026:** [GitHub Actions run #9](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/actions/runs/44131621619), for commit `ede1771`, completed successfully in **29 seconds**. Its **Run tests** and **Upload test results** steps succeeded for the seven-test login/logout version. The workflow summary provides the run-level evidence; the `test-results` artifact contains the detailed Surefire reports.

**Eleven-test push CI verification, 17 November 2026:** The push workflow for the eleven-test version also succeeded on **17 November 2026**, for commit [`60e6af9`](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/commit/60e6af935c73a21b4cc60d0aba6c2fa64983411d/checks). The user-provided GitHub Actions summary shows a successful `test` job with a duration of **56 seconds**. This is workflow-level evidence; the summary records success for that earlier eleven-test version.

**Earlier thirteen-test version:** The thirteen-test version passed locally. On **17 November 2026**, the tester also reported that all tests passed in GitHub Actions for the product-details version, commit [`7085166`](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/commit/7085166d30cf0f056faf8a58455e6c51ad1773ef/checks). Local counts were verified from Surefire reports; the CI result is based on the tester's confirmation.

**Earlier seventeen-test version:** The seventeen-test version passed locally in headless Chrome on **17 November 2026**. Its push workflow also succeeded in [GitHub Actions run #23](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/actions/runs/44149916341) for commit `69373de`. The `test` job completed in **1 minute 5 seconds**, as verified directly in GitHub. This records workflow-level CI success; the local per-class results are listed separately above.

**Current twenty-two-test version:** verified locally on 17 November 2026. Its first GitHub Actions run is pending the checkout branch push.

The additional `error_user` execution of TC-SORT-03 is **Not Run** and not implemented. All 22 logical cases have baseline automated coverage; this does not establish release readiness for the other accounts or resolve exploratory defects. Record each later account-specific execution separately with its result, actual behavior, evidence, and related issue where applicable.

Allowed results: **Not Run**, **Pass**, **Fail**, and **Blocked**. Record a blocker and its issue when applicable. Never mark an unexecuted case as passed based on another account's result.

## Implementation order

1. **Completed:** initialize the Java/Maven project with Selenium, JUnit, separate page objects, and isolated browser sessions.
2. **Completed:** implement five login cases and verify their workflow in GitHub Actions run #5.
3. **Completed locally:** implement two logout cases and verify the complete seven-test suite in a headless Maven run.
4. **Completed:** verify the seven-test workflow in GitHub Actions run #9; test execution and report upload succeeded. The logout pull request was merged into `main` before starting the sorting branch.
5. **Completed:** implement four standard-user sorting cases, verify the complete eleven-test suite locally, and verify the successful push workflow (56-second `test` job). The sorting pull request was merged into `main` before starting the product-details branch.
6. **Completed:** implement and review both product-detail navigation cases, verify all thirteen tests locally, and record the tester-reported successful GitHub Actions run. The product-details pull request was merged into `main` before starting the cart branch.
7. **Completed:** implement and review the four cart cases, verify all seventeen tests locally, and verify the successful push workflow in GitHub Actions run #23 (1 minute 5 seconds). The cart pull request was merged into `main` before starting the checkout branch.
8. **Completed locally:** implement the five checkout cases and verify the complete twenty-two-test suite. Code review, push, CI, and the checkout pull request remain pending.
9. **Planned:** implement and execute the `error_user` sorting variant.
10. **Planned:** review failures, file reproducible issues, and write the release report with the actual tested account scope.

Selenium Grid can be added as a separate infrastructure extension. It is not required to begin implementing these cases.
