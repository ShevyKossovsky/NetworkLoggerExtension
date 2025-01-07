import Driver.DriverStoreManager;
import Extensions.NetworkLoggerExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(NetworkLoggerExtension.class)
public class GoogleSearchTest {

    private ChromeDriver driver;

    @BeforeEach
    public void setUp() {
        driver = DriverStoreManager.getCurrentDriver();
        driver.get("https://www.google.com/");
    }

    @Test
    public void searchForGoogleTest() throws InterruptedException {
        WebElement search=driver.findElement(By.name("q"));
        search.sendKeys("Selenium");
        search.submit();
        Thread.sleep(5000);
        assertFalse(driver.getCurrentUrl().contains("Testing"),
                "URL does not contain the expected text.");
    }

}
