import Orders.Order;
import Orders.OrdersMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class ParametrizedCreateOrderTest {
    private final List<String> colors;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public ParametrizedCreateOrderTest(List<String> colors){
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][] {
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()},
        };
    }

    @Test
    @DisplayName("Is order created with different colors")
    public void createOrderWithDifferentColorsCreated(){
        Order order = new Order(colors);

        Response response = OrdersMethods.createOrder(order);
        response.then().assertThat().statusCode(201).and()
                .body("track", notNullValue());
    }
}
