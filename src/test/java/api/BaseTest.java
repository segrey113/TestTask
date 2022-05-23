package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import utils.Card;
import utils.PropertyHelper;

public class BaseTest {
    protected static OrderFactory factory = OrderFactory.getFactory();
    protected static int sleepTime = PropertyHelper.getConf().getSleepTime() * 1000;
    protected static int maxCountOfPayTry = PropertyHelper.getConf().getMaxCountOfPayTry();

    protected static Card cardWithSms = new Card(
            PropertyHelper.getConf().getCardNumberWithSMS(),
            PropertyHelper.getConf().getCardYearWithSMS(),
            PropertyHelper.getConf().getCardMonthWithSMS(),
            PropertyHelper.getConf().getCardNameWithSMS(),
            PropertyHelper.getConf().getCardCvcWithSMS());

    protected static Card card = new Card(
            PropertyHelper.getConf().getCardNumber(),
            PropertyHelper.getConf().getCardYear(),
            PropertyHelper.getConf().getCardMonth(),
            PropertyHelper.getConf().getCardName(),
            PropertyHelper.getConf().getCardCvc());

    protected Order order;

    protected static void sleep() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    protected Order createOrder(long amount, int currency, int sessionTimeoutSecs) throws JsonProcessingException {
        order = OrderFactory.getFactory().createOrder(
                PropertyHelper.getConf().startUrl(),
                PropertyHelper.getConf().getUserName(),
                PropertyHelper.getConf().getUserPassword(),
                amount,
                PropertyHelper.getConf().returnUrl(),
                currency,
                sessionTimeoutSecs
        );
        Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        return order;
    }
}
