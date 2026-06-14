package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.Credito;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define los métodos de servicio para la gestión del ciclo de vida de los créditos.
 * Controla el desembolso y el desglose financiero de la deuda.
 */
public interface CreditoService {

    /**
     * Metodo para obtener un crédito específico por su identificador.
     *
     * @param id El parámetro ID del crédito a buscar.
     * @return Retorna el objeto crédito encontrado envuelto en un Optional.
     */
    Optional<Credito> getById(Integer id);

    /**
     * Metodo para obtener la lista de todos los créditos registrados en el sistema.
     *
     * @return Lista de objetos de tipo Credito.
     */
    List<Credito> getAll();

    /**
     * Metodo para registrar un nuevo crédito (desembolso).
     * Genera automáticamente el cronograma y las cuotas asociadas.
     *
     * @param credito Es el objeto crédito a guardar.
     * @return Retorna el objeto crédito registrado.
     */
    Credito create(Credito credito);

    /**
     * Metodo para actualizar los datos de un crédito existente.
     *
     * @param old El objeto crédito original antes de los cambios.
     * @param credito El objeto crédito con los nuevos datos a actualizar.
     * @return Retorna el objeto crédito actualizado.
     */
    Credito update(Credito old, Credito credito);

    /**
     * Metodo para eliminar un crédito del sistema por su identificador.
     *
     * @param id Parámetro ID del crédito a eliminar.
     */
    void deleteById(Integer id);
}