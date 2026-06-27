package pe.edu.utp.pf.service;

import pe.edu.utp.pf.model.AsesorFinanciero;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define los métodos de servicio para la gestión del personal de la agencia.
 * Administra los datos de los asesores encargados de las solicitudes.
 */
public interface AsesorFinancieroService {

    /**
     * Metodo para obtener un asesor financiero por su identificador.
     *
     * @param id El parámetro ID del asesor a buscar.
     * @return Retorna el objeto asesor encontrado envuelto en un Optional.
     */
    Optional<AsesorFinanciero> getById(Integer id);

    /**
     * Metodo para obtener la lista de todos los asesores financieros de la agencia.
     *
     * @return Lista de objetos de tipo AsesorFinanciero.
     */
    List<AsesorFinanciero> getAll();

    /**
     * Metodo para registrar un nuevo asesor financiero en el sistema.
     *
     * @param asesor Es el objeto asesor a guardar.
     * @return Retorna el objeto asesor registrado.
     */
    AsesorFinanciero create(AsesorFinanciero asesor);

    /**
     * Metodo para actualizar los datos de un asesor financiero existente.
     *
     * @param old    El objeto asesor original antes de los cambios.
     * @param asesor El objeto asesor con los datos actualizados.
     * @return Retorna el objeto asesor actualizado.
     */
    AsesorFinanciero update(AsesorFinanciero old, AsesorFinanciero asesor);

    /**
     * Metodo para eliminar un asesor financiero del sistema por su identificador.
     *
     * @param id Parámetro ID del asesor a eliminar.
     */
    void deleteById(Integer id);
}
