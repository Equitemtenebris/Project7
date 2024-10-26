package clients;

import io.restassured.response.Response;
import io.qameta.allure.Step;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderListClientTest {

    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private final sprint7.clients.OrderListClient orderListClient = new sprint7.clients.OrderListClient(baseUrl);

    @Test
    @Step("Получение списка заказов без courierId")
    public void getOrdersWithoutCourierId_Success() {
        Response response = orderListClient.getOrders();

        validateResponse(response);
    }

    @Step("Проверка ответа")
    private void validateResponse(Response response) {
        // Проверяем, что код ответа 200 (OK)
        assertEquals("Ожидаемый код ответа 200", 200, response.getStatusCode());

        // Проверяем, что тело ответа содержит список заказов
        assertTrue("Список заказов должен быть не пустым",
                response.jsonPath().getList("orders").size() > 0);
    }
}
