package ui.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import ui.driver.DriverProvider;

public abstract class BasePage {
    WebDriver driver;
    public BasePage() {
        driver=DriverProvider.getDriver();
        PageFactory.initElements(driver, this);
    }
}
