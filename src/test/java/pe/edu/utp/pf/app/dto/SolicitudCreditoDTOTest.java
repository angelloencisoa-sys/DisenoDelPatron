package pe.edu.utp.pf.app.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pe.edu.utp.pf.dto.SolicitudCreditoDTO;

/**
 * Clase de prueba unitaria para SolicitudCreditoDTO
 * Valida getters, setters y constructores de la clase DTO.
 * @author Grupo 07
 * @version 2.0
 */
class SolicitudCreditoDTOTest {

    private SolicitudCreditoDTO dto;

    @BeforeEach
    void setup() {
        dto = new SolicitudCreditoDTO();
    }

    @DisplayName("DTO - Setter y Getter para montoSolicitado")
    @Test
    void dto_SetAndGetMontoSolicitado() {
        Double montoEsperado = 10000.0;
        dto.setMontoSolicitado(montoEsperado);

        assertThat(dto.getMontoSolicitado()).isEqualTo(montoEsperado);
    }

    @DisplayName("DTO - Setter y Getter para plazoMeses")
    @Test
    void dto_SetAndGetPlazoMeses() {
        Integer plazoEsperado = 12;
        dto.setPlazoMeses(plazoEsperado);

        assertThat(dto.getPlazoMeses()).isEqualTo(plazoEsperado);
    }

    @DisplayName("DTO - Setter y Getter para estado")
    @Test
    void dto_SetAndGetEstado() {
        String estadoEsperado = "Pendiente";
        dto.setEstado(estadoEsperado);

        assertThat(dto.getEstado()).isEqualTo(estadoEsperado);
    }

    @DisplayName("DTO - Constructor sin parámetros")
    @Test
    void dto_DefaultConstructor() {
        SolicitudCreditoDTO nuevoDTO = new SolicitudCreditoDTO();

        assertThat(nuevoDTO).isNotNull();
        assertThat(nuevoDTO.getMontoSolicitado()).isNull();
        assertThat(nuevoDTO.getPlazoMeses()).isNull();
        assertThat(nuevoDTO.getEstado()).isNull();
    }

    @DisplayName("DTO - Valores iniciales nulos")
    @Test
    void dto_InitialValuesAreNull() {
        assertThat(dto.getMontoSolicitado()).isNull();
        assertThat(dto.getPlazoMeses()).isNull();
        assertThat(dto.getEstado()).isNull();
    }

    @DisplayName("DTO - Todos los setters y getters")
    @Test
    void dto_AllSettersAndGetters() {
        Double monto = 15000.0;
        Integer plazo = 24;
        String estado = "En Revisión";

        dto.setMontoSolicitado(monto);
        dto.setPlazoMeses(plazo);
        dto.setEstado(estado);

        assertThat(dto.getMontoSolicitado()).isEqualTo(monto);
        assertThat(dto.getPlazoMeses()).isEqualTo(plazo);
        assertThat(dto.getEstado()).isEqualTo(estado);
    }

    @DisplayName("DTO - Múltiples solicitudes con diferentes valores")
    @Test
    void dto_MultipleSolicitudesWithDifferentValues() {
        SolicitudCreditoDTO solicitud1 = new SolicitudCreditoDTO();
        solicitud1.setMontoSolicitado(5000.0);
        solicitud1.setPlazoMeses(6);
        solicitud1.setEstado("Pendiente");

        SolicitudCreditoDTO solicitud2 = new SolicitudCreditoDTO();
        solicitud2.setMontoSolicitado(20000.0);
        solicitud2.setPlazoMeses(36);
        solicitud2.setEstado("Aprobada");

        assertThat(solicitud1.getMontoSolicitado()).isNotEqualTo(solicitud2.getMontoSolicitado());
        assertThat(solicitud1.getPlazoMeses()).isNotEqualTo(solicitud2.getPlazoMeses());
        assertThat(solicitud1.getEstado()).isNotEqualTo(solicitud2.getEstado());
    }

    @DisplayName("DTO - Estados válidos")
    @Test
    void dto_ValidStates() {
        String[] estadosValidos = {"Pendiente", "En Revisión", "Pre-Aprobada", "Aprobada", "Rechazada"};

        for (String estado : estadosValidos) {
            SolicitudCreditoDTO solicitud = new SolicitudCreditoDTO();
            solicitud.setEstado(estado);

            assertThat(solicitud.getEstado()).isEqualTo(estado);
        }
    }

    @DisplayName("DTO - Plazos válidos en meses")
    @Test
    void dto_ValidPlazosMeses() {
        int[] plazos = {6, 12, 18, 24, 30, 36};

        for (int plazo : plazos) {
            SolicitudCreditoDTO solicitud = new SolicitudCreditoDTO();
            solicitud.setPlazoMeses(plazo);

            assertThat(solicitud.getPlazoMeses()).isEqualTo(plazo);
            assertThat(solicitud.getPlazoMeses()).isGreaterThan(0);
        }
    }

    @DisplayName("DTO - Montos positivos")
    @Test
    void dto_PositiveAmounts() {
        dto.setMontoSolicitado(12500.0);

        assertThat(dto.getMontoSolicitado()).isGreaterThan(0.0);
    }

    @DisplayName("DTO - Flujo completo de datos")
    @Test
    void dto_CompleteDataFlow() {
        Double monto = 8000.0;
        Integer plazo = 18;
        String estado = "En Revisión";

        dto.setMontoSolicitado(monto);
        dto.setPlazoMeses(plazo);
        dto.setEstado(estado);

        SolicitudCreditoDTO dtoVerificacion = new SolicitudCreditoDTO();
        dtoVerificacion.setMontoSolicitado(dto.getMontoSolicitado());
        dtoVerificacion.setPlazoMeses(dto.getPlazoMeses());
        dtoVerificacion.setEstado(dto.getEstado());

        assertThat(dtoVerificacion.getMontoSolicitado()).isEqualTo(monto);
        assertThat(dtoVerificacion.getPlazoMeses()).isEqualTo(plazo);
        assertThat(dtoVerificacion.getEstado()).isEqualTo(estado);
    }
}