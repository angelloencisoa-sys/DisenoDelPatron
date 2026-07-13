package pe.edu.utp.pf.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.model.AsesorFinanciero;
import pe.edu.utp.pf.repository.AsesorFinancieroRepository;
import pe.edu.utp.pf.service.AsesorFinancieroService;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsesorFinancieroServiceImpl implements AsesorFinancieroService {

    private final AsesorFinancieroRepository repo;

    @Transactional(readOnly = true)
    @Override
    public Optional<AsesorFinanciero> getById(Integer id) {
        try {
            return repo.findById(id);
        } catch (DataAccessException e) {
            log.error("Error BD al buscar asesor {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AsesorFinanciero> getAll() {
        try {
            return repo.findAll();
        } catch (DataAccessException e) {
            log.error("Error BD al listar asesores: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public AsesorFinanciero create(AsesorFinanciero asesor) {
        try {
            asesor.setIdAsesor(null);
            return repo.save(asesor);
        } catch (DataAccessException e) {
            log.error("Error al registrar asesor: {}", e.getMessage());
            throw new ServiceException("Error al registrar asesor", e);
        }
    }

    @Transactional
    @Override
    public AsesorFinanciero update(AsesorFinanciero old, AsesorFinanciero asesor) {
        try {
            old.setNombreAsesor(asesor.getNombreAsesor());
            old.setCodigoAgencia(asesor.getCodigoAgencia());
            return repo.save(old);
        } catch (DataAccessException e) {
            log.error("Error al actualizar asesor: {}", e.getMessage());
            throw new ServiceException("Error al actualizar", e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Error al eliminar asesor {}: {}", id, e.getMessage());
            throw new ServiceException("Error al eliminar", e);
        }
    }
}