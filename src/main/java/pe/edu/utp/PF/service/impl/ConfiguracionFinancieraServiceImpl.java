package pe.edu.utp.PF.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.edu.utp.PF.repository.ConfiguracionFinancieraRepository;
import pe.edu.utp.PF.service.ConfiguracionFinancieraService;
import pe.edu.utp.PF.model.ConfiguracionFinanciera;
import pe.edu.utp.PF.service.patron.singleton.UtilSingleton;

@RequiredArgsConstructor
@Slf4j
@Service
public class ConfiguracionFinancieraServiceImpl implements ConfiguracionFinancieraService {

    private final ConfiguracionFinancieraRepository repo;

    @Transactional
    @Override
    public ConfiguracionFinanciera getConfiguracionUnica() {
        try {
            // El Singleton garantiza una sola instancia en memoria
            ConfiguracionFinanciera instanciaMemoria = UtilSingleton.getInstance();

            // Verificamos si ya existe guardada en la Base de Datos (ID fijo = 1)
            var configuracionDb = repo.findById(1);

            if (configuracionDb.isPresent()) {
                log.debug("Configuracion Financiera (Singleton) recuperada desde Base de Datos.");
                // Sincronizamos la instancia de memoria con los ultimos datos guardados
                ConfiguracionFinanciera dbValue = configuracionDb.get();
                instanciaMemoria.setTasaInteresMaximaLegal(dbValue.getTasaInteresMaximaLegal());
                instanciaMemoria.setPorcentajeMoraDiaria(dbValue.getPorcentajeMoraDiaria());
                instanciaMemoria.setIgv(dbValue.getIgv());
            } else {
                log.info("Primera ejecucion: Guardando valores por defecto del Singleton en la BD.");
                repo.save(instanciaMemoria);
            }

            return instanciaMemoria;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al obtener configuracion Singleton: {}", e.getMessage());
            return UtilSingleton.getInstance(); // Failsafe
        } catch (Exception e) {
            log.error("Error inesperado al gestionar Singleton financiero: {}", e.getMessage(), e);
            return UtilSingleton.getInstance();
        }
    }

    @Transactional
    @Override
    public ConfiguracionFinanciera updateConfiguracion(ConfiguracionFinanciera p) {
        try {
            // Sincronizamos con nuestra instancia unica Singleton
            ConfiguracionFinanciera old = UtilSingleton.getInstance();
            old.setTasaInteresMaximaLegal(p.getTasaInteresMaximaLegal());
            old.setPorcentajeMoraDiaria(p.getPorcentajeMoraDiaria());
            old.setIgv(p.getIgv());

            log.info("Actualizando parametros globales del Singleton Financiero.");
            return repo.save(old);

        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al actualizar parametros globales: {}", e.getMessage());
            throw new RuntimeException("Error de base de datos al actualizar configuracion", e);
        } catch (Exception e) {
            log.error("Error inesperado al actualizar configuracion Singleton: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno", e);
        }
    }
}
