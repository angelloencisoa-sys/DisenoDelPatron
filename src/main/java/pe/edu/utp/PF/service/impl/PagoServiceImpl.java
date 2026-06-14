package pe.edu.utp.PF.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.PF.model.Pago;
import pe.edu.utp.PF.repository.PagoRepository;
import pe.edu.utp.PF.service.PagoService;


@Slf4j
@RequiredArgsConstructor
@Service
public class PagoServiceImpl implements PagoService {

    private PagoRepository repo;

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
            pago.setFechaHoraTransaccion(LocalDateTime.now());
            // Al ser polimórfico, si le pasas un PagoEfectivo o PagoBilleteraDigital, JPA sabrá qué hacer.
            return repo.save(pago);
        } catch (DataAccessException e) {
            log.error("Error al registrar pago: {}", e.getMessage());
            throw new RuntimeException("Error al procesar pago", e);
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
            throw new RuntimeException("Error al actualizar", e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Error al anular pago {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al anular", e);
        }
    }
}
