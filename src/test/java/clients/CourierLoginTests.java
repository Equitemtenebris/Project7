package clients;

import sprint7.clients.CourierClient;
import sprint7.models.Courier;
import sprint7.models.CourierId;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static sprint7.generators.CourierGenerator.randomCourier;

public class CourierLoginTests {

    private CourierClient courierClient;
    private Courier courier;
    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierClient = new CourierClient();
        courier = randomCourier();
        courierClient.create(courier);  // Создаем курьера для авторизации
    }

    @Test
    @Step("Проверка успешной авторизации курьера")
    public void courierCanLoginSuccessfully() {
        Response loginResponse = courierClient.login(courier);
        id = loginResponse.as(CourierId.class).getId();

        assertEquals("Неверный статус код при успешной авторизации", SC_OK, loginResponse.statusCode());
        assertNotNull("ID курьера должен быть возвращен при успешной авторизации", id);
    }

    @Test
    @Step("Проверка авторизации без логина")
    public void loginWithoutLoginReturnsError() {
        courier.withLogin(null);  // Убираем логин
        Response loginResponse = courierClient.login(courier);

        assertEquals("Неверный статус код при отсутствии логина", SC_BAD_REQUEST, loginResponse.statusCode());
    }

    @Test
    @Step("Проверка авторизации без пароля")
    public void loginWithoutPasswordReturnsError() {
        courier.withPassword(null);  // Убираем пароль
        Response loginResponse = courierClient.login(courier);

        assertEquals("Неверный статус код при отсутствии пароля", SC_BAD_REQUEST, loginResponse.statusCode());
    }



    @Test
    @Step("Проверка ошибки при неверном логине")
    public void loginWithIncorrectLoginReturnsError() {
        courier.withLogin("wrongLogin");  // Неверный логин
        Response loginResponse = courierClient.login(courier);

        assertEquals("Неверный статус код при неверном логине", SC_NOT_FOUND, loginResponse.statusCode());
    }

    @Test
    @Step("Проверка ошибки при неверном пароле")
    public void loginWithIncorrectPasswordReturnsError() {
        courier.withPassword("wrongPassword");  // Неверный пароль
        Response loginResponse = courierClient.login(courier);

        assertEquals("Неверный статус код при неверном пароле", SC_NOT_FOUND, loginResponse.statusCode());
    }

    @Test
    @Step("Проверка ошибки при авторизации несуществующего пользователя")
    public void loginNonExistentUserReturnsError() {
        Courier nonExistentCourier = new Courier().withLogin("nonExistent").withPassword("123456");
        Response loginResponse = courierClient.login(nonExistentCourier);

        assertEquals("Неверный статус код при авторизации несуществующего пользователя", SC_NOT_FOUND, loginResponse.statusCode());
    }
}
