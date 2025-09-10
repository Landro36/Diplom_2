package ru.yandex.praktikum.order;

import io.qameta.allure.Epic;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.api.OrderClient;
import ru.yandex.praktikum.api.UserClient;
import ru.yandex.praktikum.constants.RandomData;
import ru.yandex.praktikum.entity.Order;
import ru.yandex.praktikum.entity.User;
import java.util.List;

@Epic("Get order and ingredients")
public class OrderGetTest {
    private ValidatableResponse response;
    private User user;
    private Order order;
    private UserClient userClient1;
    private OrderClient orderClient1;

    @BeforeEach
    public void setUp() {
        user = RandomData.getRandomData();
        order = new Order();
        userClient1 = new UserClient();
        orderClient1 = new OrderClient();
        fullListIngredients();
    }

    @Test
    @DisplayName("Get all ingredients")
    public void getAllIngredients() {
        Response responseGetAllIngredients = orderClient1.getAllIngredients();
        orderClient1.checkAllIngredients(responseGetAllIngredients);
    }

    @Test
    @DisplayName("Get all orders")
    public void getAllOrders() {
        Response responseCreateOrderWithoutAuthorization = orderClient1.createOrderWithoutAuthorization(order);
        Response responseGetAllOrders = orderClient1.getAllOrders();
        orderClient1.checkAllOrders(responseGetAllOrders);
    }

    @Test
    @DisplayName("Get order by authorization user")
    public void getOrderByAuthorizationUser() {
        response = userClient1.createUser(user);
        String accessToken = response.extract().path("accessToken");
        Response responseLoginUser = userClient1.loginUser(user, accessToken);
        Response responseCreateOrderByAuthorization = orderClient1.createOrderByAuthorization(order, accessToken);
        Response responseGetOrdersByAuthorization = orderClient1.getOrdersByAuthorization(accessToken);
        orderClient1.checkGetOrdersByAuthorization(responseGetOrdersByAuthorization);
        response = userClient1.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }

    @Test
    @DisplayName("Get order without authorization user")
    public void getOrderWithoutAuthorizationUser() {
        Response responseCreateOrderWithoutAuthorization = orderClient1.createOrderWithoutAuthorization(order);
        Response responseGetOrdersWithoutAuthorization = orderClient1.getOrdersWithoutAuthorization();
        orderClient1.checkGetOrdersWithoutAuthorization(responseGetOrdersWithoutAuthorization);
    }

    private void fullListIngredients() {
        response = orderClient1.getAllIngredientsFullList();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(5));
        ingredients.add(list.get(0));
    }
}