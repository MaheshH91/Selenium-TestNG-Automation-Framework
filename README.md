## ⚙️ Prerequisites

Before running the project, ensure the following are installed and configured:

* **Java JDK:** Version 21
* **Apache Maven:** Version 3.9+
* **Google Chrome**
* **Mozilla Firefox**
* **Microsoft Edge**
* **Git** — recommended for source-code management

### Verify Java

```bash
java -version
```

Expected:

```text
java version "21..."
```

### Verify Maven

```bash
mvn -version
```

Ensure Maven is using Java 21.

---

## 📦 Install Dependencies

To clean the project, compile the source code, and download all Maven dependencies:

```bash
mvn clean install -DskipTests
```

Alternatively, to resolve Maven dependencies without running the tests:

```bash
mvn dependency:resolve
```

---

## ▶️ How to Run Tests

### Run Full Regression Suite

Chrome is the default browser:

```bash
mvn clean test
```

### Run Tests in Chrome Headless Mode

```bash
mvn clean test -Dbrowser=chromeheadless
```

> The `chromeheadless` option must be supported by the framework's `DriverFactory`/browser configuration.

### Run Tests on Firefox

```bash
mvn clean test -Dbrowser=firefox
```

### Run Tests on Microsoft Edge

```bash
mvn clean test -Dbrowser=edge
```

---

## 🧪 TestNG Suite Execution

The framework supports execution through TestNG XML suites and Maven profiles.

| Profile           | Suite XML                             | Focus                          |
| ----------------- | ------------------------------------- | ------------------------------ |
| `Regression`      | `testSuites/testng.xml`               | Complete regression suite      |
| `Purchase`        | `testSuites/Purchase.xml`             | End-to-end purchase workflow   |
| `ErrorValidation` | `testSuites/ErrorValidationTests.xml` | Login and validation scenarios |

### Run Regression Suite

```bash
mvn clean test -PRegression
```

### Run Purchase Suite

```bash
mvn clean test -PPurchase
```

### Run Error Validation Suite

```bash
mvn clean test -PErrorValidation
```

### Run a Specific Suite Directly

You can also override the suite XML property:

```bash
mvn clean test -DsuiteXmlFile=testSuites/Purchase.xml
```

---

## 🔄 Data-Driven Architecture

Test data is externalized in JSON format:

```text
src/test/resources/testData/PurchaseOrder.json
```

Example:

```json
[
  {
    "email": "user@example.com",
    "password": "<password>",
    "product": "ZARA COAT 3"
  },
  {
    "email": "test@example.com",
    "password": "<password>",
    "product": "ADIDAS ORIGINAL"
  }
]
```

> **Security:** Do not commit real usernames, passwords, API keys, tokens, or other credentials to GitHub. Use environment variables, CI/CD secrets, or local configuration files.

The JSON data is parsed into `HashMap<String, String>` objects through `JsonUtils`.

Example TestNG test:

```java
@Test(
    dataProvider = "purchaseOrderData",
    dataProviderClass = TestDataProvider.class,
    groups = {"Purchase"}
)
public void submitOrder(HashMap<String, String> input) {

    String email = input.get("email");
    String password = input.get("password");
    String product = input.get("product");

    // Test execution
}
```

---

## 🧩 Page Object Model (POM)

The framework follows the Page Object Model design pattern.

```text
Test Class
    │
    │ Assertions & Test Flow
    ▼
Page Objects
    │
    │ Locators & Page Actions
    ▼
Abstract Components
    │
    │ Common Navigation & Reusable Components
    ▼
Utilities
    │
    │ Waits, JavaScript, Configuration, JSON
    ▼
WebDriver
    │
    │ ThreadLocal Browser Session
    ▼
Browser
```

### `AbstractComponents.java`

Contains reusable functionality such as:

* Header/navigation links
* Cart navigation
* Orders navigation
* Explicit waits
* JavaScript interactions
* Common page-level operations

### Page Objects

Page Objects encapsulate:

* WebElement locators
* Page-specific actions
* Page navigation

Example:

```java
public class LoginPage extends AbstractComponents {

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
```

Page methods can return the next Page Object when an action causes navigation.

---

## 🧵 Parallel Execution & Thread Safety

The framework uses `ThreadLocal<WebDriver>` to support parallel execution.

Example:

```java
private static final ThreadLocal<WebDriver> tdriver =
        new ThreadLocal<>();

public static WebDriver getDriver() {
    return tdriver.get();
}
```

Each execution thread receives its own WebDriver instance.

This provides:

* Thread-safe browser sessions
* Independent test execution
* Support for parallel TestNG execution
* Reduced risk of driver/session conflicts

The driver is removed after test execution:

```java
@AfterMethod(alwaysRun = true)
public void tearDown() {

    if (getDriver() != null) {
        getDriver().quit();
        tdriver.remove();
    }
}
```

Using `ThreadLocal.remove()` also helps prevent stale thread-local references when worker threads are reused.

---

## 🔁 Dynamic Retry Mechanism

The framework provides automatic retry support for transient failures.

### `Retry.java`

Implements TestNG's:

