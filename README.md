# Selenium TestNG Automation Framework (RSA E-Commerce)

An enterprise-ready, data-driven test automation framework built using **Selenium WebDriver (Java 21)** and **TestNG**. It automates the end-to-end shopping workflow on the Rahul Shetty Academy client portal with parallel execution, thread-safe session handling, and ExtentReports reporting.

---

## 🏗️ Architecture & Features

- **Design Pattern**: Page Object Model (POM) with component-level abstraction.
- **Thread Safety**: Isolated `ThreadLocal<WebDriver>` instances to enable concurrency across parallel tests.
- **Data-Driven**: Externalized datasets in JSON format parsed dynamically via Jackson.
- **Resilience**: Dynamic retry logic via TestNG `IAnnotationTransformer` for transient errors.
- **Reporting**: ExtentReports 5 with inline Base64 failure screenshots.
- **CI/CD Ready**: Fully compatible with headless executions via Jenkins pipelines.

---

## 📂 Project Structure

```text
SeleniumFrameWorkDesignRSA/
├── pom.xml                                  # Project dependencies and profiles
├── testSuites/                              # TestNG suite XML definitions
│   ├── testng.xml                           # Parallel regression suite
│   ├── Purchase.xml                         # E2E purchase flow
│   └── ErrorValidationTests.xml             # Negative test suite
├── src/
│   ├── main/java/rahulshettyacademy/
│   │   ├── abstractComponents/              # Reusable page components & explicit waits
│   │   └── pageObjects/                     # Page Object classes
│   └── test/
│       ├── java/rahulshettyacademy/
│       │   ├── tests/                       # TestNG test classes
│       │   ├── testComponents/              # BaseTest, Listeners, Retry analyzers
│       │   ├── data/                        # JSON DataReader and @DataProvider bindings
│       │   └── resources/                   # ExtentReporter configuration
│       └── resources/                       # Config properties & JSON test data
└── reports/                                 # Generated HTML test execution reports
```

---

## ⚙️ Prerequisites

Before running the project, install:

- **JDK 21**
- **Maven**
- **Google Chrome**
- **Mozilla Firefox** (only if Firefox execution is required)
- IDE such as Eclipse, IntelliJ IDEA, or VS Code (optional)

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

Make sure Maven is using the intended Java 21 installation.

---

## 📦 Install Dependencies

From the project root:

```bash
mvn clean install
```

Or run the tests directly:

```bash
mvn clean test
```

Maven will download the dependencies defined in `pom.xml`.

---

## ▶️ How to Run Tests

### Run full regression suite on Chrome (default)

```bash
mvn clean test
```

### Run tests in Headless mode

```bash
mvn clean test -Dbrowser=chromeheadless
```

### Run tests on Firefox

```bash
mvn clean test -Dbrowser=firefox
```

### Run specific suite using Maven Profiles

```bash
mvn clean test -PRegression
```

```bash
mvn clean test -PPurchase
```

```bash
mvn clean test -PErrorValidation
```

---

## 🧪 TestNG Suites

### Regression

```text
testSuites/testng.xml
```

Used for the regression test execution.

Run:

```bash
mvn clean test -PRegression
```

### Purchase

```text
testSuites/Purchase.xml
```

Used for the end-to-end purchase workflow.

Run:

```bash
mvn clean test -PPurchase
```

### Error Validation

```text
testSuites/ErrorValidationTests.xml
```

Used for negative/error validation scenarios.

Run:

```bash
mvn clean test -PErrorValidation
```

---

## 🔄 Data-Driven Testing

Test data is externalized in JSON format and parsed using Jackson.

The framework uses TestNG `@DataProvider` bindings to execute the same test flow with multiple datasets.

Typical data includes:

```text
email
password
product
```

This allows one automation test to execute against multiple combinations of test data.

---

## 🧩 Page Object Model

The framework follows the Page Object Model.

```text
Test Class
    ↓
Page Object
    ↓
Abstract Components
    ↓
WebDriver
    ↓
Application
```

Page Objects contain:

- WebElement locators
- Page-specific actions
- Navigation methods
- UI interaction methods

Reusable functionality such as waits and common page operations is maintained in `abstractComponents`.

---

## 🧵 Parallel Execution & Thread Safety

The framework supports parallel execution using isolated:

```java
ThreadLocal<WebDriver>
```

Each parallel test thread receives its own WebDriver instance.

Conceptually:

```text
Thread 1 → WebDriver 1 → Test Data 1
Thread 2 → WebDriver 2 → Test Data 2
Thread 3 → WebDriver 3 → Test Data 3
```

This prevents one test thread from accidentally using another test's browser session.

---

## 🔁 Retry Mechanism

The framework uses TestNG `IAnnotationTransformer` for dynamic retry configuration.

This allows transient failures to be retried automatically when configured.

The retry-related implementation is maintained under:

```text
src/test/java/rahulshettyacademy/testComponents/
```

---

## ⏳ Explicit Waits

Reusable explicit wait functionality is maintained in the abstract component layer.

Example:

```java
WebDriverWait wait =
        new WebDriverWait(driver, Duration.ofSeconds(10));

WebElement element = wait.until(
        ExpectedConditions.elementToBeClickable(locator)
);

element.click();
```

Prefer explicit waits for synchronization instead of unnecessary `Thread.sleep()` calls.

