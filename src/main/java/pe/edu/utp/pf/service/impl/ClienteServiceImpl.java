package pe.edu.utp.pf.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.edu.utp.pf.exception.ServiceException;
import pe.edu.utp.pf.model.Cliente;
import pe.edu.utp.pf.repository.ClienteRepository;
import pe.edu.utp.pf.service.ClienteService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interface ClienteService.
 * Gestiona la persistencia polimórfica de clientes naturales y jurídicos de manera unificada.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    /**
     * Obtiene un cliente por su identificador único.
     *
     * @param id Parámetro ID del cliente a buscar.
     * @return Optional con el cliente encontrado, o vacío si ocurre un error o no existe.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<Cliente> getById(Integer id) {
        try {
            Optional<Cliente> cliente = repo.findById(id);
            if (cliente.isPresent()) {
                log.debug("Cliente encontrado: ID {}", id);
            } else {
                log.debug("Cliente no encontrado con ID: {}", id);
            }
            return cliente;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al buscar cliente con ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Recupera todos los clientes registrados en la base de datos.
     *
     * @return Lista completa de clientes.
     */
    @Transactional(readOnly = true)
    @Override
    public List<Cliente> getAll() {
        try {
            var clientes = repo.findAll();
            log.debug("Total de clientes encontrados: {}", clientes.size());
            return clientes;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al obtener clientes: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Crea un nuevo registro de cliente (Natural o Jurídico).
     *
     * @param cliente El objeto Cliente a guardar.
     * @return El cliente guardado exitosamente.
     * @throws RuntimeException si ocurre un error de persistencia.
     */
    @Transactional
    @Override
    public Cliente create(Cliente cliente) {
        try {
            cliente.setIdCliente(null);
            return repo.save(cliente);
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al crear cliente: {}", e.getMessage());
            throw new ServiceException("Error de base de datos al crear cliente", e);
        }
    }

    /**
     * Actualiza los datos de contacto y detalles principales de un cliente.
     *
     * @param old     El objeto Cliente actual recuperado de la base de datos.
     * @param cliente El objeto Cliente con la nueva información.
     * @return El cliente actualizado.
     * @throws RuntimeException si ocurre un error de persistencia.
     */
    @Transactional
    @Override
    public Cliente update(Cliente old, Cliente cliente) {
        try {
            old.setNombresCompletos(cliente.getNombresCompletos());
            old.setDireccion(cliente.getDireccion());
            old.setTelefono(cliente.getTelefono());
            old.setEmail(cliente.getEmail());
            return repo.save(old);
        } catch (DataAccessException e) {
            log.error("Error al actualizar cliente: {}", e.getMessage());
            throw new ServiceException("Error de base de datos al actualizar", e);
        }
    }

    /**
     * Elimina a un cliente del sistema mediante su ID.
     *
     * @param id Parámetro ID del cliente a eliminar.
     * @throws RuntimeException si ocurre un error en la base de datos.
     */
    @Transactional
    @Override
    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
            log.debug("Cliente con ID {} eliminado exitosamente", id);
        } catch (DataAccessException e) {
            log.error("Error al eliminar cliente con ID {}: {}", id, e.getMessage());
            throw new ServiceException("Error al eliminar cliente", e);
        }
    }
}