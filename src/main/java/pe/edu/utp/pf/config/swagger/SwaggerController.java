package pe.edu.utp.pf.config.swagger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controlador utilitario encargado de simplificar el acceso a la documentación de la API.
 * Proporciona un punto de entrada amigable (friendly URL) para evitar recordar la ruta completa de Swagger UI.
 */
@RestController
public class SwaggerController {

    /**
     * Redirige las peticiones entrantes de la ruta corta hacia la interfaz gráfica de Swagger.
     * Ejemplo de uso local: http://localhost:8090/api/swagger-ui
     * El cual redirigirá automáticamente a: http://localhost:8090/api/swagger-ui/index.html
     * @return Un objeto {@link RedirectView} que despacha la navegación hacia la URL interna de Swagger UI.
     */
    @GetMapping("/swagger-ui")
    public RedirectView index() {
        return new RedirectView("swagger-ui/index.html");
    }
}
