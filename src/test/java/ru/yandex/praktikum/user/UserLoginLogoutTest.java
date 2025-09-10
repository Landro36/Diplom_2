package ru.yandex.praktikum.user;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Epic;
import ru.yandex.praktikum.entity.User;
import ru.yandex.praktikum.api.UserClient;
import ru.yandex.praktikum.constants.RandomData;
import org.apache.commons.lang3.StringUtils;
import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Login and logout user")
public class UserLoginLogoutTest {
    private ValidatableResponse response;
    private UserClient userClient;
    private User user;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        user = RandomData.getRandomData();
        userClient = new UserClient();
    }

    @AfterEach
    public void clearState() {
        userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("User login by valid credentials")
    public void userLoginByValidCredentials() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.loginUserByValidCredentials(user, accessToken);
        int statusCode = response.extract().statusCode();
        boolean isLogin = response.extract().path("success");

        assertThat("Token is null", accessToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("User is login incorrect", isLogin, equalTo(true));
    }

    @Test
    @DisplayName("User logout by valid credentials")
    public void userLogoutByValidCredentials() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.loginUserByValidCredentials(user, accessToken);
        String refreshToken = response.extract().path("refreshToken");
        refreshToken = "{\"token\":\"" + refreshToken + "\"}";
        response = userClient.logoutUserByValidCredentials(refreshToken);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isLogout = response.extract().path("success");

        assertThat("Token is null", refreshToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("Message not equal", message, equalTo("Successful logout"));
        assertThat("User is logout incorrect", isLogout, equalTo(true));
    }

    @Test
    @DisplayName("User login is empty email")
    public void userLoginByEmptyEmail() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        user.setEmail(null);
        response = userClient.loginUserByValidCredentials(user, accessToken);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isLogin = response.extract().path("success");

        assertThat("Token is null", accessToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo("email or password are incorrect"));
        assertThat("User is login correct", isLogin, equalTo(false));
    }

    @Test
    @DisplayName("User login is empty password")
    public void userLoginByEmptyPassword() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        user.setPassword(null);
        response = userClient.loginUserByValidCredentials(user, accessToken);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isLogin = response.extract().path("success");

        assertThat("Token is null", accessToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo("email or password are incorrect"));
        assertThat("User is login correct", isLogin, equalTo(false));
    }
}