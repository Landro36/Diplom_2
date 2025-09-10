package ru.yandex.praktikum.user;

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
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Update user")
public class UserUpdateTest {
    private static final String MESSAGE_UNAUTHORIZED = "You should be authorised";
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
    @DisplayName("Update user by authorization")
    public void updateUserByAuthorization() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.loginUser(user, accessToken);
        response = userClient.updateUserByAuthorization(RandomData.getRandomData(), accessToken);
        int statusCode = response.extract().statusCode();
        boolean isUpdate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_OK));
        assertThat("User is update incorrect", isUpdate, equalTo(true));
    }

    @Test
    @DisplayName("Update user without authorization")
    public void updateUserWithoutAuthorization() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.updateUserWithoutAuthorization(RandomData.getRandomData());
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isUpdate = response.extract().path("success");

        assertThat("Code not equal", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("Message not equal", message, equalTo(MESSAGE_UNAUTHORIZED));
        assertThat("User is update correct", isUpdate, equalTo(false));
    }
}
