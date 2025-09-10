package ru.yandex.praktikum.api;

import io.qameta.allure.Step;
import ru.yandex.praktikum.entity.User;
import ru.yandex.praktikum.config.BurgerConfig;
import ru.yandex.praktikum.constants.Urls;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends BurgerConfig {
    @Step("Отправить GET запрос в /api/auth/user")
    public ValidatableResponse getUser(String accessToken) {
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

    @Step("Отправить POST запрос в /api/auth/login")
    public ValidatableResponse loginUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .log().all()
                .post(Urls.AUTH + "login")
                .then()
                .log().all();
    }

    @Step("Отправить POST запрос в /api/auth/logout")
    public ValidatableResponse logoutUser(String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .log().all()
                .post(Urls.AUTH + "logout")
                .then()
                .log().all();
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
