# Network Logger Extension for Selenium WebDriver

This project provides an extension for logging network requests and responses during the execution of Selenium WebDriver tests. The extension integrates with the Chrome DevTools protocol to intercept network activity (requests and responses), and logs relevant information to the console for further analysis.

The extension ensures that network data is captured for each test execution and that the logs are printed after each test. In the event of an exception during test execution, the captured network data is logged before the exception is thrown.

## Features

- **Intercept Network Activity:** Logs HTTP requests and responses made during Selenium WebDriver tests.
- **Integration with DevTools:** Uses the Chrome DevTools protocol to capture network traffic.
- **Automatic Logging:** Logs network requests and responses before and after each test run.
- **Exception Handling:** Logs network data if an exception occurs during the test execution.
