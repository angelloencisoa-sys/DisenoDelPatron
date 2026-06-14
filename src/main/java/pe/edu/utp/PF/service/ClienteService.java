package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define los métodos de servicio para la gestión de la identidad de los clientes.
 * Soporta de manera polimórfica a clientes naturales y jurídicos.
 */
public interface ClienteService {

    /**
     * Metodo para obtener un cliente por su identificador único.
     *
     * @param id El parámetro ID del cliente a buscar.
     * @return Retorna el objeto cliente encontrado envuelto en un Optional.
     */
    Optional<Cliente> getById(Integer id);

    /**
     * Metodo para obtener la lista de todos los clientes registrados.
     *
     * @return Lista de objetos de tipo Cliente.
     */
    List<Cliente> getAll();

    /**
     * Metodo para registrar un nuevo cliente en el sistema.
     *
     * @param cliente Es el objeto cliente a guardar.
     * @return Retorna el objeto cliente registrado.
     */
    Cliente create(Cliente cliente);

    /**
     * Metodo para actualizar los datos personales o empresariales de un cliente existente.
     *
     * @param old El objeto cliente original antes de los cambios.
     * @param cliente El objeto cliente con los datos actualizados.
     * @return Retorna el objeto cliente actualizado.
     */
    Cliente update(Cliente old, Cliente cliente);

    /**
     * Metodo para eliminar un cliente del sistema por su identificador.
     *
     * @param id Parámetro ID del cliente a eliminar.
     */
    void deleteById(Integer id);
}