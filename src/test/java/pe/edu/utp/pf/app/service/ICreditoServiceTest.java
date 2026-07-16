package pe.edu.utp.pf.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import pe.edu.utp.pf.model.Credito;
import pe.edu.utp.pf.model.Cronograma;
import pe.edu.utp.pf.repository.CreditoRepository;
import pe.edu.utp.pf.service.impl.CreditoServiceImpl;

/**
 * Clase de prueba unitaria para CreditoService
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class ICreditoServiceTest {

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

        // Configurar cronograma para el crédito guardado
        Cronograma cronograma = new Cronograma();
        cronograma.setIdCronograma(1);
        cronograma.setFechaGeneracion(LocalDate.now(ZoneId.of("America/Lima")));
        cronograma.setCredito(saveCredito);
        saveCredito.setCronograma(cronograma);

        creditoList = List.of(credito, saveCredito);
    }

    @DisplayName("Service - Registrar y retornar Crédito")
    @Test
    void service_Save_Return_Credito() {
        // 1. Preparación
        when(this.repoMock.save(any())).thenReturn(saveCredito);
        Credito saveCreditoResult = null;

        // 2. Ejecución
        try {
            saveCreditoResult = this.serviceMock.create(credito);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(saveCreditoResult).isNotNull();
        assertThat(saveCreditoResult.getIdCredito()).isNotNull();
        assertThat(saveCreditoResult.getCapitalPrestado()).isEqualTo(5000.0);
    }

    @DisplayName("Service - Registrar y retornar Exception")
    @Test
    void service_Save_HandleException() {
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);
        Credito saveCreditoResult = null;

        try {
            saveCreditoResult = this.serviceMock.create(credito);
            assertThat(saveCreditoResult.getIdCredito()).isNotNull();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar todos los Créditos")
    @Test
    void service_FindAll_ReturnsAllRecords() {
        when(this.repoMock.findAll()).thenReturn(creditoList);
        List<Credito> list = this.serviceMock.getAll();

        assertThat(list).hasSizeGreaterThan(1).contains(saveCredito, credito);

    }

    @DisplayName("Service - Buscar y retornar por Id un Crédito")
    @Test
    void service_FindById_ReturnObjectId() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveCredito));
        Optional<Credito> creditoWithId = null;

        try {
            creditoWithId = this.serviceMock.getById(1);
            assertThat(creditoWithId.get().getIdCredito()).isNotNull();
            assertThat(creditoWithId.get().getCapitalPrestado()).isEqualTo(5000.0);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar por Id y retornar un objeto vacío")
    @Test
    void service_FindById_ReturnsNoRecord() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<Credito> creditoWithId = null;

        try {
            creditoWithId = this.serviceMock.getById(1);
            assertThat(creditoWithId).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar por Id una Excepción")
    @Test
    void service_FindById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(RuntimeException.class);
        Optional<Credito> creditoWithId = null;

        try {
            creditoWithId = this.serviceMock.getById(1);
            assertThat(creditoWithId).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Actualizar Crédito")
    @Test
    void service_Update_ReturnUpdatedCredito() {
        Credito creditoModificado = new Credito();
        creditoModificado.setCapitalPrestado(6000.0);
        creditoModificado.setTasaInteresAnual(14.0);
        creditoModificado.setEstadoCredito("Vigente");

        when(this.repoMock.save(any())).thenReturn(saveCredito);
        Credito creditoUpdated = null;

        try {
            creditoUpdated = this.serviceMock.update(saveCredito, creditoModificado);
            assertThat(creditoUpdated).isNotNull();
            assertThat(creditoUpdated.getIdCredito()).isEqualTo(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Service - Eliminar Crédito")
    @Test
    void service_Delete_SuccessfulDeletion() {
        try {
            this.serviceMock.deleteById(1);
            assertTrue(true);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Crear Crédito con Cronograma automático")
    @Test
    void service_Create_WithAutoCronograma() {
        when(this.repoMock.save(any())).thenReturn(saveCredito);
        Credito saveCreditoResult = null;

        try {
            saveCreditoResult = this.serviceMock.create(credito);
            assertThat(saveCreditoResult).isNotNull();
            assertThat(saveCreditoResult.getCronograma()).isNotNull();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
