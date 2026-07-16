package pe.edu.utp.pf.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.repository.ContratoRepository;
import pe.edu.utp.pf.repository.SolicitudCreditoRepository;
import pe.edu.utp.pf.service.impl.ContratoServiceImpl;
import pe.edu.utp.pf.service.patron.prototype.Contrato;
import pe.edu.utp.pf.exception.ServiceException;

/**
 * Clase de prueba unitaria para ContratoService (Patrón Prototype)
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IContratoServiceTest {

    @Mock
    private ContratoRepository repoMock;

    @Mock
    private SolicitudCreditoRepository solicitudRepoMock;

    @InjectMocks
    private ContratoServiceImpl serviceMock;

    private Contrato contrato;
    private Contrato saveContrato;
    private SolicitudCredito saveSolicitud;

    @BeforeEach
    void setup() {
        // Solicitud guardada (necesaria para crear contrato)
        saveSolicitud = new SolicitudCredito();
        saveSolicitud.setIdSolicitud(1);
        saveSolicitud.setMontoSolicitado(10000.0);
        saveSolicitud.setPlazoMeses(12);
        saveSolicitud.setEstado("Aprobada");

        // Contrato sin guardar
        contrato = new Contrato();
        contrato.setTipo("Consumo");
        contrato.setClausulasExtras("Cláusulas estándar para crédito de consumo: Tasa fija...");
        contrato.setFechaFirma(LocalDate.now(ZoneId.of("America/Lima")));
        contrato.setSolicitud(saveSolicitud);

        // Contrato guardado (con ID)
        saveContrato = new Contrato();
        saveContrato.setIdContrato(1);
        saveContrato.setTipo("Consumo");
        saveContrato.setClausulasExtras("Cláusulas estándar para crédito de consumo: Tasa fija...");
        saveContrato.setFechaFirma(LocalDate.now(ZoneId.of("America/Lima")));
        saveContrato.setSolicitud(saveSolicitud);
    }

    @DisplayName("Service - Buscar y retornar Contrato por Id")
    @Test
    void service_FindById_ReturnContrato() {
        // 1. Preparación
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveContrato));
        Optional<Contrato> contratoResult = null;

        // 2. Ejecución
        try {
            contratoResult = this.serviceMock.getById(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(contratoResult).isNotNull().isPresent().hasValueSatisfying(c -> {
                    assertThat(c.getIdContrato()).isEqualTo(1);
                    assertThat(c.getTipo()).isEqualTo("Consumo");
                });

    }

    @DisplayName("Service - Buscar por Id y retornar vacío")
    @Test
    void service_FindById_ReturnsNoRecord() {
        // 1. Preparación
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<Contrato> contratoResult = null;

        // 2. Ejecución
        try {
            contratoResult = this.serviceMock.getById(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(contratoResult).isNotNull().isNotPresent();
    }

    @DisplayName("Service - Buscar por Id y retornar Excepción")
    @Test
    void service_FindById_HandleException() {
        // 1. Preparación
        when(this.repoMock.findById(anyInt())).thenThrow(RuntimeException.class);
        Optional<Contrato> contratoResult = null;

        // 2. Ejecución
        try {
            contratoResult = this.serviceMock.getById(1);
            assertThat(contratoResult).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Generar Contrato desde Plantilla (Prototype)")
    @Test
    void service_GenerarContrato_FromTemplate_Consumo() {
        // 1. Preparación
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenReturn(saveContrato);
        Contrato contratoGenerado = null;

        // 2. Ejecución
        try {
            contratoGenerado = this.serviceMock.generarContratoDesdePlantilla("Consumo", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(contratoGenerado).isNotNull();
        assertThat(contratoGenerado.getIdContrato()).isNotNull();
        assertThat(contratoGenerado.getTipo()).isEqualTo("Consumo");
        assertThat(contratoGenerado.getSolicitud()).isNotNull();
        assertThat(contratoGenerado.getFechaFirma()).isNotNull();
    }

    @DisplayName("Service - Generar Contrato desde Plantilla (Microempresa)")
    @Test
    void service_GenerarContrato_FromTemplate_Microempresa() {
        // 1. Preparación
        Contrato contratoMicroempresa = new Contrato();
        contratoMicroempresa.setIdContrato(2);
        contratoMicroempresa.setTipo("Microempresa");
        contratoMicroempresa.setClausulasExtras("Cláusulas estándar para crédito Pyme: Respaldo de inventario...");
        contratoMicroempresa.setFechaFirma(LocalDate.now(ZoneId.of("America/Lima")));
        contratoMicroempresa.setSolicitud(saveSolicitud);

        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenReturn(contratoMicroempresa);
        Contrato contratoGenerado = null;

        // 2. Ejecución
        try {
            contratoGenerado = this.serviceMock.generarContratoDesdePlantilla("Microempresa", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(contratoGenerado).isNotNull();
        assertThat(contratoGenerado.getTipo()).isEqualTo("Microempresa");
        assertThat(contratoGenerado.getClausulasExtras()).contains("Pyme");
    }

    @DisplayName("Service - Generar Contrato con Solicitud no existente")
    @Test
    void service_GenerarContrato_SolicitudNotFound_ThrowException() {
        // 1. Preparación
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.empty());

        // 2. Ejecución y 3. Comparación
        try {
            this.serviceMock.generarContratoDesdePlantilla("Consumo", 999);
            assertThat(false).isTrue(); // No debe llegar aquí
        } catch (ServiceException e) {
            // La implementación lanza ServiceException cuando no existe la solicitud
            assertThat(e).isInstanceOf(ServiceException.class);
            // No validar mensaje específico porque el catch general usa "Error interno del servidor"
            assertThat(e.getMessage()).isNotNull();
        }
    }

    @DisplayName("Service - Generar Contrato con tipo inválido")
    @Test
    void service_GenerarContrato_InvalidType_ThrowException() {
        // 1. Preparación
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));

        // 2. Ejecución y 3. Comparación
        try {
            this.serviceMock.generarContratoDesdePlantilla("TipoInvalido", 1);
            // Si llegamos aquí, significa que no se lanzó excepción (test falla)
            assertThat(true).isFalse();
        } catch (ServiceException e) {
            // La implementación captura IllegalArgumentException y la convierte en ServiceException
            assertThat(e).isInstanceOf(ServiceException.class);
            assertThat(e.getMessage()).isNotNull();
        }
    }

    @DisplayName("Service - Verificar clonación del Contrato (Prototype)")
    @Test
    void service_Prototype_CloningContrato() {
        // 1. Preparación
        Contrato plantilla = new Contrato();
        plantilla.setTipo("Consumo");
        plantilla.setClausulasExtras("Cláusulas originales");

        // Crear una copia usando el constructor copia
        Contrato copia = new Contrato(plantilla);

        // 2. Comparación
        // La copia debe tener los mismos datos
        assertThat(copia.getTipo()).isEqualTo(plantilla.getTipo());
        assertThat(copia.getClausulasExtras()).isEqualTo(plantilla.getClausulasExtras());

        // Pero IDs diferentes (nueva instancia)
        assertThat(copia.getIdContrato()).isNull();
        assertThat(copia.getSolicitud()).isNull();
    }

    @DisplayName("Service - Contrato tiene fecha de firma correcta")
    @Test
    void service_GenerarContrato_HasCorrectFechaFirma() {
        // 1. Preparación
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenReturn(saveContrato);
        Contrato contratoGenerado = null;
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Lima"));

        // 2. Ejecución
        try {
            contratoGenerado = this.serviceMock.generarContratoDesdePlantilla("Consumo", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(contratoGenerado.getFechaFirma()).isNotNull();
        assertThat(contratoGenerado.getFechaFirma()).isEqualTo(hoy);
    }

    @DisplayName("Service - Contrato vinculado a la solicitud correcta")
    @Test
    void service_GenerarContrato_LinkedToCorrectSolicitud() {
        // 1. Preparación
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenReturn(saveContrato);
        Contrato contratoGenerado = null;

        // 2. Ejecución
        try {
            contratoGenerado = this.serviceMock.generarContratoDesdePlantilla("Consumo", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(contratoGenerado.getSolicitud()).isNotNull();
        assertThat(contratoGenerado.getSolicitud().getIdSolicitud()).isEqualTo(1);
        assertThat(contratoGenerado.getSolicitud().getMontoSolicitado()).isEqualTo(10000.0);
    }

    @DisplayName("Service - Manejo de excepción en BD al guardar contrato")
    @Test
    void service_GenerarContrato_DatabaseException() {
        // 1. Preparación
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);

        // 2. Ejecución y 3. Comparación
        try {
            this.serviceMock.generarContratoDesdePlantilla("Consumo", 1);
            assertThat(false).isTrue(); // No debe llegar aquí
        } catch (ServiceException e) {
            assertThat(e).isInstanceOf(ServiceException.class);
        }
    }
}
