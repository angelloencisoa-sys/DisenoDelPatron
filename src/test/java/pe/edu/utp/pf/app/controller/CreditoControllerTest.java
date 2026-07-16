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
import pe.edu.utp.pf.controller.CreditoController;
import pe.edu.utp.pf.dto.CreditoDTO;
import pe.edu.utp.pf.model.Credito;
import pe.edu.utp.pf.service.CreditoService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Controller para CreditoController
 * Prueba la funcionalidad de la gestión del ciclo de vida de los créditos bajo Spring Boot 4.x
 *
 * @author Grupo 07
 * @version 3.2
 */
@WebMvcTest(CreditoController.class)
class CreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreditoService creditoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Credito credito;
    private Credito creditoGuardado;
    private CreditoDTO creditoDTO;

    @BeforeEach
    void setup() {
        credito = new Credito();
        credito.setCapitalPrestado(5000.0);
        credito.setTasaInteresAnual(14.5);
        credito.setEstadoCredito("Vigente");

        creditoGuardado = new Credito();
        creditoGuardado.setIdCredito(1);
        creditoGuardado.setCapitalPrestado(5000.0);
        creditoGuardado.setTasaInteresAnual(14.5);
        creditoGuardado.setEstadoCredito("Vigente");

        creditoDTO = new CreditoDTO();
        creditoDTO.setCapitalPrestado(5000.0);
        creditoDTO.setTasaInteresAnual(14.5);
        creditoDTO.setEstadoCredito("Vigente");
    }

    @DisplayName("GET /api/creditos - Listar todos los créditos")
    @Test
    void controller_GetAll_ReturnsAllCreditos() throws Exception {
        List<Credito> creditosList = List.of(creditoGuardado);
        when(creditoService.getAll()).thenReturn(creditosList);

        mockMvc.perform(get("/api/creditos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idCredito").value(1))
                .andExpect(jsonPath("$[0].capitalPrestado").value(5000.0));

        verify(creditoService, times(1)).getAll();
    }

    @DisplayName("GET /api/creditos/{id} - Obtener crédito por ID")
    @Test
    void controller_GetById_ReturnsCredito() throws Exception {
        when(creditoService.getById(1)).thenReturn(Optional.of(creditoGuardado));

        mockMvc.perform(get("/api/creditos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCredito").value(1))
                .andExpect(jsonPath("$.estadoCredito").value("Vigente"));

        verify(creditoService, times(1)).getById(1);
    }

    @DisplayName("GET /api/creditos/{id} - Crédito no encontrado")
    @Test
    void controller_GetById_CreditoNotFound() throws Exception {
        when(creditoService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/creditos/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(creditoService, times(1)).getById(99);
    }

    @DisplayName("POST /api/creditos - Registrar nuevo crédito individual manualmente")
    @Test
    void controller_Post_RegistrarCredito() throws Exception {
        when(creditoService.create(any(Credito.class))).thenReturn(creditoGuardado);

        mockMvc.perform(post("/api/creditos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCredito").value(1));

        verify(creditoService, times(1)).create(any(Credito.class));
    }

    @DisplayName("POST /api/creditos?cantidad=X - Generación masiva aleatoria de créditos")
    @Test
    void controller_Post_GenerarCreditosAleatorios() throws Exception {
        // Simulamos la creación exitosa de un crédito cuando el bucle for llame a create()
        when(creditoService.create(any(Credito.class))).thenReturn(creditoGuardado);

        mockMvc.perform(post("/api/creditos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("cantidad", "3"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Se generaron con éxito 3 créditos de prueba. Verifícalos en el GET para observar sus cronogramas auto-creados."));

        // Se verifica que se invocó el método 'create' las 3 veces especificadas en 'cantidad'
        verify(creditoService, times(3)).create(any(Credito.class));
    }

    @DisplayName("POST /api/creditos - Sin datos ni cantidad retorna error BadRequest")
    @Test
    void controller_Post_SinDatosRetornaError() throws Exception {
        mockMvc.perform(post("/api/creditos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Debe enviar un objeto JSON de crédito o especificar una 'cantidad' para la generación."));
    }

    @DisplayName("PUT /api/creditos/{id} - Actualizar condiciones de un crédito")
    @Test
    void controller_Put_ActualizarCredito() throws Exception {
        Credito creditoActualizado = new Credito();
        creditoActualizado.setIdCredito(1);
        creditoActualizado.setCapitalPrestado(5000.0);
        creditoActualizado.setTasaInteresAnual(14.5);
        creditoActualizado.setEstadoCredito("Cancelado");

        when(creditoService.getById(1)).thenReturn(Optional.of(creditoGuardado));
        when(creditoService.update(any(Credito.class), any(Credito.class)))
                .thenReturn(creditoActualizado);

        mockMvc.perform(put("/api/creditos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoCredito").value("Cancelado"));

        verify(creditoService, times(1)).getById(1);
        verify(creditoService, times(1)).update(any(Credito.class), any(Credito.class));
    }

    @DisplayName("PUT /api/creditos/{id} - Crédito a actualizar no existe")
    @Test
    void controller_Put_ActualizarCreditoInexistente() throws Exception {
        when(creditoService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/creditos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditoDTO)))
                .andExpect(status().isNotFound());

        verify(creditoService, times(1)).getById(99);
        verify(creditoService, never()).update(any(Credito.class), any(Credito.class));
    }

    @DisplayName("DELETE /api/creditos/{id} - Eliminar un crédito existente")
    @Test
    void controller_Delete_EliminarCreditoExistente() throws Exception {
        when(creditoService.getById(1)).thenReturn(Optional.of(creditoGuardado));
        doNothing().when(creditoService).deleteById(1);

        mockMvc.perform(delete("/api/creditos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(creditoService, times(1)).getById(1);
        verify(creditoService, times(1)).deleteById(1);
    }

    @DisplayName("DELETE /api/creditos/{id} - Intentar eliminar crédito inexistente")
    @Test
    void controller_Delete_CreditoInexistente() throws Exception {
        when(creditoService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/creditos/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(creditoService, times(1)).getById(99);
        verify(creditoService, never()).deleteById(99);
    }
}