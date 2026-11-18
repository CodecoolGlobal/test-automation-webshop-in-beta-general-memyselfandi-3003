# BUG-CART-002 - Backpack Remove is ineffective on Products and product details

**Tracking:** [GitHub issue](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/17)

**Defect ID:** BUG-CART-002
**Status:** Reproduced; open application defect.
**Affected accounts:** problem_user, error_user
**Suggested severity:** Medium

## Environment and preconditions

Verified on 18 November 2026 at https://www.saucedemo.com/ using Chrome 158.0.8127.36 on Windows, headless, window 1920 × 1080 (content viewport 1904 × 929). Start a fresh browser session. Use the valid demo password displayed on the application's login page; do not include it in this ticket.

## Steps to reproduce

1. Sign in as problem_user with an empty cart and add Sauce Labs Backpack.
2. On Products, select its Remove button; inspect the badge and cart contents.
3. In a fresh session, add Backpack, open https://www.saucedemo.com/inventory-item.html?id=4 and select Remove.
4. Repeat independently as error_user.

## Expected result

Remove should delete Backpack and restore Add to cart; an empty cart should have no badge.

## Actual result

Both entry points leave Remove displayed, the badge at 1, and Backpack in the cart. Removal from the cart page works for both accounts and was used to verify cleanup.

## Impact

Customers cannot remove Backpack through the list or its details. Cart-page removal is a working alternative.

## Evidence and scope

Reproduced with a temporary Selenium diagnostic run on 18 November 2026. These observations are separate from the 23-execution JUnit suite; no new automated case is implied. Structured evidence is retained in the repository under [structured evidence](../evidence/2026-11-18/rechecks.json).

Adding this report does not fix the external application. Keep this issue open until the behavior is corrected and retested.
