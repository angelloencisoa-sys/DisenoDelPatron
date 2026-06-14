package pe.edu.utp.PF.service.patron.singleton;

import pe.edu.utp.PF.model.ConfiguracionFinanciera;

public class UtilSingleton {

    private static ConfiguracionFinanciera instancia;

    private UtilSingleton() {}

    public static synchronized ConfiguracionFinanciera getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionFinanciera();
        }
        return instancia;
    }
}
