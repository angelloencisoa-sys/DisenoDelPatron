package pe.edu.utp.pf.service.patron.prototype;

import java.util.HashMap;


/**
 * Clase gestora (Registry) encargada de administrar y almacenar en memoria los prototipos
 * de contratos del sistema financiero.
 *
 * @author Grupo 07
 * @version 2.0
 */
public class GestorContrato {

    /**
     * Mapa en memoria (caché) que almacena las plantillas base indexadas por su tipo.
     */
    private HashMap<String, Contrato> prototipos = new HashMap<>();

    /**
     * Inicializa el registro precargando en memoria las plantillas predefinidas
     * para los créditos de "Consumo" y "Microempresa".
     */
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

    /**
     * Recupera y duplica un contrato plantilla según el tipo solicitado.
     *
     * @param tipo El nombre identificador de la plantilla a clonar (ej. "Consumo", "Microempresa").
     * @return Una nueva instancia limpia de Contrato generada a partir del prototipo en memoria.
     * @throws IllegalArgumentException Si el tipo de contrato especificado no existe en el registro.
     */
    public Contrato obtenerContrato(String tipo) {
        Contrato prototipo = prototipos.get(tipo);
        if (prototipo == null) {
            throw new IllegalArgumentException("No existe un prototipo de contrato para el tipo: " + tipo);
        }
        return new Contrato(prototipo);
    }
}
