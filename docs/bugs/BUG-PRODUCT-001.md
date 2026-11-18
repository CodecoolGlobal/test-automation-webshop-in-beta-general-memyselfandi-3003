# BUG-PRODUCT-001 - Backpack image and name open Fleece Jacket details

**Tracking:** [GitHub issue](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/14)

**Defect ID:** BUG-PRODUCT-001
**Status:** Reproduced; open application defect.
**Affected accounts:** problem_user
**Suggested severity:** Medium

## Environment and preconditions

Verified on 18 November 2026 at https://www.saucedemo.com/ using Chrome 158.0.8127.36 on Windows, headless, window 1920 × 1080 (content viewport 1904 × 929). Start a fresh browser session. Use the valid demo password displayed on the application's login page; do not include it in this ticket.

## Steps to reproduce

1. Sign in as problem_user and locate Sauce Labs Backpack ($29.99).
2. Click the Backpack image and inspect the destination name, price and URL.
3. Return to /inventory.html and independently click the Backpack name.

## Expected result

Both links should open Sauce Labs Backpack details, matching the selected product.

## Actual result

Both links open /inventory-item.html?id=5, displaying Sauce Labs Fleece Jacket at $49.99. Backpack's own details are /inventory-item.html?id=4.

## Impact

The selected product and destination product differ for both entry points.

## Evidence and scope

Reproduced with a temporary Selenium diagnostic run on 18 November 2026. These observations are separate from the 23-execution JUnit suite; no new automated case is implied. Structured evidence is retained in the repository under [structured evidence](../evidence/2026-11-18/rechecks.json).

Adding this report does not fix the external application. Keep this issue open until the behavior is corrected and retested.
