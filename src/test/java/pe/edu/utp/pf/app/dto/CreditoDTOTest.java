package pe.edu.utp.pf.app.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pe.edu.utp.pf.dto.CreditoDTO;

/**
 * Clase de prueba unitaria para CreditoDTO
 * Valida getters, setters y constructores de la clase DTO.
 * @author Grupo 07
 * @version 2.0
 */
class CreditoDTOTest {

    private CreditoDTO dto;

    @BeforeEach
    void setup() {
        dto = new CreditoDTO();
    }

    @DisplayName("DTO - Setter y Getter para capitalPrestado")
    @Test
    void dto_SetAndGetCapitalPrestado() {
        Double capitalEsperado = 5000.0;
        dto.setCapitalPrestado(capitalEsperado);

        assertThat(dto.getCapitalPrestado()).isEqualTo(capitalEsperado);
    }

    @DisplayName("DTO - Setter y Getter para tasaInteresAnual")
    @Test
    void dto_SetAndGetTasaInteresAnual() {
        Double tasaEsperada = 12.5;
        dto.setTasaInteresAnual(tasaEsperada);

        assertThat(dto.getTasaInteresAnual()).isEqualTo(tasaEsperada);
    }

    @DisplayName("DTO - Setter y Getter para estadoCredito")
    @Test
    void dto_SetAndGetEstadoCredito() {
        String estadoEsperado = "Vigente";
        dto.setEstadoCredito(estadoEsperado);

        assertThat(dto.getEstadoCredito()).isEqualTo(estadoEsperado);
    }

    @DisplayName("DTO - Constructor sin parámetros")
    @Test
    void dto_DefaultConstructor() {
        CreditoDTO nuevoDTO = new CreditoDTO();

        assertThat(nuevoDTO).isNotNull();
        assertThat(nuevoDTO.getCapitalPrestado()).isNull();
        assertThat(nuevoDTO.getTasaInteresAnual()).isNull();
        assertThat(nuevoDTO.getEstadoCredito()).isNull();
    }

    @DisplayName("DTO - Valores iniciales nulos")
    @Test
    void dto_InitialValuesAreNull() {
        assertThat(dto.getCapitalPrestado()).isNull();
        assertThat(dto.getTasaInteresAnual()).isNull();
        assertThat(dto.getEstadoCredito()).isNull();
    }

    @DisplayName("DTO - Todos los setters y getters")
    @Test
    void dto_AllSettersAndGetters() {
        Double capital = 10000.0;
        Double tasa = 14.5;
        String estado = "Al Día";

        dto.setCapitalPrestado(capital);
        dto.setTasaInteresAnual(tasa);
        dto.setEstadoCredito(estado);

        assertThat(dto.getCapitalPrestado()).isEqualTo(capital);
        assertThat(dto.getTasaInteresAnual()).isEqualTo(tasa);
        assertThat(dto.getEstadoCredito()).isEqualTo(estado);
    }

    @DisplayName("DTO - Múltiples créditos con diferentes valores")
    @Test
    void dto_MultipleCreditosWithDifferentValues() {
        CreditoDTO credito1 = new CreditoDTO();
        credito1.setCapitalPrestado(3000.0);
        credito1.setTasaInteresAnual(11.0);
        credito1.setEstadoCredito("Vigente");

        CreditoDTO credito2 = new CreditoDTO();
        credito2.setCapitalPrestado(8000.0);
        credito2.setTasaInteresAnual(15.0);
        credito2.setEstadoCredito("Refinanciado");

        assertThat(credito1.getCapitalPrestado()).isNotEqualTo(credito2.getCapitalPrestado());
        assertThat(credito1.getTasaInteresAnual()).isNotEqualTo(credito2.getTasaInteresAnual());
        assertThat(credito1.getEstadoCredito()).isNotEqualTo(credito2.getEstadoCredito());
    }

    @DisplayName("DTO - Flujo completo de datos")
    @Test
    void dto_CompleteDataFlow() {
        Double capital = 7500.0;
        Double tasa = 13.5;
        String estado = "Vigente";

        dto.setCapitalPrestado(capital);
        dto.setTasaInteresAnual(tasa);
        dto.setEstadoCredito(estado);

        CreditoDTO dtoVerificacion = new CreditoDTO();
        dtoVerificacion.setCapitalPrestado(dto.getCapitalPrestado());
        dtoVerificacion.setTasaInteresAnual(dto.getTasaInteresAnual());
        dtoVerificacion.setEstadoCredito(dto.getEstadoCredito());

        assertThat(dtoVerificacion.getCapitalPrestado()).isEqualTo(capital);
        assertThat(dtoVerificacion.getTasaInteresAnual()).isEqualTo(tasa);
        assertThat(dtoVerificacion.getEstadoCredito()).isEqualTo(estado);
    }

    @DisplayName("DTO - Validar montos positivos")
    @Test
    void dto_PositiveAmounts() {
        dto.setCapitalPrestado(5000.0);
        dto.setTasaInteresAnual(12.5);

        assertThat(dto.getCapitalPrestado()).isGreaterThan(0.0);
        assertThat(dto.getTasaInteresAnual()).isGreaterThan(0.0);
    }
}