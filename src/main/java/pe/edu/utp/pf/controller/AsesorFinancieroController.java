package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.dto.AsesorFinancieroDTO;
import pe.edu.utp.pf.model.AsesorFinanciero;
import pe.edu.utp.pf.service.AsesorFinancieroService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/asesores")
@RequiredArgsConstructor
@Tag(name = "Asesores Financieros", description = "Operaciones del personal de créditos - Grupo 07")
public class AsesorFinancieroController {

    private final AsesorFinancieroService asesorService;

    @GetMapping
    @Operation(summary = "Listar todos los asesores", description = "Retorna de manera limpia la lista completa de asesores registrados en la BD H2.")
    public ResponseEntity<List<AsesorFinanciero>> getAll() {
        log.info("Solicitud para listar todos los asesores financieros");
        return ResponseEntity.ok(asesorService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener asesor por ID")
    public ResponseEntity<AsesorFinanciero> getById(
            @Parameter(description = "ID del asesor (idAsesor)", required = true, example = "1")
            @PathVariable Integer id) {
        return asesorService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar asesores")
    public ResponseEntity<Object> registrarAsesor(
            @RequestParam(required = false) Integer cantidad,
            @RequestBody(required = false) AsesorFinancieroDTO asesorDTO) {

        if (asesorDTO != null) {
            log.info("Registrando un asesor financiero individual de forma manual");

            // Mapear manualmente (o usar ModelMapper/MapStruct) el DTO a la Entidad
            AsesorFinanciero asesor = new AsesorFinanciero();
            asesor.setNombreAsesor(asesorDTO.getNombreAsesor());
            asesor.setCodigoAgencia(asesorDTO.getCodigoAgencia());

            return ResponseEntity.status(HttpStatus.CREATED).body(asesorService.create(asesor));
        }

        return ResponseEntity.badRequest().body("Debe enviar un objeto JSON o especificar una 'cantidad'.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un asesor")
    public ResponseEntity<AsesorFinanciero> actualizar(
            @Parameter(description = "ID del asesor a modificar", required = true, example = "1")
            @PathVariable Integer id,
            @RequestBody AsesorFinancieroDTO asesorDTO) { // <--- Solución S4684: Uso de DTO
        return asesorService.getById(id)
                .map(old -> {
                    AsesorFinanciero datosModificados = new AsesorFinanciero();
                    datosModificados.setNombreAsesor(asesorDTO.getNombreAsesor());
                    datosModificados.setCodigoAgencia(asesorDTO.getCodigoAgencia());

                    return ResponseEntity.ok(asesorService.update(old, datosModificados));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un asesor")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del asesor a eliminar", required = true, example = "1")
            @PathVariable Integer id) {
        return asesorService.getById(id)
                .map(entidad -> {
                    asesorService.deleteById(id);
                    // CAMBIO AQUÍ: Se eliminó el "new ResponseEntity<Void>"
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}