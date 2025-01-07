package Driver;

import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * The DriverStoreManager class is a singleton that manages a central store for WebDriver instances.
 * It provides a thread-safe mechanism for storing and retrieving WebDriver objects, specifically ChromeDriver instances,
 * associated with unique keys. This class is designed for scenarios where WebDriver instances need to be accessed
 * globally across different parts of the application or test execution flow.
 * <p>
 * The DriverStoreManager ensures that only one instance of the store exists throughout the lifecycle of the application
 * or test execution, providing centralized control over WebDriver instances.
 * </p>
 *
 * @author Shevy Kossovsky
 */
public class DriverStoreManager {

    // A map that holds key-value pairs where keys are Strings and values are ChromeDriver instances.
    private static final Map<String, ChromeDriver> driversMap = new HashMap<>();
    private static ChromeDriver currentDriver;

    /**
     * Private constructor to prevent external instantiation of the class.
     * As a singleton, the DriverStoreManager ensures that only one instance of the store exists
     * throughout the lifecycle of the application or test execution.
     */
    private DriverStoreManager() {
    }

    /**
     * Adds a ChromeDriver instance to the internal drivers map.
     * The key is a String that uniquely identifies the ChromeDriver instance, and the value is the ChromeDriver itself.
     * <p>
     * This method is static, allowing it to be called without an instance of the class.
     * </p>
     *
     * @param key   the unique key to associate with the ChromeDriver instance, must not be null
     * @param value the ChromeDriver instance to be stored, may be null
     * @throws IllegalArgumentException if the key is null
     */
    public static void addDriverToDriversMap(String key, ChromeDriver value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        driversMap.put(key, value);
    }

    /**
     * Retrieves the ChromeDriver instance associated with the given key from the internal map.
     * The key is used to look up the value stored in the map.
     * <p>
     * This method returns null if no ChromeDriver instance is associated with the key.
     * </p>
     *
     * @param key the unique key whose associated ChromeDriver instance is to be returned, must not be null
     * @return the ChromeDriver instance associated with the key, or null if the key does not exist in the map
     * @throws IllegalArgumentException if the key is null
     */
    public static ChromeDriver getDriverFromDriversMap(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        return driversMap.get(key);
    }

    /**
     * Sets the current active ChromeDriver instance to be used globally.
     *
     * @param driver the ChromeDriver instance to be set as the current driver
     */
    public static void setCurrentDriver(ChromeDriver driver) {

        currentDriver = driver;
    }

    /**
     * Retrieves the current active ChromeDriver instance.
     *
     * @return the current active ChromeDriver instance
     */
    public static ChromeDriver getCurrentDriver() {
        return currentDriver;
    }

    /**
     * Removes the current active ChromeDriver instance.
     * After calling this method, {@link #getCurrentDriver()} will return null.
     */
    public static void removeCurrentDriver() {
        currentDriver = null;
    }

    /**
     * Removes the ChromeDriver instance associated with the given key from the internal map.
     *
     * @param key the key whose associated ChromeDriver instance is to be removed
     * @throws IllegalArgumentException if the key is null
     */
    public static void removeDriverFromDriversMap(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        driversMap.remove(key);
    }

    /**
     * Checks if the map contains an entry for the given key.
     *
     * @param key the key to check for in the map
     * @return true if the map contains an entry for the key, false otherwise
     * @throws IllegalArgumentException if the key is null
     */
    public static boolean containsDriverInDriversMap(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        return driversMap.containsKey(key);
    }

    /**
     * Clears all entries from the internal map.
     * This removes all stored ChromeDriver instances.
     */
    public static void clearDriversMap() {
        driversMap.clear();
    }

    /**
     * Returns the number of ChromeDriver instances currently stored in the internal map.
     *
     * @return the number of ChromeDriver instances in the map
     */
    public static int driversNoInMap() {
        return driversMap.size();
    }

    /**
     * Retrieves all ChromeDriver instances stored in the internal map.
     *
     * @return a Map containing all keys and their corresponding ChromeDriver instances
     */
    public static Map<String, ChromeDriver> getAllDrivers() {
        return driversMap;
    }
}
