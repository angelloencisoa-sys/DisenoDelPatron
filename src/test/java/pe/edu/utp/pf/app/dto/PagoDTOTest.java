package pe.edu.utp.pf.app.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pe.edu.utp.pf.dto.PagoDTO;

/**
 * Clase de prueba unitaria para PagoDTO
 * Valida getters, setters y constructores de la clase DTO.
 * @author Grupo 07
 * @version 2.0
 */
class PagoDTOTest {

    private PagoDTO dto;

    @BeforeEach
    void setup() {
        dto = new PagoDTO();
    }

    @DisplayName("DTO - Setter y Getter para montoAbonado")
    @Test
    void dto_SetAndGetMontoAbonado() {
        Double montoEsperado = 500.0;
        dto.setMontoAbonado(montoEsperado);

        assertThat(dto.getMontoAbonado()).isEqualTo(montoEsperado);
    }

    @DisplayName("DTO - Constructor sin parámetros")
    @Test
    void dto_DefaultConstructor() {
        PagoDTO nuevoDTO = new PagoDTO();

        assertThat(nuevoDTO).isNotNull();
        assertThat(nuevoDTO.getMontoAbonado()).isNull();
    }

    @DisplayName("DTO - Valores iniciales nulos")
    @Test
    void dto_InitialValuesAreNull() {
        assertThat(dto.getMontoAbonado()).isNull();
    }

    @DisplayName("DTO - Múltiples setters y getters")
    @Test
    void dto_MultipleSettersAndGetters() {
        Double monto1 = 300.0;
        Double monto2 = 750.50;

        PagoDTO pago1 = new PagoDTO();
        pago1.setMontoAbonado(monto1);

        PagoDTO pago2 = new PagoDTO();
        pago2.setMontoAbonado(monto2);

        assertThat(pago1.getMontoAbonado()).isEqualTo(monto1);
        assertThat(pago2.getMontoAbonado()).isEqualTo(monto2);
    }

    @DisplayName("DTO - Monto positivo")
    @Test
    void dto_PositiveAmount() {
        Double montoPositivo = 1000.0;
        dto.setMontoAbonado(montoPositivo);

        assertThat(dto.getMontoAbonado()).isGreaterThan(0.0);
    }

    @DisplayName("DTO - Monto con decimales")
    @Test
    void dto_AmountWithDecimals() {
        Double montoConDecimales = 250.75;
        dto.setMontoAbonado(montoConDecimales);

        assertThat(dto.getMontoAbonado()).isEqualTo(250.75);
    }

    @DisplayName("DTO - Cambio de monto abonado")
    @Test
    void dto_ChangingAmount() {
        Double montoOriginal = 100.0;
        Double montoNuevo = 500.0;

        dto.setMontoAbonado(montoOriginal);
        assertThat(dto.getMontoAbonado()).isEqualTo(montoOriginal);

        dto.setMontoAbonado(montoNuevo);
        assertThat(dto.getMontoAbonado()).isEqualTo(montoNuevo);
    }

    @DisplayName("DTO - Flujo completo de datos")
    @Test
    void dto_CompleteDataFlow() {
        Double monto = 650.50;

        dto.setMontoAbonado(monto);

        PagoDTO dtoVerificacion = new PagoDTO();
        dtoVerificacion.setMontoAbonado(dto.getMontoAbonado());

        assertThat(dtoVerificacion.getMontoAbonado()).isEqualTo(monto);
    }

    @DisplayName("DTO - Múltiples pagos")
    @Test
    void dto_MultiplePagos() {
        PagoDTO pago1 = new PagoDTO();
        pago1.setMontoAbonado(200.0);

        PagoDTO pago2 = new PagoDTO();
        pago2.setMontoAbonado(150.0);

        PagoDTO pago3 = new PagoDTO();
        pago3.setMontoAbonado(350.0);

        Double totalEsperado = pago1.getMontoAbonado() + pago2.getMontoAbonado() + pago3.getMontoAbonado();

        assertThat(totalEsperado).isEqualTo(700.0);
    }
}