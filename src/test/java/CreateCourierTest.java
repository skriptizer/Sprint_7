import Couriers.Courier;
import Couriers.CouriersMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    private String id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Is courier created")
    public void createCourierCorrectDataOkTrue(){
        Courier courier = new Courier("lord", "12343", "saskeаа");
        Response response = CouriersMethods.createCourier(courier);
        id = CouriersMethods.getCourierId(courier);

        response.then().assertThat().statusCode(201).and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Is create duplicate courier")
    public void createCourierDuplicateConflict(){
        Courier courier = new Courier("lord", "12343", "saskeаа");
        CouriersMethods.createCourier(courier);
        id = CouriersMethods.getCourierId(courier);

        Response response = CouriersMethods.createCourier(courier);
        response.then().assertThat().statusCode(409).and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Is create courier without login")
    public void createCourierWithoutLoginBadRequest(){
        Courier courier = new Courier(null, "12343", "saskeаа");

        Response response = CouriersMethods.createCourier(courier);
        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Is create courier without password")
    public void createCourierWithoutPasswordBadRequest(){
        Courier courier = new Courier("lord", null, "saskeаа");

        Response response = CouriersMethods.createCourier(courier);
        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Is create courier without first name")
    public void createCourierWithoutFirstNameOkTrue(){
        Courier courier = new Courier("lord", "12343", null);
        Response response = CouriersMethods.createCourier(courier);
        id = CouriersMethods.getCourierId(courier);

        response.then().assertThat().statusCode(201).and()
                .body("ok", equalTo(true));
    }

    @After
    public void deleteCourier(){
        if (id != null) {
            CouriersMethods.deleteCourier(id);
        }
    }
}
