package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.SolicitudCredito;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define los métodos de servicio para la gestión de expedientes y trámites iniciales.
 * Controla la evaluación de riesgo y las garantías asociadas a la solicitud.
 */
public interface SolicitudCreditoService {

    /**
     * Metodo para obtener una solicitud de crédito por su identificador.
     *
     * @param id El parámetro ID de la solicitud a buscar.
     * @return Retorna el objeto solicitud encontrado envuelto en un Optional.
     */
    Optional<SolicitudCredito> getById(Integer id);

    /**
     * Metodo para obtener la lista de todas las solicitudes de crédito.
     *
     * @return Lista de objetos de tipo SolicitudCredito.
     */
    List<SolicitudCredito> getAll();

    /**
     * Metodo para registrar una nueva solicitud de crédito en el sistema.
     *
     * @param solicitud Es el objeto solicitud a guardar.
     * @return Retorna el objeto solicitud registrado (generalmente en estado "Pendiente").
     */
    SolicitudCredito create(SolicitudCredito solicitud);

    /**
     * Metodo para actualizar una solicitud de crédito existente (ej. cambiar su estado a "Aprobada").
     *
     * @param old El objeto solicitud original antes de los cambios.
     * @param solicitud El objeto solicitud con los datos actualizados.
     * @return Retorna el objeto solicitud actualizado.
     */
    SolicitudCredito update(SolicitudCredito old, SolicitudCredito solicitud);

    /**
     * Metodo para eliminar una solicitud de crédito por su identificador.
     *
     * @param id Parámetro ID de la solicitud a eliminar.
     */
    void deleteById(Integer id);
}
