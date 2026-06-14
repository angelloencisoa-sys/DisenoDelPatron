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


/**
 * Implementación de la interface CreditoService.
 * Administra el estado financiero principal de las operaciones aprobadas y gestiona
 * en cascada entidades clave como el Cronograma y las Cuotas.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CreditoServiceImpl implements CreditoService {

    private final CreditoRepository repo;

    /**
     * Busca un crédito en la base de datos a partir de su ID.
     *
     * @param id Parámetro ID del crédito a buscar.
     * @return Optional conteniendo el crédito si existe.
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
     * Lista todos los créditos existentes en el sistema financiero.
     *
     * @return Colección en forma de lista de la entidad Credito.
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
     * Desembolsa un nuevo crédito. Por configuración JPA, este proceso también debe
     * guardar automáticamente el Cronograma y Cuotas asociadas.
     *
     * @param credito Objeto de tipo Credito a desembolsar.
     * @return El crédito persistido en la base de datos.
     * @throws RuntimeException ante fallos de persistencia.
     */
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

    /**
     * Actualiza atributos específicos de un crédito vigente, como su tasa de interés o estado.
     *
     * @param old Entidad original recuperada previamente de la BD.
     * @param credito Entidad portadora de los nuevos valores.
     * @return El crédito actualizado.
     * @throws RuntimeException ante errores en el guardado.
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
            throw new RuntimeException("Error al actualizar", e);
        }
    }

    /**
     * Elimina el registro físico de un crédito de la base de datos.
     *
     * @param id ID del crédito a eliminar.
     * @throws RuntimeException ante restricciones de llave foránea o fallos DB.
     */
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