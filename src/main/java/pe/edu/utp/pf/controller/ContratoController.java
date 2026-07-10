package pe.edu.utp.pf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.service.ContratoService;
import pe.edu.utp.pf.service.patron.prototype.Contrato;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
public class ContratoController {


    private final ContratoService contratoService;

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> obtenerPorId(@PathVariable Integer id) {
        return contratoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/generar")
    public ResponseEntity<Contrato> generarContrato(
            @RequestParam String tipoContrato,
            @RequestParam Integer idSolicitud) {

        Contrato contratoClonado = contratoService.generarContratoDesdePlantilla(tipoContrato, idSolicitud);

        return ResponseEntity.status(HttpStatus.CREATED).body(contratoClonado);
    }
}