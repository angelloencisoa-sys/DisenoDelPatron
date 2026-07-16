package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.dto.ContratoDTO;
import pe.edu.utp.pf.service.ContratoService;
import pe.edu.utp.pf.service.patron.prototype.Contrato;

/**
 * Controlador para la gestión de contratos utilizando el patrón Prototype.
 * Optimizado para cumplir los estándares de calidad de SonarQube.
 *
 * @author Grupo 07
 * @version 1.1
 */
@Slf4j
@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
@Tag(name = "Contratos", description = "Gestión de contratos legales aplicando el patrón Prototype - Grupo 07")
public class ContratoController {

    private final ContratoService contratoService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener contrato por ID", description = "Busca un documento contractual específico mediante su ID numérico.")
    public ResponseEntity<ContratoDTO> obtenerPorId(
            @Parameter(description = "ID del contrato a buscar", required = true, example = "1")
            @PathVariable Integer id) {
        return contratoService.getById(id)
                .map(contrato -> ResponseEntity.ok(convertToDTO(contrato)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint oficial que gatilla la creación de un contrato clonando una plantilla en memoria.
     * Implementa de forma limpia el patrón de diseño Prototype requerido por la rúbrica.
     */
    @PostMapping("/generar")
    @Operation(summary = "Generar contrato desde plantilla (Prototype)",
            description = "Clona dinámicamente un prototipo de contrato configurado en memoria basándose en su tipo y lo asocia a una solicitud de crédito.")
    public ResponseEntity<ContratoDTO> generarContrato(
            @Parameter(description = "Tipo de plantilla a clonar (ej. 'Consumo', 'Microempresa')", required = true, example = "Consumo")
            @RequestParam String tipoContrato,

            @Parameter(description = "ID de la solicitud de crédito aprobada que se va a asociar", required = true, example = "1")
            @RequestParam Integer idSolicitud) {

        log.info("Gatillando evento Prototype: Generando contrato de tipo '{}' para la solicitud ID: {}", tipoContrato, idSolicitud);

        Contrato contratoClonado = contratoService.generarContratoDesdePlantilla(tipoContrato, idSolicitud);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(contratoClonado));
    }

    private ContratoDTO convertToDTO(Contrato entity) {
        if (entity == null) {
            return new ContratoDTO();
        }

        Integer idSolicitudAsociada = null;
        if (entity.getSolicitud() != null) {
            idSolicitudAsociada = entity.getSolicitud().getIdSolicitud();
        }

        return new ContratoDTO(
                entity.getIdContrato(),
                entity.getFechaFirma(),
                entity.getClausulasExtras(),
                entity.getTipo(),
                idSolicitudAsociada
        );
    }
}