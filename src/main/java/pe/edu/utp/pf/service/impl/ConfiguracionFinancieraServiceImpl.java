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
 * Gestiona de manera centralizada las políticas, tasas de interés e IGV
 * aplicando el patrón de diseño Singleton para mantener una única instancia global.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ConfiguracionFinancieraServiceImpl implements ConfiguracionFinancieraService {

    private final ConfiguracionFinancieraRepository repo;

    /**
     * Recupera la única instancia global de configuración financiera.
     * Intenta sincronizar el estado cargado desde la base de datos con la instancia Singleton en memoria.
     * En caso de no existir registros en la BD, persiste la instancia actual por defecto.
     *
     * @return La configuración financiera global única (Singleton).
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
                instanciaMemoria.setIdConfiguracion(1); // Asegurar ID consistente
                instanciaMemoria.setTasaInteresMaximaLegal(dbValue.getTasaInteresMaximaLegal());
                instanciaMemoria.setPorcentajeMoraDiaria(dbValue.getPorcentajeMoraDiaria());
                instanciaMemoria.setIgv(dbValue.getIgv());
            } else {
                log.info("Primera ejecucion: Guardando valores por defecto del Singleton en la BD.");


                ConfiguracionFinanciera nuevaConfigDb = new ConfiguracionFinanciera();
                nuevaConfigDb.setTasaInteresMaximaLegal(instanciaMemoria.getTasaInteresMaximaLegal());
                nuevaConfigDb.setPorcentajeMoraDiaria(instanciaMemoria.getPorcentajeMoraDiaria());
                nuevaConfigDb.setIgv(instanciaMemoria.getIgv());

                repo.save(nuevaConfigDb);
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
     * Actualiza la configuración financiera del sistema.
     *
     * @param p El objeto que contiene los nuevos parámetros financieros.
     * @return La configuración financiera actualizada y persistida.
     * @throws ServiceException si ocurre un error de base de datos o fallo interno.
     */
    @Transactional
    @Override
    public ConfiguracionFinanciera updateConfiguracion(ConfiguracionFinanciera p) {
        try {
            ConfiguracionFinanciera old = UtilSingleton.getInstance();
            old.setIdConfiguracion(1); // Clave para evitar la duplicidad
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
     * Realiza el cálculo matemático de la mora acumulada por retraso de pago,
     * basándose en la tasa configurada globalmente.
     *
     * @param montoCapital El capital original del pago vencido.
     * @param diasRetraso Cantidad de días de retraso transcurridos.
     * @return El monto total calculado de la mora penalizable. Retorna 0.0 si los parámetros son inválidos.
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