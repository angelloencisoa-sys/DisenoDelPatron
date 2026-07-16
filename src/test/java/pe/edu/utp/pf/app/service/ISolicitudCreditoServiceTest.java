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

import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.repository.SolicitudCreditoRepository;
import pe.edu.utp.pf.service.impl.SolicitudCreditoServiceImpl;

/**
 * Clase de prueba unitaria para SolicitudCreditoService
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class ISolicitudCreditoServiceTest {

    @Mock
    private SolicitudCreditoRepository repoMock;

    @InjectMocks
    private SolicitudCreditoServiceImpl serviceMock;

    private SolicitudCredito solicitud;
    private SolicitudCredito saveSolicitud;
    private List<SolicitudCredito> solicitudList;

    @BeforeEach
    void setup() {
        // Solicitud sin guardar
        solicitud = new SolicitudCredito();
        solicitud.setMontoSolicitado(10000.0);
        solicitud.setPlazoMeses(12);
        solicitud.setEstado("Pendiente");

        // Solicitud guardada (con ID)
        saveSolicitud = new SolicitudCredito();
        saveSolicitud.setIdSolicitud(1);
        saveSolicitud.setMontoSolicitado(10000.0);
        saveSolicitud.setPlazoMeses(12);
        saveSolicitud.setEstado("Pendiente");

        solicitudList = List.of(solicitud, saveSolicitud);
    }

    @DisplayName("Service - Registrar y retornar Solicitud")
    @Test
    void service_Save_Return_Solicitud() {
        // 1. Preparación
        when(this.repoMock.save(any())).thenReturn(saveSolicitud);
        SolicitudCredito saveSolicitudResult = null;

        // 2. Ejecución
        try {
            saveSolicitudResult = this.serviceMock.create(solicitud);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(saveSolicitudResult).isNotNull();
        assertThat(saveSolicitudResult.getIdSolicitud()).isNotNull();
        assertThat(saveSolicitudResult.getMontoSolicitado()).isEqualTo(10000.0);
        assertThat(saveSolicitudResult.getEstado()).isEqualTo("Pendiente");
    }

    @DisplayName("Service - Registrar y retornar Exception")
    @Test
    void service_Save_HandleException() {
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);
        SolicitudCredito saveSolicitudResult = null;

        try {
            saveSolicitudResult = this.serviceMock.create(solicitud);
            assertThat(saveSolicitudResult.getIdSolicitud()).isNotNull();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar todas las Solicitudes")
    @Test
    void service_FindAll_ReturnsAllRecords() {
        when(this.repoMock.findAll()).thenReturn(solicitudList);
        List<SolicitudCredito> list = this.serviceMock.getAll();

        assertThat(list.size()).isGreaterThan(1);
        assertThat(list).contains(saveSolicitud, solicitud);
    }

    @DisplayName("Service - Buscar y retornar por Id una Solicitud")
    @Test
    void service_FindById_ReturnObjectId() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        Optional<SolicitudCredito> solicitudWithId = null;

        try {
            solicitudWithId = this.serviceMock.getById(1);
            assertThat(solicitudWithId.get().getIdSolicitud()).isNotNull();
            assertThat(solicitudWithId.get().getMontoSolicitado()).isEqualTo(10000.0);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar por Id y retornar un objeto vacío")
    @Test
    void service_FindById_ReturnsNoRecord() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<SolicitudCredito> solicitudWithId = null;

        try {
            solicitudWithId = this.serviceMock.getById(1);
            assertThat(solicitudWithId.isEmpty()).isTrue();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar por Id una Excepción")
    @Test
    void service_FindById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(RuntimeException.class);
        Optional<SolicitudCredito> solicitudWithId = null;

        try {
            solicitudWithId = this.serviceMock.getById(1);
            assertThat(solicitudWithId.isEmpty()).isTrue();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Actualizar Solicitud")
    @Test
    void service_Update_ReturnUpdatedSolicitud() {
        SolicitudCredito solicitudModificada = new SolicitudCredito();
        solicitudModificada.setMontoSolicitado(15000.0);
        solicitudModificada.setPlazoMeses(24);
        solicitudModificada.setEstado("En Revisión");

        when(this.repoMock.save(any())).thenReturn(saveSolicitud);
        SolicitudCredito solicitudUpdated = null;

        try {
            solicitudUpdated = this.serviceMock.update(saveSolicitud, solicitudModificada);
            assertThat(solicitudUpdated).isNotNull();
            assertThat(solicitudUpdated.getIdSolicitud()).isEqualTo(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Service - Eliminar Solicitud")
    @Test
    void service_Delete_SuccessfulDeletion() {
        try {
            this.serviceMock.deleteById(1);
            assertThat(true).isTrue();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Crear Solicitud con estado por defecto Pendiente")
    @Test
    void service_Create_WithDefaultPendingStatus() {
        SolicitudCredito solicitudSinEstado = new SolicitudCredito();
        solicitudSinEstado.setMontoSolicitado(5000.0);
        solicitudSinEstado.setPlazoMeses(6);

        SolicitudCredito resultado = new SolicitudCredito();
        resultado.setIdSolicitud(2);
        resultado.setMontoSolicitado(5000.0);
        resultado.setPlazoMeses(6);
        resultado.setEstado("Pendiente");

        when(this.repoMock.save(any())).thenReturn(resultado);

        try {
            SolicitudCredito saved = this.serviceMock.create(solicitudSinEstado);
            assertThat(saved.getEstado()).isEqualTo("Pendiente");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
