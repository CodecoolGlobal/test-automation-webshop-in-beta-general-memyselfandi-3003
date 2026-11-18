# SauceDemo release status report

**Assessment date:** 18 November 2026  
**Recommendation: NO-GO — the tested product is not safe to release across the assessed account scope.**

Core purchase behavior fails for problem_user and error_user: three products cannot be added, customer information is lost or corrupted, and order completion stops without feedback. Sorting and product identity also fail for selected accounts. Passing standard-user checks does not establish readiness for the other supported accounts.

## Product and revision assessed

- Hosted application: https://www.saucedemo.com/. The application does not expose a tested build identifier; observations describe the site at execution time.
- Test code: main commit 69a1fe3 (merged PR #9); the feature/defect-reports branch adds documentation and evidence without changing the Java tests.
- Local verification: Windows, Chrome 158.0.8127.36, headless, window 1920 × 1080; Java runtime 25.0.4.1, compilation release 17; Selenium 4.52.0, JUnit Jupiter 6.1.3, Maven 3.9.16.
- Targeted recheck viewport: 1904 × 929. Visual findings are limited to those recorded dimensions.

## Automated execution results

The full suite ran locally on **18 November 2026**, finishing at **09:42:46 CET** in **58.585 seconds**. It executed **23 tests: 22 passed, 1 failed, 0 errors, 0 skipped**. Maven returned exit code 1.

| Test class | Executed | Passed | Failed |
| --- | ---: | ---: | ---: |
| LoginTest | 5 | 5 | 0 |
| LogoutTest | 2 | 2 | 0 |
| ProductSortingTest | 4 | 4 | 0 |
| ProductDetailsTest | 2 | 2 | 0 |
| CartTest | 4 | 4 | 0 |
| CheckoutTest | 5 | 5 | 0 |
| ErrorUserSortingTest | 1 | 0 | 1 |
| **Total** | **23** | **22** | **1** |

The failing test is ErrorUserSortingTest.sortErrorUserPricesAscending, exposing [BUG-SORT-001 / issue #8](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/8). Three assertions fail inside that single execution: unexpected alert, reverted option, and unsorted prices. No tests were disabled and no failures were ignored.

The same 23/1/0/0 result was previously verified in [GitHub Actions run #32 on 17 November](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/actions/runs/44163805385/job/134324359434). That is CI evidence from the earlier run; the 18 November execution above is local. Generated XML/text results are in target/surefire-reports; a portable [execution summary](docs/evidence/2026-11-18/junit-summary.txt) is retained with this report.

The tester-provided GitHub Actions log from **18 November 2026** also reports **23 tests: 22 passed, 1 failed, 0 errors, 0 skipped**. Only ErrorUserSortingTest.sortErrorUserPricesAscending failed, with the same three assertions for BUG-SORT-001. Maven finished at **10:03:55 UTC**, took **51.964 seconds**, and returned exit code 1. This is CI evidence supplied as a job-log excerpt; its run URL and tested commit were not included. The CDP warning mentions browser protocol version 157, whereas the local run above used Chrome 158. No new failing test appears in this CI result.

## Coverage and exploratory findings

The suite implements 22 logical cases across the six required stories, with 1–5 logical cases per story. TC-SORT-03 has two account executions, giving 23 in total. Eight cases are negative: four login, one post-logout access check, and three checkout required-field checks.

Standard_user provides the baseline for all six stories; locked_out_user covers restricted login; error_user has targeted automated price-sorting coverage. The other account variants were explored rather than fully automated. On 18 November, temporary Selenium diagnostics completed 20 targeted scenarios and confirmed fourteen additional defects. These are diagnostic observations, not twenty additional JUnit test cases.

All **15 confirmed defects** have open GitHub tickets, indexed in [BUG_REPORTS.md](BUG_REPORTS.md). Important release risks include:

| Risk | Evidence |
| --- | --- |
| Bolt T-Shirt, Fleece Jacket and Red T-Shirt cannot be added from the list or details for problem_user and error_user. | [Issue #16](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/16) |
| Last Name entry corrupts First Name and blocks problem_user checkout. | [Issue #18](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/18) |
| error_user loses Last Name input and Finish silently leaves the accepted order on its overview. | [Issue #19](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/19), [issue #20](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/20) |
| Product links can open a different item; images and descriptions can be incorrect or absent. | [Issue #14](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/14), [#11](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/11), [#15](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/15) |
| Sorting fails for error_user, problem_user and visual_user in the documented variants. | [Issue #8](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/8), [#12](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/12), [#13](https://github.com/CodecoolGlobal/test-automation-webshop-in-beta-general-memyselfandi-3003/issues/13) |

Reset-state inconsistency, ineffective product-page removal, broken About navigation and two visual misplacements are also recorded. Reporting a defect or merging its regression test does not fix the hosted application.

## Limits and unresolved questions

- This is selected browser-functional coverage, not exhaustive testing of every product/account combination.
- No cross-browser, mobile, accessibility, security, load or production payment testing was performed.
- Performance_glitch_user slowness has no agreed response-time threshold. Establish measurement conditions and an SLA before assigning a timing pass/fail result.
- Dynamic Catalog repetition and clickability need an agreed expected behavior.
- A separate visual_user checkout-text defect was not reproduced at the recorded viewport. Earlier broad notes are not treated as confirmed tickets without a concrete failure.
- PDF export was observed during the original manual exploration but is not an automated assertion.
- The Chrome/Selenium CDP version warning appeared; all 23 WebDriver executions completed, with no execution errors. It is not the recorded cause of the sorting failure.

## Conditions for reconsidering release

Correct the purchase blockers, product-identity failures and sorting defects, then retest their exact account/product/surface variants. Review the remaining defects explicitly and record any accepted residual risk with the responsible stakeholder. Resolve the open behavior and performance criteria, rerun the full suite in CI, and update this report with the actual revision and results.

A green standard-user baseline alone is insufficient for this broader release scope.
