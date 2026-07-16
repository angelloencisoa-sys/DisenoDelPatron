package pe.edu.utp.pf.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.pf.controller.ConfiguracionFinancieraController;
import pe.edu.utp.pf.dto.ConfiguracionFinancieraDTO;
import pe.edu.utp.pf.model.ConfiguracionFinanciera;
import pe.edu.utp.pf.service.ConfiguracionFinancieraService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Controller para ConfiguracionFinancieraController
 * Prueba la gestión de parámetros globales financieros utilizando DTOs bajo Spring Boot 4.x
 */
@WebMvcTest(ConfiguracionFinancieraController.class)
class ConfiguracionFinancieraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfiguracionFinancieraService configuracionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ConfiguracionFinanciera configuracion;
    private ConfiguracionFinancieraDTO configuracionDTO;

    @BeforeEach
    void setup() {
        configuracion = new ConfiguracionFinanciera();
        configuracion.setIdConfiguracion(1);
        configuracion.setTasaInteresMaximaLegal(35.0);
        configuracion.setPorcentajeMoraDiaria(0.05);
        configuracion.setIgv(0.18);

        configuracionDTO = new ConfiguracionFinancieraDTO(35.0, 0.05, 0.18);
    }

    @DisplayName("GET /api/configuraciones - Obtener configuración global")
    @Test
    void controller_Get_ObtenerConfiguracionGlobal() throws Exception {
        when(configuracionService.getConfiguracionUnica()).thenReturn(configuracion);

        mockMvc.perform(get("/api/configuraciones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasaInteresMaximaLegal").value(35.0))
                .andExpect(jsonPath("$.porcentajeMoraDiaria").value(0.05))
                .andExpect(jsonPath("$.igv").value(0.18));

        verify(configuracionService, times(1)).getConfiguracionUnica();
    }

    @DisplayName("GET /api/configuraciones - Retorna cuerpo vacío si la configuración es nula")
    @Test
    void controller_Get_ObtenerConfiguracionGlobalNula() throws Exception {
        when(configuracionService.getConfiguracionUnica()).thenReturn(null);

        mockMvc.perform(get("/api/configuraciones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(configuracionService, times(1)).getConfiguracionUnica();
    }

    @DisplayName("PUT /api/configuraciones - Actualizar configuración global")
    @Test
    void controller_Put_ActualizarConfiguracionGlobal() throws Exception {
        ConfiguracionFinanciera configuracionActualizada = new ConfiguracionFinanciera();
        configuracionActualizada.setIdConfiguracion(1);
        configuracionActualizada.setTasaInteresMaximaLegal(40.0);
        configuracionActualizada.setPorcentajeMoraDiaria(0.06);
        configuracionActualizada.setIgv(0.18);

        when(configuracionService.updateConfiguracion(any(ConfiguracionFinanciera.class)))
                .thenReturn(configuracionActualizada);

        mockMvc.perform(put("/api/configuraciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configuracionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasaInteresMaximaLegal").value(40.0));

        verify(configuracionService, times(1)).updateConfiguracion(any(ConfiguracionFinanciera.class));
    }

    @DisplayName("PUT /api/configuraciones - Envío de payload nulo o vacío")
    @Test
    void controller_Put_ActualizarConfiguracionConNullBody() throws Exception {
        when(configuracionService.updateConfiguracion(null)).thenReturn(null);

        mockMvc.perform(put("/api/configuraciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Controller Unit Test - Llamada directa con DTO nulo")
    @Test
    void unitTest_ActualizarConfiguracionConDtoNulo() {
        // Instanciamos el controlador directamente con el mock
        ConfiguracionFinancieraController controllerDirecto =
                new ConfiguracionFinancieraController(configuracionService);

        // Al pasar null directamente, forzamos que convertToEntity reciba null
        ResponseEntity<ConfiguracionFinancieraDTO> response =
                controllerDirecto.actualizarConfiguracionGlobal(null);

        // Verificamos que no se rompa la lógica y retorne una respuesta con body nulo
        org.junit.jupiter.api.Assertions.assertNotNull(response);
        org.junit.jupiter.api.Assertions.assertNull(response.getBody());
    }

    @DisplayName("GET /api/configuraciones/calcular-mora - Calcular mora por retraso")
    @Test
    void controller_Get_CalcularMora() throws Exception {
        double capital = 1000.0;
        int dias = 30;
        double moraEsperada = 150.0;

        when(configuracionService.calcularMoraPorRetraso(capital, dias)).thenReturn(moraEsperada);

        mockMvc.perform(get("/api/configuraciones/calcular-mora")
                        .param("capital", "1000.0")
                        .param("dias", "30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("150.0"));

        verify(configuracionService, times(1)).calcularMoraPorRetraso(1000.0, 30);
    }

    @DisplayName("GET /api/configuraciones/calcular-mora - Parámetros faltantes retorna error")
    @Test
    void controller_Get_CalcularMoraSinParametros() throws Exception {
        mockMvc.perform(get("/api/configuraciones/calcular-mora")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("GET /api/configuraciones/calcular-mora - Capital negativo")
    @Test
    void controller_Get_CalcularMoraCapitalNegativo() throws Exception {
        when(configuracionService.calcularMoraPorRetraso(-1000.0, 30)).thenReturn(0.0);

        mockMvc.perform(get("/api/configuraciones/calcular-mora")
                        .param("capital", "-1000.0")
                        .param("dias", "30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(configuracionService, times(1)).calcularMoraPorRetraso(-1000.0, 30);
    }
}