package ru.yandex.praktikum.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import static io.restassured.http.ContentType.JSON;
import static ru.yandex.praktikum.constants.Urls.BASE_URL;

public class BurgerConfig {

    protected RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}
