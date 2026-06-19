package pe.edu.utp.pf.service.impl;

import java.time.LocalDateTime;
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


/**
 * Implementación de la interface PagoService.
 * Maneja el ingreso de flujos de dinero al sistema amortizando cuotas,
 * soportando polimorfismo sobre diferentes modalidades de pago.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository repo;

    /**
     * Busca un pago o comprobante individual de acuerdo a su ID.
     *
     * @param id Identificador único del pago.
     * @return Un objeto Optional con el resultado de la búsqueda.
     */
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

    /**
     * Retorna el historial de todos los pagos registrados por la institución.
     *
     * @return Una lista de entidades derivadas de la clase Pago.
     */
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

    /**
     * Procesa un nuevo ingreso de pago y lo registra con su estampa de tiempo actual.
     *
     * @param pago Entidad Pago (o sus hijas: Efectivo, Billetera Digital, etc.) a guardar.
     * @return La operación de pago finalizada.
     * @throws RuntimeException Si ocurren problemas en la inserción.
     */
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
            throw new ServiceException("Error al procesar pago", e);
        }
    }

    /**
     * Edita información de un pago (ej. corrección del monto en un cuadre de caja).
     *
     * @param old El registro original validado del pago.
     * @param pago El registro conteniendo las modificaciones deseadas.
     * @return El pago con las correcciones aplicadas.
     * @throws RuntimeException si la base de datos rechaza la actualización.
     */
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

    /**
     * Anula o elimina un pago erróneo del sistema mediante su ID.
     *
     * @param id Parámetro ID del pago a anular.
     * @throws RuntimeException En caso de fallas durante el borrado.
     */
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
