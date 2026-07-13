package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.model.Pago;
import pe.edu.utp.pf.model.PagoEfectivo; // Asegura que esta clase exista en pe.edu.utp.pf.model
import pe.edu.utp.pf.service.PagoService;

import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Gestión polimórfica de ingresos de dinero y amortización de cuotas - Grupo 07")
public class PagoController {

    private final PagoService pagoService;
    private final Random random = new Random();

    @GetMapping
    @Operation(summary = "Listar todos los pagos", description = "Retorna el historial completo de transacciones e ingresos registrados en la BD H2.")
    public ResponseEntity<List<Pago>> listarTodos() {
        log.info("Solicitud para listar el historial de pagos");
        return ResponseEntity.ok(pagoService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID")
    public ResponseEntity<Pago> obtenerPorId(
            @Parameter(description = "ID único del registro de pago", required = true, example = "1")
            @PathVariable Integer id) {
        return pagoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar pagos", description = "Permite registrar un pago manual enviando su JSON polimórfico, o auto-generar transacciones masivas usando el parámetro 'cantidad'.")
    public ResponseEntity<?> registrarPago(
            @Parameter(description = "Cantidad opcional de pagos aleatorios a auto-generar para pruebas", required = false, example = "5")
            @RequestParam(required = false) Integer cantidad,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = false)
            @RequestBody(required = false) Pago pago) {

        // Escenario A: Generación masiva aleatoria para simular amortizaciones en la BD
        if (cantidad != null && cantidad > 0) {
            log.info("Generando {} transacciones de pago aleatorias", cantidad);

            for (int i = 0; i < cantidad; i++) {
                // Instanciamos usando la subclase PagoEfectivo
                PagoEfectivo pagoFalso = new PagoEfectivo();

                // 💡 CORRECCIÓN: Usamos el método correcto 'setMontoAbonado' detectado en tu servicio
                double montoAleatorio = 50.0 + (random.nextInt(10) * 20); // Entre 50 y 230 soles
                pagoFalso.setMontoAbonado(montoAleatorio);

                // Nota: No seteamos fecha ni ID, tu 'PagoServiceImpl' se encarga de ponerle la hora de Lima e id nulo.
                pagoService.create(pagoFalso);
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Se generaron con éxito " + cantidad + " registros de pago polimórficos en tu base de datos H2.");
        }

        // Escenario B: Registro manual clásico vía JSON
        if (pago != null) {
            log.info("Registrando un pago individual de forma manual");
            return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.create(pago));
        }

        return ResponseEntity.badRequest().body("Debe enviar un objeto JSON de pago o especificar una 'cantidad' para la generación.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un registro de pago")
    public ResponseEntity<Pago> actualizarPago(
            @Parameter(description = "ID del pago a modificar", required = true, example = "1")
            @PathVariable Integer id,
            @RequestBody Pago pago) {
        return pagoService.getById(id)
                .map(old -> ResponseEntity.ok(pagoService.update(old, pago)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Anular/Eliminar un pago")
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "ID del pago a eliminar/anular", required = true, example = "1")
            @PathVariable Integer id) {
        return pagoService.getById(id)
                .map(entidad -> {
                    pagoService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
