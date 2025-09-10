package ru.yandex.praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.entity.Order;
import ru.yandex.praktikum.config.BurgerConfig;
import ru.yandex.praktikum.constants.Urls;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class OrderClient extends BurgerConfig {
    @Step("Отправить GET запрос в /api/ingredients")
    public Response getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Urls.INGREDIENTS);
    }
    @Step("Send GET request to /api/ingredients")
    public ValidatableResponse getAllIngredientsFullList() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Urls.INGREDIENTS)
                .then()
                .log().all();
    }
    @Step("Проверка GET запрос в /api/ingredients")
    public void checkAllIngredients(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }
    @Step("Отправить GET запрос в /api/orders")
    public ValidatableResponse getOrdersByAuthorization(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(Urls.ORDERS)
                .then()
                .log().all();
    }

    @Step("Отправить GET запрос в /api/orders")
    public ValidatableResponse getOrdersWithoutAuthorization() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Urls.ORDERS)
                .then()
                .log().all();
    }

    @Step("Отправить GET запрос в /api/orders/all")
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Urls.ORDERS + "all")
                .then()
                .log().all();
    }

    @Step("Отправить POST запрос в /api/orders")
    public ValidatableResponse createOrderByAuthorization(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .log().all()
                .post(Urls.ORDERS)
                .then()
                .log().all();
    }

    @Step("Отправить POST запрос в /api/orders")
    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .log().all()
                .post(Urls.ORDERS)
                .then()
                .log().all();
    }
}
