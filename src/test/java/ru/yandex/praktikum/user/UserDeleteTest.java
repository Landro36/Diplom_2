package ru.yandex.praktikum.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Epic;
import ru.yandex.praktikum.entity.User;
import ru.yandex.praktikum.api.UserClient;
import ru.yandex.praktikum.constants.RandomData;
import org.apache.commons.lang3.StringUtils;
import io.restassured.response.ValidatableResponse;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Delete user")
public class UserDeleteTest {
    private static final String MESSAGE_ACCEPTED = "User successfully removed";
    private UserClient userClient;
    private User user;

    @BeforeEach
    public void setUp() {
        user = RandomData.getRandomData();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("User delete by valid credentials")
    public void userDeleteByValidCredentials() {
        ValidatableResponse response = userClient.createUser(user);
        String accessToken = response.extract().path("accessToken");
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        boolean isDelete = response.extract().path("success");

        assertThat("Token is null", accessToken, notNullValue());
        assertThat("Code not equal", statusCode, equalTo(SC_ACCEPTED));
        assertThat("Message not equal", message, equalTo(MESSAGE_ACCEPTED));
        assertThat("User is delete incorrect", isDelete, equalTo(true));
    }
}