package ru.yandex.praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.config.BurgerConfig;
import ru.yandex.praktikum.constants.Urls;
import ru.yandex.praktikum.entity.Order;
import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderClient extends BurgerConfig {
    @Step("Отправить GET запрос в /api/ingredients")
    public Response getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Urls.INGREDIENTS);
    }
    @Step("Отправить GET запрос в /api/ingredients")
    public ValidatableResponse getAllIngredientsAndChangeHashIngredient() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Urls.INGREDIENTS)
                .then()
                .log().all();
    }
    @Step("Проверка GET запрос в /api/ingredients")
    public void checkAllIngredientsAndChangeHashIngredient(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
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
    public Response getOrdersByAuthorization(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(Urls.ORDERS);
    }
    @Step("Проверка GET запрос в /api/ingredients")
    public void checkGetOrdersByAuthorization(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }
    @Step("Проверка GET запрос в /api/ingredients")
    public void checkGetOrdersWithoutAuthorization(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }
    @Step("Отправить GET запрос в /api/orders")
    public Response getOrdersWithoutAuthorization() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Urls.ORDERS);
    }
    @Step("Отправить GET запрос в /api/orders/all")
    public Response getAllOrders() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(Urls.ORDERS + "all");
    }
    @Step("Проверка GET запрос в /api/ingredients")
    public void checkAllOrders(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }
    @Step("Отправить POST запрос в /api/orders")
    public Response createOrderByAuthorization(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .log().all()
                .post(Urls.ORDERS);
    }
    @Step("Проверка GET запрос в /api/ingredients")
    public void checkCreateOrderByAuthorization(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .assertThat()
                .body("order.number", notNullValue())
                .and()
                .assertThat()
                .body("order._id", notNullValue());
    }
    @Step("Отправить POST запрос в /api/orders")
    public Response createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .log().all()
                .post(Urls.ORDERS);
    }
    @Step("Проверка POST запрос в /api/orders")
    public void checkCreateOrderWithoutAuthorization(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .assertThat()
                .body("order.number", notNullValue());
    }
    @Step("Проверка POST запрос в /api/orders")
    public void checkCreateWithoutAuthorizationAndChangeHashIngredient(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .assertThat()
                .body("success", equalTo(false));
    }
}