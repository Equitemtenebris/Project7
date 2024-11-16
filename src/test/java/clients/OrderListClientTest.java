package clients;

import io.restassured.response.Response;
import io.qameta.allure.Step;
import org.junit.Test;
import sprint7.clients.OrderListClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderListClientTest {

    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";
    private final OrderListClient orderListClient = new OrderListClient(baseUrl);

    @Test
    @Step("Получение списка заказов без courierId")
    public void getOrdersWithoutCourierId_Success() {
        Response response = orderListClient.getOrders();
        validateResponse(response);
    }

    @Step("Проверка ответа")
    private void validateResponse(Response response) {
        // Проверяем, что код ответа SC_OK (200)
        assertEquals("Ожидаемый код ответа 200", SC_OK, response.getStatusCode());

        // Проверяем, что тело ответа содержит список заказов
        assertTrue("Список заказов должен быть не пустым",
                response.jsonPath().getList("orders").size() > 0);
    }
}
