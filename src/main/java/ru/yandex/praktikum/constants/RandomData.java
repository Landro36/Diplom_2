package ru.yandex.praktikum.constants;

import ru.yandex.praktikum.entity.User;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {
    public static User getRandomData() {
        String random_name = RandomStringUtils.randomAlphabetic(10);
        String random_email = random_name.toLowerCase() + "@yandex.ru";
        String random_password = RandomStringUtils.randomAlphabetic(10);

        return new User(random_email, random_password, random_name);
    }
}
