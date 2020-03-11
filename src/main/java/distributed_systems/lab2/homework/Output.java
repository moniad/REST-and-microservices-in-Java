package distributed_systems.lab2.homework;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Output {
    private String word;
    private List<Definition> definitions;

    @Override
    public String toString() {
        return "word: " + word + ", defs: " + definitions.toString();
    }
}
