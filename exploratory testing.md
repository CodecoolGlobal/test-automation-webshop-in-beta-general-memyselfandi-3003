# SauceDemo — Webshop Test Project

Test planning and quality assessment for [SauceDemo](https://www.saucedemo.com/), covering authentication, product browsing, shopping cart management, and checkout.

**Current stage:** initial manual exploration completed; test design and automation are planned. This README records the tester's observations and provides a starting point for choosing automated tests. It does not describe an implemented automation framework or a completed CI pipeline.

## Project objectives

- Define 1–5 focused test cases for each required user story.
- Automate suitable cases without manual browser interaction.
- Keep test code maintainable through clear responsibilities and reusable page objects.
- Create reproducible GitHub issues for confirmed defects.
- Run automated tests in a GitHub-connected pipeline and retain readable results.
- Produce a release status report based on the test results and documented coverage.

## Required scope

| User story | What needs to be verified |
| --- | --- |
| Log in | Valid users can sign in; invalid or restricted access is rejected. |
| Log out | Users can end their session. |
| Browse an ordered product list | Products appear in the selected name or price order. |
| View a product | A product has its own detail page with consistent information. |
| Manage the cart | Users can add products, review their cart, and remove products. |
| Check out | Users can enter the required information and complete an order. |

Additional features explored: Reset App State, About, Dynamic Catalog, order PDF generation, and Back Home.

## How to read the findings

The findings below come from manual exploration, not automated test reports.

- **Observed working:** the described interaction worked during exploration; this is not proof of complete feature coverage.
- **Observed failure:** an interaction did not work as described in the notes. Exact reproduction details may still need to be collected.
- **Blocked:** an earlier failure prevented the next step from being tested through the normal flow.
- **Not documented:** the notes do not establish the outcome.

The original notes reference screenshots, but the image files are not included with this README. Browser version, operating system, and execution timestamps were not recorded. Add these details and attach the relevant evidence when creating issues.

## Test accounts and selection

All six accounts were explored. Full regression coverage does not need to be duplicated for every account: select additional accounts when they cover a different behavior or risk.

| Account | Observed behavior | Proposed testing role |
| --- | --- | --- |
| `standard_user` | Main shopping flow works in the explored cases. A reset-related UI inconsistency was observed. | Primary account for the six required user stories. |
| `locked_out_user` | Sign-in is rejected. | Focused negative login case. Post-login cases are not applicable. |
| `problem_user` | Product images, sorting, cart actions, checkout input, and About show problems. | Targeted defect reproduction and selected regression cases. |
| `performance_glitch_user` | Login and navigation are slow; checkout completes. Dynamic Catalog behaves differently. | Exploratory performance observations and targeted functional checks. |
| `error_user` | Sorting reports an error; some cart actions fail; Finish does not complete the order. | Targeted error-handling and defect reproduction cases. |
| `visual_user` | Layout problems and price-sorting failures were observed; checkout can be completed. | Selected visual checks and sorting regression cases. |

This is a proposed automation scope, not a claim that all account-specific tests already exist. Whether each account represents a supported release configuration must be stated in the final release report.

**Credentials:** supply login data through environment variables or CI secrets. Do not commit passwords or populated local environment files.

## Manual exploration findings

### `standard_user`

**Observed working**

- Products can be added to the cart from the product list.
- Removing a product from the list updates the cart and decreases its item-count indicator.
- All four sorting options work: name ascending, name descending, price ascending, and price descending.
- Products can be removed from the cart page.
- Checkout reaches the order completion page.
- Generate PDF Order produces a PDF file. Its contents have not been documented as verified.
- Back Home returns to the product list.
- Clicking a product image opens a product detail page; an observed URL was `/inventory-item.html?id=0`.
- Logout signs the user out.

**Other observations**

- About opens an external web application; the exact destination was not recorded.
- Dynamic Catalog shows product images, names, and prices. The displayed products are not clickable. The notes describe one entry per product and no stock-quantity information; the intended behavior has not been confirmed.
- Reset App State clears the cart, but previously added products still display **Remove**. Refreshing the page restores the expected button display.

**Not documented:** Add to cart and Remove on the product detail page.

### `locked_out_user`

- Sign-in is rejected.
- The exact error message still needs to be recorded.
- Post-login features, including logout, are not applicable because the account cannot sign in.

### `problem_user`

- The product list displays the same image for every product.
- None of the sorting options changes the order.
- Some products cannot be added from the product list.
- Remove does not work on the product list for products already added.
- Remove works on the cart page.
- When typing into Last Name after entering First Name, characters affect First Name instead of populating Last Name. Characters were reported to replace one another rather than form the intended text.
- This input problem blocks completion of the normal checkout flow. Finish, order PDF generation, and post-order Back Home are therefore blocked.
- Clicking a product image opens a detail page; an observed URL was `/inventory-item.html?id=5`. The detail page displays the product's individual image instead of the repeated list image. Consistency of product identity across pages still needs checking.
- Some detail-page Add to cart actions fail. The notes also describe products that cannot be added from their detail page but can be added from the list. Record the exact products for each behavior.
- Remove does not work on the product detail page.
- Dynamic Catalog behaves as described for `standard_user`.
- About displays a “page not found” result; the destination URL and exact message need recording.
- Reset App State behaves as described for `standard_user`.
- Logout signs the user out.

### `performance_glitch_user`

- Login and page transitions are noticeably slow. No timings were recorded, so no numerical performance threshold has been assessed.
- Remove works on the product list.
- Sorting works according to the notes; each of the four options should be recorded explicitly during formal execution.
- Checkout and Finish work, and a PDF can be generated.
- Back Home works with a noticeable delay.
- Clicking a product image opens a detail page; an observed URL was `/inventory-item.html?id=4`.
- About behaves as described for `standard_user`.
- After Reset App State, Remove remains displayed. Clicking it restores Add to cart without requiring a page refresh.
- Dynamic Catalog displays images and multiple size variants. Products are not clickable. Scrolling repeatedly loads the same items. Whether this repetition violates the intended catalog behavior needs confirmation.
- Logout signs the user out.

**Not documented:** Remove on the cart page, and Add to cart / Remove on the product detail page.

### `error_user`

- Some products can be added from the list, while others cannot. Record the exact product names.
- Remove does not work on the product list but does work on the cart page.
- Sorting displays a popup: “Sorting is broken! This error has been reported to Backtrace.”
- Clicking a product image opens a product detail page.
- Some products can be added from their detail page, while others cannot. Remove does not work there.
- The checkout notes indicate that First Name and Postal Code are required, while Last Name may be omitted. Reconfirm the exact field behavior before filing a validation defect.
- Finish does not complete the order. Order PDF generation and post-order Back Home are blocked through this flow.
- Dynamic Catalog, About, and Reset App State behave as described for `standard_user`.
- Logout signs the user out.

Acceptance of arbitrary text is not classified as a defect without a defined validation rule.

### `visual_user`

- Layout issues affect the cart icon, checkout text, and some product-list controls. Capture the exact affected elements and viewport size for reproducible visual issues.
- Name sorting works in both directions; price sorting does not work.
- Add to cart and Remove work on the product list.
- Remove works on the cart page.
- Add to cart and Remove work on the product detail page.
- Checkout can be completed with valid data.
- Dynamic Catalog and About behave as described for `standard_user`.
- Reset App State shows the same stale-button behavior described for `standard_user`, but clicking Remove restores Add to cart without refreshing.
- Logout signs the user out.

**Not documented:** order PDF generation and post-order Back Home.

## Proposed test cases

These are planned cases, not executed results. They keep the initial suite within the assignment's limit of 1–5 cases per user story. Account-specific variants should be selected deliberately rather than multiplying the complete suite across every account.

| User story | Proposed cases |
| --- | --- |
| Log in — 4 cases | Valid credentials; invalid password; missing required credentials; locked account rejected. |
| Log out — 2 cases | Logout returns the user to the login page; protected pages remain inaccessible after logout, including direct navigation and browser Back. |
| Ordered product list — 4 cases | Verify complete name order A–Z; name order Z–A; price order low–high; price order high–low. |
| Product details — 2 cases | Open a product via its image and check identity, price, and description; open it via its name and check the same information. |
| Cart — 4 cases | Add and remove from the list; add and remove from the detail page; remove from the cart page; verify several distinct products and the cart count. |
| Checkout — 5 cases | Complete an order and verify its products and totals; omit First Name; omit Last Name; omit Postal Code; cancel checkout and verify the resulting cart state. |

Empty-cart checkout, PDF content, Back Home, reset consistency, About, and Dynamic Catalog are additional exploratory or extension candidates. Confirm their expected behavior and priority before expanding the initial suite.

For failed behaviors, automated assertions should describe the expected correct behavior. A known defect may therefore produce a failing test; do not change the assertion merely to accept the defect.

## Findings to turn into issues

The exploration identified the following issue candidates. This list does not mean GitHub tickets have already been created.

- **Shared reset inconsistency:** cart contents are cleared while product buttons still display Remove.
- **`problem_user`:** repeated product images, ineffective sorting, product-specific Add to cart failures, ineffective Remove actions, checkout input corruption, and an About navigation failure.
- **`error_user`:** sorting errors, product-specific Add to cart failures, ineffective Remove actions, and an unresponsive Finish action.
- **`visual_user`:** displaced interface elements and ineffective price sorting.
- **Further investigation:** checkout name-field requirements for `error_user`, repeated Dynamic Catalog entries for `performance_glitch_user`, and measured login/navigation delays.

Create separate issues for independently reproducible failures. If the same failure affects several accounts or pages, list those affected contexts in the issue rather than automatically creating duplicates.

Each issue should contain:

1. A clear title and affected account.
2. Environment details and the starting URL.
3. Preconditions, including cart and session state.
4. Exact steps, product names, and sample input values.
5. Expected and actual results.
6. Evidence, reproducibility, and any observed workaround.

## Planned implementation and CI

The discussed starting point is **Java, Selenium WebDriver, JUnit, Maven, and Page Object Model**, with **GitHub Actions** for CI. These choices remain proposed until the project is initialized.

The first implementation milestone is one complete path:

> Start a fresh browser → sign in as `standard_user` → verify the product list → close the browser.

Run that same test locally and in CI before expanding the suite. The pipeline should prepare the environment, run the tests, and retain test results and useful failure evidence. Setup instructions and the verified run command will be added after implementation.

**Selenium Grid is an optional extension.** GitHub Actions orchestrates the workflow; Grid provides browser sessions that the tests control through RemoteWebDriver. Grid is not required by the assignment's pipeline criterion, and running Grid locally does not move the workload off the local machine.

Tests must start from a known state and remain independent. Because Reset App State itself shows inconsistent UI behavior, do not rely on it as the sole cleanup mechanism without verifying the resulting state.

## Remaining work

- Complete the explicitly undocumented account checks above.
- Record exact locked-account feedback and product-specific failures.
- Verify negative login cases, required checkout fields, product-information consistency, and order totals.
- Verify session protection after logout; clicking Logout itself has already been observed to work for every account that can sign in.
- Confirm expected behavior for ambiguous catalog and validation observations.
- Finalize test cases and account selection, then implement the automation and CI pipeline.
- File confirmed issues with reproducible evidence.
- Write the release status report after executing the selected suite.

## Release assessment

No final release recommendation has been made. The exploratory notes show shopping-flow failures for several accounts, but the automated suite, formal execution results, and supported-account scope are not yet established.

The final status report must answer **“Is it safe to release this product?”** using the executed coverage, confirmed defects, blocked cases, and remaining risks. A successful flow with `standard_user` alone does not establish readiness for all account configurations.
