package pe.edu.utp.pf.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.pf.controller.ContratoController;
import pe.edu.utp.pf.dto.ContratoDTO;
import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.service.ContratoService;
import pe.edu.utp.pf.service.patron.prototype.Contrato;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Controller para ContratoController
 * Prueba la funcionalidad del patrón Prototype para generación de contratos bajo Spring Boot 4.x
 *
 * @author Grupo 07
 * @version 3.5
 */
@WebMvcTest(ContratoController.class)
class ContratoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContratoService contratoService;

    @Autowired
    private ContratoController contratoController;

    private Contrato contrato;

    @BeforeEach
    void setup() {
        contrato = new Contrato();
        contrato.setIdContrato(1);
        contrato.setTipo("Consumo");
        contrato.setFechaFirma(LocalDate.now(ZoneId.of("America/Lima")));
        contrato.setClausulasExtras("Cláusula de garantía estándar");
    }

    @DisplayName("GET /api/contratos/{id} - Obtener contrato por ID sin solicitud asociada")
    @Test
    void controller_GetById_ReturnsContratoSinSolicitud() throws Exception {
        when(contratoService.getById(1)).thenReturn(Optional.of(contrato));

        mockMvc.perform(get("/api/contratos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idContrato").value(1))
                .andExpect(jsonPath("$.tipo").value("Consumo"))
                .andExpect(jsonPath("$.idSolicitud").value(org.hamcrest.Matchers.nullValue()));

        verify(contratoService, times(1)).getById(1);
    }

    @DisplayName("GET /api/contratos/{id} - Obtener contrato por ID con solicitud asociada")
    @Test
    void controller_GetById_ReturnsContratoConSolicitud() throws Exception {
        SolicitudCredito solicitud = new SolicitudCredito();
        solicitud.setIdSolicitud(15);
        contrato.setSolicitud(solicitud);

        when(contratoService.getById(1)).thenReturn(Optional.of(contrato));

        mockMvc.perform(get("/api/contratos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idContrato").value(1))
                .andExpect(jsonPath("$.idSolicitud").value(15));

        verify(contratoService, times(1)).getById(1);
    }

    @DisplayName("GET /api/contratos/{id} - Contrato no encontrado")
    @Test
    void controller_GetById_ContratoNotFound() throws Exception {
        when(contratoService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/contratos/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(contratoService, times(1)).getById(99);
    }

    @DisplayName("Unitario - Convertir entidad nula a DTO utilizando reflexión para cobertura total")
    @Test
    void unit_ConvertToDTO_WithNullEntity_ReturnsEmptyDTO() throws Exception {
        // Obtenemos acceso al método privado convertToDTO por medio de Reflection
        Method convertToDTOMethod = ContratoController.class.getDeclaredMethod("convertToDTO", Contrato.class);
        convertToDTOMethod.setAccessible(true);


        ContratoDTO resultado = (ContratoDTO) convertToDTOMethod.invoke(contratoController, (Contrato) null);


        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdContrato()).isNull();
    }

    @DisplayName("POST /api/contratos/generar - Generar contrato desde plantilla (Prototype)")
    @Test
    void controller_Post_GenerarContratoDesdePrototype() throws Exception {
        when(contratoService.generarContratoDesdePlantilla("Consumo", 1)).thenReturn(contrato);

        mockMvc.perform(post("/api/contratos/generar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tipoContrato", "Consumo")
                        .param("idSolicitud", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("Consumo"));

        verify(contratoService, times(1)).generarContratoDesdePlantilla("Consumo", 1);
    }

    @DisplayName("POST /api/contratos/generar - Generar contrato tipo Microempresa")
    @Test
    void controller_Post_GenerarContratoMicroempresa() throws Exception {
        Contrato contratoMicroempresa = new Contrato();
        contratoMicroempresa.setIdContrato(2);
        contratoMicroempresa.setTipo("Microempresa");
        contratoMicroempresa.setFechaFirma(LocalDate.now(ZoneId.of("America/Lima")));

        when(contratoService.generarContratoDesdePlantilla("Microempresa", 2)).thenReturn(contratoMicroempresa);

        mockMvc.perform(post("/api/contratos/generar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tipoContrato", "Microempresa")
                        .param("idSolicitud", "2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("Microempresa"));

        verify(contratoService, times(1)).generarContratoDesdePlantilla("Microempresa", 2);
    }

    @DisplayName("POST /api/contratos/generar - Parámetros faltantes retorna error")
    @Test
    void controller_Post_GenerarContratoSinParametros() throws Exception {
        mockMvc.perform(post("/api/contratos/generar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}