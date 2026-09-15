# Java Web UI Automation Framework

A Selenium WebDriver + Java + TestNG based UI automation framework with Page Object Model, WebDriverManager, ExtentReports, automatic retry handling, screenshots on failures, logging, and externalized test configuration.

## Tech Stack

- **Java 17**
- **Selenium WebDriver 4.16.1**
- **TestNG 7.8.0**
- **Maven**
- **WebDriverManager 5.8.0**
- **ExtentReports 5.0.8**
- **Log4j**
- **Apache POI**
- **Lombok**
- **Page Object Model (POM)**

## Framework Features

- Cross-browser execution for **Chrome, Firefox, and Edge**
- WebDriver binaries managed automatically using WebDriverManager
- Page Object Model using Selenium `PageFactory`
- Test execution controlled through `testng.xml`
- Centralized browser and application configuration
- Extent HTML reporting
- Automatic screenshots on test failures
- Automatic retry mechanism configurable through `common.properties`
- TestNG listeners and annotation transformer
- Reusable base test setup and teardown
- Logging through the framework logging utility
- Maven-based dependency and test execution management

## Project Structure

```text
java-web-ui-automation-main/
│
├── pom.xml
├── common.properties
├── extent.properties
├── testng.xml
├── README.md
│
└── src/
    ├── common/
    │   ├── AnnotationTransformer.java
    │   ├── BaseTest.java
    │   ├── ExtentManager.java
    │   ├── ExtentTestManager.java
    │   ├── IRetryAnalyzer.java
    │   ├── Log.java
    │   ├── Login.java
    │   ├── RetryAnalyzer.java
    │   ├── TestListener.java
    │   └── Util.java
    │
    ├── page_objects/
    │   └── HomePage.java
    │
    └── tests/
        └── LoginTest.java
```

## Architecture

```text
                 ┌─────────────────────┐
                 │     testng.xml      │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │     LoginTest       │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │      BaseTest       │
                 │ Setup / Teardown    │
                 └──────────┬──────────┘
                            │
             ┌──────────────┼──────────────┐
             ▼              ▼              ▼
      ┌────────────┐ ┌────────────┐ ┌──────────────┐
      │   Login    │ │ HomePage   │ │    Util      │
      │ Page Object│ │ Page Object│ │ WebDriver    │
      └────────────┘ └────────────┘ │ / Utilities  │
                                    └──────────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │    TestListener     │
                 │ RetryAnalyzer       │
                 │   ExtentReports     │
                 └─────────────────────┘
```

## Prerequisites

Install the following before running the project:

1. **JDK 17 or later**
2. **Apache Maven**
3. A supported browser:
   - Google Chrome
   - Mozilla Firefox
   - Microsoft Edge

Verify the installations:

```bash
java -version
mvn -version
```

## Configuration

The framework reads execution settings from `common.properties`.

Example:

```properties
browser=chrome
url=http://istio-ingress-uat.barraq.com.sa.internal/merchant/#/login
phoneNumber=565656565
password=demopassword
otp=111111
limit=0
```

### Configuration Parameters

| Property | Description | Example |
|---|---|---|
| `browser` | Browser to execute against | `chrome` |
| `url` | Application login URL | Application URL |
| `phoneNumber` | Test login phone number | `565656565` |
| `password` | Test login password | `demopassword` |
| `otp` | Test OTP | `111111` |
| `limit` | Maximum retry count | `0` |

> **Security:** Do not commit real passwords, OTPs, API keys, or other credentials to source control. Prefer environment variables or a secure secret-management solution for production/CI execution.

## Supported Browsers

The browser is selected from `common.properties`.

```properties
browser=chrome
```

Supported values:

```text
chrome
firefox
edge
```

The framework uses WebDriverManager to automatically set up the appropriate browser driver.

## How to Run

### Run the complete TestNG suite

From the project root:

```bash
mvn clean test
```

Maven uses `testng.xml` through the Surefire plugin.

### Run TestNG directly from an IDE

Import the project as a Maven project and run:

```text
testng.xml
```

Alternatively, execute `LoginTest.ValidLogin` directly from IntelliJ IDEA or Eclipse.

## Test Flow

The current sample test validates a successful application login:

1. `BaseTest` loads `common.properties`.
2. The configured browser is initialized.
3. The application URL is opened.
4. `Login` enters the phone number and password.
5. The login button is clicked.
6. The OTP is entered.
7. The Verify button is clicked.
8. `HomePage` validates that the user avatar is displayed.
9. The test result is captured by the TestNG listener.
10. The browser is closed after execution.

Example:

```java
@Test
public void ValidLogin() throws InterruptedException, IOException {
    Login login = new Login(driver);
    login.loginToApp();

    HomePage homePage = new HomePage(driver);

    Assert.assertTrue(homePage.iconUser.isDisplayed());
}
```

## Page Object Model

The framework separates test logic from UI interaction.

### Login Page

`src/common/Login.java`

Contains:

- Phone number locator
- Password locator
- OTP locators
- Login button
- Verify button
- Login workflow methods

It supports both:

```java
login.loginToApp(phoneNumber, password, otp);
```

and configuration-driven login:

```java
login.loginToApp();
```

### Home Page

`src/page_objects/HomePage.java`

Contains the user avatar locator used to verify successful login.

