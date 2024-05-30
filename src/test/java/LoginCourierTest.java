import Couriers.Courier;
import Couriers.CouriersMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class LoginCourierTest {
    private String id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Is courier logged")
    public void loginCourierCorrectDataOk(){
        Courier courier = new Courier("ivaka", "12343", "saske");
        CouriersMethods.createCourier(courier);
        id = CouriersMethods.getCourierId(courier);

        Response response = CouriersMethods.login(courier);
        response.then().assertThat().statusCode(200).and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Is courier logged without login")
    public void logInCourierWithoutLoginBadRequest(){
        Courier courier = new Courier("", "1234");

        Response response = CouriersMethods.login(courier);
        response.then().assertThat().statusCode(400).and()
                .body("message",equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("is courier logged without password")
    public void logInCourierWithoutPasswordBadRequest(){
        Courier courier = new Courier("ninja","");

        Response response = CouriersMethods.login(courier);
        response.then().assertThat().statusCode(400).and()
                .body("message",equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("is courier logged with wrong password")
    public void logInCourierWithWrongPasswordNotFound(){
        Courier courier = new Courier("kuzad", "1234");
        CouriersMethods.createCourier(courier);
        id = CouriersMethods.getCourierId(courier);

        Response response = CouriersMethods.login(new Courier("kuzad", "1235"));
        response.then().assertThat().statusCode(404).and()
                .body("message",equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("is courier logged with wrong login")
    public void logInCourierWithWrongLoginNotFound(){
        Courier courier = new Courier("kuzad", "1234");
        CouriersMethods.createCourier(courier);
        id = CouriersMethods.getCourierId(courier);

        Response response = CouriersMethods.login(new Courier("kuzadi", "1234"));
        response.then().assertThat().statusCode(404).and()
                .body("message",equalTo("Учетная запись не найдена"));
    }

    @After
    public void deleteCourier(){
        if (id != null) {
            CouriersMethods.deleteCourier(id);
        }
    }
}
