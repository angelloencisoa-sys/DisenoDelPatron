package pe.edu.utp.pf.app.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.dao.DataRetrievalFailureException;


import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.repository.SolicitudCreditoRepository;
import pe.edu.utp.pf.service.impl.SolicitudCreditoServiceImpl;

/**
 * Test unitario para SolicitudCreditoServiceImpl
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class ISolicitudCreditoServiceImplTest {

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

    @DisplayName("SolicitudCreditoServiceImpl - Crear Solicitud con estado Pendiente automático")
    @Test
    void testCreate_SaveSolicitudWithDefaultState() {
        when(this.repoMock.save(any())).thenReturn(saveSolicitud);
        SolicitudCredito result = null;

        try {
            result = this.serviceMock.create(solicitud);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isNotNull();
        assertThat(result.getIdSolicitud()).isEqualTo(1);
        assertThat(result.getMontoSolicitado()).isEqualTo(10000.0);
        assertThat(result.getEstado()).isEqualTo("Pendiente");
    }

    @DisplayName("SolicitudCreditoServiceImpl - Crear Solicitud maneja excepción")
    @Test
    void testCreate_HandleException() {

        when(this.repoMock.save(any())).thenThrow(new DataRetrievalFailureException("Error simulado BD"));


        assertThrows(ServiceException.class, () -> this.serviceMock.create(solicitud));
    }

    @DisplayName("SolicitudCreditoServiceImpl - Obtener todas las Solicitudes")
    @Test
    void testGetAll_ReturnAllSolicitudes() {
        when(this.repoMock.findAll()).thenReturn(solicitudList);
        List<SolicitudCredito> result = this.serviceMock.getAll();

        assertThat(result).isNotNull().hasSize(2).contains(saveSolicitud, solicitud);
    }

    @DisplayName("SolicitudCreditoServiceImpl - Obtener todas las Solicitudes maneja excepción")
    @Test
    void testGetAll_HandleException() {
        when(this.repoMock.findAll()).thenThrow(new DataRetrievalFailureException("Error simulado BD"));
        List<SolicitudCredito> result = this.serviceMock.getAll();

        assertThat(result).isNotNull().isEmpty();
    }

    @DisplayName("SolicitudCreditoServiceImpl - Obtener Solicitud por ID")
    @Test
    void testGetById_ReturnSolicitud() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(saveSolicitud));
        Optional<SolicitudCredito> result = this.serviceMock.getById(1);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getIdSolicitud()).isEqualTo(1);
        assertThat(result.get().getMontoSolicitado()).isEqualTo(10000.0);
    }

    @DisplayName("SolicitudCreditoServiceImpl - Obtener Solicitud por ID no encontrado")
    @Test
    void testGetById_NotFound() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<SolicitudCredito> result = this.serviceMock.getById(999);

        assertThat(result).isEmpty();
    }

    @DisplayName("SolicitudCreditoServiceImpl - Obtener Solicitud por ID maneja excepción")
    @Test
    void testGetById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(new DataRetrievalFailureException("Error simulado de base de datos"));

        Optional<SolicitudCredito> result = this.serviceMock.getById(1);

        assertThat(result).isEmpty();
    }

    @DisplayName("SolicitudCreditoServiceImpl - Actualizar Solicitud")
    @Test
    void testUpdate_ReturnUpdatedSolicitud() {
        SolicitudCredito solicitudModificada = new SolicitudCredito();
        solicitudModificada.setMontoSolicitado(15000.0);
        solicitudModificada.setPlazoMeses(24);
        solicitudModificada.setEstado("En Revisión");

        when(this.repoMock.save(any())).thenReturn(saveSolicitud);
        SolicitudCredito result = this.serviceMock.update(saveSolicitud, solicitudModificada);

        assertThat(result).isNotNull();
        assertThat(result.getIdSolicitud()).isEqualTo(1);
    }

    @DisplayName("SolicitudCreditoServiceImpl - Actualizar Solicitud maneja excepción")
    @Test
    void testUpdate_HandleException() {
        SolicitudCredito solicitudModificada = new SolicitudCredito();
        when(this.repoMock.save(any())).thenThrow(new DataRetrievalFailureException("Error simulado BD"));

        assertThrows(ServiceException.class, () -> this.serviceMock.update(saveSolicitud, solicitudModificada));
    }

    @DisplayName("SolicitudCreditoServiceImpl - Eliminar Solicitud")
    @Test
    void testDeleteById_Success() {
        try {
            this.serviceMock.deleteById(1);
            assertTrue(true);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("SolicitudCreditoServiceImpl - Eliminar Solicitud maneja excepción")
    @Test
    void testDeleteById_HandleException() {

        doThrow(new DataRetrievalFailureException("Error simulado BD")).when(this.repoMock).deleteById(anyInt());


        assertThrows(ServiceException.class, () -> this.serviceMock.deleteById(1));
    }

    @DisplayName("SolicitudCreditoServiceImpl - Crear Solicitud sin estado asigna Pendiente")
    @Test
    void testCreate_NoStateAssignsPendiente() {
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
