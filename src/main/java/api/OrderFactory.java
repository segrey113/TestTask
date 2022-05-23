package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

public class OrderFactory {
    private static OrderFactory factory = null;
    private String register = "/register.do?";

    private OrderFactory() {
    }

    public static OrderFactory getFactory() {
        if (factory == null) factory = new OrderFactory();
        return factory;
    }

    public Order createOrder(String basicUrl, String userName, String password, long amount, String returnUrl, int currency, int sessionTimeoutSecs) throws JsonProcessingException {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(basicUrl+register)
                .addParam("userName", userName)
                .addParam("password", password)
                .addParam("amount", amount)
                .addParam("returnUrl", returnUrl)
                .addParam("currency", currency)
                .addParam("sessionTimeoutSecs", sessionTimeoutSecs)
                .build();
        Response response = RestAssured.given().spec(specification).when().get().then().extract().response();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode newNode = mapper.readTree(response.asString());

        if( newNode.path("errorCode").asInt()>0)throw new  IllegalArgumentException();
        return new Order( newNode.path("orderId").textValue(),newNode.path("formUrl").textValue(),userName,password,basicUrl);

    }


}
