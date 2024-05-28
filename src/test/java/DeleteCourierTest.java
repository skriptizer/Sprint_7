import Couriers.Courier;
import Couriers.CouriersMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Is courier deleted")
    public void deleteCourierCorrectIdOkTrue(){
        Courier courier = new Courier("lord", "12343", "saskeаа");
        CouriersMethods.createCourier(courier);
        String id = CouriersMethods.getCourierId(courier);

        Response response = CouriersMethods.deleteCourier(id);
        response.then().assertThat().statusCode(200).and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Is courier deleted with wrong id")
    public void deleteCourierWithWrongIdNotFound(){
        String id = "-1";

        Response response = CouriersMethods.deleteCourier(id);
        response.then().assertThat().statusCode(404).and()
                .body("message", equalTo("Курьера с таким id нет."));
    }

    @Test
    @DisplayName("Is courier deleted without id")
    public void deleteCourierWithoutIdBadRequest(){
        Response response = CouriersMethods.deleteCourier("");
        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

}
