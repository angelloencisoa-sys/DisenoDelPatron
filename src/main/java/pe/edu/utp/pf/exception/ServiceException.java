package pe.edu.utp.pf.exception;

/**
 * Excepción personalizada de la capa de lógica de negocio (Services).
 *
 * @author Grupo 07
 * @version 2.0
 */
public class ServiceException extends RuntimeException {

    /**
     * Constructor que inicializa la excepción con un mensaje descriptivo y la causa original.
     *
     * @param mensaje Explicación detallada del error ocurrido durante la ejecución del servicio.
     * @param causa   Excepción original Throwable que desencadenó el fallo técnico.
     */
    public ServiceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
