package pe.edu.utp.PF.service;

import pe.edu.utp.PF.model.Pago;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define los métodos de servicio para la gestión de los ingresos de dinero.
 * Maneja de manera polimórfica los diferentes tipos de pago (Efectivo, Billetera Digital, etc.).
 */
public interface PagoService {

    /**
     * Metodo para obtener un pago específico por su identificador.
     *
     * @param id El parámetro ID del pago a buscar.
     * @return Retorna el objeto pago encontrado envuelto en un Optional.
     */
    Optional<Pago> getById(Integer id);

    /**
     * Metodo para obtener la lista de todos los pagos registrados.
     *
     * @return Lista de objetos de tipo Pago.
     */
    List<Pago> getAll();

    /**
     * Metodo para registrar un nuevo ingreso o pago.
     *
     * @param pago Es el objeto pago (puede ser cualquier subclase de Pago) a guardar.
     * @return Retorna el objeto pago registrado.
     */
    Pago create(Pago pago);

    /**
     * Metodo para actualizar un registro de pago existente.
     *
     * @param old El objeto pago original antes de los cambios.
     * @param pago El objeto pago con los datos actualizados.
     * @return Retorna el objeto pago actualizado.
     */
    Pago update(Pago old, Pago pago);

    /**
     * Metodo para eliminar o anular un pago del sistema por su identificador.
     *
     * @param id Parámetro ID del pago a anular/eliminar.
     */
    void deleteById(Integer id);
}