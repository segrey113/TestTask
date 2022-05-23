package ui.page;

import api.BaseTest;
import api.OrderStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import ui.driver.DriverProvider;
import utils.Card;
import utils.PropertyHelper;

public class PaymentPageTest extends BaseTest {

    private PaymentPage paymentPage;


    private void inputCard(Card card) {
        paymentPage.inputCardNumber(card.getNumber());
        paymentPage.inputMonth(card.getMonth());
        paymentPage.inputYear(card.getYear());
        paymentPage.inputCVCName(card.getCvc());
        paymentPage.inputOwnerName(card.getName());
    }



    private PaymentPage registerOrder(long amount, int currency, int sessionTimeoutSecs) throws JsonProcessingException {
        order =createOrder(amount,currency,sessionTimeoutSecs);
        Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        return paymentPage.open(amount, currency, order.getUrl());

    }

    private void confirmPayment() throws JsonProcessingException {
        ConfirmPaymentPage confirmPaymentPage = paymentPage.clickPayButton();
        sleep();
        Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        confirmPaymentPage.inputPassword(PropertyHelper.getConf().getSmsCode());
        sleep();
    }


    @BeforeEach
    public void setUp() {
        paymentPage = new PaymentPage();
    }

    @AfterAll
    public static void close() {
        DriverProvider.tearDown();
    }

    @Test
    public void correctDataCardWithoutSMS() throws JsonProcessingException {
        paymentPage = registerOrder(2000, 643,1200);
        inputCard(card);
        paymentPage.clickPayButton();
        sleep();
        Assertions.assertEquals(OrderStatus.DEPOSITED, order.getOrderStatus());

    }

    @Test
    public void correctDataCardWithoutSMSWithEmail() throws JsonProcessingException {
        paymentPage = registerOrder(233300, 643,1200);
        paymentPage.clickEmailCheckBox();
        inputCard(card);
        paymentPage.inputEmail(PropertyHelper.getConf().getTestEmail());
        paymentPage.clickPayButton();
        sleep();
        Assertions.assertEquals(OrderStatus.DEPOSITED, order.getOrderStatus());

    }

    @Test
    public void correctDataCardWithSMS() throws JsonProcessingException {
        paymentPage = registerOrder(555555, 643,1200);
        inputCard(cardWithSms);
        confirmPayment();
        Assertions.assertEquals(OrderStatus.DEPOSITED, order.getOrderStatus());

    }

    @Test
    public void correctDataCardWithSMSWithEmail() throws JsonProcessingException {
        paymentPage = registerOrder(235323300, 643,1200);
        paymentPage.clickEmailCheckBox();
        paymentPage.inputEmail(PropertyHelper.getConf().getTestEmail());
        inputCard(cardWithSms);
        confirmPayment();
        Assertions.assertEquals(OrderStatus.DEPOSITED, order.getOrderStatus());

    }

    @Test
    public void correctDataCardWithWrongSMS() throws JsonProcessingException {
        paymentPage = registerOrder(555555, 643,1200);
        inputCard(cardWithSms);
        ConfirmPaymentPage confirmPaymentPage = paymentPage.clickPayButton();
        sleep();
        Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        confirmPaymentPage.inputPassword("11");
        sleep();
        Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());

    }
}
