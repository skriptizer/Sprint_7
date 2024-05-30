import Couriers.Courier;
import Couriers.CouriersMethods;
import Orders.Order;
import Orders.OrdersMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class AcceptOrderTest {
    private String courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Is it possible to accept order")
    public void acceptOrderCorrectDataOkTrue(){
        Courier courier = new Courier("lord", "12343", "saskeаа");
        Order order = new Order();

        CouriersMethods.createCourier(courier);
        String track = OrdersMethods.createOrder(order).path("track").toString();

        courierId = CouriersMethods.getCourierId(courier);
        String orderId = OrdersMethods.getOrderByTrack(track).path("order.id").toString();

        Response response = OrdersMethods.acceptOrder(orderId, courierId);
        response.then().assertThat().statusCode(200).and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Is it possible to accept order without courier id")
    public void acceptOrderWithoutCourierIdBadRequest(){
        Order order = new Order();

        String track = OrdersMethods.createOrder(order).path("track").toString();
        String orderId = OrdersMethods.getOrderByTrack(track).path("order.id").toString();

        Response response = OrdersMethods.acceptOrder(orderId, null);
        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Is it possible to accept order without order id")
    public void acceptOrderWithoutOrderIdBadRequest(){
        Courier courier = new Courier("lord", "12343", "saskeаа");

        CouriersMethods.createCourier(courier);
        courierId = CouriersMethods.getCourierId(courier);

        Response response = OrdersMethods.acceptOrder("", courierId);
        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Is it possible to accept order with wrong courier id")
    public void acceptOrderWithWrongCourierIdNotFound(){
        Order order = new Order();

        String track = OrdersMethods.createOrder(order).path("track").toString();
        String orderId = OrdersMethods.getOrderByTrack(track).path("order.id").toString();

        Response response = OrdersMethods.acceptOrder(orderId, "-1");
        response.then().assertThat().statusCode(404).and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Is it possible to accept order with wrong order id")
    public void acceptOrderWithWrongOrderIdNotFound(){
        Courier courier = new Courier("lord", "12343", "saskeаа");

        CouriersMethods.createCourier(courier);
        courierId = CouriersMethods.getCourierId(courier);

        Response response = OrdersMethods.acceptOrder("-1", courierId);
        response.then().assertThat().statusCode(404).and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Is it possible to accept already accepted order")
    public void acceptOrderTwiceConflict(){
        Courier courier = new Courier("lord", "12343", "saskeаа");
        Order order = new Order();

        CouriersMethods.createCourier(courier);
        String track = OrdersMethods.createOrder(order).path("track").toString();

        courierId = CouriersMethods.getCourierId(courier);
        String orderId = OrdersMethods.getOrderByTrack(track).path("order.id").toString();
        OrdersMethods.acceptOrder(orderId, courierId);

        Response response = OrdersMethods.acceptOrder(orderId, courierId);
        response.then().assertThat().statusCode(409).and()
                .body("message", equalTo("Этот заказ уже в работе"));
    }

    @After
    public void deleteCourier(){
        if (courierId != null) {
            CouriersMethods.deleteCourier(courierId);
        }
    }
}