---

## 🌍 Country Selection

For the checkout country autocomplete, wait for the actual suggestion element rather than relying on a `.ta-results` container when that container is not available in the current DOM.

Example:

```java
By countryOption =
        By.xpath("//button[contains(@class,'ta-item')][2]");

WebDriverWait wait =
        new WebDriverWait(driver, Duration.ofSeconds(10));

WebElement option = wait.until(
        ExpectedConditions.elementToBeClickable(countryOption)
);

option.click();
```

If the application provides a stable country-name attribute/text, a country-specific locator can be used instead of selecting the second suggestion.

---

## ❌ Login Error Validation

The application error message used by the current test is:

```text
Incorrect email or password.
```

The expected assertion should match the displayed text exactly:

```java
Assert.assertEquals(
        actualMessage,
        "Incorrect email or password.",
        "Login error message mismatch."
);
```

These two values are different:

```text
Incorrect email password.
Incorrect email or password.
```

Therefore, `Assert.assertEquals()` will fail when the expected value does not exactly match the actual application message.

---

## 📊 ExtentReports

ExtentReports 5 is used for HTML test execution reporting.

Generated reports are maintained under:

```text
reports/
```

Example:

```text
reports/index.html
```

Open the generated HTML report in a browser after execution.

The report can provide:

- Test execution status
- Passed tests
- Failed tests
- Failure details
- Screenshots
- Test execution information

---

## 📸 Failure Screenshots

The framework supports inline Base64 screenshots for failures through ExtentReports.

When a test fails, the listener/reporting implementation can attach the screenshot to the corresponding ExtentReports test entry.

---

## 🤖 CI/CD / Jenkins

The framework is designed to support CI/CD execution.

For headless execution:

```bash
mvn clean test -Dbrowser=chromeheadless
```

A Jenkins pipeline can invoke the same Maven command.

Example pipeline command:

```bash
mvn clean test -Dbrowser=chromeheadless
```

---

## 🛠️ Common Maven Commands

### Clean the project

```bash
mvn clean
```

### Compile the project

```bash
mvn compile
```

### Compile test sources

```bash
mvn test-compile
```

### Run tests

```bash
mvn test
```

### Clean and run tests

```bash
mvn clean test
```

### Install the project

```bash
mvn clean install
```

### Run Regression

```bash
mvn clean test -PRegression
```

### Run Purchase

```bash
mvn clean test -PPurchase
```

### Run Error Validation

```bash
mvn clean test -PErrorValidation
```

### Run Chrome headless

```bash
mvn clean test -Dbrowser=chromeheadless
```

### Run Firefox

```bash
mvn clean test -Dbrowser=firefox
```

---

## 🐞 Troubleshooting

### ElementClickInterceptedException

If Selenium reports:

```text
ElementClickInterceptedException
element is not clickable
```

use an explicit clickable wait:

```java
WebDriverWait wait =
        new WebDriverWait(driver, Duration.ofSeconds(10));

WebElement element = wait.until(
        ExpectedConditions.elementToBeClickable(locator)
);

element.click();
```

If required, scroll the element into view before clicking.

---

### TimeoutException for `.ta-results`

If you see:

```text
TimeoutException:
waiting for visibility of element found by
By.cssSelector: .ta-results
```

the `.ta-results` locator may not exist in the current page DOM.

Instead, wait for the actual country suggestion:

```java
By countryOption =
        By.xpath("//button[contains(@class,'ta-item')][2]");

wait.until(
        ExpectedConditions.elementToBeClickable(countryOption)
);
```

---

### Assertion Failure

If you see:

```text
Expected: Incorrect email password.
Actual:   Incorrect email or password.
```

update the expected value to:

```text
Incorrect email or password.
```

---

### Java Version Mismatch

Check:

```bash
java -version
```

and:

```bash
mvn -version
```

The project is configured for Java 21.

---

## 📋 Recommended Execution Flow

```text
1. Clone/Open the project
          ↓
2. Verify Java 21
          ↓
3. Verify Maven
          ↓
4. Run mvn clean install
          ↓
5. Select the required browser/suite
          ↓
6. Execute Maven command
          ↓
7. Review console execution
          ↓
8. Open reports/index.html
          ↓
9. Check failures and screenshots
          ↓
10. Check target/surefire-reports if required
```

---

## 🚀 Quick Start

For a new machine:

```bash
git clone <repository-url>
cd SeleniumFrameWorkDesignRSA
mvn clean install
mvn clean test
```

For headless Chrome:

```bash
mvn clean test -Dbrowser=chromeheadless
```

For Purchase:

```bash
mvn clean test -PPurchase
```

For Error Validation:

```bash
mvn clean test -PErrorValidation
```

For Regression:

```bash
mvn clean test -PRegression
```

---

## 📌 Notes

- Use Java 21 for this project.
- Keep Maven configured to use the same JDK.
- Prefer explicit waits over `Thread.sleep()`.
- Keep page-specific UI actions inside Page Objects.
- Keep reusable functionality inside `abstractComponents`.
- Use JSON for externalized test data.
- Use TestNG `@DataProvider` for multiple datasets.
- Use `ThreadLocal<WebDriver>` for parallel execution.
- Review ExtentReports after every execution.
- Review `target/surefire-reports` when detailed Maven/TestNG results are required.
