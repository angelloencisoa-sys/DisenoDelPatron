package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.model.ConfiguracionFinanciera;
import pe.edu.utp.pf.service.ConfiguracionFinancieraService;

@Slf4j
@RestController
@RequestMapping("/api/configuracion")
@RequiredArgsConstructor
@Tag(name = "Configuración Financiera", description = "Parámetros globales del sistema e impuestos aplicando el patrón Singleton - Grupo 07")
public class ConfiguracionFinancieraController {

    private final ConfiguracionFinancieraService configService;

    /**
     * Recupera los parámetros globales vigentes del sistema.
     * Al ser un Singleton, siempre devuelve la misma y única instancia (ID 1).
     */
    @GetMapping
    @Operation(summary = "Obtener parámetros globales (Singleton)",
            description = "Recupera la única instancia con las tasas legales, moras e IGV del sistema financiero.")
    public ResponseEntity<ConfiguracionFinanciera> obtenerConfiguracion() {
        log.info("Solicitud REST para recuperar la instancia única de configuración");
        return ResponseEntity.ok(configService.getConfiguracionUnica());
    }

    /**
     * Modifica los valores de las tasas globales del negocio.
     */
    @PutMapping
    @Operation(summary = "Actualizar parámetros globales",
            description = "Modifica los porcentajes de IGV, moras o tasas máximas, impactando de inmediato en los nuevos cálculos del sistema.")
    public ResponseEntity<ConfiguracionFinanciera> actualizarConfiguracion(
            @RequestBody ConfiguracionFinanciera nuevaConfig) {
        log.info("Solicitud REST para actualizar los valores del Singleton financiero");
        return ResponseEntity.ok(configService.updateConfiguracion(nuevaConfig));
    }

    /**
     * Operación funcional experta (GRASP) expuesta directamente para simular o verificar
     * el costo de penalizaciones de cuotas vencidas sin alterar la base de datos.
     */
    @GetMapping("/calcular-mora")
    @Operation(summary = "Calcular mora por retraso",
            description = "Calcula en tiempo real la penalización monetaria aplicando la tasa del Singleton sobre un capital adeudado.")
    public ResponseEntity<Double> calcularMora(
            @Parameter(description = "Monto de capital de la cuota vencida (en Soles)", required = true, example = "500.0")
            @RequestParam Double capital,

            @Parameter(description = "Días transcurridos desde el vencimiento", required = true, example = "10")
            @RequestParam Integer dias) {

        log.info("Solicitud para calcular simulación de mora: Capital S/ {}, Días {}", capital, dias);
        Double moraCalculada = configService.calcularMoraPorRetraso(capital, dias);
        return ResponseEntity.ok(moraCalculada);
    }
}
