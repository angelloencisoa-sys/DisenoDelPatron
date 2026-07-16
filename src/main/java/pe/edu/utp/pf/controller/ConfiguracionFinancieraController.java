package pe.edu.utp.pf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.pf.dto.ConfiguracionFinancieraDTO;
import pe.edu.utp.pf.model.ConfiguracionFinanciera;
import pe.edu.utp.pf.service.ConfiguracionFinancieraService;

/**
 * Controlador para la gestión de parámetros globales financieros.
 * Utiliza DTOs para cumplir con los estándares de seguridad exigidos por SonarQube.
 */
@RestController
@RequestMapping("/api/configuraciones")
@RequiredArgsConstructor
public class ConfiguracionFinancieraController {

    private final ConfiguracionFinancieraService configuracionService;

    /**
     * Obtiene la única instancia global mapeada a un DTO seguro.
     */
    @GetMapping
    public ResponseEntity<ConfiguracionFinancieraDTO> obtenerConfiguracionGlobal() {
        ConfiguracionFinanciera config = configuracionService.getConfiguracionUnica();
        return ResponseEntity.ok(convertToDTO(config));
    }

    /**
     * Recibe un DTO, lo convierte a entidad y actualiza el recurso global.
     */
    @PutMapping
    public ResponseEntity<ConfiguracionFinancieraDTO> actualizarConfiguracionGlobal(
            @RequestBody ConfiguracionFinancieraDTO dto) {

        ConfiguracionFinanciera entidadParaActualizar = convertToEntity(dto);
        ConfiguracionFinanciera actualizada = configuracionService.updateConfiguracion(entidadParaActualizar);

        return ResponseEntity.ok(convertToDTO(actualizada));
    }

    /**
     * Endpoint utilitario de negocio para calcular la mora.
     */
    @GetMapping("/calcular-mora")
    public ResponseEntity<Double> calcularMora(@RequestParam Double capital, @RequestParam Integer dias) {
        Double moraCalculada = configuracionService.calcularMoraPorRetraso(capital, dias);
        return ResponseEntity.ok(moraCalculada);
    }

    private ConfiguracionFinancieraDTO convertToDTO(ConfiguracionFinanciera entity) {
        if (entity == null) {
            return null;
        }
        return new ConfiguracionFinancieraDTO(
                entity.getTasaInteresMaximaLegal(),
                entity.getPorcentajeMoraDiaria(),
                entity.getIgv()
        );
    }

    private ConfiguracionFinanciera convertToEntity(ConfiguracionFinancieraDTO dto) {
        if (dto == null) {
            return null;
        }
        ConfiguracionFinanciera entity = new ConfiguracionFinanciera();
        entity.setIdConfiguracion(1);
        entity.setTasaInteresMaximaLegal(dto.getTasaInteresMaximaLegal());
        entity.setPorcentajeMoraDiaria(dto.getPorcentajeMoraDiaria());
        entity.setIgv(dto.getIgv());
        return entity;
    }
}