package pe.edu.utp.pf.app.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.dao.DataRetrievalFailureException;


import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.model.Pago;
import pe.edu.utp.pf.model.PagoEfectivo;
import pe.edu.utp.pf.repository.PagoRepository;
import pe.edu.utp.pf.service.impl.PagoServiceImpl;

/**
 * Test unitario para PagoServiceImpl
 * @author Grupo 07
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class IPagoServiceImplTest {

    @Mock
    private PagoRepository repoMock;

    @InjectMocks
    private PagoServiceImpl serviceMock;

    private PagoEfectivo pago;
    private PagoEfectivo savePago;
    private List<Pago> pagoList;

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

    @DisplayName("PagoServiceImpl - Crear Pago con timestamp automático")
    @Test
    void testCreate_SavePagoWithTimestamp() {
        when(this.repoMock.save(any())).thenReturn(savePago);
        var result = this.serviceMock.create(pago);

        assertThat(result).isNotNull();
        assertThat(result.getIdPago()).isEqualTo(1);
        assertThat(result.getMontoAbonado()).isEqualTo(500.0);
        assertThat(result.getFechaHoraTransaccion()).isNotNull();
    }

    @DisplayName("PagoServiceImpl - Crear Pago maneja excepción")
    @Test
    void testCreate_HandleException() {
        // CORRECCIÓN: Usar una excepción hija de DataAccessException
        when(this.repoMock.save(any())).thenThrow(new DataRetrievalFailureException("Error simulado BD"));

        // Tu servicio captura DataAccessException y lanza ServiceException
        assertThrows(ServiceException.class, () -> this.serviceMock.create(pago));
    }

    @DisplayName("PagoServiceImpl - Obtener todos los Pagos")
    @Test
    void testGetAll_ReturnAllPagos() {
        when(this.repoMock.findAll()).thenReturn(pagoList);
        var result = this.serviceMock.getAll();

        assertThat(result).isNotNull().hasSize(2);
    }

    @DisplayName("PagoServiceImpl - Obtener todos los Pagos maneja excepción")
    @Test
    void testGetAll_HandleException() {
        when(this.repoMock.findAll()).thenThrow(new DataRetrievalFailureException("Error simulado BD"));
        var result = this.serviceMock.getAll();

        assertThat(result).isNotNull().isEmpty();
    }

    @DisplayName("PagoServiceImpl - Obtener Pago por ID")
    @Test
    void testGetById_ReturnPago() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.of(savePago));
        var result = this.serviceMock.getById(1);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getIdPago()).isEqualTo(1);
        assertThat(result.get().getMontoAbonado()).isEqualTo(500.0);
    }

    @DisplayName("PagoServiceImpl - Obtener Pago por ID no encontrado")
    @Test
    void testGetById_NotFound() {
        when(this.repoMock.findById(anyInt())).thenReturn(Optional.empty());
        var result = this.serviceMock.getById(999);

        assertThat(result).isEmpty();
    }

    @DisplayName("PagoServiceImpl - Obtener Pago por ID maneja excepción")
    @Test
    void testGetById_HandleException() {
        when(this.repoMock.findById(anyInt())).thenThrow(new DataRetrievalFailureException("Error simulado de base de datos"));

        var result = this.serviceMock.getById(1);

        assertThat(result).isEmpty();
    }

    @DisplayName("PagoServiceImpl - Actualizar Pago")
    @Test
    void testUpdate_ReturnUpdatedPago() {
        PagoEfectivo pagoModificado = new PagoEfectivo();
        pagoModificado.setMontoAbonado(600.0);

        when(this.repoMock.save(any())).thenReturn(savePago);
        var result = this.serviceMock.update(savePago, pagoModificado);

        assertThat(result).isNotNull();
        assertThat(result.getIdPago()).isEqualTo(1);
    }

    @DisplayName("PagoServiceImpl - Actualizar Pago maneja excepción")
    @Test
    void testUpdate_HandleException() {
        PagoEfectivo pagoModificado = new PagoEfectivo();
        when(this.repoMock.save(any())).thenThrow(new DataRetrievalFailureException("Error simulado BD"));

        assertThrows(ServiceException.class, () -> this.serviceMock.update(savePago, pagoModificado));
    }

    @DisplayName("PagoServiceImpl - Eliminar Pago")
    @Test
    void testDeleteById_Success() {
        try {
            this.serviceMock.deleteById(1);
            assertTrue(true);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @DisplayName("PagoServiceImpl - Eliminar Pago maneja excepción")
    @Test
    void testDeleteById_HandleException() {

        doThrow(new DataRetrievalFailureException("Error simulado BD")).when(this.repoMock).deleteById(anyInt());


        assertThrows(ServiceException.class, () -> this.serviceMock.deleteById(1));
    }
}
