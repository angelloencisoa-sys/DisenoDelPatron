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
import pe.edu.utp.pf.controller.AsesorFinancieroController;
import pe.edu.utp.pf.dto.AsesorFinancieroDTO;
import pe.edu.utp.pf.model.AsesorFinanciero;
import pe.edu.utp.pf.service.AsesorFinancieroService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Controller para AsesorFinancieroController
 * Versión optimizada para Spring Boot 4.x con corrección de inyección de Jackson.
 *
 * @author Grupo 07
 * @version 3.1
 */
@WebMvcTest(AsesorFinancieroController.class)
class AsesorFinancieroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsesorFinancieroService asesorService;

    // Instanciado de forma directa para solucionar el UnsatisfiedDependencyException
    private final ObjectMapper objectMapper = new ObjectMapper();

    private AsesorFinanciero asesor;
    private AsesorFinanciero asesorGuardado;
    private AsesorFinancieroDTO asesorDTO;

    @BeforeEach
    void setup() {
        asesor = new AsesorFinanciero();
        asesor.setNombreAsesor("Carlos Mendoza");
        asesor.setCodigoAgencia("AGE001");

        asesorGuardado = new AsesorFinanciero();
        asesorGuardado.setIdAsesor(1);
        asesorGuardado.setNombreAsesor("Carlos Mendoza");
        asesorGuardado.setCodigoAgencia("AGE001");

        asesorDTO = new AsesorFinancieroDTO();
        asesorDTO.setNombreAsesor("Carlos Mendoza");
        asesorDTO.setCodigoAgencia("AGE001");
    }

    @DisplayName("GET /api/asesores - Listar todos los asesores")
    @Test
    void controller_GetAll_ReturnsAllAsesores() throws Exception {
        List<AsesorFinanciero> asesoresList = List.of(asesorGuardado);
        when(asesorService.getAll()).thenReturn(asesoresList);

        mockMvc.perform(get("/api/asesores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(asesorService, times(1)).getAll();
    }

    @DisplayName("GET /api/asesores/{id} - Obtener asesor por ID")
    @Test
    void controller_GetById_ReturnsAsesor() throws Exception {
        when(asesorService.getById(1)).thenReturn(Optional.of(asesorGuardado));

        mockMvc.perform(get("/api/asesores/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsesor").value(1))
                .andExpect(jsonPath("$.nombreAsesor").value("Carlos Mendoza"));

        verify(asesorService, times(1)).getById(1);
    }

    @DisplayName("GET /api/asesores/{id} - Asesor no encontrado")
    @Test
    void controller_GetById_AsesorNotFound() throws Exception {
        when(asesorService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/asesores/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(asesorService, times(1)).getById(99);
    }

    @DisplayName("POST /api/asesores - Registrar nuevo asesor")
    @Test
    void controller_Post_RegistrarAsesor() throws Exception {
        when(asesorService.create(any(AsesorFinanciero.class))).thenReturn(asesorGuardado);

        mockMvc.perform(post("/api/asesores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asesorDTO)))
                .andExpect(status().isCreated());

        verify(asesorService, times(1)).create(any(AsesorFinanciero.class));
    }

    @DisplayName("POST /api/asesores - Sin datos retorna error")
    @Test
    void controller_Post_SinDatosRetornaError() throws Exception {
        mockMvc.perform(post("/api/asesores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("PUT /api/asesores/{id} - Actualizar asesor")
    @Test
    void controller_Put_ActualizarAsesor() throws Exception {
        AsesorFinanciero asesorActualizado = new AsesorFinanciero();
        asesorActualizado.setIdAsesor(1);
        asesorActualizado.setNombreAsesor("Carlos Mendoza ACTUALIZADO");
        asesorActualizado.setCodigoAgencia("AGE001");

        when(asesorService.getById(1)).thenReturn(Optional.of(asesorGuardado));
        when(asesorService.update(any(AsesorFinanciero.class), any(AsesorFinanciero.class)))
                .thenReturn(asesorActualizado);

        mockMvc.perform(put("/api/asesores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asesorDTO)))
                .andExpect(status().isOk());

        verify(asesorService, times(1)).getById(1);
        verify(asesorService, times(1)).update(any(AsesorFinanciero.class), any(AsesorFinanciero.class));
    }

    @DisplayName("PUT /api/asesores/{id} - Asesor no encontrado")
    @Test
    void controller_Put_AsesorNotFound() throws Exception {
        when(asesorService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/asesores/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asesorDTO)))
                .andExpect(status().isNotFound());

        verify(asesorService, times(1)).getById(99);
        verify(asesorService, never()).update(any(), any());
    }

    @DisplayName("DELETE /api/asesores/{id} - Eliminar asesor")
    @Test
    void controller_Delete_EliminarAsesor() throws Exception {
        when(asesorService.getById(1)).thenReturn(Optional.of(asesorGuardado));
        doNothing().when(asesorService).deleteById(1);

        mockMvc.perform(delete("/api/asesores/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(asesorService, times(1)).getById(1);
        verify(asesorService, times(1)).deleteById(1);
    }

    @DisplayName("DELETE /api/asesores/{id} - Asesor no encontrado")
    @Test
    void controller_Delete_AsesorNotFound() throws Exception {
        when(asesorService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/asesores/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(asesorService, times(1)).getById(99);
        verify(asesorService, never()).deleteById(anyInt());
    }
}