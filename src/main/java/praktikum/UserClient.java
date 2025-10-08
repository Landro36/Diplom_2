package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.http.ContentType;
import praktikum.model.UserCreated;
import praktikum.model.UserLogin;
import static io.restassured.RestAssured.given;
import static praktikum.Constants.*;

public class UserClient {
    @Step("Создание нового пользователя")
    public ValidatableResponse getNewUser(UserCreated user) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(user)
                .when()
                .post(CREATE_USER)
                .then().log().all();
    }
    @Step("Логин пользователя в системе")
    public ValidatableResponse logInUser(UserLogin user) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(user)
                .when()
                .post(LOGIN_USER)
                .then().log().all();
    }
    @Step("Удаление пользователя из системы")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .header("Authorization", accessToken) // Токен в заголовке
                .when()
                .delete(USER_MANAGEMENT)
                .then().log().all();
    }
    @Step("Получение информации о пользователе")
    public ValidatableResponse getUser(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .get(USER_MANAGEMENT)
                .then().log().all();
    }
    @Step("Обновление информации о пользователе")
    public ValidatableResponse userUpdate(UserLogin user, String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_MANAGEMENT)
                .then().log().all();
    }
}
