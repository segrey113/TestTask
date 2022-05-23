package api;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Test {
    public static void main(String[] args) {
        OrderFactory factory = OrderFactory.getFactory();
        Order order= null;
        try {
           // order = factory.createOrder("https://web.rbsdev.com/sbrfpayment-release/rest" ,"a-test-emelyanov-api","19052022","3003799","http://kzntest.ru/",643 );
            System.out.println(  order.getOrderStatus());
            System.out.println(order.getId());
            System.out.println(order.getUrl());
         //   order.pay("24","12","5555555555555599","GGjk","123","ru",null);
         //   System.out.println(order.getOrderStatus());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }
}
