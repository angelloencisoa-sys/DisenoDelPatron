package pe.edu.utp.pf.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pe.edu.utp.pf.controller.ClienteController;
import pe.edu.utp.pf.dto.ClienteDTO;
import pe.edu.utp.pf.model.Cliente;
import pe.edu.utp.pf.model.HistorialCrediticio;
import pe.edu.utp.pf.model.PerfilRiesgo;
import pe.edu.utp.pf.service.ClienteService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Controller para ClienteController
 * Prueba todos los endpoints REST usando MockMvc bajo Spring Boot 4.x
 *
 * @author Grupo 07
 * @version 3.1
 */
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    // Instanciado directamente para resolver el error UnsatisfiedDependencyException
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Cliente cliente;
    private Cliente clienteGuardado;
    private ClienteDTO clienteDTO;
    private PerfilRiesgo perfilRiesgo;
    private HistorialCrediticio historialCrediticio;

    @BeforeEach
    void setup() {
        // Cliente sin guardar
        cliente = new Cliente();
        cliente.setNombresCompletos("Juan Pérez García");
        cliente.setDireccion("Av. Arequipa 2600, Lince");
        cliente.setTelefono("987654321");
        cliente.setEmail("juan.perez@utp.edu.pe");

        // Perfil de riesgo
        perfilRiesgo = new PerfilRiesgo();
        perfilRiesgo.setIdPerfil(1);
        perfilRiesgo.setScoreCrediticio(750);
        perfilRiesgo.setNivelRiesgo("Bajo");
        perfilRiesgo.setFechaUltimaEvaluacion(LocalDate.now(ZoneId.of("America/Lima")));

        // Historial crediticio
        historialCrediticio = new HistorialCrediticio();
        historialCrediticio.setIdHistorial(1);
        historialCrediticio.setNumeroCreditosActivos(2);
        historialCrediticio.setTieneDeudasCastigadas(false);

        cliente.setPerfilRiesgo(perfilRiesgo);
        cliente.setHistorialCrediticio(historialCrediticio);

        // Cliente guardado con ID
        clienteGuardado = new Cliente();
        clienteGuardado.setIdCliente(1);
        clienteGuardado.setNombresCompletos("Juan Pérez García");
        clienteGuardado.setDireccion("Av. Arequipa 2600, Lince");
        clienteGuardado.setTelefono("987654321");
        clienteGuardado.setEmail("juan.perez@utp.edu.pe");
        clienteGuardado.setPerfilRiesgo(perfilRiesgo);
        clienteGuardado.setHistorialCrediticio(historialCrediticio);

        // DTO
        clienteDTO = new ClienteDTO();
        clienteDTO.setNombresCompletos("Juan Pérez García");
        clienteDTO.setDireccion("Av. Arequipa 2600, Lince");
        clienteDTO.setTelefono("987654321");
        clienteDTO.setEmail("juan.perez@utp.edu.pe");
    }

    @DisplayName("GET /api/clientes - Listar todos los clientes")
    @Test
    void controller_GetAll_ReturnsAllClientes() throws Exception {
        // Arrange
        List<Cliente> clienteList = List.of(clienteGuardado);
        when(clienteService.getAll()).thenReturn(clienteList);

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Verify
        verify(clienteService, times(1)).getAll();
        assertThat(result.getResponse().getContentAsString()).isNotBlank();
    }

    @DisplayName("GET /api/clientes/{id} - Obtener cliente por ID")
    @Test
    void controller_GetById_ReturnsCliente() throws Exception {
        // Arrange
        when(clienteService.getById(1)).thenReturn(Optional.of(clienteGuardado));

        // Act & Assert
        mockMvc.perform(get("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.nombresCompletos").value("Juan Pérez García"));

        // Verify
        verify(clienteService, times(1)).getById(1);
    }

    @DisplayName("GET /api/clientes/{id} - Cliente no encontrado")
    @Test
    void controller_GetById_ClienteNotFound() throws Exception {
        // Arrange
        when(clienteService.getById(99)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify
        verify(clienteService, times(1)).getById(99);
    }

    @DisplayName("POST /api/clientes - Registrar nuevo cliente")
    @Test
    void controller_Post_RegistrarClienteIndividual() throws Exception {
        // Arrange
        when(clienteService.create(any(Cliente.class))).thenReturn(clienteGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated());

        // Verify
        verify(clienteService, times(1)).create(any(Cliente.class));
    }

    @DisplayName("POST /api/clientes?cantidad=5 - Generar clientes aleatorios")
    @Test
    void controller_Post_GenerarClientesAleatorios() throws Exception {
        // Arrange
        when(clienteService.create(any(Cliente.class))).thenReturn(clienteGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/clientes?cantidad=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Se generaron con éxito 5 clientes")));

        // Verify - Debe llamar create 5 veces
        verify(clienteService, times(5)).create(any(Cliente.class));
    }

    @DisplayName("POST /api/clientes - Sin datos retorna error")
    @Test
    void controller_Post_SinDatosRetornaError() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Debe enviar un objeto JSON")));
    }

    @DisplayName("PUT /api/clientes/{id} - Actualizar cliente")
    @Test
    void controller_Put_ActualizarCliente() throws Exception {
        // Arrange
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setIdCliente(1);
        clienteActualizado.setNombresCompletos("Juan Pérez García ACTUALIZADO");
        clienteActualizado.setDireccion("Av. Arequipa 2600, Lince");
        clienteActualizado.setTelefono("987654321");
        clienteActualizado.setEmail("juan.perez@utp.edu.pe");

        when(clienteService.getById(1)).thenReturn(Optional.of(clienteGuardado));
        when(clienteService.update(any(Cliente.class), any(Cliente.class))).thenReturn(clienteActualizado);

        // Act & Assert
        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1));

        // Verify
        verify(clienteService, times(1)).getById(1);
        verify(clienteService, times(1)).update(any(Cliente.class), any(Cliente.class));
    }

    @DisplayName("PUT /api/clientes/{id} - Cliente no encontrado")
    @Test
    void controller_Put_ClienteNotFound() throws Exception {
        // Arrange
        when(clienteService.getById(99)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isNotFound());

        // Verify
        verify(clienteService, times(1)).getById(99);
        verify(clienteService, never()).update(any(), any());
    }

    @DisplayName("DELETE /api/clientes/{id} - Eliminar cliente")
    @Test
    void controller_Delete_EliminarCliente() throws Exception {
        // Arrange
        when(clienteService.getById(1)).thenReturn(Optional.of(clienteGuardado));
        doNothing().when(clienteService).deleteById(1);

        // Act & Assert
        mockMvc.perform(delete("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify
        verify(clienteService, times(1)).getById(1);
        verify(clienteService, times(1)).deleteById(1);
    }

    @DisplayName("DELETE /api/clientes/{id} - Cliente no encontrado")
    @Test
    void controller_Delete_ClienteNotFound() throws Exception {
        // Arrange
        when(clienteService.getById(99)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify
        verify(clienteService, times(1)).getById(99);
        verify(clienteService, never()).deleteById(anyInt());
    }
}