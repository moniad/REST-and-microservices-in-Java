package distributed_systems.lab2.homework.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SingleEntryOutput {
    private String word;
    private List<Definition> definitions;

    @Override
    public String toString() {
        return "word: " + word + ", defs: " + definitions.toString();
    }
}
