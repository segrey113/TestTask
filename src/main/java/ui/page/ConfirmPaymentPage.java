package ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmPaymentPage {
    private WebDriver driver;
    private By passwordInputField = By.xpath("//input[@id='password']");

    public ConfirmPaymentPage(WebDriver driver) {
        this.driver = driver;
    }

    public ConfirmPaymentPage inputPassword(String password) {
        driver.findElement(passwordInputField).sendKeys(password);
        return this;
    }
}

