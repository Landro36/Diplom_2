package ru.yandex.praktikum.order;

import io.restassured.response.Response;
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
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Get order and ingredients")
public class OrderGetTest {
    private static final String MESSAGE_UNAUTHORIZED = "You should be authorised";
    private ValidatableResponse response;
    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;

    @BeforeEach
    public void setUp() {
        user = RandomData.getRandomData();
        order = new Order();
        userClient = new UserClient();
        orderClient = new OrderClient();
        fullListIngredients();
    }

    @Test
    @DisplayName("Get all ingredients")
    public void getAllIngredients() {
        Response responseGetAllIngredients = orderClient.getAllIngredients();
        orderClient.checkAllIngredients(responseGetAllIngredients);
    }

    @Test
    @DisplayName("Get all orders")
    public void getAllOrders() {
        response = orderClient.createOrderWithoutAuthorization(order);
        response = orderClient.getAllOrders();
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Orders is get incorrect", isGet, equalTo(true));

    }

    @Test
    @DisplayName("Get order by authorization user")
    public void getOrderByAuthorizationUser() {
        response = userClient.createUser(user);
        String accessToken = response.extract().path("accessToken");
        response = userClient.loginUser(user, accessToken);
        response = orderClient.createOrderByAuthorization(order, accessToken);
        response = orderClient.getOrdersByAuthorization(accessToken);
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Order is get incorrect", isGet, equalTo(true));
    }

    @Test
    @DisplayName("Get order without authorization user")
    public void getOrderWithoutAuthorizationUser() {
        response = orderClient.createOrderWithoutAuthorization(order);
        response = orderClient.getOrdersWithoutAuthorization();
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isGet = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo(MESSAGE_UNAUTHORIZED));
        assertThat("Order is get correct", isGet, equalTo(false));
    }

    private void fullListIngredients() {
        response = orderClient.getAllIngredientsFullList();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(5));
        ingredients.add(list.get(0));
    }
}
