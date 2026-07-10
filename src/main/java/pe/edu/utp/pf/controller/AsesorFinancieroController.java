package pe.edu.utp.pf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.model.AsesorFinanciero;
import pe.edu.utp.pf.service.AsesorFinancieroService;

import java.util.List;

@RestController
@RequestMapping("/api/asesores")
@RequiredArgsConstructor
public class AsesorFinancieroController {

    private final AsesorFinancieroService asesorService;

    @GetMapping
    public ResponseEntity<List<AsesorFinanciero>> listarTodos() {
        return ResponseEntity.ok(asesorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsesorFinanciero> obtenerPorId(@PathVariable Integer id) {
        return asesorService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AsesorFinanciero> registrar(@RequestBody AsesorFinanciero asesor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asesorService.create(asesor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsesorFinanciero> actualizar(@PathVariable Integer id, @RequestBody AsesorFinanciero asesor) {
        return asesorService.getById(id)
                .map(old -> ResponseEntity.ok(asesorService.update(old, asesor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return asesorService.getById(id)
                .map(entidad -> {
                    asesorService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}