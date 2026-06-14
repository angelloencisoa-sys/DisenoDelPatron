package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    Optional<Cliente> getById(Integer id);
    List<Cliente> getAll();
    Cliente create(Cliente cliente);
    Cliente update(Cliente old, Cliente cliente);
    void deleteById(Integer id);
}
