# Network Logger Extension for Selenium WebDriver

This project provides an extension for logging network requests and responses during the execution of Selenium Chrome Driver tests. The extension integrates with the Chrome DevTools protocol to intercept network activity (requests and responses), and saves the logs to individual files for further analysis.

The extension ensures that network data is captured for each test execution and that the logs are written to a dedicated directory. In the event of an exception during test execution, the captured network data is also logged before the exception is thrown.

## Features

- **Intercept Network Activity:** Logs HTTP requests and responses made during Selenium WebDriver tests.
- **Integration with DevTools:** Uses the Chrome DevTools protocol to capture network traffic.
- **Automatic Logging:** Saves network requests and responses to a file after each test run.
- **Exception Handling:** Logs network data to a file if an exception occurs during the test execution.

## Logging Details

- **Log Directory:** All log files are saved in a dedicated `logs` directory created in the root of the project.
- **File Naming Convention:** Each log file is named using the format: `TestName_YYYY-MM-DD_HH-mm-ss.txt`.
  - `TestName` corresponds to the name of the test being executed.
  - `YYYY-MM-DD_HH-mm-ss` is the timestamp indicating when the test started.
- **Log Content:** Each log file contains a detailed list of network requests and responses captured during the test execution.
  - Example entries include:
    - `Request: [Method: GET, URL: https://example.com]`
    - `Response: [Status: 200, URL: https://example.com, Content-Type: text/html]`

## Usage Instructions

1. **Setup:** Include this extension in your Selenium WebDriver test framework.
2. **Execution:** Run your tests as usual. The extension will automatically capture network activity.
3. **Logs:** Check the `logs` directory for detailed log files after test execution.

## Benefits

- Provides comprehensive insights into network activity during tests.
- Helps debug HTTP requests and responses efficiently.
- Automatically organizes logs for easy access and review.
