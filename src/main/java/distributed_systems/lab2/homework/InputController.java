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

@Controller
public class InputController {

    @GetMapping("/input")
    public String inputForm(Model model) {
        model.addAttribute("input", new Input());
        return "input";
    }

    @PostMapping("/input")
    public ResponseEntity<Input> inputSubmit(@Valid @RequestBody @ModelAttribute Input input) {
        return new ResponseEntity<>(input, HttpStatus.OK);
    }

}