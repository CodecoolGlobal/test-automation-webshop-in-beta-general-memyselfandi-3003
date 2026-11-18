# Evidence recorded on 18 November 2026

This directory contains selected screenshots and structured observations from temporary Selenium diagnostic runs, plus a summary of the existing JUnit suite.

- **rechecks.json:** 20 completed diagnostic scenarios from the catalog, product links, checkout, About, reset, cart matrix, description and layout probes. A first error-user cart probe stopped because its page-load wait required a missing description; that incomplete diagnostic was excluded. A later completed error-user cart run is included.
- **junit-summary.txt:** seven test classes, 23 executions, 22 passes and one failure from the complete local Maven run. Generated Surefire XML/text originals remain in target/surefire-reports and are ignored by Git.
- **PNG files:** selected post-login application states and standard-user comparison images. A screenshot records only its capture moment; the JSON provides before/after state where relevant.

The diagnostic source and compiled classes are outside the project. They introduce no test framework, dependencies or additional JUnit cases. Screenshots are static defect evidence, not an automated test-failure screenshot feature.

All diagnostic sessions used Windows, Chrome 158.0.8127.36, headless, window 1920 × 1080 and viewport 1904 × 929. Timestamps in rechecks.json are UTC. Screenshot references use filenames within this directory; credentials and machine-specific screenshot paths are omitted.

See [the defect register](../../../BUG_REPORTS.md) and [release report](../../../RELEASE_STATUS.md) for interpretation.
