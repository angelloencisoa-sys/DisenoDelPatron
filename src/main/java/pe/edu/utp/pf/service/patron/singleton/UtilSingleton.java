package pe.edu.utp.pf.service.patron.singleton;

import pe.edu.utp.pf.model.ConfiguracionFinanciera;

/**
 * Clase utilitaria que implementa el patrón de diseño creacional Singleton.
 *
 * @author Grupo 07
 * @version 2.0
 */
public class UtilSingleton {

    /**
     * Variable estática privada que mantiene en memoria la referencia a la instancia única.
     */
    private static ConfiguracionFinanciera instancia;

    /**
     * Constructor privado para restringir la instanciación directa mediante el operador {@code new}
     * desde clases externas, cumpliendo con la regla estructural del patrón Singleton.
     */
    private UtilSingleton() {
    }

    /**
     * Metodo estático sincronizado que provee el punto de acceso global a la configuración financiera.
     *
     * @return La instancia única y global de ConfiguracionFinanciera.
     */
    public static synchronized ConfiguracionFinanciera getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionFinanciera();
            instancia.setIdConfiguracion(1);
            instancia.setTasaInteresMaximaLegal(83.4);
            instancia.setPorcentajeMoraDiaria(0.5);
            instancia.setIgv(18.0);
        }
        return instancia;
    }
}
