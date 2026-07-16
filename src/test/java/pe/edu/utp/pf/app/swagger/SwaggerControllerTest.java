package pe.edu.utp.pf.app.swagger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.pf.config.swagger.SwaggerController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test unitario para SwaggerController.
 * Verifica la correcta redirección hacia la interfaz de Swagger UI bajo Spring Boot 4.x.
 *
 * @author Grupo 07
 * @version 1.0
 */
@WebMvcTest(SwaggerController.class)
class SwaggerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Swagger - Debe redirigir la ruta amigable a la URL de Swagger UI")
    @Test
    void index_ShouldRedirectToSwaggerUiIndexHtml() throws Exception {
        mockMvc.perform(get("/swagger-ui")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection()) // Valida código de estado 3xx
                .andExpect(redirectedUrl("swagger-ui/index.html")); // Valida la URL de destino de la redirección
    }
}