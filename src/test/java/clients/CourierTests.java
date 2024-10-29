package clients;

import sprint7.clients.CourierClient;
import sprint7.models.Courier;
import sprint7.models.CourierId;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static sprint7.generators.CourierGenerator.randomCourier;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class CourierTests {

    private CourierClient courierClient;
    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (id != 0) {
            courierClient.delete(id);
        }
    }

    @Test
    @Step("Создание нового курьера с уникальными данными")
    public void createCourier() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());

        Response loginResponse = courierClient.login(courier);
        id = loginResponse.as(CourierId.class).getId();
        assertEquals("Неверный статус код при входе", SC_OK, loginResponse.statusCode());
    }

    @Test
    @Step("Нельзя создать курьера с уже существующими данными")
    public void shouldNotAllowDuplicateCourier() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код при создании", SC_CREATED, response.statusCode());

        // Пробуем создать курьера с тем же логином
        Response duplicateResponse = courierClient.create(courier);
        assertEquals("Должна быть ошибка 409 при создании дубликата", SC_CONFLICT, duplicateResponse.statusCode());

        Response loginResponse = courierClient.login(courier);
        id = loginResponse.as(CourierId.class).getId();
    }

    @Test
    @Step("Нельзя создать курьера без обязательного поля 'пароль'")
    public void shouldNotCreateCourierWithoutPassword() {
        Courier courier = new Courier().withLogin("uniqueLogin").withFirstName("TestName");
        Response response = courierClient.create(courier);
        assertEquals("Должна быть ошибка 400 при отсутствии пароля", SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @Step("Нельзя создать курьера без обязательного поля 'логин'")
    public void shouldNotCreateCourierWithoutLogin() {
        Courier courier = new Courier().withPassword("password123").withFirstName("TestName");
        Response response = courierClient.create(courier);
        assertEquals("Должна быть ошибка 400 при отсутствии логина", SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @Step("Создание курьера с существующим логином")
    public void shouldReturnErrorWhenLoginAlreadyExists() {
        // Создаем первого курьера
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код при создании первого курьера", SC_CREATED, response.statusCode());

        Response loginResponse = courierClient.login(courier);
        id = loginResponse.as(CourierId.class).getId();

        // Создаем курьера с таким же логином
        Courier duplicateCourier = new Courier()
                .withLogin(courier.getLogin())
                .withPassword("newPassword")
                .withFirstName("NewName");
        Response duplicateResponse = courierClient.create(duplicateCourier);
        assertEquals("Должна быть ошибка 409 при создании с существующим логином", SC_CONFLICT, duplicateResponse.statusCode());
    }
}
