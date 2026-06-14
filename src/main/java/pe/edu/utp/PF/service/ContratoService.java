package pe.edu.utp.PF.service;

import pe.edu.utp.PF.service.patron.prototype.Contrato;

import java.util.Optional;

public interface ContratoService {
    /**
     * Metodo para obtener un objeto contrato por Id
     *
     * @param id Parametro Id del objeto a buscar
     * @return Objeto contrato encontrado en Optional
     */
    Optional<Contrato> getById(Integer id);

    /**
     * Metodo para generar y registrar un nuevo contrato clonando un prototipo en memoria
     *
     * @param tipoContrato Tipo de plantilla a clonar ("Consumo", "Microempresa")
     * @param idSolicitud  ID de la solicitud de credito a asociar
     * @return Retorna el objeto contrato registrado en la base de datos
     */
    Contrato generarContratoDesdePlantilla(String tipoContrato, Integer idSolicitud);
}
