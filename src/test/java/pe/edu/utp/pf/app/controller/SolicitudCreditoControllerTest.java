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
import pe.edu.utp.pf.controller.SolicitudCreditoController;
import pe.edu.utp.pf.dto.SolicitudCreditoDTO;
import pe.edu.utp.pf.model.*;
import pe.edu.utp.pf.service.AsesorFinancieroService;
import pe.edu.utp.pf.service.ClienteService;
import pe.edu.utp.pf.service.CreditoService;
import pe.edu.utp.pf.service.SolicitudCreditoService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test Controller para SolicitudCreditoController
 * Prueba la gestión de solicitudes de crédito y su aprobación de forma segura.
 *
 * @author Grupo 07
 * @version 3.1
 */
@WebMvcTest(SolicitudCreditoController.class)
class SolicitudCreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudCreditoService solicitudService;

    @MockitoBean
    private CreditoService creditoService;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private AsesorFinancieroService asesorService;

    // Se instancia directamente para evitar el fallo de inyección por falta de bean en el contexto reducido de WebMvcTest
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SolicitudCredito solicitud;
    private SolicitudCredito solicitudGuardada;
    private SolicitudCreditoDTO solicitudDTO;
    private Cliente cliente;
    private AsesorFinanciero asesor;
    private Credito credito;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setIdCliente(1);
        cliente.setNombresCompletos("Juan Pérez");

        asesor = new AsesorFinanciero();
        asesor.setIdAsesor(1);
        asesor.setNombreAsesor("Carlos Mendoza");

        solicitud = new SolicitudCredito();
        solicitud.setMontoSolicitado(5000.0);
        solicitud.setPlazoMeses(12);
        solicitud.setEstado("Pendiente");
        solicitud.setCliente(cliente);
        solicitud.setAsesor(asesor);

        solicitudGuardada = new SolicitudCredito();
        solicitudGuardada.setIdSolicitud(1);
        solicitudGuardada.setMontoSolicitado(5000.0);
        solicitudGuardada.setPlazoMeses(12);
        solicitudGuardada.setEstado("Pendiente");
        solicitudGuardada.setCliente(cliente);
        solicitudGuardada.setAsesor(asesor);

        solicitudDTO = new SolicitudCreditoDTO();
        solicitudDTO.setMontoSolicitado(5000.0);
        solicitudDTO.setPlazoMeses(12);
        solicitudDTO.setEstado("Pendiente");

        credito = new Credito();
        credito.setIdCredito(1);
        credito.setCapitalPrestado(5000.0);
        credito.setTasaInteresAnual(14.5);
        credito.setEstadoCredito("Vigente");
    }

    @DisplayName("GET /api/solicitudes - Listar todas las solicitudes")
    @Test
    void controller_GetAll_ReturnsAllSolicitudes() throws Exception {
        List<SolicitudCredito> solicitudesList = List.of(solicitudGuardada);
        when(solicitudService.getAll()).thenReturn(solicitudesList);

        mockMvc.perform(get("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(solicitudService, times(1)).getAll();
    }

    @DisplayName("POST /api/solicitudes - Registrar solicitud individual")
    @Test
    void controller_Post_RegistrarSolicitud() throws Exception {
        when(solicitudService.create(any(SolicitudCredito.class))).thenReturn(solicitudGuardada);

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isCreated());

        verify(solicitudService, times(1)).create(any(SolicitudCredito.class));
    }

    @DisplayName("POST /api/solicitudes?cantidad=3 - Generar solicitudes aleatorias")
    @Test
    void controller_Post_GenerarSolicitudesAleatorias() throws Exception {
        List<Cliente> clientes = List.of(cliente);
        List<AsesorFinanciero> asesores = List.of(asesor);

        when(clienteService.getAll()).thenReturn(clientes);
        when(asesorService.getAll()).thenReturn(asesores);
        when(solicitudService.create(any(SolicitudCredito.class))).thenReturn(solicitudGuardada);

        mockMvc.perform(post("/api/solicitudes?cantidad=3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Se generaron con éxito 3")));

        verify(solicitudService, times(3)).create(any(SolicitudCredito.class));
    }

    @DisplayName("POST /api/solicitudes?cantidad=0 - Cantidad igual a cero no genera aleatorias y evalúa el DTO")
    @Test
    void controller_Post_GenerarSolicitudesCantidadCero() throws Exception {
        // Al enviar cantidad=0, no debe entrar al flujo de generación aleatoria.
        // Si el DTO también es nulo, debe caer al final retornando Bad Request.
        mockMvc.perform(post("/api/solicitudes?cantidad=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Debe enviar un objeto JSON o especificar una 'cantidad'")));

        // Verificamos que NO se haya intentado llamar al servicio de creación para aleatorios
        verify(solicitudService, never()).create(any(SolicitudCredito.class));
    }

    @DisplayName("POST /api/solicitudes?cantidad=3 - Sin clientes retorna error")
    @Test
    void controller_Post_GenerarSolicitudesSinClientes() throws Exception {
        List<Cliente> clientesVacios = List.of();
        List<AsesorFinanciero> asesores = List.of(asesor);

        when(clienteService.getAll()).thenReturn(clientesVacios);
        when(asesorService.getAll()).thenReturn(asesores);

        mockMvc.perform(post("/api/solicitudes?cantidad=3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error")));
    }

    @DisplayName("POST /api/solicitudes - Sin datos retorna error")
    @Test
    void controller_Post_SinDatosRetornaError() throws Exception {
        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST /api/solicitudes/{id}/aprobar - Aprobar solicitud")
    @Test
    void controller_Post_AprobarSolicitud() throws Exception {
        when(solicitudService.getById(1)).thenReturn(Optional.of(solicitudGuardada));
        when(solicitudService.update(any(SolicitudCredito.class), any(SolicitudCredito.class)))
                .thenReturn(solicitudGuardada);
        when(creditoService.create(any(Credito.class))).thenReturn(credito);

        mockMvc.perform(post("/api/solicitudes/1/aprobar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(solicitudService, times(1)).getById(1);
        verify(solicitudService, times(1)).update(any(SolicitudCredito.class), any(SolicitudCredito.class));
        verify(creditoService, times(1)).create(any(Credito.class));
    }

    @DisplayName("POST /api/solicitudes/{id}/aprobar - Solicitud ya aprobada retorna error")
    @Test
    void controller_Post_AprobarSolicitudYaAprobada() throws Exception {
        SolicitudCredito solicitudAprobada = new SolicitudCredito();
        solicitudAprobada.setIdSolicitud(1);
        solicitudAprobada.setEstado("Aprobada");

        when(solicitudService.getById(1)).thenReturn(Optional.of(solicitudAprobada));

        mockMvc.perform(post("/api/solicitudes/1/aprobar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ya se encuentra aprobada")));

        verify(solicitudService, times(1)).getById(1);
        verify(solicitudService, never()).update(any(), any());
        verify(creditoService, never()).create(any());
    }

    @DisplayName("POST /api/solicitudes/{id}/aprobar - Solicitud no encontrada")
    @Test
    void controller_Post_AprobarSolicitudNoEncontrada() throws Exception {
        when(solicitudService.getById(99)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/solicitudes/99/aprobar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(solicitudService, times(1)).getById(99);
        verify(creditoService, never()).create(any());
    }
}