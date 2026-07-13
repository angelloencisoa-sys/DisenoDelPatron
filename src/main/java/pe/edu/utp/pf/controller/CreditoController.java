package pe.edu.utp.pf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.dto.CreditoDTO;
import pe.edu.utp.pf.model.Credito;
import pe.edu.utp.pf.service.CreditoService;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/creditos")
@RequiredArgsConstructor
@Tag(name = "Créditos", description = "Administración de préstamos aprobados, desembolsos y cronogramas en cascada - Grupo 07")
public class CreditoController {

    private final CreditoService creditoService;
    private final SecureRandom random = new SecureRandom();

    @GetMapping
    @Operation(summary = "Listar todos los créditos", description = "Retorna la lista completa de préstamos vigentes o liquidados en la BD H2.")
    public ResponseEntity<List<Credito>> listarTodos() {
        log.info("Solicitud para listar todas las cuentas de crédito");
        return ResponseEntity.ok(creditoService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener crédito por ID")
    public ResponseEntity<Credito> obtenerPorId(
            @Parameter(description = "ID único de la cuenta de crédito", required = true, example = "1")
            @PathVariable Integer id) {
        return creditoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Desembolsar créditos", description = "Permite registrar un crédito manual vía JSON, o simular desembolsos masivos aleatorios usando el parámetro 'cantidad'.")
    public ResponseEntity<Object> desembolsarDirecto(
            @Parameter(description = "Cantidad opcional de créditos aleatorios a auto-generar para pruebas", required = false, example = "3")
            @RequestParam(required = false) Integer cantidad,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = false)
            @RequestBody(required = false) CreditoDTO creditoDTO) {


        if (cantidad != null && cantidad > 0) {
            log.info("Gatillando generación masiva: Creando {} créditos de prueba", cantidad);

            String[] estadosValidos = {"Vigente", "Al Día", "Refinanciado"};

            for (int i = 0; i < cantidad; i++) {
                Credito falso = new Credito();
                falso.setCapitalPrestado(2000.0 + (random.nextInt(16) * 500));
                falso.setTasaInteresAnual(12.5 + random.nextInt(5));
                falso.setFechaDesembolso(LocalDate.now(ZoneId.of("America/Lima"))
                        .minusDays(random.nextInt(15)));
                falso.setEstadoCredito(estadosValidos[random.nextInt(estadosValidos.length)]);

                falso.setContrato(null);
                falso.setCronograma(null);
                falso.setSeguros(null);

                creditoService.create(falso);
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Se generaron con éxito " + cantidad + " créditos de prueba. Verifícalos en el GET para observar sus cronogramas auto-creados.");
        }


        if (creditoDTO != null) {
            log.info("Procesando desembolso de crédito individual manual");

            Credito nuevoCredito = new Credito();
            nuevoCredito.setCapitalPrestado(creditoDTO.getCapitalPrestado());
            nuevoCredito.setTasaInteresAnual(creditoDTO.getTasaInteresAnual());
            nuevoCredito.setEstadoCredito(creditoDTO.getEstadoCredito());
            nuevoCredito.setFechaDesembolso(LocalDate.now(ZoneId.of("America/Lima")));

            return ResponseEntity.status(HttpStatus.CREATED).body(creditoService.create(nuevoCredito));
        }

        return ResponseEntity.badRequest().body("Debe enviar un objeto JSON de crédito o especificar una 'cantidad' para la generación.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar condiciones de un crédito")
    public ResponseEntity<Credito> actualizarCredito(
            @Parameter(description = "ID del crédito a modificar", required = true, example = "1")
            @PathVariable Integer id,
            @RequestBody CreditoDTO creditoDTO) {
        return creditoService.getById(id)
                .map(old -> {
                    Credito datosModificados = new Credito();
                    datosModificados.setCapitalPrestado(creditoDTO.getCapitalPrestado());
                    datosModificados.setTasaInteresAnual(creditoDTO.getTasaInteresAnual());
                    datosModificados.setEstadoCredito(creditoDTO.getEstadoCredito());

                    return ResponseEntity.ok(creditoService.update(old, datosModificados));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un crédito")
    public ResponseEntity<Void> eliminarCredito(
            @Parameter(description = "ID del crédito a eliminar", required = true, example = "1")
            @PathVariable Integer id) {
        return creditoService.getById(id)
                .map(entidad -> {
                    creditoService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}