# Enterprise Selenium Java Test Automation Framework

![Java 25](https://img.shields.io/badge/Java-25-orange.svg)
![Selenium WebDriver 4](https://img.shields.io/badge/Selenium-WebDriver%204-brightgreen.svg)
![TestNG](https://img.shields.io/badge/TestNG-Testing-red.svg)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36.svg)
![ExtentReports 5](https://img.shields.io/badge/ExtentReports-5-blue.svg)
![Jenkins](https://img.shields.io/badge/Jenkins-Pipeline%20Ready-brightgreen.svg)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-blue.svg)

A modular, data-driven UI test automation framework built with **Java 25, Selenium WebDriver 4, TestNG, Maven, and ExtentReports 5**. The framework uses the Page Object Model (POM), centralized browser management, reusable page and utility classes, and CI/CD pipelines for repeatable browser testing.

It is designed for end-to-end web application testing, including authentication, product selection, cart validation, checkout workflows, regression testing, and negative/error validation.

---

## Author & Profiles

- **GitHub Profile:** [MaheshH91](https://github.com/MaheshH91)
- **LinkedIn Profile:** [Mahesh Holkar](https://www.linkedin.com/in/maheshholkarqaengineer/)

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming language |
| Selenium WebDriver 4 | Browser automation |
| TestNG | Test execution, grouping, suites, and data providers |
| Apache Maven | Build and dependency management |
| Page Object Model (POM) | Maintainable page-layer design |
| Jackson / JSON | Data-driven test input and JSON parsing |
| ExtentReports 5 | HTML execution reporting |
| Git and GitHub | Source control |
| Jenkins | CI/CD pipeline execution |
| GitHub Actions | Automated workflow execution |

---

## Key Features

- Page Object Model with reusable page classes
- Centralized `DriverFactory` for browser setup
- Thread-local WebDriver management for isolated test sessions
- Chrome, Chrome headless, Firefox, and Edge execution options, subject to local configuration
- Data-driven tests using JSON and TestNG DataProviders
- Regression, purchase workflow, and error-validation suites
- Explicit wait and JavaScript utility methods
- TestNG listeners and retry support
- ExtentReports HTML reports and failure screenshots
- Maven-based execution
- Jenkins and GitHub Actions CI/CD integration

---

## Prerequisites

Install the following before running the project:

- JDK 25 (use the JDK version configured by the project)
- Apache Maven 3.9+
- Google Chrome, Mozilla Firefox, or Microsoft Edge
- Git
- Network access to the test application

Check Java and Maven:

```bash
java -version
mvn -version
```

Confirm that `JAVA_HOME` points to the intended JDK.

---

## Clone the Repository

```bash
git clone https://github.com/MaheshH91/<your-selenium-java-testng-repository>.git
cd <your-selenium-java-testng-repository>
```

Replace the placeholder repository name with the actual repository name.

---

## Build the Project

Download dependencies and compile the project:

```bash
mvn clean install -DskipTests
```

Compile source and test classes:

```bash
mvn clean test-compile
```

---

## Run Tests

### Run the default TestNG suite

```bash
mvn clean test
```

### Run in Chrome headless mode

```bash
mvn clean test -Dbrowser=chrome-headless
```

### Run in Firefox or Edge

```bash
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

The supported browser property values depend on the implementation in `DriverFactory` and Maven configuration.

---

## TestNG Suites and Maven Profiles

The framework README/source configuration describes these suite files:

| Suite / Profile | TestNG XML | Purpose |
|---|---|---|
| Regression | `testSuites/testng-regression.xml` | End-to-end regression coverage |
| Purchase | `testSuites/testng-purchase.xml` | Checkout, order placement, and order verification |
| Error Validation | `testSuites/testng-error-validation.xml` | Authentication failures, validation messages, and cart boundary checks |

Run suites by Maven profile:

```bash
mvn clean test -PRegression
mvn clean test -PPurchase
mvn clean test -PErrorValidation
```

Run a specific suite XML file:

```bash
mvn clean test -DsuiteXmlFile=testSuites/testng-purchase.xml
```

Use the profile and suite names configured in your current `pom.xml`.

---

## Data-Driven Testing

Test data is stored separately from test logic in JSON, for example:

`src/test/resources/testdata/PurchaseOrder.json`

```json
[
  {
    "email": "user@example.com",
    "password": "replace-with-test-password",
    "product": "ZARA COAT 3"
  },
  {
    "email": "user2@example.com",
    "password": "replace-with-test-password",
    "product": "ADIDAS ORIGINAL"
  }
]
```

`JsonUtils` reads JSON data and `PurchaseDataProvider` supplies the test data to TestNG tests. Use dedicated test credentials and never commit real passwords, tokens, or other secrets.

Example test pattern:

```java
@Test(
    dataProvider = "purchaseOrders",
    dataProviderClass = PurchaseDataProvider.class,
    groups = {"Purchase", "Regression"}
)
public void submitOrderE2ETest(HashMap<String, String> data) {
    // Use test data to log in, select a product, and validate checkout.
}
```

Adapt the example to the actual methods and package names in the project.

---

## Framework Architecture

```text
Test Layer
  └── BaseTest and test classes
       └── Page Objects
            ├── LoginPage
            ├── ProductCatalogPage
            ├── CartPage
            ├── CheckoutPage
            ├── OrderConfirmationPage
            └── OrderHistoryPage
                 └── BasePage and reusable utilities
                      └── DriverFactory
                           └── WebDriver browser session
```

### Core Components

- **`BaseTest.java`** — manages test setup and teardown.
- **`DriverFactory.java`** — centralizes WebDriver creation and browser options.
- **`BasePage.java`** — provides shared navigation, wait, and browser interaction methods.
- **Page classes** — encapsulate page locators and page-specific actions.
- **`ConfigReader.java`** — reads framework configuration.
- **`WaitUtils.java`** — provides reusable synchronization helpers.
- **`JsonUtils.java`** — supports JSON test data reading.
- **`PurchaseDataProvider.java`** — provides test data to TestNG methods.
- **TestNG listeners and retry classes** — handle test events, reporting integration, and configured retries.
- **`ExtentManager.java`** — manages ExtentReports setup.

---

## Parallel Execution and Retry Handling

The framework uses `ThreadLocal<WebDriver>` to keep browser instances isolated across threads. TestNG suite XML files and DataProviders can be configured for parallel execution.

Retry behavior is implemented through TestNG retry/listener components where configured. Review `testng-*.xml` and `pom.xml` to confirm thread counts and retry settings before enabling parallel runs in a shared test environment.

---

## Reports and Screenshots

ExtentReports is used to generate HTML execution reports. The framework configuration describes reports under a timestamped path similar to:

```text
reports/ExecutionReport_<timestamp>.html
```

Depending on the configured listeners, reports can include test status, logs, and failure screenshots.

After execution, open the generated HTML report in a browser. The actual report path is determined by the current reporting configuration.

---

## CI/CD Integration

### GitHub Actions

The workflow is intended to run tests automatically on configured events such as pushes and pull requests. A typical workflow should:

1. Check out the repository.
2. Set up the required JDK.
3. Configure Maven dependency caching.
4. Run the Maven/TestNG suite in headless browser mode.
5. Upload test reports and logs as workflow artifacts.

Workflow file described by this project:

```text
.github/workflows/selenium-tests.yml
```

### Jenkins

The Jenkins pipeline can be configured to select the browser, test suite, and environment, execute Maven tests, and archive reports.

Pipeline file:

```text
Jenkinsfile
```

Required Jenkins plugins and pipeline steps must be installed/configured in the Jenkins instance. Store credentials in Jenkins credentials management rather than in source files.

---

## Project Structure

```text
selenium-automation-framework/
├── .github/
│   └── workflows/
│       └── selenium-tests.yml
├── Jenkinsfile
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/framework/
│   │   │   ├── core/
│   │   │   │   └── BasePage.java
│   │   │   ├── pages/
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── ProductCatalogPage.java
│   │   │   │   ├── CartPage.java
│   │   │   │   ├── CheckoutPage.java
│   │   │   │   ├── OrderConfirmationPage.java
│   │   │   │   └── OrderHistoryPage.java
│   │   │   └── utils/
│   │   │       ├── ConfigReader.java
│   │   │       ├── JavaScriptUtils.java
│   │   │       ├── JsonUtils.java
│   │   │       └── WaitUtils.java
│   │   ├── resources/
│   │   │   └── log4j2.xml
│   │   └── test/
│   │       ├── java/com/framework/
│   │       │   ├── base/
│   │       │   │   ├── BaseTest.java
│   │       │   │   └── DriverFactory.java
│   │       │   ├── data/
│   │       │   │   └── PurchaseDataProvider.java
│   │       │   ├── listeners/
│   │       │   ├── reporting/
│   │       │   └── tests/
│   │       └── resources/
│   │           ├── config.properties
│   │           └── testdata/
│   │               └── PurchaseOrder.json
│   └── test/
└── testSuites/
    ├── testng-regression.xml
    ├── testng-purchase.xml
    └── testng-error-validation.xml
```

*This is a representative structure based on the framework details supplied. Keep the tree aligned with the actual repository contents.*

---

## Troubleshooting

| Issue | Checks |
|---|---|
| Maven reports an unsupported Java release | Check `java -version`, `mvn -version`, `JAVA_HOME`, and the compiler version in `pom.xml`. |
| No tests execute | Check TestNG suite paths, package/class names, groups, and Maven Surefire configuration. |
| Browser does not start in CI | Check headless options, browser installation, permissions, and runner dependencies. |
| Element not found or timeout | Verify locators, page state, and explicit wait conditions. |
| Report is missing | Check listener registration, report output path, and write permissions. |
| Tests fail only in parallel | Check shared state, static page objects, test data collisions, and ThreadLocal cleanup. |

---

## Security Best Practices

- Do not commit passwords, API keys, access tokens, or personal data.
- Keep test credentials in environment variables or a CI/CD secret store.
- Use dedicated test accounts and non-production environments.
- Avoid capturing sensitive information in logs, screenshots, or reports.
- Review retry settings so genuine functional failures are not hidden.

---

## Contributing

1. Create a feature branch.
2. Make a focused change and add or update tests.
3. Run the relevant TestNG suite locally.
4. Review generated reports.
5. Open a pull request with a clear summary of the change.

---

## Author

**Mahesh Holkar** — QA Automation Engineer

- GitHub: [MaheshH91](https://github.com/MaheshH91)
- LinkedIn: [Mahesh Holkar](https://www.linkedin.com/in/maheshholkarqaengineer/)

---

If this framework helps you, feel free to explore the repository and connect through GitHub or LinkedIn.
