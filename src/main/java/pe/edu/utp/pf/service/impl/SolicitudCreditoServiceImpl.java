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

@Slf4j
@RequiredArgsConstructor
@Service
public class SolicitudCreditoServiceImpl implements SolicitudCreditoService {

    private final SolicitudCreditoRepository repo;

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