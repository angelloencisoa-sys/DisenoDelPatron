package pe.edu.utp.pf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error en el servicio");
        response.put("mensaje", ex.getMessage());

        // Si el mensaje contiene "no existe", devolvemos 404 (Not Found)
        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("no existe")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // De lo contrario, un 400 Bad Request genérico
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}