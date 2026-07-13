package pe.edu.utp.pf.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.model.Credito;
import pe.edu.utp.pf.model.Cronograma;
import pe.edu.utp.pf.repository.CreditoRepository;
import pe.edu.utp.pf.service.CreditoService;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreditoServiceImpl implements CreditoService {

    private final CreditoRepository repo;

    @Transactional(readOnly = true)
    @Override
    public Optional<Credito> getById(Integer id) {
        try {
            return repo.findById(id);
        } catch (DataAccessException e) {
            log.error("Error al buscar crédito {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Credito> getAll() {
        try {
            return repo.findAll();
        } catch (DataAccessException e) {
            log.error("Error al listar créditos: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public Credito create(Credito credito) {
        try {
            credito.setIdCredito(null);

            if (credito.getCronograma() == null) {
                Cronograma nuevoCronograma = new Cronograma();
                nuevoCronograma.setFechaGeneracion(LocalDate.now(ZoneId.of("America/Lima")));
                nuevoCronograma.setCredito(credito);
                nuevoCronograma.setCuotas(Collections.emptyList());

                credito.setCronograma(nuevoCronograma);
            }

            return repo.save(credito);
        } catch (DataAccessException e) {
            log.error("Error al desembolsar/crear crédito: {}", e.getMessage());
            throw new ServiceException("Error al crear crédito", e);
        }
    }

    @Transactional
    @Override
    public Credito update(Credito old, Credito credito) {
        try {
            old.setEstadoCredito(credito.getEstadoCredito());
            old.setTasaInteresAnual(credito.getTasaInteresAnual());
            return repo.save(old);
        } catch (DataAccessException e) {
            log.error("Error al actualizar crédito: {}", e.getMessage());
            throw new ServiceException("Error al actualizar", e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Error al eliminar crédito {}: {}", id, e.getMessage());
            throw new ServiceException("Error al eliminar crédito", e);
        }
    }
}