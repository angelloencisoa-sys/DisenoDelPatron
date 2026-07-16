package pe.edu.utp.pf.app.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
 * Test unitario para ContratoServiceImpl
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IContratoServiceImplTest {

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
        // Solicitud guardada
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

        // Contrato guardado
        saveContrato = new Contrato();
        saveContrato.setIdContrato(1);
        saveContrato.setTipo("Consumo");
        saveContrato.setClausulasExtras("Cláusulas estándar para crédito de consumo: Tasa fija...");
        saveContrato.setFechaFirma(LocalDate.now(ZoneId.of("America/Lima")));
        saveContrato.setSolicitud(saveSolicitud);
    }

    @DisplayName("ContratoServiceImpl - Buscar Contrato por ID")
    @Test
    void testGetById_ReturnContrato() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveContrato));
        Optional<Contrato> result = this.serviceMock.getById(1);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getIdContrato()).isEqualTo(1);
        assertThat(result.get().getTipo()).isEqualTo("Consumo");
    }

    @DisplayName("ContratoServiceImpl - Buscar Contrato por ID no encontrado")
    @Test
    void testGetById_NotFound() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<Contrato> result = this.serviceMock.getById(999);

        assertThat(result).isEmpty();
    }

    @DisplayName("ContratoServiceImpl - Buscar Contrato por ID maneja excepción")
    @Test
    void testGetById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(RuntimeException.class);
        Optional<Contrato> result = this.serviceMock.getById(1);

        assertThat(result).isEmpty();
    }

    @DisplayName("ContratoServiceImpl - Generar Contrato desde Plantilla Consumo")
    @Test
    void testGenerarContratoDesdePlantilla_Consumo() {
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenReturn(saveContrato);
        Contrato result = null;

        try {
            result = this.serviceMock.generarContratoDesdePlantilla("Consumo", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isNotNull();
        assertThat(result.getIdContrato()).isEqualTo(1);
        assertThat(result.getTipo()).isEqualTo("Consumo");
        assertThat(result.getSolicitud()).isNotNull();
        assertThat(result.getFechaFirma()).isNotNull();
    }

    @DisplayName("ContratoServiceImpl - Generar Contrato desde Plantilla Microempresa")
    @Test
    void testGenerarContratoDesdePlantilla_Microempresa() {
        Contrato contratoMicroempresa = new Contrato();
        contratoMicroempresa.setIdContrato(2);
        contratoMicroempresa.setTipo("Microempresa");
        contratoMicroempresa.setClausulasExtras("Cláusulas estándar para crédito Pyme: Respaldo de inventario...");
        contratoMicroempresa.setFechaFirma(LocalDate.now(ZoneId.of("America/Lima")));
        contratoMicroempresa.setSolicitud(saveSolicitud);

        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenReturn(contratoMicroempresa);
        Contrato result = null;

        try {
            result = this.serviceMock.generarContratoDesdePlantilla("Microempresa", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isNotNull();
        assertThat(result.getTipo()).isEqualTo("Microempresa");
        assertThat(result.getClausulasExtras()).contains("Pyme");
    }

    @DisplayName("ContratoServiceImpl - Generar Contrato solicitud no existe")
    @Test
    void testGenerarContratoDesdePlantilla_SolicitudNotFound() {
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            this.serviceMock.generarContratoDesdePlantilla("Consumo", 999);
        });

        assertThat(exception).isInstanceOf(ServiceException.class);
        assertThat(exception.getMessage()).isNotNull();
    }

    @DisplayName("ContratoServiceImpl - Generar Contrato con tipo inválido")
    @Test
    void testGenerarContratoDesdePlantilla_InvalidType() {
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            this.serviceMock.generarContratoDesdePlantilla("TipoInvalido", 1);
        });

        assertThat(exception).isInstanceOf(ServiceException.class);
        assertThat(exception.getMessage()).isNotNull();
    }

    @DisplayName("ContratoServiceImpl - Verificar clonación del Contrato (Prototype)")
    @Test
    void testPrototype_Cloning() {
        Contrato plantilla = new Contrato();
        plantilla.setTipo("Consumo");
        plantilla.setClausulasExtras("Cláusulas originales");

        Contrato copia = new Contrato(plantilla);

        assertThat(copia.getTipo()).isEqualTo(plantilla.getTipo());
        assertThat(copia.getClausulasExtras()).isEqualTo(plantilla.getClausulasExtras());
        assertThat(copia.getIdContrato()).isNull();
        assertThat(copia.getSolicitud()).isNull();
    }

    @DisplayName("ContratoServiceImpl - Contrato tiene fecha de firma correcta")
    @Test
    void testGenerarContratoDesdePlantilla_HasCorrectFechaFirma() {
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenReturn(saveContrato);
        Contrato result = null;
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Lima"));

        try {
            result = this.serviceMock.generarContratoDesdePlantilla("Consumo", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result.getFechaFirma()).isNotNull();
        assertThat(result.getFechaFirma()).isEqualTo(hoy);
    }

    @DisplayName("ContratoServiceImpl - Contrato vinculado a solicitud correcta")
    @Test
    void testGenerarContratoDesdePlantilla_LinkedToCorrectSolicitud() {
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenReturn(saveContrato);
        Contrato result = null;

        try {
            result = this.serviceMock.generarContratoDesdePlantilla("Consumo", 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result.getSolicitud()).isNotNull();
        assertThat(result.getSolicitud().getIdSolicitud()).isEqualTo(1);
        assertThat(result.getSolicitud().getMontoSolicitado()).isEqualTo(10000.0);
    }

    @DisplayName("ContratoServiceImpl - Generar Contrato maneja excepción BD")
    @Test
    void testGenerarContratoDesdePlantilla_DatabaseException() {
        when(this.solicitudRepoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            this.serviceMock.generarContratoDesdePlantilla("Consumo", 1);
        });

        assertThat(exception).isInstanceOf(ServiceException.class);
    }
}
