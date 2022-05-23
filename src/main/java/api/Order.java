package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Calendar;


@Getter
@AllArgsConstructor
public class Order {
    private String id;
    private String url;
    private String userName;
    private String password;
    private String basicURL;
    private Response getOrderInformathion(){
        RequestSpecification orderStatusSpecification = new RequestSpecBuilder()
                .setBaseUri(basicURL + "/getOrderStatusExtended.do")
                .addParam("userName", userName)
                .addParam("password", password)
                .addParam("orderId", id)
                .build();
        return RestAssured.given().spec(orderStatusSpecification).when().get().then().extract().response();
    }

    public OrderStatus getOrderStatus() throws JsonProcessingException {
        Response response = getOrderInformathion();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode newNode = mapper.readTree(response.asString());
        switch (newNode.path("orderStatus").asInt()) {
            case (5):
            case (0):
            case (1):
                return OrderStatus.CREATED;
            case (2):
                return OrderStatus.DEPOSITED;
            case (3):
                return OrderStatus.REVERSED;
            case (4):
                return OrderStatus.REFUND;
            case (6):
                return OrderStatus.DECLINED;
            default:throw new IllegalArgumentException();

        }

    }

    public void pay(String cardYear, String cardMonth, String cardNumber, String carsOwnerName, String cvc, String language, String email) {
        cardYear = getCardsYearInFullFormat(cardYear);
        RequestSpecification orderStatusSpecification = new RequestSpecBuilder()
                .setBaseUri(basicURL + "/processform.do")
                .addFormParam("MDORDER", this.id)
                .addFormParam("$EXPIRY", cardYear + cardMonth)
                .addFormParam("$PAN", cardNumber)
                .addFormParam("YYYY", cardYear)
                .addFormParam("TEXT", carsOwnerName)
                .addFormParam("$CVC", cvc)
                .addFormParam("language", language)
                .addFormParam("email", email)
                .addFormParam("bindingNotNeeded", true)
                .addFormParam("jsonParams", "{\"pageName\":\"rbs\"}")
                .build();
        Response response = RestAssured.given().spec(orderStatusSpecification).when().post().then().extract().response();

    }
    public long getRefundedAmount() throws JsonProcessingException {
        Response response=getOrderInformathion();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode newNode = mapper.readTree(response.asString());
        return newNode.path("paymentAmountInfo").path("refundedAmount").asLong();
    }

    public void reverse(){
        RequestSpecification orderStatusSpecification = new RequestSpecBuilder()
                .setBaseUri(basicURL + "/reverse.do")
                .addFormParam("userName", userName)
                .addFormParam("password", password)
                .addFormParam("orderId", id)
                .build();
        Response response = RestAssured.given().spec(orderStatusSpecification).when().post().then().extract().response();
    }

    public void refund(long amount){
        RequestSpecification orderStatusSpecification = new RequestSpecBuilder()
                .setBaseUri(basicURL + "/refund.do")
                .addFormParam("userName", userName)
                .addFormParam("password", password)
                .addFormParam("orderId", id)
                .addFormParam("amount", amount)
                .build();
        Response response = RestAssured.given().spec(orderStatusSpecification).when().post().then().extract().response();
    }

    private String getCardsYearInFullFormat(String cardYear) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        StringBuilder yearBuilder = new StringBuilder();
        yearBuilder.append(year).setLength(yearBuilder.length() - 2);
        return yearBuilder.append(cardYear).toString();
    }
}
