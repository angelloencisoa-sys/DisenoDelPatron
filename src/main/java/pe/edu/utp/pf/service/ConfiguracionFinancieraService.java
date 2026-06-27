package pe.edu.utp.pf.service;

import pe.edu.utp.pf.model.ConfiguracionFinanciera;

/**
 * Interface que define los métodos de servicio para la configuración global del sistema.
 * Aplica el patrón Singleton para garantizar una única instancia de los parámetros financieros.
 */
public interface ConfiguracionFinancieraService {

    /**
     * Metodo para obtener la instancia única de los parámetros financieros del sistema.
     *
     * @return Objeto único de ConfiguracionFinanciera con los datos actuales.
     */
    ConfiguracionFinanciera getConfiguracionUnica();

    /**
     * Metodo para actualizar los valores de la configuración global (ej. IGV, moras).
     *
     * @param p Objeto con los nuevos valores de tasas e IGV a actualizar.
     * @return Retorna el objeto único actualizado en la base de datos.
     */
    ConfiguracionFinanciera updateConfiguracion(ConfiguracionFinanciera p);

    /**
     * Metodo funcional de negocio para calcular el monto exacto de la mora.
     * Aplica el porcentaje de mora diaria global sobre el capital adeudado según los días de retraso.
     *
     * @param montoCapital El dinero original que el cliente debía pagar en esa cuota.
     * @param diasRetraso  Cantidad de días transcurridos desde el vencimiento de la cuota.
     * @return El monto en soles de la mora calculada (Double). Retorna 0.0 si no hay retraso.
     */
    Double calcularMoraPorRetraso(Double montoCapital, Integer diasRetraso);
}