```java
IRetryAnalyzer
```

The retry mechanism reruns a failed test according to the configured retry count.

### `AnnotationTransformer.java`

Implements:

```java
IAnnotationTransformer
```

It dynamically applies the retry analyzer to tests, avoiding the need to add the retry annotation manually to every test method.

---

## 📊 ExtentReports & Screenshots

After execution, the framework generates an HTML execution report:

```text
reports/index.html
```

The report provides:

* Test execution status
* Passed/failed test details
* Failure information
* Screenshots
* Execution logs

### Failed-Test Screenshots

Screenshots can be attached directly to failed test nodes using Base64 encoding.

This avoids dependency on relative screenshot file paths and makes the report easier to archive in CI/CD environments.

### Headless/CI Environment

When running on Linux CI agents, browser-opening code should check whether the environment is graphical:

```java
if (!GraphicsEnvironment.isHeadless()) {
    // Open report in browser
}
```

This prevents `HeadlessException` when running Jenkins or other CI agents without a display.

---

## 🤖 Jenkins / CI-CD Integration

The framework can be executed from Jenkins using Maven.

### Jenkins Execution Command

```bash
mvn clean test -Dbrowser=chromeheadless -PRegression
```

### Recommended Jenkins Artifacts

Archive:

```text
reports/**
```

and:

```text
target/surefire-reports/**
```

For example:

```text
target/surefire-reports/testng-results.xml
```

Depending on the Jenkins configuration, these artifacts can be retained after the build for debugging and reporting.

### Jenkins HTML Report

If Jenkins blocks CSS/JavaScript resources in an archived HTML report because of its Content Security Policy, the Jenkins administrator can review the CSP configuration.

A commonly used Jenkins Script Console setting is:

```groovy
System.setProperty(
    "hudson.model.DirectoryBrowserSupport.CSP",
    ""
)
```

> **Security note:** Disabling Jenkins CSP reduces browser-side security protections. Apply this only when appropriate for your Jenkins environment and preferably use a more restrictive CSP configuration when possible.

---

## 🐞 Common Troubleshooting

| Issue / Error                                  | Possible Cause                                                | Solution                                                       |
| ---------------------------------------------- | ------------------------------------------------------------- | -------------------------------------------------------------- |
| `ElementClickInterceptedException`             | Dropdown, overlay, or animation blocks the element            | Use an explicit wait or JavaScript click when appropriate      |
| `Resource file not found on classpath`         | Resource is outside the Maven test classpath                  | Place configuration/data files under `src/test/resources/`     |
| `AssertionError: Login error message mismatch` | Expected text doesn't match the application                   | Verify the actual application message and update the assertion |
| `Tests run: 0`                                 | Incorrect TestNG suite/group configuration                    | Verify `<classes>`, `<packages>`, and group configuration      |
| `TimeoutException`                             | Element did not become available within the wait period       | Review locator, application state, and explicit wait           |
| `NoSuchElementException`                       | Incorrect/stale locator or page not loaded                    | Validate locator and synchronize with the page                 |
| `SessionNotCreatedException`                   | Browser/driver compatibility issue                            | Update browser/Selenium version or use Selenium Manager        |
| `HeadlessException`                            | Attempt to open a browser/report in a headless CI environment | Check `GraphicsEnvironment.isHeadless()`                       |
| `StaleElementReferenceException`               | DOM refreshed after locating the element                      | Re-locate the element before interacting                       |
| `ElementNotInteractableException`              | Element exists but cannot currently be interacted with        | Wait for visibility/clickability and verify page state         |

---

## 🏗️ Framework Execution Flow

```text
Maven Command
     │
     ▼
TestNG Suite
     │
     ▼
BaseTest
     │
     ▼
WebDriver Initialization
     │
     ▼
ThreadLocal<WebDriver>
     │
     ▼
Test Class
     │
     ▼
Page Object
     │
     ▼
Abstract Components
     │
     ▼
Utilities
     │
     ▼
Application Under Test
     │
     ▼
Assertions
     │
     ▼
ExtentReports
     │
     ▼
Screenshots / Test Results
```

---

## 📁 Recommended Project Structure

```text
SeleniumAutomationFramework/
│
├── pom.xml
├── README.md
│
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── pageObjects/
│   │       ├── AbstractComponents/
│   │       └── utils/
│   │
│   └── test/
│       ├── java/
│       │   ├── tests/
│       │   ├── testComponents/
│       │   └── listeners/
│       │
│       └── resources/
│           ├── testData/
│           │   └── PurchaseOrder.json
│           └── config.properties
│
├── testSuites/
│   ├── testng.xml
│   ├── Purchase.xml
│   └── ErrorValidationTests.xml
│
├── reports/
│
└── target/
```

---

## 🚀 Quick Start

Clone the repository and run:

```bash
mvn clean install -DskipTests
```

Then execute the regression suite:

```bash
mvn clean test -PRegression
```

For CI/CD execution:

```bash
mvn clean test -Dbrowser=chromeheadless -PRegression
```

After execution, check:

```text
reports/index.html
```

and:

```text
target/surefire-reports/
```

for execution results.
