package pe.edu.utp.pf.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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

import pe.edu.utp.pf.model.PagoEfectivo;
import pe.edu.utp.pf.repository.PagoRepository;
import pe.edu.utp.pf.service.impl.PagoServiceImpl;

/**
 * Clase de prueba unitaria para PagoService
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IPagoServiceTest {

    @Mock
    private PagoRepository repoMock;

    @InjectMocks
    private PagoServiceImpl serviceMock;

    private PagoEfectivo pago;
    private PagoEfectivo savePago;
    private List<PagoEfectivo> pagoList;

    @BeforeEach
    void setup() {
        // Pago sin guardar
        pago = new PagoEfectivo();
        pago.setMontoAbonado(500.0);
        pago.setCodigoCaja("CAJA-001");
        pago.setNumeroVoucher("VOC-2024-001");

        // Pago guardado (con ID)
        savePago = new PagoEfectivo();
        savePago.setIdPago(1);
        savePago.setMontoAbonado(500.0);
        savePago.setFechaHoraTransaccion(LocalDateTime.now(ZoneId.of("America/Lima")));
        savePago.setCodigoCaja("CAJA-001");
        savePago.setNumeroVoucher("VOC-2024-001");

        pagoList = List.of(pago, savePago);
    }

    @DisplayName("Service - Registrar y retornar Pago")
    @Test
    void service_Save_Return_Pago() {
        // 1. Preparación
        when(this.repoMock.save(any())).thenReturn(savePago);
        PagoEfectivo savePagoResult = null;

        // 2. Ejecución
        try {
            savePagoResult = (PagoEfectivo) this.serviceMock.create(pago);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3. Comparación
        assertThat(savePagoResult).isNotNull();
        assertThat(savePagoResult.getIdPago()).isNotNull();
        assertThat(savePagoResult.getMontoAbonado()).isEqualTo(500.0);
    }

    @DisplayName("Service - Registrar y retornar Exception")
    @Test
    void service_Save_HandleException() {
        when(this.repoMock.save(any())).thenThrow(RuntimeException.class);
        PagoEfectivo savePagoResult = null;

        try {
            savePagoResult = (PagoEfectivo) this.serviceMock.create(pago);
            assertThat(savePagoResult.getIdPago()).isNotNull();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar todos los Pagos")
    @Test
    void service_FindAll_ReturnsAllRecords() {
        when(this.repoMock.findAll()).thenReturn(List.of(pago, savePago));
        List<?> list = this.serviceMock.getAll();

        assertThat(list).hasSizeGreaterThan(1);
    }

    @DisplayName("Service - Buscar y retornar por Id un Pago")
    @Test
    void service_FindById_ReturnObjectId() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(savePago));
        Optional<?> pagoWithId = null;

        try {
            pagoWithId = this.serviceMock.getById(1);
            assertThat(pagoWithId.get()).isNotNull();
            assertThat(((PagoEfectivo) pagoWithId.get()).getMontoAbonado()).isEqualTo(500.0);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar por Id y retornar un objeto vacío")
    @Test
    void service_FindById_ReturnsNoRecord() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        Optional<?> pagoWithId = null;

        try {
            pagoWithId = this.serviceMock.getById(1);
            assertThat(pagoWithId).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Buscar y retornar por Id una Excepción")
    @Test
    void service_FindById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(RuntimeException.class);
        Optional<?> pagoWithId = null;

        try {
            pagoWithId = this.serviceMock.getById(1);
            assertThat(pagoWithId).isNotPresent();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("Service - Actualizar Pago")
    @Test
    void service_Update_ReturnUpdatedPago() {
        PagoEfectivo pagoModificado = new PagoEfectivo();
        pagoModificado.setMontoAbonado(600.0);

        when(this.repoMock.save(any())).thenReturn(savePago);
        var pagoUpdated = this.serviceMock.update(savePago, pagoModificado);

        try {
            assertThat(pagoUpdated).isNotNull();
            assertThat(pagoUpdated.getIdPago()).isEqualTo(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Service - Eliminar Pago")
    @Test
    void service_Delete_SuccessfulDeletion() {
        try {
            this.serviceMock.deleteById(1);
            assertTrue(true);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }
}
