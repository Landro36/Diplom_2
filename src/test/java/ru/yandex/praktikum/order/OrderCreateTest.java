package ru.yandex.praktikum.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Epic;
import ru.yandex.praktikum.entity.User;
import ru.yandex.praktikum.entity.Order;
import ru.yandex.praktikum.api.UserClient;
import ru.yandex.praktikum.api.OrderClient;
import ru.yandex.praktikum.constants.RandomData;
import org.apache.commons.lang3.StringUtils;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Создание заказа")
public class OrderCreateTest {
    private static final String MESSAGE_BAD_REQUEST = "Ingredient ids must be provided";
    private ValidatableResponse response;
    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;

    private void fillListIngredients() {
        response = orderClient.getAllIngredientsFullList();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(5));
        ingredients.add(list.get(0));
    }
    @BeforeEach
    public void setUp() {
        user = RandomData.getRandomData();
        order = new Order();
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void orderCreateByAuthorization() {
        fillListIngredients();
        response = userClient.createUser(user);
        String accessToken = response.extract().path("accessToken");
        response = userClient.loginUser(user, accessToken);
        response = orderClient.createOrderByAuthorization(order, accessToken);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        String orderId = response.extract().path("order._id");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Код ответа отличается от ожидаемого", statusCode, equalTo(SC_OK));
        assertThat("Заказ создан неправильно", isCreate, equalTo(true));
        assertThat("Номер заказа равен нулю", orderNumber, notNullValue());
        assertThat("id заказа равен нулю", orderId, notNullValue());
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем")
    public void orderCreateWithoutAuthorization() {
        fillListIngredients();
        response = orderClient.createOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");

        assertThat("Код ответа отличается от ожидаемого", statusCode, equalTo(SC_OK));
        assertThat("Заказ создан неправильно", isCreate, equalTo(true));
        assertThat("Номер заказа равен нулю", orderNumber, notNullValue());
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем без ингредиентов")
    public void orderCreateWithoutAuthorizationAndIngredients() {
        response = orderClient.createOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isCreate = response.extract().path("success");

        assertThat("Код ответа отличается от ожидаемого", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Message отличается от ожидаемого", message, equalTo(MESSAGE_BAD_REQUEST));
        assertThat("Заказ создан неправильно", isCreate, equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем и невалидный хеш ингредиентов")
    public void orderCreateWithoutAuthorizationAndChangeHashIngredient() {
        response = orderClient.getAllIngredientsFullList();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(5).replaceAll("a", "l"));
        ingredients.add(list.get(0));
        response = orderClient.createOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();

        assertThat("Код ответа отличается от ожидаемого", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));

    }
}
