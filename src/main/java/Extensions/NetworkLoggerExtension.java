package Extensions;

import Driver.DriverStoreManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v131.network.Network;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

/**
 * This extension enables the logging of network requests and responses during
 * the execution of Selenium WebDriver tests. It integrates with the DevTools
 * protocol to intercept network activity (requests and responses) and logs
 * relevant information to a dedicated log file for further analysis.
 *
 * The extension will:
 * - Initialize a new ChromeDriver instance before each test.
 * - Intercept network activity using DevTools.
 * - Log the intercepted network data to a file named with the test's name,
 *   date, and time in a dedicated "logs" directory.
 */
public class NetworkLoggerExtension implements BeforeEachCallback, AfterEachCallback, TestExecutionExceptionHandler {

    private ChromeDriver driver;
    private final List<String> networkLogs = new CopyOnWriteArrayList<>();

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
        WebDriverManager.chromedriver().setup();
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
     * after the test execution, and that network logs are saved to a file.
     *
     * @param context The context of the test execution.
     * @throws Exception if there is an error while quitting the WebDriver.
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        saveNetworkLogs(context);
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Handles any test execution exceptions, logs intercepted network data,
     * and ensures that the WebDriver is quit before the exception is thrown.
     *
     * @param context   The context of the test execution.
     * @param throwable The throwable exception that occurred during test execution.
     * @throws Throwable rethrows the exception after logging network data.
     */
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        saveNetworkLogs(context);
        if (driver != null) {
            driver.quit();
        }
        throw throwable;
    }

    /**
     * Saves the captured network activity (requests and responses) to a log file.
     * The log file is created in a "logs" directory with a name that includes
     * the test's name, date, and time.
     *
     * @param context The context of the test execution, used to retrieve the test name.
     */
    private void saveNetworkLogs(ExtensionContext context) {
        String testName = context.getTestMethod().map(method -> method.getName()).orElse("unknown-test");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String logFileName = String.format("%s_%s.log", testName, timestamp);
        File logDirectory = new File("logs");

        // Ensure the "logs" directory exists
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }

        // Write logs to the file
        File logFile = new File(logDirectory, logFileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            if (!networkLogs.isEmpty()) {
                for (String logEntry : networkLogs) {
                    writer.write(logEntry);
                    writer.newLine();
                }
            } else {
                writer.write("No network requests were intercepted.");
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write network logs to file: " + e.getMessage());
        }
    }
}
