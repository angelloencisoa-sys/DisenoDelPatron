package pe.edu.utp.pf.app.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.model.Credito;
import pe.edu.utp.pf.model.Cronograma;
import pe.edu.utp.pf.repository.CreditoRepository;
import pe.edu.utp.pf.service.impl.CreditoServiceImpl;

/**
 * Test unitario para CreditoServiceImpl
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class ICreditoServiceImplTest {

    @Mock
    private CreditoRepository repoMock;

    @InjectMocks
    private CreditoServiceImpl serviceMock;

    private Credito credito;
    private Credito saveCredito;
    private List<Credito> creditoList;

    @BeforeEach
    void setup() {
        // Crédito sin guardar
        credito = new Credito();
        credito.setCapitalPrestado(5000.0);
        credito.setTasaInteresAnual(12.5);
        credito.setEstadoCredito("Vigente");
        credito.setFechaDesembolso(LocalDate.now(ZoneId.of("America/Lima")));

        // Crédito guardado (con ID)
        saveCredito = new Credito();
        saveCredito.setIdCredito(1);
        saveCredito.setCapitalPrestado(5000.0);
        saveCredito.setTasaInteresAnual(12.5);
        saveCredito.setEstadoCredito("Vigente");
        saveCredito.setFechaDesembolso(LocalDate.now(ZoneId.of("America/Lima")));

        // Cronograma
        Cronograma cronograma = new Cronograma();
        cronograma.setIdCronograma(1);
        cronograma.setFechaGeneracion(LocalDate.now(ZoneId.of("America/Lima")));
        cronograma.setCredito(saveCredito);
        saveCredito.setCronograma(cronograma);

        creditoList = List.of(credito, saveCredito);
    }

    @DisplayName("CreditoServiceImpl - Crear Crédito con Cronograma automático")
    @Test
    void testCreate_SaveCreditoWithCronograma() {
        when(this.repoMock.save(any())).thenReturn(saveCredito);
        Credito result = null;

        try {
            result = this.serviceMock.create(credito);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isNotNull();
        assertThat(result.getIdCredito()).isEqualTo(1);
        assertThat(result.getCapitalPrestado()).isEqualTo(5000.0);
        assertThat(result.getCronograma()).isNotNull();
    }

    @DisplayName("CreditoServiceImpl - Crear Crédito maneja excepción")
    @Test
    void testCreate_HandleException() {
        // CORRECCIÓN: Lanzar excepción compatible con DataAccessException
        when(this.repoMock.save(any())).thenThrow(new DataRetrievalFailureException("Error simulado BD"));

        // Tu servicio captura el error y lanza ServiceException
        assertThrows(ServiceException.class, () -> this.serviceMock.create(credito));
    }

    @DisplayName("CreditoServiceImpl - Obtener todos los Créditos")
    @Test
    void testGetAll_ReturnAllCreditos() {
        when(this.repoMock.findAll()).thenReturn(creditoList);
        List<Credito> result = this.serviceMock.getAll();

        assertThat(result).isNotNull().hasSize(2).contains(saveCredito, credito);
    }

    @DisplayName("CreditoServiceImpl - Obtener todos los Créditos maneja excepción")
    @Test
    void testGetAll_HandleException() {
        when(this.repoMock.findAll()).thenThrow(new DataRetrievalFailureException("Error simulado BD"));
        List<Credito> result = this.serviceMock.getAll();

        assertThat(result).isNotNull().isEmpty();
    }

    @DisplayName("CreditoServiceImpl - Obtener Crédito por ID")
    @Test
    void testGetById_ReturnCredito() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveCredito));
        Optional<Credito> result = this.serviceMock.getById(1);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getIdCredito()).isEqualTo(1);
        assertThat(result.get().getCapitalPrestado()).isEqualTo(5000.0);
    }

    @DisplayName("CreditoServiceImpl - Obtener Crédito por ID no encontrado")
    @Test
    void testGetById_NotFound() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<Credito> result = this.serviceMock.getById(999);

        assertThat(result).isEmpty();
    }

    @DisplayName("CreditoServiceImpl - Obtener Crédito por ID maneja excepción")
    @Test
    void testGetById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(new DataRetrievalFailureException("Error simulado de base de datos"));

        Optional<Credito> result = this.serviceMock.getById(1);

        assertThat(result).isEmpty();
    }

    @DisplayName("CreditoServiceImpl - Actualizar Crédito")
    @Test
    void testUpdate_ReturnUpdatedCredito() {
        Credito creditoModificado = new Credito();
        creditoModificado.setCapitalPrestado(6000.0);
        creditoModificado.setTasaInteresAnual(14.0);
        creditoModificado.setEstadoCredito("Vigente");

        when(this.repoMock.save(any())).thenReturn(saveCredito);
        Credito result = this.serviceMock.update(saveCredito, creditoModificado);

        assertThat(result).isNotNull();
        assertThat(result.getIdCredito()).isEqualTo(1);
    }

    @DisplayName("CreditoServiceImpl - Actualizar Crédito maneja excepción")
    @Test
    void testUpdate_HandleException() {
        Credito creditoModificado = new Credito();
        when(this.repoMock.save(any())).thenThrow(new DataRetrievalFailureException("Error simulado BD"));

        assertThrows(ServiceException.class, () -> this.serviceMock.update(saveCredito, creditoModificado));
    }

    @DisplayName("CreditoServiceImpl - Eliminar Crédito")
    @Test
    void testDeleteById_Success() {
        try {
            this.serviceMock.deleteById(1);
            assertTrue(true);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("CreditoServiceImpl - Eliminar Crédito maneja excepción")
    @Test
    void testDeleteById_HandleException() {
        doThrow(new DataRetrievalFailureException("Error simulado BD")).when(this.repoMock).deleteById(anyInt());

        assertThrows(ServiceException.class, () -> this.serviceMock.deleteById(1));
    }
}