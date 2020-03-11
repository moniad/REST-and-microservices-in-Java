package distributed_systems.lab2.homework;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Quote {
    @JsonAlias({"en"})
    private String quote;
    private String author;

    @Override
    public String toString() {
        return "Quote [quote = " + quote + ", author = " + author + "]";
    }
}
