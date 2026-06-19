package pe.edu.utp.pf.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.model.SolicitudCredito;
import pe.edu.utp.pf.repository.SolicitudCreditoRepository;
import pe.edu.utp.pf.service.SolicitudCreditoService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interface SolicitudCreditoService.
 * Coordina las etapas iniciales del trámite del crédito, su evaluación y sus garantías.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SolicitudCreditoServiceImpl implements SolicitudCreditoService {

    private final SolicitudCreditoRepository repo;

    /**
     * Recupera el expediente completo de una solicitud por su ID.
     *
     * @param id El identificador único de la solicitud a buscar.
     * @return Un objeto Optional con el resultado de la búsqueda en BD.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<SolicitudCredito> getById(Integer id) {
        try {
            return repo.findById(id);
        } catch (DataAccessException e) {
            log.error("Error BD al buscar solicitud {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Consulta todas las solicitudes de crédito ingresadas en la agencia.
     *
     * @return Una colección de objetos de tipo SolicitudCredito.
     */
    @Transactional(readOnly = true)
    @Override
    public List<SolicitudCredito> getAll() {
        try {
            return repo.findAll();
        } catch (DataAccessException e) {
            log.error("Error BD al listar solicitudes: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Registra una nueva solicitud de trámite configurando su estado inicial como "Pendiente".
     *
     * @param solicitud Entidad correspondiente al trámite en proceso de apertura.
     * @return La solicitud grabada con su número de expediente (ID).
     * @throws RuntimeException Ante cualquier excepción originada por el motor de BD.
     */
    @Transactional
    @Override
    public SolicitudCredito create(SolicitudCredito solicitud) {
        try {
            solicitud.setIdSolicitud(null);
            solicitud.setEstado("Pendiente"); // Estado inicial por defecto
            return repo.save(solicitud);
        } catch (DataAccessException e) {
            log.error("Error al crear solicitud: {}", e.getMessage());
            throw new ServiceException("Error al crear solicitud", e);
        }
    }

    /**
     * Modifica el estado, plazos o montos de una solicitud de crédito en revisión.
     *
     * @param old El registro original en base de datos.
     * @param solicitud El objeto modificado proveniente del controlador o vista.
     * @return La solicitud debidamente actualizada.
     * @throws RuntimeException si surgen conflictos de persistencia.
     */
    @Transactional
    @Override
    public SolicitudCredito update(SolicitudCredito old, SolicitudCredito solicitud) {
        try {
            old.setMontoSolicitado(solicitud.getMontoSolicitado());
            old.setPlazoMeses(solicitud.getPlazoMeses());
            old.setEstado(solicitud.getEstado());
            return repo.save(old);
        } catch (DataAccessException e) {
            log.error("Error al actualizar solicitud: {}", e.getMessage());
            throw new ServiceException("Error al actualizar solicitud", e);
        }
    }

    /**
     * Borra una solicitud de crédito del sistema, lo que implicaría también la
     * remoción en cascada de sus evaluaciones o garantías atadas si están configuradas así en JPA.
     *
     * @param id El identificador único de la solicitud a desechar.
     * @throws RuntimeException si la base de datos niega la eliminación.
     */
    @Transactional
    @Override
    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Error al eliminar solicitud {}: {}", id, e.getMessage());
            throw new ServiceException("Error al eliminar", e);
        }
    }
}
