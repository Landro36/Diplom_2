package praktikum;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import praktikum.model.UserCreated;
import praktikum.model.UserLogin;

import java.net.HttpURLConnection;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;

class UserUpdateAuthTest {
    private UserClient client;
    private String accessToken;
    private UserCreated user;
    @BeforeEach
    public void setUp() {
        client = new UserClient();
        user = UserCreated.random();

        // Создаем пользователя для авторизации и получаем токен
        var response = client.getNewUser(user)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .extract().body().jsonPath();

        accessToken = response.getString("accessToken");

        client.logInUser(UserLogin.from(user))
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Изменение email для авторизованного пользователя, новая почта уникальна")
    public void changeEmailWithNonUniqueEmailForLoggedInUserTest() {
        changeEmailWithNonUniqueEmailForLoggedInUser();
    }

    @Test
    @DisplayName("Изменение email для авторизованного пользователя, новая почта уникальна")
    public void changeEmailWithUniqueEmailForLoggedInUserTest() {
        changeEmailWithUniqueEmailForLoggedInUser();
    }

    @Test
    @DisplayName("Изменение name для авторизованного пользователя")
    public void changeNameForLoggedInUserTest() {
        changeNameForLoggedInUser();
    }

    @Test
    @DisplayName("Изменение password для авторизованного пользователя")
    public void changePasswordForLoggedInUserTest() {
        changePasswordForLoggedInUser();
    }

    @Step("Смена email для авторизованного пользователя")
    private void changeEmailWithUniqueEmailForLoggedInUser() {
        String newEmail = "new_email_" + RandomStringUtils.randomNumeric(10) + "@yandex.ru";

        UserLogin updatedUser = new UserLogin(
                newEmail,               // новый email
                user.getName(),         // старое имя
                user.getPassword()      // старый пароль
        );

        client.userUpdate(updatedUser, accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail))
                .body("user.name", equalTo(user.getName()));

        step("Проверка обновленных данных", () -> {
            client.getUser(accessToken)
                    .assertThat()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .body("success", equalTo(true))
                    .body("user.email", equalTo(newEmail));
        });
    }
    @Step("Смена email на неуникальный для авторизованного пользователя")
    private void changeEmailWithNonUniqueEmailForLoggedInUser() {
        String nonUniqueEmail = user.getEmail();

        UserLogin updatedUser = new UserLogin(
                nonUniqueEmail,
                user.getName(),
                user.getPassword()
        );
        client.userUpdate(updatedUser, accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN) // Ожидаем ошибку
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));

        step("Проверка, что email остался прежним", () -> {
            client.getUser(accessToken)
                    .assertThat()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .body("success", equalTo(true))
                    .body("user.email", equalTo(nonUniqueEmail));
        });
    }
    @Step("Смена name для авторизованного пользователя")
    private void changeNameForLoggedInUser() {
        String newName = "new_name_" + RandomStringUtils.randomNumeric(10);

        UserLogin updatedUser = new UserLogin(
                user.getEmail(),
                newName,
                user.getPassword()
        );
        client.userUpdate(updatedUser, accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(newName));

        step("Проверка обновленных данных", () -> {
            client.getUser(accessToken)
                    .assertThat()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .body("success", equalTo(true))
                    .body("user.name", equalTo(newName));
        });
    }
    @Step("Смена password для авторизованного пользователя")
    private void changePasswordForLoggedInUser() {
        String newPassword = "Password_" + RandomStringUtils.randomNumeric(10);

        UserLogin updatedUser = new UserLogin(
                user.getEmail(),
                user.getName(),
                newPassword
        );
        client.userUpdate(updatedUser, accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }
    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            client.deleteUser(accessToken)
                    .assertThat()
                    .statusCode(HttpURLConnection.HTTP_ACCEPTED)
                    .body("success", equalTo(true))
                    .body("message", equalTo("User successfully removed"));
        }
    }
}