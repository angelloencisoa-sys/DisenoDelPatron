package pe.edu.utp.pf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Test de integración para verificar que el contexto de Spring Boot se inicia correctamente.
 * Diseñado para alcanzar el 100% de cobertura en la clase principal PfGrupo07Application.
 *
 * @author Grupo 07
 * @version 1.0
 */
@SpringBootTest
class PfGrupo07ApplicationTests {

    @DisplayName("Context - El contexto de Spring debe cargar correctamente")
    @Test
    void contextLoads() {
        // Verifica que la aplicación se inicialice sin lanzar ninguna excepción
        assertThatCode(() -> PfGrupo07Application.main(new String[]{}))
                .doesNotThrowAnyException();
    }
}