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

/**
 * Implementación de la interface CreditoService.
 * Gestiona el desembolso, actualización y estado de los créditos financieros,
 * encargándose adicionalmente de asociar un cronograma de cuotas inicial en su creación.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CreditoServiceImpl implements CreditoService {

    private final CreditoRepository repo;

    /**
     * Recupera un crédito por su identificador único.
     *
     * @param id Parámetro ID del crédito a buscar.
     * @return Optional con el crédito encontrado, o vacío si no existe o hay error de base de datos.
     */
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

    /**
     * Recupera todos los créditos registrados.
     *
     * @return Lista completa de créditos, o lista vacía en caso de error de acceso a datos.
     */
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

    /**
     * Registra y desembolsa un nuevo crédito en el sistema.
     * Inicializa un cronograma básico vacío asociado al crédito si no cuenta con uno.
     *
     * @param credito El objeto Credito con los datos del financiamiento.
     * @return El crédito guardado en la base de datos con su identificador.
     * @throws ServiceException si ocurre un error al persistir el registro del crédito.
     */
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

    /**
     * Actualiza las condiciones vigentes de un crédito (ej. cambio de estado o reajuste de tasas).
     *
     * @param old El crédito actual recuperado de la base de datos.
     * @param credito El crédito modificado con los nuevos datos.
     * @return El crédito actualizado y persistido.
     * @throws ServiceException si ocurre un error al guardar los cambios en la base de datos.
     */
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

    /**
     * Elimina un registro de crédito a partir de su ID.
     *
     * @param id Parámetro ID del crédito a eliminar.
     * @throws ServiceException si ocurre un error al eliminar.
     */
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