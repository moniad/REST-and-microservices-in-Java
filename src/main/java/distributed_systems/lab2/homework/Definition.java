package distributed_systems.lab2.homework;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Definition {
    private String definition;
    private String partOfSpeech;

    @Override
    public String toString() {
        return "def: " + definition + " pOS: " + partOfSpeech;
    }
}
