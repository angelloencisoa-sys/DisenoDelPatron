package pe.edu.utp.pf.config.swagger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * http://localhost:8090/api/swagger-ui/index.html
 */

@RestController
public class SwaggerController {

    @GetMapping("/swagger-ui")
    public RedirectView index() {
        return new RedirectView ("swagger-ui/index.html");
    }
}