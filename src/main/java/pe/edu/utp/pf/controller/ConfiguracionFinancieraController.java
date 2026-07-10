package pe.edu.utp.pf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.model.ConfiguracionFinanciera;
import pe.edu.utp.pf.service.ConfiguracionFinancieraService;

@RestController
@RequestMapping("/api/configuraciones")
@RequiredArgsConstructor
public class ConfiguracionFinancieraController {

    private final ConfiguracionFinancieraService configuracionService;


    @GetMapping
    public ResponseEntity<ConfiguracionFinanciera> obtenerConfiguracionGlobal() {
        return ResponseEntity.ok(configuracionService.getConfiguracionUnica());
    }

    @PutMapping
    public ResponseEntity<ConfiguracionFinanciera> actualizarConfiguracionGlobal(@RequestBody ConfiguracionFinanciera configuracion) {
        ConfiguracionFinanciera actualizada = configuracionService.updateConfiguracion(configuracion);
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping("/calcular-mora")
    public ResponseEntity<Double> calcularMora(@RequestParam Double capital, @RequestParam Integer dias) {
        Double moraCalculada = configuracionService.calcularMoraPorRetraso(capital, dias);
        return ResponseEntity.ok(moraCalculada);
    }
}