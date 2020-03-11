package distributed_systems.lab2.homework;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Output {
    private String searchWord;
    private Quote quote;
    private List<SingleEntryOutput> quoteWords;
}
