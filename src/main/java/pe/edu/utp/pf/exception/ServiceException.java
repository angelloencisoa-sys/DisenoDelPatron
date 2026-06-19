package pe.edu.utp.pf.exception;

public class ServiceException extends RuntimeException {

    public ServiceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
