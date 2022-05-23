package ui.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import utils.AppConfig;
import utils.PropertyHelper;

import java.time.Duration;

public class DriverProvider {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        AppConfig config = PropertyHelper.getConf();
        if (driver == null) {
            if (config.webDriverBrowserName().equalsIgnoreCase(Browsers.CHROME.getName())) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (config.webDriverBrowserName().equalsIgnoreCase(Browsers.FIREFOX.getName())) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else {
                throw new IllegalArgumentException(config.webDriverBrowserName() + " is not supported");
            }
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.pageLoadTimeout()));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.elementTimeout()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.elementTimeout()));
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