## Base Test

`BaseTest.java` provides common test lifecycle management.

### Before each test

- Loads configuration
- Creates the WebDriver
- Maximizes the browser
- Opens the application
- Sets the implicit wait
- Creates the ExtentReports test entry

### After each test

- Quits the WebDriver

This allows individual test classes to focus on business scenarios instead of repeated browser setup code.

## Retry Mechanism

`AnnotationTransformer` automatically attaches `RetryAnalyzer` to TestNG test methods.

The retry count is configured in `common.properties`:

```properties
limit=0
```

For example:

```properties
limit=2
```

allows a failed test to be retried up to two times.

The retry implementation also captures a screenshot when a retry is triggered.

## Failure Screenshots

The framework captures screenshots for failed tests using Selenium's `TakesScreenshot` API.

Screenshots are embedded into the ExtentReports test result as Base64 media.

This provides useful evidence when diagnosing failed UI tests.

## ExtentReports

ExtentReports is initialized by `ExtentManager`.

The framework creates an HTML execution report.

The configured report location in the current implementation is:

```text
reports/extent-report.html
```

After execution, open the generated HTML report in a browser.

> The project also contains `extent.properties`. The current Java implementation creates the report using `ExtentSparkReporter`, so the Java configuration in `ExtentManager` is the effective report location.

## TestNG Listeners

`TestListener` handles:

- Test start logging
- Test success logging
- Test failure logging
- Test skipped logging
- Failure screenshots
- ExtentReports flushing

`AnnotationTransformer` applies the retry analyzer automatically to tests.

The listeners are registered in `testng.xml`:

```xml
<listeners>
    <listener class-name="common.TestListener" />
    <listener class-name="common.AnnotationTransformer" />
</listeners>
```

## Utilities

`Util.java` contains reusable framework utilities including:

- Browser initialization
- Chrome/Firefox/Edge WebDriver setup
- New-window URL validation
- ExtentReports information logging
- Base URL extraction
- JavaScript scrolling
- JavaScript clicking

Example:

```java
WebDriver driver = Util.getBrowserDriver("chrome");
```

## Maven Configuration

The project is configured for Java 17.

Important Maven plugins include:

- `maven-compiler-plugin`
- `maven-surefire-plugin`

The Surefire plugin executes:

```text
testng.xml
```

## Dependencies

Key dependencies configured in `pom.xml` include:

```text
Selenium Java       4.16.1
TestNG              7.8.0
WebDriverManager    5.8.0
ExtentReports       5.0.8
Log4j               2.19.0 / 1.2.8
Apache POI          5.2.5
Lombok              1.18.26
```

## Recommended CI/CD Usage

This framework can be integrated into CI/CD systems such as:

- GitHub Actions
- Jenkins
- GitLab CI/CD
- Azure DevOps

A typical pipeline can execute:

```bash
mvn clean test
```

Recommended CI improvements include:

- Store credentials in CI secrets
- Parameterize the application URL
- Parameterize browser/environment
- Publish ExtentReports as build artifacts
- Capture screenshots on failure
- Add headless execution support
- Add parallel TestNG execution
- Add environment-specific property files

## Recommended Improvements

For production-scale automation, the following improvements would make the framework more maintainable:

1. Move credentials out of `common.properties`.
2. Replace implicit waits with explicit/WebDriverWait-based synchronization.
3. Introduce separate page-object packages for each application area.
4. Add test data builders or external test data management.
5. Add environment-specific configuration.
6. Add CI/CD pipeline configuration.
7. Add browser headless mode.
8. Add parallel execution configuration.
9. Add API/database utilities if required by the test strategy.
10. Upgrade/clean up legacy dependencies where appropriate.
11. Add a dedicated `src/test/java` Maven-standard directory structure.
12. Add screenshots and reports as CI artifacts.
13. Add meaningful TestNG `description` values for better reporting.
14. Add README badges and project contribution guidelines for a public GitHub repository.

## Troubleshooting

### Maven command is not recognized

Install Maven and ensure Maven's `bin` directory is available in the system `PATH`.

### Java version issue

Verify:

```bash
java -version
```

The project compiler is configured for:

```text
Java 17
```

### Browser driver issue

WebDriverManager is responsible for driver setup. Ensure the machine has internet access when a required driver needs to be downloaded.

### Application URL is unreachable

Check the `url` property in:

```text
common.properties
```

and verify that the application environment is accessible from the execution machine/network.

### Login test fails

Verify:

- Application is available
- Phone number is valid for the test environment
- Password is valid
- OTP is valid
- Login page locators have not changed

## Example GitHub Workflow

A simple CI job can execute:

```yaml
name: UI Automation

on:
  push:
  pull_request:

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven

      - name: Run tests
        run: mvn clean test
```

For real projects, credentials and environment-specific values should be supplied through GitHub Actions Secrets/Variables rather than committed to the repository.

## Author / Project

**Java Web UI Automation Framework**

Built with Selenium WebDriver, Java, TestNG, Maven, Page Object Model, WebDriverManager, and ExtentReports.

---

### Quick Start

```bash
git clone <repository-url>
cd java-web-ui-automation-main
mvn clean test
```

After execution, check:

```text
reports/extent-report.html
```

for the ExtentReports execution report.
