package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.dto.SolicitudCreditoDTO;
import pe.edu.utp.pf.model.AsesorFinanciero;
import pe.edu.utp.pf.model.Cliente;
import pe.edu.utp.pf.model.Credito;
import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.service.AsesorFinancieroService;
import pe.edu.utp.pf.service.ClienteService;
import pe.edu.utp.pf.service.CreditoService;
import pe.edu.utp.pf.service.SolicitudCreditoService;

import java.security.SecureRandom;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Solicitudes de Crédito", description = "Gestión de expedientes de financiamiento y eventos de aprobación - Grupo 07")
public class SolicitudCreditoController {

    private final SolicitudCreditoService solicitudService;
    private final CreditoService creditoService;
    private final ClienteService clienteService;
    private final AsesorFinancieroService asesorService;
    private final SecureRandom random = new SecureRandom();

    @GetMapping
    @Operation(summary = "Listar todas las solicitudes", description = "Retorna la lista de expedientes registrados de manera limpia.")
    public ResponseEntity<List<SolicitudCredito>> listarTodas() {
        log.info("Solicitud para listar expedientes de crédito");
        return ResponseEntity.ok(solicitudService.getAll());
    }

    @PostMapping
    @Operation(summary = "Registrar solicitudes", description = "Permite registrar una solicitud manual vía JSON, o auto-generar registros de prueba vinculados a clientes y asesores existentes.")
    public ResponseEntity<Object> registrarSolicitud(
            @Parameter(description = "Cantidad opcional de solicitudes aleatorias a auto-generar", required = false, example = "3")
            @RequestParam(required = false) Integer cantidad,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = false)
            @RequestBody(required = false) SolicitudCreditoDTO solicitudDTO) {

        if (cantidad != null && cantidad > 0) {
            log.info("Generando {} solicitudes de crédito aleatorias", cantidad);

            List<Cliente> clientesDisponibles = clienteService.getAll();
            List<AsesorFinanciero> asesoresDisponibles = asesorService.getAll();

            if (clientesDisponibles.isEmpty() || asesoresDisponibles.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Debes generar primero Clientes y Asesores en sus respectivos endpoints antes de poblar solicitudes.");
            }

            String[] estadosPosibles = {"Pendiente", "En Revisión", "Pre-Aprobada"};

            for (int i = 0; i < cantidad; i++) {
                SolicitudCredito falsa = new SolicitudCredito();

                falsa.setMontoSolicitado(1000.0 + (random.nextInt(18) * 500));
                falsa.setPlazoMeses((random.nextInt(4) + 1) * 6);
                falsa.setEstado(estadosPosibles[random.nextInt(estadosPosibles.length)]);

                falsa.setCliente(clientesDisponibles.get(random.nextInt(clientesDisponibles.size())));
                falsa.setAsesor(asesoresDisponibles.get(random.nextInt(asesoresDisponibles.size())));

                solicitudService.create(falsa);
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Se generaron con éxito " + cantidad + " solicitudes vinculadas correctamente en tu base de datos H2.");
        }

        if (solicitudDTO != null) {
            log.info("Registrando solicitud de crédito manual");

            SolicitudCredito nuevaSolicitud = new SolicitudCredito();
            nuevaSolicitud.setMontoSolicitado(solicitudDTO.getMontoSolicitado());
            nuevaSolicitud.setPlazoMeses(solicitudDTO.getPlazoMeses());
            nuevaSolicitud.setEstado(solicitudDTO.getEstado());

            SolicitudCredito solicitudCreada = solicitudService.create(nuevaSolicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitudCreada);
        }

        return ResponseEntity.badRequest().body("Debe enviar un objeto JSON o especificar una 'cantidad' para la generación.");
    }

    /*
     * Evento del sistema que procesa la aprobación de un expediente y gatilla la
     * creación de la cuenta de préstamo asignando responsabilidades expertas.
     */
    @PostMapping("/{id}/aprobar")
    @Operation(summary = "Aprobar y desembolsar solicitud", description = "Cambia el estado a 'Aprobada' y gatilla automáticamente el flujo de generación del Crédito y Cronograma.")
    public ResponseEntity<Object> aprobarYDesembolsar(
            @Parameter(description = "ID numérico de la solicitud a aprobar", required = true, example = "1")
            @PathVariable Integer id) {

        return solicitudService.getById(id)
                .map(solicitudExistente -> {
                    if ("Aprobada".equalsIgnoreCase(solicitudExistente.getEstado())) {
                        return ResponseEntity.badRequest().body((Object) "La solicitud ya se encuentra aprobada.");
                    }

                    solicitudExistente.setEstado("Aprobada");
                    solicitudService.update(solicitudExistente, solicitudExistente);

                    Credito nuevoCredito = financiarDesdeSolicitud(solicitudExistente);
                    Credito creditoProcesado = creditoService.create(nuevoCredito);

                    return ResponseEntity.ok((Object) creditoProcesado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Método helper que asocia los datos de la solicitud aprobada hacia la entidad Credito.
     */
    private Credito financiarDesdeSolicitud(SolicitudCredito solicitud) {
        Credito credito = new Credito();
        credito.setCapitalPrestado(solicitud.getMontoSolicitado());
        credito.setTasaInteresAnual(14.5);
        credito.setEstadoCredito("Vigente");
        credito.setContrato(solicitud.getContrato());
        return credito;
    }
}