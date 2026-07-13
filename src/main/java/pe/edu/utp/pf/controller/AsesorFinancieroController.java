package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @Operation(summary = "Registrar asesores", description = "Permite registrar un asesor enviando su JSON, o generar registros masivos aleatorios usando el parámetro 'cantidad'.")
    public ResponseEntity<Object> registrar(
            @Parameter(description = "Cantidad opcional de asesores aleatorios a auto-generar", required = false, example = "5")
            @RequestParam(required = false) Integer cantidad,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = false)
            @RequestBody(required = false) AsesorFinanciero asesor) {

        if (asesor != null) {
            log.info("Registrando un asesor financiero individual de forma manual");
            return ResponseEntity.status(HttpStatus.CREATED).body(asesorService.create(asesor));
        }

        return ResponseEntity.badRequest().body("Debe enviar un objeto JSON o especificar una 'cantidad' para la generación.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un asesor")
    public ResponseEntity<AsesorFinanciero> actualizar(
            @Parameter(description = "ID del asesor a modificar", required = true, example = "1")
            @PathVariable Integer id,
            @RequestBody AsesorFinanciero asesor) {
        return asesorService.getById(id)
                .map(old -> ResponseEntity.ok(asesorService.update(old, asesor)))
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