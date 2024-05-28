package Orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersMethods {

    @Step("Create order")
    public static Response createOrder(Order order){
        return given().header("Content-type", "application/json")
                .auth().none()
                .body(order)
                .post("/api/v1/orders");
    }

    @Step("Get list of orders")
    public static Response getListOfOrders(){
        return given().header("Content-type", "application/json")
                .auth().none()
                .get("/api/v1/orders");
    }

    @Step("Get order by track")
    public static Response getOrderByTrack(String track){
        return given().header("Content-type", "application/json")
                .auth().none()
                .queryParam("t", track)
                .get("/api/v1/orders/track");
    }

    @Step("Accept order")
    public static Response acceptOrder(String orderId, String courierId){
        return given().header("Content-type", "application/json")
                .auth().none()
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/" + orderId);
    }
}
