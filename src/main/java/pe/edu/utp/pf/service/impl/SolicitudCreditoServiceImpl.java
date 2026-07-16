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
 * Gestiona la radicación, control de estados (Pendiente, Aprobada, Rechazada) y
 * flujos de negocio relacionados a las solicitudes de crédito.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SolicitudCreditoServiceImpl implements SolicitudCreditoService {

    private final SolicitudCreditoRepository repo;

    /**
     * Busca una solicitud de crédito mediante su identificador único.
     *
     * @param id Parámetro ID de la solicitud a buscar.
     * @return Optional con la solicitud de crédito encontrada, o vacío si no se halla o hay error de BD.
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
     * Lista todas las solicitudes de crédito registradas en la base de datos.
     *
     * @return Lista de solicitudes de crédito registradas, o lista vacía en caso de error.
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
     * Registra una nueva solicitud de crédito en el sistema.
     * Por defecto, inicializa el estado de la solicitud como "Pendiente" si no viene predefinido.
     *
     * @param solicitud El objeto SolicitudCredito con los detalles del financiamiento requerido.
     * @return La solicitud guardada y persistida.
     * @throws ServiceException si la operación de inserción en la base de datos falla.
     */
    @Transactional
    @Override
    public SolicitudCredito create(SolicitudCredito solicitud) {
        try {
            solicitud.setIdSolicitud(null);
            if (solicitud.getEstado() == null) {
                solicitud.setEstado("Pendiente");
            }
            return repo.save(solicitud);
        } catch (DataAccessException e) {
            log.error("Error al crear solicitud: {}", e.getMessage());
            throw new ServiceException("Error al crear solicitud", e);
        }
    }

    /**
     * Actualiza la información y el estado de una solicitud de crédito existente.
     *
     * @param old El registro actual de la solicitud en base de datos.
     * @param solicitud El objeto con los nuevos datos que actualizarán la solicitud.
     * @return La solicitud de crédito con los cambios aplicados y guardados.
     * @throws ServiceException si ocurre un error de acceso a datos durante la actualización.
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
     * Elimina una solicitud de crédito de la base de datos según su ID.
     *
     * @param id Parámetro ID de la solicitud a borrar.
     * @throws ServiceException si falla el proceso de eliminación.
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