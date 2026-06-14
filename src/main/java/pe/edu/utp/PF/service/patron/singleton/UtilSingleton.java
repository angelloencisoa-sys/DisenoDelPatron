package pe.edu.utp.PF.service.patron.singleton;

import pe.edu.utp.PF.model.ConfiguracionFinanciera;

public class UtilSingleton {

    private static ConfiguracionFinanciera instancia;

    // Constructor privado para impedir instanciación externa
    private UtilSingleton() {}

    public static synchronized ConfiguracionFinanciera getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionFinanciera();
            // Valores base del ejemplo
            instancia.setIdConfiguracion(1);
            instancia.setTasaInteresMaximaLegal(83.4);
            instancia.setPorcentajeMoraDiaria(0.5);
            instancia.setIgv(18.0);
        }
        return instancia;
    }
}
