# BUG-CART-001 - Three products cannot be added from the list or details

**Tracking:** [GitHub issue](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/16)

**Defect ID:** BUG-CART-001
**Status:** Reproduced; open application defect.
**Affected accounts:** problem_user, error_user
**Suggested severity:** High

## Environment and preconditions

Verified on 18 November 2026 at https://www.saucedemo.com/ using Chrome 158.0.8127.36 on Windows, headless, window 1920 × 1080 (content viewport 1904 × 929). Start a fresh browser session. Use the valid demo password displayed on the application's login page; do not include it in this ticket.

## Steps to reproduce

1. Sign in as problem_user with an empty cart.
2. On Products, select Add to cart for Sauce Labs Bolt T-Shirt.
3. Inspect the button and badge, then open the cart to check its contents.
4. With an empty cart, navigate directly to https://www.saucedemo.com/inventory-item.html?id=1 and select Add to cart.
5. Repeat independently for error_user.

## Expected result

The selected product should appear in the cart with quantity 1; the badge and button should reflect the addition.

## Actual result

The button remains Add to cart, no badge appears and the cart remains empty. The same failure was confirmed for Sauce Labs Fleece Jacket (id=5) and Test.allTheThings() T-Shirt (Red) (id=3), on both surfaces for both accounts. Backpack, Bike Light and Onesie could be added. Detail checks used each product's direct URL to avoid the separate problem-user navigation defect.

## Impact

Three products cannot be purchased through either add entry point. Each attempt started with a verified empty cart.

## Evidence and scope

Reproduced with a temporary Selenium diagnostic run on 18 November 2026. These observations are separate from the 23-execution JUnit suite; no new automated case is implied. Structured evidence is retained in the repository under [structured evidence](../evidence/2026-11-18/rechecks.json).

Adding this report does not fix the external application. Keep this issue open until the behavior is corrected and retested.
