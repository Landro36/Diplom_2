package praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreated {
    private String email;
    private String name;
    private String password;
    //Метод для генерации случайного пользователя
    public  static UserCreated random() {
        var rnd = new Random();
        return new UserCreated(
                "mva" + RandomStringUtils.randomNumeric(10) + "@yandex.ru",
                "mva_" + RandomStringUtils.randomNumeric(10),
                RandomStringUtils.randomNumeric(10)
        );
    }
}
