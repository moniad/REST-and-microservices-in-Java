package distributed_systems.lab2.homework.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import distributed_systems.lab2.homework.Input;
import distributed_systems.lab2.homework.Output;
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
import java.util.HashMap;
import java.util.Map;

@Controller
public class InputController {

    @GetMapping("/input")
    public String inputForm(Model model) {
        model.addAttribute("input", new Input());
        return "input";
    }

    @PostMapping("/input")
    public ResponseEntity<Output> inputSubmit(@Valid @RequestBody @ModelAttribute Input input) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", input.getContent());

        String basicUrl = "https://wordsapiv1.p.rapidapi.com/words";

        HttpURLConnection connection = getURLConnection(basicUrl.concat(
                ParameterStringBuilder.getParametersString(parameters))
                .concat("/definitions"));

        Map<String, String> headers = initHeaders();
        setHeaders(connection, headers);

        String response = getResponse(connection);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Output resultOutput = mapper.readValue(response, Output.class);

        System.out.println("RESULT OUTPUT:  " + resultOutput);

        connection.disconnect();

        return new ResponseEntity<>(resultOutput, HttpStatus.OK);
    }

    private static String getResponse(HttpURLConnection connection) throws IOException {
        System.out.println("STATUS: " + connection.getResponseCode());

        BufferedReader in;
        StringBuilder content = new StringBuilder();

        if (connection.getResponseCode() > 299) {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            System.out.println("ERROR reading stream: " + in.toString());
            throw new IOException();
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
        connection.setConnectTimeout(5000); // 5 sec
        connection.setReadTimeout(5000);

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