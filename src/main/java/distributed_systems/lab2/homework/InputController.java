package distributed_systems.lab2.homework;

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
    public ResponseEntity<Input> inputSubmit(@Valid @RequestBody @ModelAttribute Input input) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", input.getContent());

        String basicUrl = "https://date.nager.at/api/v2/PublicHolidays";
        URL url = new URL(basicUrl.concat(ParameterStringBuilder.getParametersString(parameters)));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // prepare connection
        connection.setRequestMethod("GET");
        connection.setDoOutput(true); // to add params to request
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(5000); // 5 sec
        connection.setReadTimeout(5000);

        // read the response code
        System.out.println("STATUS: " + connection.getResponseCode());

        BufferedReader in;
        StringBuilder content = new StringBuilder();

        if (connection.getResponseCode() > 299) {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            System.out.println("ERROR reading stream: " + in.toString());
            return new ResponseEntity<>(new Input(":("), HttpStatus.NOT_ACCEPTABLE);
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            System.out.println("SUCCESS reading stream: " + content.toString());
        }
        connection.disconnect();

        return new ResponseEntity<>(new Input(content.toString()), HttpStatus.OK);
    }
}