package praktikum;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import praktikum.model.OrderCreated;
import static io.restassured.RestAssured.given;
import static praktikum.Constants.*;

public class OrderClient {
    @Step("Создание нового заказа")
    public ValidatableResponse createOrder(OrderCreated order, String accessToken) {

        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .header("Authorization", accessToken) // Токен для авторизованного пользователя
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then().log().all();
    }
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(OrderCreated order) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then().log().all();
    }
    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .when()
                .get(GET_INGREDIENTS)
                .then().log().all();
    }
    @Step("Получение заказов пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .get(CREATE_ORDER)
                .then().log().all();
    }
    @Step("Получение заказов без авторизации")
    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .when()
                .get(CREATE_ORDER)
                .then().log().all();
    }
}
