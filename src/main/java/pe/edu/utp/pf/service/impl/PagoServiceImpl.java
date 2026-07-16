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

/**
 * Implementación de la interface PagoService.
 * Maneja la lógica de negocio y transaccionalidad de las transacciones de pago
 * realizadas por los clientes hacia sus créditos.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository repo;

    /**
     * Obtiene una transacción de pago por su ID único.
     *
     * @param id Parámetro ID de la transacción de pago a buscar.
     * @return Optional con el pago encontrado, o vacío en caso de error o inexistencia.
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
     * Recupera el histórico de todos los pagos registrados.
     *
     * @return Lista completa de transacciones de pago, o vacía en caso de fallos en base de datos.
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
     * Registra un nuevo pago en el sistema.
     * Captura automáticamente la fecha y hora de la transacción usando la zona horaria de Perú (America/Lima).
     *
     * @param pago El objeto Pago con los datos del abono.
     * @return El pago registrado y persistido con su ID asignado.
     * @throws ServiceException si la persistencia del pago falla en la capa de datos.
     */
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

    /**
     * Actualiza el monto o detalles específicos de un pago existente.
     *
     * @param old El pago actual registrado en la base de datos.
     * @param pago El objeto Pago con los nuevos valores.
     * @return El pago con las modificaciones aplicadas.
     * @throws ServiceException si ocurre un error de base de datos durante el guardado.
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
     * Elimina una transacción de pago registrada mediante su ID.
     *
     * @param id Parámetro ID del pago a eliminar.
     * @throws ServiceException si ocurre un error durante el proceso de borrado.
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