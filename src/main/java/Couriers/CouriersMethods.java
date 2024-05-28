package Couriers;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class CouriersMethods {

    @Step("Create courier")
    public static Response createCourier(Courier courier){
        return given().header("Content-type", "application/json")
                .auth().none()
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step("Log in")
    public static Response login(Courier courier){
        return given().header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login");
    }

    @Step("Get courier Id")
    public static String getCourierId(Courier courier){
        return login(courier).path("id").toString();
    }

    @Step("Delete courier")
    public static Response deleteCourier(String id){
        return given().header("Content-type", "application/json")
                .delete("/api/v1/courier/" + id);
    }

}
