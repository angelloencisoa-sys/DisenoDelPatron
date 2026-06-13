package pe.edu.utp.PF.service.patron.prototype;

import java.util.HashMap;

public class GestorContrato {

    private HashMap<String, Contrato> prototipos = new HashMap<>();

    public GestorContrato() {
        // Inicializamos los prototipos base como hiciste con Circulo y Rectangulo
        Contrato contratoConsumo = new Contrato();
        contratoConsumo.tipo = "Consumo";
        contratoConsumo.setClausulasExtras("Cláusulas estándar para crédito de consumo: Tasa fija...");

        Contrato contratoMicroempresa = new Contrato();
        contratoMicroempresa.tipo = "Microempresa";
        contratoMicroempresa.setClausulasExtras("Cláusulas estándar para crédito Pyme: Respaldo de inventario...");

        prototipos.put("Consumo", contratoConsumo);
        prototipos.put("Microempresa", contratoMicroempresa);
    }

    // Exactamente igual a: public Forma obtenerFormas(String tipo)
    public Contrato obtenerContrato(String tipo) throws CloneNotSupportedException {
        return (Contrato) prototipos.get(tipo).clone();
    }
}
