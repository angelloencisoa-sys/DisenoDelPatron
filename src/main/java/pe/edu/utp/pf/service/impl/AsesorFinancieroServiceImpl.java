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
 * Gestiona el ciclo de vida y operaciones de negocio para los asesores financieros.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AsesorFinancieroServiceImpl implements AsesorFinancieroService {

    private final AsesorFinancieroRepository repo;

    /**
     * Obtiene un asesor financiero por su identificador único.
     *
     * @param id Parámetro ID del asesor a buscar.
     * @return Optional con el asesor financiero encontrado, o vacío si ocurre un error de acceso a datos.
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
     * Recupera todos los asesores financieros registrados en el sistema.
     *
     * @return Lista de asesores financieros, o una lista vacía en caso de error de acceso a datos.
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
     * Registra un nuevo asesor financiero en el sistema.
     *
     * @param asesor El objeto AsesorFinanciero con los datos a persistir.
     * @return El asesor financiero guardado con su identificador generado.
     * @throws ServiceException si ocurre un error de persistencia en la base de datos.
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
     * @param asesor El objeto AsesorFinanciero con los nuevos datos a aplicar.
     * @return El asesor financiero actualizado.
     * @throws ServiceException si ocurre un error en la capa de persistencia.
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
     * Elimina un asesor financiero de la base de datos a partir de su ID.
     *
     * @param id Parámetro ID del asesor financiero a eliminar.
     * @throws ServiceException si ocurre un error al procesar la eliminación.
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