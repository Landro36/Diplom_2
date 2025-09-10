package ru.yandex.praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.config.BurgerConfig;
import ru.yandex.praktikum.constants.Urls;
import ru.yandex.praktikum.entity.User;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserClient extends BurgerConfig {
    @Step("Отправить GET запрос в /api/auth/user")
    public Response getUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(Urls.AUTH + "user");
    }
    @Step("Отправить GET запрос в /api/auth/user")
    public ValidatableResponse getUserByValidCredentials(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(Urls.AUTH + "user")
                .then()
                .log().all();
    }
    @Step("Отправить POST запрос в /api/auth/register")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .post(Urls.AUTH + "register")
                .then()
                .log().all();
    }

    @Step("Проверка GET запрос в /api/ingredients")
    public void checkCreateUser(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }
    @Step("Отправить POST запрос в /api/auth/login")
    public Response loginUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .log().all()
                .post(Urls.AUTH + "login");
    }
    @Step("Отправить POST запрос в /api/auth/login")
    public ValidatableResponse loginUserByValidCredentials(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .log().all()
                .post(Urls.AUTH + "login")
                .then()
                .log().all();
    }
    @Step("Проверка POST запрос в /api/auth/logout")
    public void checkLoginUser(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("accessToken", notNullValue())
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }
    @Step("Отправить POST запрос в /api/auth/logout")
    public Response logoutUser(String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .log().all()
                .post(Urls.AUTH + "logout");
    }
    @Step("Отправить POST запрос в /api/auth/logout")
    public ValidatableResponse logoutUserByValidCredentials(String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .log().all()
                .post(Urls.AUTH + "logout")
                .then()
                .log().all();
    }
    @Step("Отправить POST запрос в /api/auth/logout")
    public void checkLogoutUserByEmptyEmail(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("accessToken", notNullValue())
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .assertThat()
                .body("success", equalTo(false));
    }
    @Step("Отправить POST запрос в /api/auth/logout")
    public void checkLogoutUserByEmptyPassword(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("accessToken", notNullValue())
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .assertThat()
                .body("success", equalTo(false));
    }
    @Step("Проверка POST запрос в /api/auth/logout")
    public void checkLogoutUser(Response response) {
        response
                .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("refreshToken", notNullValue())
                .and()
                .assertThat()
                .body("message", equalTo("Successful logout"))
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }
    @Step("Отправить DELETE запрос в /api/auth/user")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .log().all()
                .delete(Urls.AUTH + "user")
                .then()
                .log().all();
    }
    @Step("Отправить PATCH запрос в /api/auth/user")
    public ValidatableResponse updateUserByAuthorization(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .log().all()
                .patch(Urls.AUTH + "user")
                .then()
                .log().all();
    }
    @Step("Отправить PATCH запрос в /api/auth/user")
    public ValidatableResponse updateUserWithoutAuthorization(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .patch(Urls.AUTH + "user")
                .then()
                .log().all();
    }
}