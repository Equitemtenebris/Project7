package sprint7.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class OrderListClient {
    private final String baseUrl;

    public OrderListClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response getOrders() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .when()
                .get("/api/v1/orders")
                .then()
                .extract()
                .response();
    }
}
