package pe.edu.utp.pf.app.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import pe.edu.utp.pf.model.Cliente;
import pe.edu.utp.pf.model.HistorialCrediticio;
import pe.edu.utp.pf.model.PerfilRiesgo;
import pe.edu.utp.pf.repository.ClienteRepository;
import pe.edu.utp.pf.service.impl.ClienteServiceImpl;


/**
 * Test unitario para ClienteServiceImpl
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IClienteServiceImplTest {

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

        // Perfil de riesgo
        PerfilRiesgo perfil = new PerfilRiesgo();
        perfil.setIdPerfil(1);
        perfil.setScoreCrediticio(750);
        perfil.setNivelRiesgo("Bajo");
        perfil.setFechaUltimaEvaluacion(LocalDate.now(ZoneId.of("America/Lima")));
        saveCliente.setPerfilRiesgo(perfil);

        // Historial crediticio
        HistorialCrediticio historial = new HistorialCrediticio();
        historial.setIdHistorial(1);
        historial.setNumeroCreditosActivos(2);
        historial.setTieneDeudasCastigadas(false);
        saveCliente.setHistorialCrediticio(historial);

        clienteList = List.of(cliente, saveCliente);
    }

    @DisplayName("ClienteServiceImpl - Crear Cliente")
    @Test
    void testCreate_SaveCliente() {
        when(this.repoMock.save(any())).thenReturn(saveCliente);
        Cliente result = null;

        try {
            result = this.serviceMock.create(cliente);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isNotNull();
        assertThat(result.getIdCliente()).isEqualTo(1);
        assertThat(result.getNombresCompletos()).isEqualTo("Juan Pérez García");
    }

    @DisplayName("ClienteServiceImpl - Crear Cliente maneja excepción")
    @Test
    void testCreate_HandleException() {
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);

        try {
            this.serviceMock.create(cliente);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("ClienteServiceImpl - Obtener todos los Clientes")
    @Test
    void testGetAll_ReturnAllClientes() {
        when(this.repoMock.findAll()).thenReturn(clienteList);
        List<Cliente> result = this.serviceMock.getAll();

        assertThat(result).isNotNull().hasSize(2).contains(saveCliente, cliente);
    }

    @DisplayName("ClienteServiceImpl - Obtener Cliente por ID")
    @Test
    void testGetById_ReturnCliente() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveCliente));
        Optional<Cliente> result = this.serviceMock.getById(1);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getIdCliente()).isEqualTo(1);
        assertThat(result.get().getNombresCompletos()).isEqualTo("Juan Pérez García");
    }

    @DisplayName("ClienteServiceImpl - Obtener Cliente por ID no encontrado")
    @Test
    void testGetById_NotFound() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<Cliente> result = this.serviceMock.getById(999);

        assertThat(result).isEmpty();
    }

    @DisplayName("ClienteServiceImpl - Obtener Cliente por ID maneja excepción")
    @Test
    void testGetById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(new DataRetrievalFailureException("Error simulado de base de datos"));

        Optional<Cliente> result = this.serviceMock.getById(1);

        assertThat(result).isEmpty();
    }

    @DisplayName("ClienteServiceImpl - Actualizar Cliente")
    @Test
    void testUpdate_ReturnUpdatedCliente() {
        Cliente clienteModificado = new Cliente();
        clienteModificado.setNombresCompletos("Carlos López García");
        clienteModificado.setDireccion("Av. Principal 500");
        clienteModificado.setTelefono("912345678");
        clienteModificado.setEmail("carlos@utp.edu.pe");

        when(this.repoMock.save(any())).thenReturn(saveCliente);
        Cliente result = this.serviceMock.update(saveCliente, clienteModificado);

        assertThat(result).isNotNull();
        assertThat(result.getIdCliente()).isEqualTo(1);
    }

    @DisplayName("ClienteServiceImpl - Eliminar Cliente")
    @Test
    void testDeleteById_Success() {
        try {
            this.serviceMock.deleteById(1);
            assertTrue(true);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("ClienteServiceImpl - Eliminar Cliente maneja excepción")
    @Test
    void testDeleteById_HandleException() {
        doThrow(RuntimeException.class).when(this.repoMock).deleteById(anyInt());

        try {
            this.serviceMock.deleteById(1);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }
}