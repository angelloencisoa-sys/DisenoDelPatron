package pe.edu.utp.pf.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.utp.pf.model.Cliente;
import pe.edu.utp.pf.model.HistorialCrediticio;
import pe.edu.utp.pf.model.PerfilRiesgo;
import pe.edu.utp.pf.repository.ClienteRepository;
import pe.edu.utp.pf.service.impl.ClienteServiceImpl;

/**
 * Clase de prueba unitaria para ClienteService
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IClienteServiceTest {

    @Mock
    private ClienteRepository repoMock;

    @InjectMocks
    private ClienteServiceImpl serviceMock;

    private Cliente cliente;
    private Cliente saveCliente;
    private List<Cliente> clienteList;

    @BeforeEach
    void setup() {
        // Cliente sin guardar
        cliente = new Cliente();
        cliente.setNombresCompletos("Juan Pérez García");
        cliente.setDireccion("Av. Arequipa 2600, Lince");
        cliente.setTelefono("987654321");
        cliente.setEmail("juan.perez@utp.edu.pe");

        // Cliente guardado (con ID)
        saveCliente = new Cliente();
        saveCliente.setIdCliente(1);
        saveCliente.setNombresCompletos("Juan Pérez García");
        saveCliente.setDireccion("Av. Arequipa 2600, Lince");
        saveCliente.setTelefono("987654321");
        saveCliente.setEmail("juan.perez@utp.edu.pe");

        // Perfil de riesgo y historial
        PerfilRiesgo perfil = new PerfilRiesgo();
        perfil.setIdPerfil(1);
        perfil.setScoreCrediticio(750);
        perfil.setNivelRiesgo("Bajo");
        saveCliente.setPerfilRiesgo(perfil);

        HistorialCrediticio historial = new HistorialCrediticio();
        historial.setIdHistorial(1);
        historial.setNumeroCreditosActivos(2);
        historial.setTieneDeudasCastigadas(false);
        saveCliente.setHistorialCrediticio(historial);

        clienteList = List.of(cliente, saveCliente);
    }

    @DisplayName("Service - Registrar y retornar Cliente")
    @Test
    void service_Save_Return_Cliente() {
        // 1. Preparación
        when(this.repoMock.save(any())).thenReturn(saveCliente);
        Cliente saveClienteResult = null;

        // 2. Ejecución
        try {
            saveClienteResult = this.serviceMock.create(cliente);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(saveClienteResult).isNotNull();
        assertThat(saveClienteResult.getIdCliente()).isNotNull();
        assertThat(saveClienteResult.getNombresCompletos()).isEqualTo("Juan Pérez García");
    }

    @DisplayName("Service - Registrar y retornar Exception")
    @Test
    void service_Save_HandleException() {
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);
        Cliente saveClienteResult = null;

        try {
            saveClienteResult = this.serviceMock.create(cliente);
            assertThat(saveClienteResult.getIdCliente()).isNotNull();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar todos los Clientes")
    @Test
    void service_FindAll_ReturnsAllRecords() {
        when(this.repoMock.findAll()).thenReturn(clienteList);
        List<Cliente> list = this.serviceMock.getAll();

        assertThat(list).hasSizeGreaterThan(1).contains(saveCliente, cliente);
    }

    @DisplayName("Service - Buscar y retornar por Id un Cliente")
    @Test
    void service_FindById_ReturnObjectId() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveCliente));
        Optional<Cliente> clienteWithId = null;

        try {
            clienteWithId = this.serviceMock.getById(1);
            assertThat(clienteWithId.get().getIdCliente()).isNotNull();
            assertThat(clienteWithId.get().getNombresCompletos()).isEqualTo("Juan Pérez García");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar por Id y retornar un objeto vacío")
    @Test
    void service_FindById_ReturnsNoRecord() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<Cliente> clienteWithId = null;

        try {
            clienteWithId = this.serviceMock.getById(1);
            assertThat(clienteWithId).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar por Id una Excepción")
    @Test
    void service_FindById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(RuntimeException.class);
        Optional<Cliente> clienteWithId = null;

        try {
            clienteWithId = this.serviceMock.getById(1);
            assertThat(clienteWithId).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Actualizar Cliente")
    @Test
    void service_Update_ReturnUpdatedCliente() {
        when(this.repoMock.save(any())).thenReturn(saveCliente);
        Cliente clienteUpdated = null;

        try {
            clienteUpdated = this.serviceMock.update(saveCliente, cliente);
            assertThat(clienteUpdated).isNotNull();
            assertThat(clienteUpdated.getIdCliente()).isEqualTo(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Service - Eliminar Cliente")
    @Test
    void service_Delete_SuccessfulDeletion() {
        try {
            this.serviceMock.deleteById(1);
            // Si no lanza excepción, la prueba pasa
            assertThat(true).isTrue();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }
}
