package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sprint7.clients.OrderClient;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderClientTest {

    private OrderClient orderClient;
    private String[] color;

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    public OrderClientTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }



    @Test
    public void createOrderWithDifferentColors() {
        Response response = sendCreateOrderRequest(color);
        validateResponse(response);
    }

    @Step("Отправляем запрос на создание заказа с цветом: {0}")
    private Response sendCreateOrderRequest(String[] color) {
        return orderClient.createOrder(
                "Naruto", "Uchiha", "Konoha, 142 apt.", 4,
                "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color);
    }

    @Step("Проверяем тело ответа и код ответа")
    private void validateResponse(Response response) {
        response.then().statusCode(201)
                .body("track", notNullValue());
    }

    @After
    public void tearDown() {
        // Удаление созданных данных, если требуется
    }
}
