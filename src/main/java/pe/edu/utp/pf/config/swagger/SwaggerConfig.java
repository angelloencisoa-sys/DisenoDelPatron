package pe.edu.utp.pf.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para centralizar la documentación de la API con OpenAPI 3 y Swagger UI.
 * Define los metadatos principales que se mostrarán en la interfaz web de Swagger.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Define y personaliza el bean global de OpenAPI para el sistema.
     * Configura el título, la versión y el propósito del backend basado en patrones GRASP.
     *
     * @return Objeto {@link OpenAPI} con la información del Sistema Financiero precargada.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Sistema Financiero - Grupo 07")
                        .version("2.0")
                        .description("Documentación de los endpoints del sistema de créditos aplicando patrones GRASP."));
    }
}
