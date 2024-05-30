import Orders.Order;
import Orders.OrdersMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderByTrackTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Get correct order by track")
    public void getCorrectOrderByCorrectTrackOk(){
        Order order = new Order();
        String track = OrdersMethods.createOrder(order).path("track").toString();

        Response response = OrdersMethods.getOrderByTrack(track);
        response.then().assertThat().statusCode(200).and()
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Get order without track")
    public void getOrderWithoutTrackBadRequest(){
        Response response = OrdersMethods.getOrderByTrack(null);
        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Get order with wrong track")
    public void getOrderWithWrongTrackNotFound(){
        Response response = OrdersMethods.getOrderByTrack("0");
        response.then().assertThat().statusCode(404).and()
                .body("message", equalTo("Заказ не найден"));
    }

}
