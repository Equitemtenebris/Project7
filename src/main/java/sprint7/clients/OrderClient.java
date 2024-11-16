package sprint7.clients;

import io.restassured.response.Response;
import sprint7.models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public Response createOrder(String firstName, String lastName, String address, int metroStation,
                                String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color))
                .when()
                .post("/api/v1/orders");
    }
}
