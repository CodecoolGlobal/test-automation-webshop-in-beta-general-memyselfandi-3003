# BUG-SORT-003 - Price sorting does not order displayed prices for visual_user

**Tracking:** [GitHub issue](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/13)

**Defect ID:** BUG-SORT-003
**Status:** Reproduced; open application defect.
**Affected accounts:** visual_user
**Suggested severity:** Medium

## Environment and preconditions

Verified on 18 November 2026 at https://www.saucedemo.com/ using Chrome 158.0.8127.36 on Windows, headless, window 1920 × 1080 (content viewport 1904 × 929). Start a fresh browser session. Use the valid demo password displayed on the application's login page; do not include it in this ticket.

## Steps to reproduce

1. Sign in as visual_user.
2. Select Price (low to high); read all six displayed prices in catalog order.
3. Reload /inventory.html, select Price (high to low), and read the displayed prices again.

## Expected result

Displayed prices should be in ascending or descending numeric order according to the selected option.

## Actual result

The requested option remains selected, but prices are not ordered. One low-to-high observation was 16.26, 22.39, 70.44, 86.96, 25.81, 99.54; high-to-low was 66.87, 79.69, 40.15, 32.95, 20.09, 3.26. Prices change between renderings, so these numbers are examples, not fixed fixtures. Name Z–A did reorder the names.

## Impact

Price-based browsing is unreliable; the defect is the displayed order, without assuming a fixed pricing rule.

## Evidence and scope

Reproduced with a temporary Selenium diagnostic run on 18 November 2026. These observations are separate from the 23-execution JUnit suite; no new automated case is implied. Structured evidence is retained in the repository under [structured evidence](../evidence/2026-11-18/rechecks.json).

Adding this report does not fix the external application. Keep this issue open until the behavior is corrected and retested.
