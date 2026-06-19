package pe.edu.utp.pf.service.patron.prototype;

import java.util.HashMap;

public class GestorContrato {

    private HashMap<String, Contrato> prototipos = new HashMap<>();

    public GestorContrato() {

        Contrato contratoConsumo = new Contrato();
        contratoConsumo.setTipo("Consumo");
        contratoConsumo.setClausulasExtras("Cláusulas estándar para crédito de consumo: Tasa fija...");

        Contrato contratoMicroempresa = new Contrato();
        contratoMicroempresa.setTipo("Microempresa");
        contratoMicroempresa.setClausulasExtras("Cláusulas estándar para crédito Pyme: Respaldo de inventario...");

        prototipos.put("Consumo", contratoConsumo);
        prototipos.put("Microempresa", contratoMicroempresa);
    }


    public Contrato obtenerContrato(String tipo) {
        Contrato prototipo = prototipos.get(tipo);
        if (prototipo == null) {
            throw new IllegalArgumentException("No existe un prototipo de contrato para el tipo: " + tipo);
        }
        // Retornamos un clon usando el constructor de copia de forma limpia
        return new Contrato(prototipo);
    }
}
