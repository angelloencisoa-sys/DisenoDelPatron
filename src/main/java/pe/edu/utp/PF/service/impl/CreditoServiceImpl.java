package pe.edu.utp.PF.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.PF.model.Credito;
import pe.edu.utp.PF.repository.CreditoRepository;
import pe.edu.utp.PF.service.CreditoService;


@Slf4j
@RequiredArgsConstructor
@Service
public class CreditoServiceImpl implements CreditoService {


    private CreditoRepository repo;

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
            // JPA guardará automáticamente el Cronograma y las Cuotas si están mapeadas con CascadeType.ALL
            return repo.save(credito);
        } catch (DataAccessException e) {
            log.error("Error al desembolsar/crear crédito: {}", e.getMessage());
            throw new RuntimeException("Error al crear crédito", e);
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
            throw new RuntimeException("Error al actualizar", e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Error al eliminar crédito {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar crédito", e);
        }
    }
}