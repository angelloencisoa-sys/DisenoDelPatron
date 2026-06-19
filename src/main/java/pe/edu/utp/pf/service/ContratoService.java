package pe.edu.utp.pf.service;

import pe.edu.utp.pf.service.patron.prototype.Contrato;

import java.util.Optional;

/**
 * Interface que define los métodos de servicio para la gestión de contratos.
 * Aplica el patrón Prototype para optimizar la generación de documentos a partir de plantillas.
 */
public interface ContratoService {
    /**
     * Metodo para obtener un objeto contrato por su Id.
     *
     * @param id Parámetro Id del objeto contrato a buscar.
     * @return Objeto contrato encontrado envuelto en un Optional.
     */
    Optional<Contrato> getById(Integer id);

    /**
     * Metodo para generar y registrar un nuevo contrato clonando un prototipo en memoria.
     *
     * @param tipoContrato Tipo de plantilla a clonar (ej. "Consumo", "Microempresa").
     * @param idSolicitud  ID de la solicitud de crédito a asociar al nuevo contrato.
     * @return Retorna el nuevo objeto contrato registrado en la base de datos.
     */
    Contrato generarContratoDesdePlantilla(String tipoContrato, Integer idSolicitud);
}
