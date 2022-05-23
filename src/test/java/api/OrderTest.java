package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.PropertyHelper;


public class OrderTest extends BaseTest {


    @Test
    public void CreateOrderWithBadDataTest() throws JsonProcessingException {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            OrderFactory.getFactory().createOrder(
                    PropertyHelper.getConf().startUrl(),
                    null,
                    null,
                    0,
                    null,
                    0,
                    1200
            );
        });
    }

    @Test
    public void createOrderTest() throws JsonProcessingException {
        createOrder(3423, 643, 1200);
    }

    @Test
    public void orderRefundTest() throws JsonProcessingException {
        createOrder(100, 643, 1200);
        order.pay(card.getYear(),
                card.getMonth(),
                card.getNumber(),
                card.getName(),
                card.getCvc(),
                "ru",
                null);
        order.refund(100);
        Assertions.assertEquals(OrderStatus.REFUND, order.getOrderStatus());
    }

    @Test
    public void orderDoubleRefundTest() throws JsonProcessingException {
        createOrder(1000, 643, 1200);
        order.pay(card.getYear(),
                card.getMonth(),
                card.getNumber(),
                card.getName(),
                card.getCvc(),
                "ru",
                null);

        order.refund(100);
        order.refund(100);
        Assertions.assertEquals(200, order.getRefundedAmount());

    }

    @Test
    public void manyRefundTest() throws JsonProcessingException {
        createOrder(500, 643, 1200);
        order.pay(card.getYear(),
                card.getMonth(),
                card.getNumber(),
                card.getName(),
                card.getCvc(),
                "ru",
                null);
        for (int i = 1; i <= 5; i++) {
            order.refund(100);
            Assertions.assertEquals(OrderStatus.REFUND, order.getOrderStatus());
            Assertions.assertEquals(i * 100, order.getRefundedAmount());
        }
    }


    @Test
    public void reverseTest() throws JsonProcessingException {
        createOrder(10000, 643, 1200);
        order.pay(card.getYear(),
                card.getMonth(),
                card.getNumber(),
                card.getName(),
                card.getCvc(),
                "ru",
                null);
        order.reverse();
        Assertions.assertEquals(OrderStatus.REVERSED, order.getOrderStatus());
    }

    @Test
    public void doubleReverseTest() throws JsonProcessingException {
        createOrder(10000, 643, 1200);
        order.pay(card.getYear(),
                card.getMonth(),
                card.getNumber(),
                card.getName(),
                card.getCvc(),
                "ru",
                null);
        RequestSpecification orderStatusSpecification = new RequestSpecBuilder()
                .setBaseUri(PropertyHelper.getConf().startUrl() + "/reverse.do")
                .addFormParam("userName", PropertyHelper.getConf().getUserName())
                .addFormParam("password", "null")
                .addFormParam("orderId", order.getId())
                .build();
        Response response = RestAssured.given().spec(orderStatusSpecification).when().post().then().extract().response();
        order.reverse();
        Assertions.assertEquals(OrderStatus.DEPOSITED, order.getOrderStatus());
    }

    @Test
    public void reverseNotPayTest() throws JsonProcessingException {
        createOrder(10000, 643, 1200);
        order.reverse();
        Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());
    }

    @Test
    public void sessionTimeOutDeclinedTest() throws JsonProcessingException {
        createOrder(10000, 643, 3);

        RestAssured.given().spec(new RequestSpecBuilder()
                .setBaseUri(PropertyHelper.getConf().startUrl() + "/processform.do")
                .build())
                .when()
                .post()
                .then()
                .extract()
                .response();
        sleep();
        Assertions.assertEquals(OrderStatus.DECLINED, order.getOrderStatus());
    }


    @Test
    public void sessionTimeOutDeclinedWithPayTest() throws JsonProcessingException {
        createOrder(10000, 643, 10);
        if (maxCountOfPayTry > 1) {


            order.pay(card.getYear(),
                    card.getMonth(),
                    "222222222222",
                    card.getName(),
                    card.getCvc(),
                    "ru",
                    null);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(OrderStatus.DECLINED, order.getOrderStatus());
    }

    @Test
    public void badCVCDeclinedTest() throws JsonProcessingException {
        createOrder(10000, 643, 1200);
        order.pay(card.getYear(),
                card.getMonth(),
                card.getNumber(),
                card.getName(),
                "999",
                "ru",
                null);
        sleep();
        Assertions.assertEquals(OrderStatus.DECLINED, order.getOrderStatus());
    }

    @Test
    public void badCardDeclinedTest() throws JsonProcessingException {
        createOrder(10000, 643, 1200);
        order.pay("23",
                card.getMonth(),
                "2222222222222222",
                card.getName(),
                card.getCvc(),
                "ru",
                null);
        sleep();
        if (maxCountOfPayTry > 1) {
            Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        } else {
            Assertions.assertEquals(OrderStatus.DECLINED, order.getOrderStatus());
        }
    }

    @Test
    public void tooManyTriesToPayDeclinedTest() throws JsonProcessingException {
        createOrder(10000, 643, 1200);
        for (int i = 0; i < maxCountOfPayTry; i++) {
            order.pay("23",
                    card.getMonth(),
                    "2222222222222222",
                    card.getName(),
                    card.getCvc(),
                    "ru",
                    null);
        }
        sleep();
        Assertions.assertEquals(OrderStatus.DECLINED, order.getOrderStatus());
    }

    @Test
    public void tooBigRefundTest() throws JsonProcessingException {
        createOrder(10000, 643, 1200);
        order.pay(card.getYear(),
                card.getMonth(),
                card.getNumber(),
                card.getName(),
                card.getCvc(),
                "ru",
                null);
        order.refund(999999);
        Assertions.assertEquals(0, order.getRefundedAmount());
    }

}
