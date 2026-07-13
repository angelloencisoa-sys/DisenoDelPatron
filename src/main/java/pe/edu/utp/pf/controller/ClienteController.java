package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.dto.ClienteDTO;
import pe.edu.utp.pf.model.Cliente;
import pe.edu.utp.pf.model.HistorialCrediticio;
import pe.edu.utp.pf.model.PerfilRiesgo;
import pe.edu.utp.pf.service.ClienteService;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones del Agregado de Clientes (Naturales y Jurídicos) - Grupo 07")
public class ClienteController {

    private final ClienteService clienteService;
    private final SecureRandom random = new SecureRandom();

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
    public ResponseEntity<Object> registrar(
            @Parameter(description = "Cantidad opcional de clientes aleatorios a auto-generar", required = false, example = "5")
            @RequestParam(required = false) Integer cantidad,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = false)
            @RequestBody(required = false) ClienteDTO clienteDTO) { // <--- Solución S4684: Uso de DTO

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

                PerfilRiesgo perfilDefecto = new PerfilRiesgo();
                perfilDefecto.setScoreCrediticio(650 + random.nextInt(150));
                perfilDefecto.setNivelRiesgo("Bajo");
                perfilDefecto.setFechaUltimaEvaluacion(LocalDate.now(ZoneId.of("America/Lima")));
                falso.setPerfilRiesgo(perfilDefecto);

                HistorialCrediticio historialDefecto = new HistorialCrediticio();
                historialDefecto.setNumeroCreditosActivos(random.nextInt(3));
                historialDefecto.setTieneDeudasCastigadas(false);
                falso.setHistorialCrediticio(historialDefecto);

                clienteService.create(falso);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Se generaron con éxito " + cantidad + " clientes aleatorios para pruebas.");
        }

        if (clienteDTO != null) {
            log.info("Registrando un cliente individual de forma manual");

            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombresCompletos(clienteDTO.getNombresCompletos());
            nuevoCliente.setDireccion(clienteDTO.getDireccion());
            nuevoCliente.setTelefono(clienteDTO.getTelefono());
            nuevoCliente.setEmail(clienteDTO.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(nuevoCliente));
        }

        return ResponseEntity.badRequest().body("Debe enviar un objeto JSON o especificar una 'cantidad' para la generación.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente")
    public ResponseEntity<Cliente> actualizar(
            @Parameter(description = "ID del cliente a modificar", required = true, example = "1")
            @PathVariable Integer id,
            @RequestBody ClienteDTO clienteDTO) { // <--- Solución S4684: Uso de DTO
        return clienteService.getById(id)
                .map(old -> {
                    // Mapeo selectivo sobre los campos editables de la entidad original
                    Cliente datosModificados = new Cliente();
                    datosModificados.setNombresCompletos(clienteDTO.getNombresCompletos());
                    datosModificados.setDireccion(clienteDTO.getDireccion());
                    datosModificados.setTelefono(clienteDTO.getTelefono());
                    datosModificados.setEmail(clienteDTO.getEmail());

                    return ResponseEntity.ok(clienteService.update(old, datosModificados));
                })
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
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}