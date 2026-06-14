package pe.edu.utp.PF.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import pe.edu.utp.PF.model.Cliente;
import pe.edu.utp.PF.repository.ClienteRepository;
import pe.edu.utp.PF.service.ClienteService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService {

    private ClienteRepository repo;

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

    @Transactional
    @Override
    public Cliente create(Cliente cliente) {
        try {
            cliente.setIdCliente(null); // Asegurar que es un insert
            return repo.save(cliente);
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al crear cliente: {}", e.getMessage());
            throw new RuntimeException("Error de base de datos al crear cliente", e);
        }
    }

    @Transactional
    @Override
    public Cliente update(Cliente old, Cliente cliente) {
        try {
            old.setNombresCompletos(cliente.getNombresCompletos());
            old.setDireccion(cliente.getDireccion());
            old.setTelefono(cliente.getTelefono());
            old.setEmail(cliente.getEmail());
            // El guardado polimórfico en JPA actualizará las tablas hijas si es Natural o Jurídico
            return repo.save(old);
        } catch (DataAccessException e) {
            log.error("Error al actualizar cliente: {}", e.getMessage());
            throw new RuntimeException("Error de base de datos al actualizar", e);
        }
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
            log.debug("Cliente con ID {} eliminado exitosamente", id);
        } catch (DataAccessException e) {
            log.error("Error al eliminar cliente con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar cliente", e);
        }
    };
}
