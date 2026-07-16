package pe.edu.utp.pf.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.pf.controller.PagoController;
import pe.edu.utp.pf.dto.PagoDTO;
import pe.edu.utp.pf.model.Pago;
import pe.edu.utp.pf.model.PagoEfectivo;
import pe.edu.utp.pf.service.PagoService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Controller para PagoController
 * Valida la persistencia, actualización y generación aleatoria de pagos bajo Spring Boot 4.x
 *
 * @author Grupo 07
 * @version 3.1
 */
@WebMvcTest(PagoController.class)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    // Eliminamos @Autowired y lo instanciamos directamente para evitar problemas en el contexto MVC
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Pago pago;
    private Pago pagoGuardado;
    private PagoDTO pagoDTO;

    @BeforeEach
    void setup() {
        pago = new PagoEfectivo();
        ((PagoEfectivo) pago).setMontoAbonado(500.0);

        pagoGuardado = new PagoEfectivo();
        ((PagoEfectivo) pagoGuardado).setIdPago(1);
        ((PagoEfectivo) pagoGuardado).setMontoAbonado(500.0);

        pagoDTO = new PagoDTO();
        pagoDTO.setMontoAbonado(500.0);
    }

    @DisplayName("GET /api/pagos - Listar todos los pagos")
    @Test
    void controller_GetAll_ReturnsAllPagos() throws Exception {
        List<Pago> pagosList = List.of(pagoGuardado);
        when(pagoService.getAll()).thenReturn(pagosList);

        mockMvc.perform(get("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(pagoService, times(1)).getAll();
    }

    @DisplayName("GET /api/pagos/{id} - Obtener pago por ID")
    @Test
    void controller_GetById_ReturnsPago() throws Exception {
        when(pagoService.getById(1)).thenReturn(Optional.of(pagoGuardado));

        mockMvc.perform(get("/api/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPago").value(1));

        verify(pagoService, times(1)).getById(1);
    }

    @DisplayName("GET /api/pagos/{id} - Pago no encontrado")
    @Test
    void controller_GetById_PagoNotFound() throws Exception {
        when(pagoService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pagos/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(pagoService, times(1)).getById(99);
    }

    @DisplayName("POST /api/pagos - Registrar nuevo pago")
    @Test
    void controller_Post_RegistrarPago() throws Exception {
        when(pagoService.create(any(Pago.class))).thenReturn(pagoGuardado);

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDTO)))
                .andExpect(status().isCreated());

        verify(pagoService, times(1)).create(any(Pago.class));
    }

    @DisplayName("POST /api/pagos?cantidad=5 - Generar pagos aleatorios")
    @Test
    void controller_Post_GenerarPagosAleatorios() throws Exception {
        when(pagoService.create(any(Pago.class))).thenReturn(pagoGuardado);

        mockMvc.perform(post("/api/pagos?cantidad=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Se generaron con éxito 5")));

        verify(pagoService, times(5)).create(any(Pago.class));
    }

    @DisplayName("POST /api/pagos?cantidad=0 - Cantidad cero retorna error")
    @Test
    void controller_Post_RegistrarPagoCantidadCero() throws Exception {
        mockMvc.perform(post("/api/pagos?cantidad=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST /api/pagos?cantidad=-1 - Cantidad negativa retorna error")
    @Test
    void controller_Post_RegistrarPagoCantidadNegativa() throws Exception {
        mockMvc.perform(post("/api/pagos?cantidad=-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST /api/pagos - Sin datos retorna error")
    @Test
    void controller_Post_SinDatosRetornaError() throws Exception {
        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("PUT /api/pagos/{id} - Actualizar pago")
    @Test
    void controller_Put_ActualizarPago() throws Exception {
        Pago pagoActualizado = new PagoEfectivo();
        ((PagoEfectivo) pagoActualizado).setIdPago(1);
        ((PagoEfectivo) pagoActualizado).setMontoAbonado(600.0);

        when(pagoService.getById(1)).thenReturn(Optional.of(pagoGuardado));
        when(pagoService.update(any(Pago.class), any(Pago.class)))
                .thenReturn(pagoActualizado);

        mockMvc.perform(put("/api/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDTO)))
                .andExpect(status().isOk());

        verify(pagoService, times(1)).getById(1);
        verify(pagoService, times(1)).update(any(Pago.class), any(Pago.class));
    }

    @DisplayName("PUT /api/pagos/{id} - Pago no encontrado")
    @Test
    void controller_Put_PagoNotFound() throws Exception {
        when(pagoService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/pagos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDTO)))
                .andExpect(status().isNotFound());

        verify(pagoService, times(1)).getById(99);
        verify(pagoService, never()).update(any(), any());
    }

    @DisplayName("DELETE /api/pagos/{id} - Eliminar pago")
    @Test
    void controller_Delete_EliminarPago() throws Exception {
        when(pagoService.getById(1)).thenReturn(Optional.of(pagoGuardado));
        doNothing().when(pagoService).deleteById(1);

        mockMvc.perform(delete("/api/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(pagoService, times(1)).getById(1);
        verify(pagoService, times(1)).deleteById(1);
    }

    @DisplayName("DELETE /api/pagos/{id} - Pago no encontrado")
    @Test
    void controller_Delete_PagoNotFound() throws Exception {
        when(pagoService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/pagos/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(pagoService, times(1)).getById(99);
        verify(pagoService, never()).deleteById(anyInt());
    }
}