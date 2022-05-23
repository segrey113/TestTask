package ui.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PaymentPage extends BasePage {

    //input fields
    @FindBy(xpath = "//input[@id='iPAN_sub']")
    private WebElement cardNumberInputField;

    @FindBy(xpath = "//input[@id='input-month']")
    private WebElement cardMonthInputField;

    @FindBy(xpath = "//input[@id='input-year']")
    private WebElement cardYearInputField;

    @FindBy(xpath = "//input[@id='iTEXT']")
    private WebElement cardOwnerNameInputField;

    @FindBy(xpath = "//input[@id='iCVC']")
    private WebElement cardCVCInputField;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement emailInputField;

    //checkboxes
    @FindBy(xpath = "//div[@id='emailCheckboxBlock']//span[@class='checkbox']")
    private WebElement emailCheckbox;

    //buttons
    @FindBy(xpath = "//button[@id='buttonPayment']")
    private WebElement payButton;


    //inputs
    public PaymentPage inputCardNumber(String cardNumber) {
        cardNumberInputField.sendKeys(cardNumber);
        return this;
    }

    public PaymentPage inputMonth(String month) {
        cardMonthInputField.sendKeys(month);
        return this;
    }

    public PaymentPage inputYear(String year) {
        cardYearInputField.sendKeys(year);
        return this;
    }

    public PaymentPage inputOwnerName(String ownerName) {
        cardOwnerNameInputField.sendKeys(ownerName);
        return this;
    }

    public PaymentPage inputCVCName(String cvc) {
        cardCVCInputField.sendKeys(cvc);
        return this;
    }

    public PaymentPage inputEmail(String email) {
        emailInputField.sendKeys(email);
        return this;
    }


    //click
    public PaymentPage clickEmailCheckBox() {
        emailCheckbox.click();
        return this;
    }

    public ConfirmPaymentPage clickPayButton() {
        payButton.click();
        return new ConfirmPaymentPage(driver);
    }

    public PaymentPage open(long amount, int currency, String url) throws JsonProcessingException {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(cardNumberInputField));
        return this;
    }
}
