package pe.edu.utp.pf.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Sistema Financiero - Grupo 07")
                        .version("2.0")
                        .description("Documentación de los endpoints del sistema de créditos aplicando patrones GRASP."));
    }
}
