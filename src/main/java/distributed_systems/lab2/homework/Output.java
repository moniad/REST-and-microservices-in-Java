package distributed_systems.lab2.homework;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Output {
    private String searchWord;
    private Quote quote;
    private List<SingleEntryOutput> quoteWords;
}
