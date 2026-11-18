# BUG-SORT-002 - Nondefault sorting choices do not change the problem-user catalog

**Tracking:** [GitHub issue](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/12)

**Defect ID:** BUG-SORT-002
**Status:** Reproduced; open application defect.
**Affected accounts:** problem_user
**Suggested severity:** Medium

## Environment and preconditions

Verified on 18 November 2026 at https://www.saucedemo.com/ using Chrome 158.0.8127.36 on Windows, headless, window 1920 × 1080 (content viewport 1904 × 929). Start a fresh browser session. Use the valid demo password displayed on the application's login page; do not include it in this ticket.

## Steps to reproduce

1. Sign in as problem_user; note the initial Name (A to Z) order.
2. Select Name (Z to A), then inspect the selected option and product order.
3. Reload /inventory.html before independently testing Price (low to high) and Price (high to low).

## Expected result

The selected option should remain selected and the complete catalog should follow that order.

## Actual result

All three nondefault choices revert to Name (A to Z); names and prices stay in their initial order. Prices remain 29.99, 9.99, 15.99, 49.99, 7.99, 15.99. No alert appears. Default A–Z alone was not used as evidence of sorting.

## Impact

Three sorting choices are ineffective. This differs from BUG-SORT-001's error-user alert.

## Evidence and scope

Reproduced with a temporary Selenium diagnostic run on 18 November 2026. These observations are separate from the 23-execution JUnit suite; no new automated case is implied. Structured evidence is retained in the repository under [structured evidence](../evidence/2026-11-18/rechecks.json).

Adding this report does not fix the external application. Keep this issue open until the behavior is corrected and retested.
