package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.ConfiguracionFinanciera;

public interface ConfiguracionFinancieraService {

    /**
     * Metodo para obtener la instancia unica de los parametros financieros del sistema
     *
     * @return Objeto unico de ConfiguracionFinanciera
     */
    ConfiguracionFinanciera getConfiguracionUnica();

    /**
     * Metodo para actualizar los valores de la configuracion global
     *
     * @param p Parámetro con los nuevos valores de tasas e IGV
     * @return Retorna el objeto unico actualizado
     */
    ConfiguracionFinanciera updateConfiguracion(ConfiguracionFinanciera p);
}
