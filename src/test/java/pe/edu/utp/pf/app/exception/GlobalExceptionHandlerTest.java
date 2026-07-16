package pe.edu.utp.pf.app.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.utp.pf.exception.GlobalExceptionHandler;
import pe.edu.utp.pf.exception.ServiceException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test unitario para GlobalExceptionHandler
 * Diseñado para alcanzar el 100% de cobertura de código (branch coverage) y cumplir con reglas SonarQube
 *
 * @author Grupo 07
 * @version 1.3
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @DisplayName("Handler - Debe retornar 404 Not Found cuando el mensaje contiene 'no existe'")
    @Test
    void handleServiceException_WhenMessageContainsNoExiste_ReturnsNotFound() {
        // Arrange
        ServiceException exception = new ServiceException("El cliente solicitado no existe en el sistema", new RuntimeException("Causa original"));

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleServiceException(exception);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // CORRECCIÓN: Uso de containsEntry en lugar de .get().isEqualTo()
        assertThat(response.getBody())
                .isNotNull()
                .containsEntry("error", "Error en el servicio")
                .containsEntry("mensaje", "El cliente solicitado no existe en el sistema");
    }

    @DisplayName("Handler - Debe retornar 400 Bad Request cuando el mensaje NO contiene 'no existe'")
    @Test
    void handleServiceException_WhenMessageDoesNotContainNoExiste_ReturnsBadRequest() {
        // Arrange
        ServiceException exception = new ServiceException("El monto a abonar excede el límite permitido", new RuntimeException("Causa original"));

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleServiceException(exception);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // CORRECCIÓN: Uso de containsEntry en lugar de .get().isEqualTo()
        assertThat(response.getBody())
                .isNotNull()
                .containsEntry("error", "Error en el servicio")
                .containsEntry("mensaje", "El monto a abonar excede el límite permitido");
    }

    @DisplayName("Handler - Debe retornar 400 Bad Request cuando el mensaje es nulo")
    @Test
    void handleServiceException_WhenMessageIsNull_ReturnsBadRequest() {
        // Arrange
        ServiceException exception = new ServiceException(null, new RuntimeException("Causa original"));

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleServiceException(exception);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // CORRECCIÓN: Uso de containsEntry en lugar de .get().isEqualTo() o .isNull()
        assertThat(response.getBody())
                .isNotNull()
                .containsEntry("error", "Error en el servicio")
                .containsEntry("mensaje", null);
    }
}