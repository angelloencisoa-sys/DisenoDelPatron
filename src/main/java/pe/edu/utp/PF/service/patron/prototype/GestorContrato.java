package pe.edu.utp.PF.service.patron.prototype;

import java.util.HashMap;

public class GestorContrato {

    private HashMap<String, Contrato> prototipos = new HashMap<>();

    public GestorContrato() {

        Contrato contratoConsumo = new Contrato();
        contratoConsumo.tipo = "Consumo";
        contratoConsumo.setClausulasExtras("Cláusulas estándar para crédito de consumo: Tasa fija...");

        Contrato contratoMicroempresa = new Contrato();
        contratoMicroempresa.tipo = "Microempresa";
        contratoMicroempresa.setClausulasExtras("Cláusulas estándar para crédito Pyme: Respaldo de inventario...");

        prototipos.put("Consumo", contratoConsumo);
        prototipos.put("Microempresa", contratoMicroempresa);
    }


    public Contrato obtenerContrato(String tipo) throws CloneNotSupportedException {
        Contrato prototipo = prototipos.get(tipo);
        if (prototipo == null) {
            throw new IllegalArgumentException("No existe un prototipo de contrato para el tipo: " + tipo);
        }
        return (Contrato) prototipo.clone();
    }
}
