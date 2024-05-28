import Orders.OrdersMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetListOfOrdersTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Is it possible to get list of orders")
    public void getListOfOrdersOkNotNullList(){
        Response response = OrdersMethods.getListOfOrders();
        response.then().assertThat().statusCode(200).and()
                .body("orders", notNullValue());
    }
}
