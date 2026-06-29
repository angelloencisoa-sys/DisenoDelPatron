package pe.edu.utp.pf.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.repository.ConfiguracionFinancieraRepository;
import pe.edu.utp.pf.service.ConfiguracionFinancieraService;
import pe.edu.utp.pf.model.ConfiguracionFinanciera;
import pe.edu.utp.pf.service.patron.singleton.UtilSingleton;

/**
 * Implementación de la interface ConfiguracionFinancieraService.
 * Coordina la instancia global Singleton de los parámetros financieros (tasas, mora, IGV)
 * y asegura su persistencia en la base de datos.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ConfiguracionFinancieraServiceImpl implements ConfiguracionFinancieraService {

    private final ConfiguracionFinancieraRepository repo;

    /**
     * Recupera y sincroniza la configuración financiera global.
     * Si no existe en la base de datos, guarda los valores por defecto del Singleton en memoria.
     *
     * @return La única instancia de ConfiguracionFinanciera válida.
     */
    @Transactional
    @Override
    public ConfiguracionFinanciera getConfiguracionUnica() {
        try {
            ConfiguracionFinanciera instanciaMemoria = UtilSingleton.getInstance();

            var configuracionDb = repo.findById(1);

            if (configuracionDb.isPresent()) {
                log.debug("Configuracion Financiera (Singleton) recuperada desde Base de Datos.");
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
            return UtilSingleton.getInstance();
        } catch (Exception e) {
            log.error("Error inesperado al gestionar Singleton financiero: {}", e.getMessage(), e);
            return UtilSingleton.getInstance();
        }
    }

    /**
     * Actualiza los valores de la instancia Singleton y los guarda en la base de datos.
     *
     * @param p Objeto ConfiguracionFinanciera que contiene las nuevas tasas o porcentajes.
     * @return El objeto Singleton actualizado.
     * @throws RuntimeException si hay problemas al guardar en la base de datos.
     */
    @Transactional
    @Override
    public ConfiguracionFinanciera updateConfiguracion(ConfiguracionFinanciera p) {
        try {
            ConfiguracionFinanciera old = UtilSingleton.getInstance();
            old.setTasaInteresMaximaLegal(p.getTasaInteresMaximaLegal());
            old.setPorcentajeMoraDiaria(p.getPorcentajeMoraDiaria());
            old.setIgv(p.getIgv());

            log.info("Actualizando parametros globales del Singleton Financiero.");
            return repo.save(old);

        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al actualizar parametros globales: {}", e.getMessage());
            throw new ServiceException("Error de base de datos al actualizar configuracion", e);
        } catch (Exception e) {
            log.error("Error inesperado al actualizar configuracion Singleton: {}", e.getMessage(), e);
            throw new ServiceException("Error interno", e);
        }
    }


    /**
     * Calcula la penalidad económica (mora) aplicable a una cuota vencida según los días de retraso.
     *
     * @param montoCapital El monto original adeudado en la cuota sobre el cual se aplicará la mora.
     * @param diasRetraso  El número de días calendario transcurridos desde el vencimiento de la cuota.
     * @return El monto total calculado de la penalidad en soles (Double). Retorna 0.0 si no aplica mora.
     */
    @Transactional
    @Override
    public Double calcularMoraPorRetraso(Double montoCapital, Integer diasRetraso) {
        try {
            if (montoCapital == null || montoCapital <= 0 || diasRetraso == null || diasRetraso <= 0) {
                log.debug("Cálculo de mora omitido: Capital o días de retraso no válidos.");
                return 0.0;
            }

            ConfiguracionFinanciera config = getConfiguracionUnica();
            Double porcentajeDiario = config.getPorcentajeMoraDiaria();

            Double montoMora = montoCapital * ((porcentajeDiario / 100.0) * diasRetraso);

            log.debug("Mora calculada: S/ {} para un capital de S/ {} con {} días de retraso.", montoMora, montoCapital, diasRetraso);

            return montoMora;

        } catch (Exception e) {
            log.error("Error inesperado al calcular la mora por retraso: {}", e.getMessage(), e);
            return 0.0;
        }
    }
}