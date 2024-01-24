package quantum;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "index"; // Este nombre debe coincidir con el nombre de tu archivo HTML principal en el directorio frontend.
    }


}