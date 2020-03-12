package distributed_systems.lab2.homework.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import distributed_systems.lab2.homework.Input;
import distributed_systems.lab2.homework.Output;
import distributed_systems.lab2.homework.Quote;
import distributed_systems.lab2.homework.SingleEntryOutput;
import distributed_systems.lab2.homework.util.ParameterStringBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class InputController {
    private static ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @GetMapping("/input")
    public String inputForm(Model model) {
        model.addAttribute("input", new Input());
        return "input";
    }

    @PostMapping("/input")
    public String inputSubmit(@Valid @RequestBody @ModelAttribute Input input, Model model) throws IOException {
        String searchWord = input.getSearchWord();

        Quote randomQuote = getRandomQuoteContainingWord(searchWord);
        List<SingleEntryOutput> singleEntryOutputs = getDefinitionsForEachWordInQuote(searchWord, randomQuote);

        Output output = Output.builder()
                .quote(randomQuote)
                .searchWord(searchWord)
                .quoteWords(singleEntryOutputs)
                .build();

        model.addAttribute("output", output);

        return "result";
    }

    // todo: use searchWord
    private static List<SingleEntryOutput> getDefinitionsForEachWordInQuote(String searchWord, Quote quote) throws IOException {
        String basicUrl = "https://wordsapiv1.p.rapidapi.com/words";
        List<SingleEntryOutput> singleEntryOutputs = new ArrayList<>();

        Map<String, String> parameters = new HashMap<>();


        //splitting a quote into a list of words
        List<String> words = Arrays.stream(quote.getQuote().split("\\W+")).distinct().collect(Collectors.toList());
        System.out.println("WORDS: " + words.toString());

        for (String word : words) {
            parameters.put("param1", word);

            HttpURLConnection connection = getURLConnection(basicUrl.concat(
                    ParameterStringBuilder.getParametersString(parameters))
                    .concat("/definitions"));

            Map<String, String> headers = initHeaders();
            setHeaders(connection, headers);

            String response = getResponse(connection);

            if (response != null) {
                SingleEntryOutput result = mapper.readValue(response, SingleEntryOutput.class);
                System.out.println("RESULT OUTPUT:  " + result);
                singleEntryOutputs.add(result);
            }

            connection.disconnect();
            parameters.remove("param1");
        }

        return singleEntryOutputs;
    }

    public static List<Quote> convertJsonToArrayList(String response) throws JsonParseException, JsonMappingException,
            IOException {

        List<Quote> quotes = mapper.readValue(
                response,
                mapper.getTypeFactory().constructCollectionType(
                        List.class, Quote.class));

        System.out.println("QUOTES: + " + quotes.toString());
        return quotes;
    }

    public static Quote getRandomQuoteContainingWord(String searchWord) throws IOException {
        String url = "http://programming-quotes-api.herokuapp.com/quotes/lang/en/";
        HttpURLConnection connection = getURLConnection(url);
        String response = getResponse(connection);

        List<Quote> quotes = convertJsonToArrayList(response).stream()
                .filter(q -> q.getQuote().contains(searchWord)).collect(Collectors.toList());

        connection.disconnect();

        Random random = new Random();
        int randomIndex = random.nextInt(quotes.size());

        return quotes.get(randomIndex);
    }

    private static String getResponse(HttpURLConnection connection) throws IOException {
        System.out.println("STATUS: " + connection.getResponseCode());

        BufferedReader in;
        StringBuilder content = new StringBuilder();

        if (connection.getResponseCode() > 299) {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            System.out.println("ERROR reading stream: " + in.toString() + ". Word probably not found");
            return null;
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            System.out.println("SUCCESS reading stream: " + content.toString());
            return content.toString();
        }
    }

    private static HttpURLConnection getURLConnection(String urlToConnect) throws IOException {
        URL url = new URL(urlToConnect);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // prepare connection
        connection.setRequestMethod("GET");
        connection.setDoOutput(true); // to add params to request
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(30000); // 30 sec
        connection.setReadTimeout(30000);

        return connection;
    }

    private static Map<String, String> initHeaders() {
        Map<String, String> headers = new HashMap<>();

        headers.put("x-rapidapi-host", "wordsapiv1.p.rapidapi.com");
        headers.put("x-rapidapi-key", "e60a7a24b3msh2abb848f9905257p161d9fjsna298c17d265e");

        return headers;
    }

    private static void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
        for (String headerKey : headers.keySet()) {
            connection.setRequestProperty(headerKey, headers.get(headerKey));
        }
    }
}