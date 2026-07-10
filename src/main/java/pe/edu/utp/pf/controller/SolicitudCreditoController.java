package pe.edu.utp.pf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.model.Credito;
import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.service.CreditoService;
import pe.edu.utp.pf.service.SolicitudCreditoService;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor


public class SolicitudCreditoController {

    private final SolicitudCreditoService solicitudService;
    private final CreditoService creditoService;

    @GetMapping
    public ResponseEntity<List<SolicitudCredito>> listarTodas() {
        return ResponseEntity.ok(solicitudService.getAll());
    }

    @PostMapping
    public ResponseEntity<SolicitudCredito> registrarSolicitud(@RequestBody SolicitudCredito solicitud) {
        SolicitudCredito nuevaSolicitud = solicitudService.create(solicitud);
        return ResponseEntity.ok(nuevaSolicitud);
    }

    /**
     * Evento del sistema que procesa la aprobación de un expediente y gatilla la
     * creación de la cuenta de préstamo asignando responsabilidades expertas.
     */
    @PostMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarYDesembolsar(@PathVariable Integer id) {
        return solicitudService.getById(id)
                .map(solicitudExistente -> {
                    if ("Aprobada".equalsIgnoreCase(solicitudExistente.getEstado())) {
                        return ResponseEntity.badRequest().body("La solicitud ya se encuentra aprobada.");
                    }


                    solicitudExistente.setEstado("Aprobada");
                    solicitudService.update(solicitudExistente, solicitudExistente);

                    Credito nuevoCredito = financiarDesdeSolicitud(solicitudExistente);
                    Credito creditoProcesado = creditoService.create(nuevoCredito);

                    return ResponseEntity.ok(creditoProcesado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
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
