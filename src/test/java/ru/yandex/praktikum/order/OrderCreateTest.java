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

@Epic("Создание заказа")
public class OrderCreateTest {
    private ValidatableResponse response;
    private User user;
    private Order order;
    private UserClient userClient1;
    private OrderClient orderClient1;

    private void fillListIngredients() {
        response = orderClient1.getAllIngredientsFullList();
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
        userClient1 = new UserClient();
        orderClient1 = new OrderClient();
    }
    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void orderCreateByAuthorization() {
        fillListIngredients();
        response = userClient1.createUser(user);
        String accessToken = response.extract().path("accessToken");
        Response responseLoginUser = userClient1.loginUser(user, accessToken);
        Response responseCreateOrderByAuthorization = orderClient1.createOrderByAuthorization(order, accessToken);
        orderClient1.checkCreateOrderByAuthorization(responseCreateOrderByAuthorization);
        response = userClient1.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Создание заказа не авторизованным пользователем")
    public void orderCreateWithoutAuthorization() {
        fillListIngredients();
        Response responseCreateOrderWithoutAuthorization = orderClient1.createOrderWithoutAuthorization(order);
        orderClient1.checkCreateOrderWithoutAuthorization(responseCreateOrderWithoutAuthorization);
    }
    @Test
    @DisplayName("Создание заказа не авторизованным пользователем без ингредиентов")
    public void orderCreateWithoutAuthorizationAndIngredients() {
        Response responseCreateOrderWithoutAuthorization = orderClient1.createOrderWithoutAuthorization(order);
        orderClient1.checkCreateWithoutAuthorizationAndChangeHashIngredient(responseCreateOrderWithoutAuthorization);
    }
    @Test
    @DisplayName("Create order without authorization and change hash ingredient")
    public void orderCreateWithoutAuthorizationAndChangeHashIngredient() {
        response = orderClient1.getAllIngredientsAndChangeHashIngredient();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(5).replaceAll("a", "l"));
        ingredients.add(list.get(0));
        Response responseCreateOrderWithoutAuthorization = orderClient1.createOrderWithoutAuthorization(order);
        orderClient1.checkAllIngredientsAndChangeHashIngredient(responseCreateOrderWithoutAuthorization);
    }
}