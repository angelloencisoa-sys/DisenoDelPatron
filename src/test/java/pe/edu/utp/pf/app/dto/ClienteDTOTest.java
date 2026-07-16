package pe.edu.utp.pf.app.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pe.edu.utp.pf.dto.ClienteDTO;

/**
 * Clase de prueba unitaria para ClienteDTO
 * Valida getters, setters y constructores de la clase DTO.
 * @author Grupo 07
 * @version 2.0
 */
class ClienteDTOTest {

    private ClienteDTO dto;

    @BeforeEach
    void setup() {
        dto = new ClienteDTO();
    }

    @DisplayName("DTO - Setter y Getter para nombresCompletos")
    @Test
    void dto_SetAndGetNombresCompletos() {
        String nombresEsperados = "Juan Pérez García";
        dto.setNombresCompletos(nombresEsperados);

        assertThat(dto.getNombresCompletos()).isEqualTo(nombresEsperados);
    }

    @DisplayName("DTO - Setter y Getter para direccion")
    @Test
    void dto_SetAndGetDireccion() {
        String direccionEsperada = "Av. Arequipa 2600, Lince";
        dto.setDireccion(direccionEsperada);

        assertThat(dto.getDireccion()).isEqualTo(direccionEsperada);
    }

    @DisplayName("DTO - Setter y Getter para telefono")
    @Test
    void dto_SetAndGetTelefono() {
        String telefonoEsperado = "987654321";
        dto.setTelefono(telefonoEsperado);

        assertThat(dto.getTelefono()).isEqualTo(telefonoEsperado);
    }

    @DisplayName("DTO - Setter y Getter para email")
    @Test
    void dto_SetAndGetEmail() {
        String emailEsperado = "juan.perez@utp.edu.pe";
        dto.setEmail(emailEsperado);

        assertThat(dto.getEmail()).isEqualTo(emailEsperado);
    }

    @DisplayName("DTO - Constructor sin parámetros")
    @Test
    void dto_DefaultConstructor() {
        ClienteDTO nuevoDTO = new ClienteDTO();

        assertThat(nuevoDTO).isNotNull();
        assertThat(nuevoDTO.getNombresCompletos()).isNull();
        assertThat(nuevoDTO.getDireccion()).isNull();
        assertThat(nuevoDTO.getTelefono()).isNull();
        assertThat(nuevoDTO.getEmail()).isNull();
    }

    @DisplayName("DTO - Valores iniciales nulos")
    @Test
    void dto_InitialValuesAreNull() {
        assertThat(dto.getNombresCompletos()).isNull();
        assertThat(dto.getDireccion()).isNull();
        assertThat(dto.getTelefono()).isNull();
        assertThat(dto.getEmail()).isNull();
    }

    @DisplayName("DTO - Todos los setters y getters")
    @Test
    void dto_AllSettersAndGetters() {
        String nombres = "Rosa María Flores";
        String direccion = "Jr. Trujillo 450, Rímac";
        String telefono = "912345678";
        String email = "rosa.flores@utp.edu.pe";

        dto.setNombresCompletos(nombres);
        dto.setDireccion(direccion);
        dto.setTelefono(telefono);
        dto.setEmail(email);

        assertThat(dto.getNombresCompletos()).isEqualTo(nombres);
        assertThat(dto.getDireccion()).isEqualTo(direccion);
        assertThat(dto.getTelefono()).isEqualTo(telefono);
        assertThat(dto.getEmail()).isEqualTo(email);
    }

    @DisplayName("DTO - Flujo completo de datos")
    @Test
    void dto_CompleteDataFlow() {
        ClienteDTO clienteOrigen = new ClienteDTO();
        clienteOrigen.setNombresCompletos("Luis Sánchez");
        clienteOrigen.setDireccion("Av. La Marina 3200, San Miguel");
        clienteOrigen.setTelefono("923456789");
        clienteOrigen.setEmail("luis.sanchez@utp.edu.pe");

        ClienteDTO clienteDestino = new ClienteDTO();
        clienteDestino.setNombresCompletos(clienteOrigen.getNombresCompletos());
        clienteDestino.setDireccion(clienteOrigen.getDireccion());
        clienteDestino.setTelefono(clienteOrigen.getTelefono());
        clienteDestino.setEmail(clienteOrigen.getEmail());

        assertThat(clienteDestino.getNombresCompletos()).isEqualTo(clienteOrigen.getNombresCompletos());
        assertThat(clienteDestino.getDireccion()).isEqualTo(clienteOrigen.getDireccion());
        assertThat(clienteDestino.getTelefono()).isEqualTo(clienteOrigen.getTelefono());
        assertThat(clienteDestino.getEmail()).isEqualTo(clienteOrigen.getEmail());
    }
}