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

/**
 * Implementación de la interface AsesorFinancieroService.
 * Gestiona la lógica de negocio y las transacciones de base de datos para el personal de la agencia.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AsesorFinancieroServiceImpl implements AsesorFinancieroService {

    private final AsesorFinancieroRepository repo;

    /**
     * Obtiene un asesor financiero por su ID.
     *
     * @param id Parámetro ID del asesor a buscar.
     * @return Optional que contiene al asesor si es encontrado, o vacío en caso contrario.
     */
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

    /**
     * Obtiene una lista de todos los asesores financieros registrados.
     *
     * @return Lista de objetos AsesorFinanciero.
     */
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

    /**
     * Crea y registra un nuevo asesor financiero en el sistema.
     *
     * @param asesor Objeto AsesorFinanciero a registrar.
     * @return El objeto AsesorFinanciero guardado con su ID generado.
     * @throws RuntimeException si ocurre un error en la base de datos.
     */
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

    /**
     * Actualiza la información de un asesor financiero existente.
     *
     * @param old    El objeto AsesorFinanciero actual recuperado de la BD.
     * @param asesor El objeto AsesorFinanciero con los datos nuevos.
     * @return El objeto AsesorFinanciero actualizado.
     * @throws RuntimeException si ocurre un error en la base de datos.
     */
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

    /**
     * Elimina a un asesor financiero de la base de datos según su ID.
     *
     * @param id Parámetro ID del asesor a eliminar.
     * @throws RuntimeException si ocurre un error en la base de datos.
     */
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