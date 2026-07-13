package pe.edu.utp.pf.service.impl;

import java.time.LocalDateTime;
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
import pe.edu.utp.pf.model.Pago;
import pe.edu.utp.pf.repository.PagoRepository;
import pe.edu.utp.pf.service.PagoService;

@Slf4j
@RequiredArgsConstructor
@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository repo;

    @Transactional(readOnly = true)
    @Override
    public Optional<Pago> getById(Integer id) {
        try {
            return repo.findById(id);
        } catch (DataAccessException e) {
            log.error("Error BD al buscar pago {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pago> getAll() {
        try {
            return repo.findAll();
        } catch (DataAccessException e) {
            log.error("Error BD al listar pagos: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public Pago create(Pago pago) {
        try {
            pago.setIdPago(null);
            pago.setFechaHoraTransaccion(LocalDateTime.now(ZoneId.of("America/Lima")));
            return repo.save(pago);
        } catch (DataAccessException e) {
            log.error("Error al registrar pago: {}", e.getMessage());
            throw new ServiceException("Error al procesar pago", e);
        }
    }

    @Transactional
    @Override
    public Pago update(Pago old, Pago pago) {
        try {
            old.setMontoAbonado(pago.getMontoAbonado());
            return repo.save(old);
        } catch (DataAccessException e) {
            log.error("Error al actualizar pago: {}", e.getMessage());
            throw new ServiceException("Error al actualizar", e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Error al anular pago {}: {}", id, e.getMessage());
            throw new ServiceException("Error al anular", e);
        }
    }
}