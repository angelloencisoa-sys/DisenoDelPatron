package pe.edu.utp.pf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.model.Credito;
import pe.edu.utp.pf.service.CreditoService;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
@RequiredArgsConstructor
public class CreditoController {

    private final CreditoService creditoService;

    @GetMapping
    public ResponseEntity<List<Credito>> listarTodos() {
        return ResponseEntity.ok(creditoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credito> obtenerPorId(@PathVariable Integer id) {
        return creditoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Credito> desembolsarDirecto(@RequestBody Credito credito) {

        return ResponseEntity.status(HttpStatus.CREATED).body(creditoService.create(credito));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Credito> actualizarCredito(@PathVariable Integer id, @RequestBody Credito credito) {
        return creditoService.getById(id)
                .map(old -> ResponseEntity.ok(creditoService.update(old, credito)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCredito(@PathVariable Integer id) {
        return creditoService.getById(id)
                .map(entidad -> {
                    creditoService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
