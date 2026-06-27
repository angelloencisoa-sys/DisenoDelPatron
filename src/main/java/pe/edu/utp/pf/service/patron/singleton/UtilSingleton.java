package pe.edu.utp.pf.service.patron.singleton;

import pe.edu.utp.pf.model.ConfiguracionFinanciera;

public class UtilSingleton {

    private static ConfiguracionFinanciera instancia;

    private UtilSingleton() {
    }

    public static synchronized ConfiguracionFinanciera getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionFinanciera();
            instancia.setIdConfiguracion(1);
            instancia.setTasaInteresMaximaLegal(83.4);
            instancia.setPorcentajeMoraDiaria(0.5); // Mora del 0.5% diario por defecto
            instancia.setIgv(18.0);
        }
        return instancia;
    }
}
