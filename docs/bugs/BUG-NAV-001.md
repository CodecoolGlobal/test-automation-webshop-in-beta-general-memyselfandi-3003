# BUG-NAV-001 - About opens a Page Not Found destination

**Tracking:** [GitHub issue](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/21)

**Defect ID:** BUG-NAV-001
**Status:** Reproduced; open application defect.
**Affected accounts:** problem_user
**Suggested severity:** Low

## Environment and preconditions

Verified on 18 November 2026 at https://www.saucedemo.com/ using Chrome 158.0.8127.36 on Windows, headless, window 1920 × 1080 (content viewport 1904 × 929). Start a fresh browser session. Use the valid demo password displayed on the application's login page; do not include it in this ticket.

## Steps to reproduce

1. Sign in as problem_user.
2. Open the navigation menu and select About.
3. Inspect the destination URL, page title and visible page message.

## Expected result

About should open the Sauce Labs information destination.

## Actual result

The menu targets https://saucelabs.com/error/404 and arrives there. The title is Not Found and the visible heading is Page Not Found. The same menu entry for standard_user targets and opens https://saucelabs.com/. No claim is made about the unmeasured HTTP response status.

## Impact

The About navigation leads to an error page.

## Evidence and scope

Reproduced with a temporary Selenium diagnostic run on 18 November 2026. These observations are separate from the 23-execution JUnit suite; no new automated case is implied. Structured evidence is retained in the repository under [structured evidence](../evidence/2026-11-18/rechecks.json).

Adding this report does not fix the external application. Keep this issue open until the behavior is corrected and retested.
