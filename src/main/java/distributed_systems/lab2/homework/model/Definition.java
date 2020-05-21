package distributed_systems.lab2.homework.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Definition {
    private String definition;

    @Override
    public String toString() {
        return "def: " + definition;
    }
}
