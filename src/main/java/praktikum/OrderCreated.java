package praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreated {
    private List<String> ingredients;
    public OrderCreated(String... ingredients) {
        this.ingredients = List.of(ingredients);
    }
}