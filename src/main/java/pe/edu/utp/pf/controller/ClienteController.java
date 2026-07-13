package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.model.Cliente;
import pe.edu.utp.pf.model.HistorialCrediticio;
import pe.edu.utp.pf.model.PerfilRiesgo;
import pe.edu.utp.pf.service.ClienteService;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones del Agregado de Clientes (Naturales y Jurídicos) - Grupo 07")
public class ClienteController {

    private final ClienteService clienteService;
    private final Random random = new Random();

    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Retorna de manera limpia la lista completa de clientes registrados en el sistema.")
    public ResponseEntity<List<Cliente>> listarTodos() {
        log.info("Solicitud para listar todos los clientes");
        return ResponseEntity.ok(clienteService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<Cliente> obtenerPorId(
            @Parameter(description = "ID del cliente (idCliente)", required = true, example = "1")
            @PathVariable Integer id) {
        return clienteService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar clientes", description = "Permite registrar un cliente enviando su JSON, o generar registros masivos aleatorios usando el parámetro 'cantidad'.")
    public ResponseEntity<?> registrar(
            @Parameter(description = "Cantidad opcional de clientes aleatorios a auto-generar", required = false, example = "5")
            @RequestParam(required = false) Integer cantidad,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = false)
            @RequestBody(required = false) Cliente cliente) {

        // Escenario A: Generación masiva aleatoria por parámetro para pruebas del Grupo 07
        if (cantidad != null && cantidad > 0) {
            log.info("Generando {} clientes aleatorios en el POST", cantidad);

            String[] nombres = {"Juan", "Rosa", "Luis", "Carmen", "Pedro", "Patricia", "Manuel", "Gabriela", "Alejandro", "Vanessa"};
            String[] apellidos = {"Quispe", "Flores", "Sánchez", "García", "Rodríguez", "Ramos", "Huamán", "Espinoza", "Benites", "Chávez"};
            String[] distritos = {"Av. Arequipa 2600, Lince", "Av. Javier Prado 1100, San Isidro", "Jr. Trujillo 450, Rímac", "Av. La Marina 3200, San Miguel", "Av. Las Flores 890, SJL"};

            for (int i = 0; i < cantidad; i++) {
                String nombreCompleto = nombres[random.nextInt(nombres.length)] + " " + apellidos[random.nextInt(apellidos.length)];
                String direccionSImulada = distritos[random.nextInt(distritos.length)];
                String telefonoSimulado = "9" + (10000000 + random.nextInt(90000000));
                String emailSimulado = nombres[i % nombres.length].toLowerCase() + random.nextInt(100) + "@utp.edu.pe";

                Cliente falso = new Cliente();
                falso.setNombresCompletos(nombreCompleto);
                falso.setDireccion(direccionSImulada);
                falso.setTelefono(telefonoSimulado);
                falso.setEmail(emailSimulado);

                // 💡 SOLUCIÓN: Inicializamos el Perfil de Riesgo con valores por defecto seguros
                PerfilRiesgo perfilDefecto = new PerfilRiesgo();
                perfilDefecto.setScoreCrediticio(650 + random.nextInt(150)); // Score entre 650 y 800
                perfilDefecto.setNivelRiesgo("Bajo");
                perfilDefecto.setFechaUltimaEvaluacion(LocalDate.now());
                falso.setPerfilRiesgo(perfilDefecto);

                // 💡 SOLUCIÓN: Inicializamos el Historial Crediticio con valores por defecto seguros
                HistorialCrediticio historialDefecto = new HistorialCrediticio();
                historialDefecto.setNumeroCreditosActivos(random.nextInt(3)); // Entre 0 y 2 créditos activos
                historialDefecto.setTieneDeudasCastigadas(false); // Cliente limpio
                falso.setHistorialCrediticio(historialDefecto);

                clienteService.create(falso);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Se generaron con éxito " + cantidad + " clientes aleatorios para pruebas.");
        }

        // Escenario B: Registro individual manual clásico
        if (cliente != null) {
            log.info("Registrando un cliente individual de forma manual");
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(cliente));
        }

        // Error si la petición llega totalmente vacía
        return ResponseEntity.badRequest().body("Debe enviar un objeto JSON o especificar una 'cantidad' para la generación.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente")
    public ResponseEntity<Cliente> actualizar(
            @Parameter(description = "ID del cliente a modificar", required = true, example = "1")
            @PathVariable Integer id,
            @RequestBody Cliente cliente) {
        return clienteService.getById(id)
                .map(old -> ResponseEntity.ok(clienteService.update(old, cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del cliente a eliminar", required = true, example = "1")
            @PathVariable Integer id) {
        return clienteService.getById(id)
                .map(entidad -> {
                    clienteService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
