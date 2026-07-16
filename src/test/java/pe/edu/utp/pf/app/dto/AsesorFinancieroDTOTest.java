package pe.edu.utp.pf.app.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pe.edu.utp.pf.dto.AsesorFinancieroDTO;

/**
 * Clase de prueba unitaria para AsesorFinancieroDTO
 * Valida getters, setters y constructores de la clase DTO.
 * @author Grupo 07
 * @version 2.0
 */
class AsesorFinancieroDTOTest {

    private AsesorFinancieroDTO dto;

    @BeforeEach
    void setup() {
        dto = new AsesorFinancieroDTO();
    }

    @DisplayName("DTO - Setter y Getter para nombreAsesor")
    @Test
    void dto_SetAndGetNombreAsesor() {
        String nombreEsperado = "Carlos Mendez";
        dto.setNombreAsesor(nombreEsperado);

        assertThat(dto.getNombreAsesor()).isEqualTo(nombreEsperado);
    }

    @DisplayName("DTO - Setter y Getter para codigoAgencia")
    @Test
    void dto_SetAndGetCodigoAgencia() {
        String codigoEsperado = "AG-001";
        dto.setCodigoAgencia(codigoEsperado);

        assertThat(dto.getCodigoAgencia()).isEqualTo(codigoEsperado);
    }

    @DisplayName("DTO - Constructor sin parámetros")
    @Test
    void dto_DefaultConstructor() {
        AsesorFinancieroDTO nuevoDTO = new AsesorFinancieroDTO();

        assertThat(nuevoDTO).isNotNull();
        assertThat(nuevoDTO.getNombreAsesor()).isNull();
        assertThat(nuevoDTO.getCodigoAgencia()).isNull();
    }

    @DisplayName("DTO - Valores iniciales nulos")
    @Test
    void dto_InitialValuesAreNull() {
        assertThat(dto.getNombreAsesor()).isNull();
        assertThat(dto.getCodigoAgencia()).isNull();
    }

    @DisplayName("DTO - Múltiples setters y getters")
    @Test
    void dto_MultipleSettersAndGetters() {
        dto.setNombreAsesor("Juan Pérez");
        dto.setCodigoAgencia("AG-002");

        assertThat(dto.getNombreAsesor()).isEqualTo("Juan Pérez");
        assertThat(dto.getCodigoAgencia()).isEqualTo("AG-002");
    }

    @DisplayName("DTO - Flujo completo de datos")
    @Test
    void dto_CompleteDataFlow() {
        String nombre = "Patricia Gómez";
        String codigo = "AG-005";

        dto.setNombreAsesor(nombre);
        dto.setCodigoAgencia(codigo);

        AsesorFinancieroDTO dtoVerificacion = new AsesorFinancieroDTO();
        dtoVerificacion.setNombreAsesor(dto.getNombreAsesor());
        dtoVerificacion.setCodigoAgencia(dto.getCodigoAgencia());

        assertThat(dtoVerificacion.getNombreAsesor()).isEqualTo(nombre);
        assertThat(dtoVerificacion.getCodigoAgencia()).isEqualTo(codigo);
    }
}