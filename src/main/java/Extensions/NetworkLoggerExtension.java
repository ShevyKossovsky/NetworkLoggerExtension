package Extensions;

import Driver.DriverStoreManager;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

/**
 * This extension enables the logging of network requests and responses during
 * the execution of Selenium WebDriver tests. It integrates with the DevTools
 * protocol to intercept network activity (requests and responses) and logs
 * relevant information to the console for further analysis.
 * <p>
 * The extension will create a new ChromeDriver instance before each test execution,
 * capture network activity, and ensure that the logs are printed after each test
 * execution. In case of any test execution exceptions, the extension will log the
 * intercepted network data before the exception is thrown.
 */
public class NetworkLoggerExtension implements BeforeEachCallback, AfterEachCallback, TestExecutionExceptionHandler {

    private ChromeDriver driver;
    private final List<String> networkLogs = new CopyOnWriteArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(NetworkLoggerExtension.class);

    /**
     * This method is executed before each test, initializing the ChromeDriver,
     * connecting to DevTools, and starting the network activity interception.
     *
     * @param context The context of the test execution.
     * @throws Exception if the WebDriver cannot be initialized or if there is an error in DevTools.
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        driver = new ChromeDriver();
        DriverStoreManager.setCurrentDriver(driver);

        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized.");
        }

        // Initialize DevTools to intercept network requests and responses
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // Listen for network requests and log them
        devTools.addListener(Network.requestWillBeSent(), request -> {
            String logEntry = String.format(
                    "Request: [Method: %s, URL: %s]",
                    request.getRequest().getMethod(), request.getRequest().getUrl()
            );
            networkLogs.add(logEntry);
        });

        // Listen for network responses and log them
        devTools.addListener(Network.responseReceived(), response -> {
            String logEntry = String.format(
                    "Response: [Status: %d, URL: %s, Content-Type: %s]",
                    response.getResponse().getStatus(), response.getResponse().getUrl(), response.getResponse().getMimeType()
            );
            networkLogs.add(logEntry);
        });
    }

    /**
     * This method is executed after each test, ensuring that the WebDriver is quit
     * after the test execution, regardless of whether the test passes or fails.
     *
     * @param context The context of the test execution.
     * @throws Exception if there is an error while quitting the WebDriver.
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Handles any test execution exceptions, logs intercepted network data,
     * and ensures that the WebDriver is quit before the exception is thrown.
     *
     * @param context The context of the test execution.
     * @param throwable The throwable exception that occurred during test execution.
     * @throws Throwable rethrows the exception after logging network data.
     */
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        logNetwork();
        if (driver != null) {
            driver.quit();
        }
        throw throwable;
    }

    /**
     * Logs the captured network activity (requests and responses) to the console.
     * If no network activity was captured, it logs a message indicating that.
     * Method for private use only.
     */
    private void logNetwork() {
        if (!networkLogs.isEmpty()) {
            networkLogs.forEach(logger::info);
        } else {
            logger.info("No network requests were intercepted.");
        }
    }
}
